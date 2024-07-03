// ============================================================================
// Copyright BRAINTRIBE TECHNOLOGY GMBH, Austria, 2002-2022
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
// ============================================================================
package com.braintribe.wire.api.tools;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.stream.Stream;

public abstract class Generics {
	
	public static Type getGenericsParameter(Class<?> type, Class<?> declaringType, String parameterName) {
		Deque<Type> stack = new ArrayDeque<Type>();
		
		if (!findInheritancePath(stack, type, declaringType))
			throw new IllegalStateException("The given type [" + type.getName() + "] does not inherit from the type [" + declaringType.getName() + "]");
		
		for (Type curType: stack) {
			ParameterizedType parameterizedType = (ParameterizedType)curType;
			
			Class<?> rawType = (Class<?>)parameterizedType.getRawType();

			int parameterIndex = indexOfGenericParameter(rawType, parameterName);
			
			if (parameterIndex == -1)
				throw new IllegalStateException("Class [" + curType.getTypeName() + "] is missing the claimed generics parameter named '" + parameterName + "'");
			
			Type parameterValue = parameterizedType.getActualTypeArguments()[parameterIndex];
			
			if (parameterValue instanceof Class<?>) {
				return parameterValue;
			}
			else if (parameterValue instanceof ParameterizedType) {
				return parameterValue;
			}
			else if (parameterValue instanceof TypeVariable<?>) {
				parameterName = ((TypeVariable<?>)parameterValue).getName();
			}
		}
	
		throw new IllegalStateException("The given type [" + type.getName() + "] does not inherit from the type [" + declaringType.getName() + "]");
	}
	
	private static boolean findInheritancePath(Deque<Type> stack, Type subType, Class<?> matchSuperType) {
		
		final Class<?> clazz;
		
		if (subType instanceof ParameterizedType) {
			clazz = (Class<?>)((ParameterizedType)subType).getRawType();
		}
		else if (subType instanceof Class<?>) {
			clazz = (Class<?>)subType;
		}
		else {
			throw new IllegalStateException("unexpected type in hierarchy: " + subType);
		}
		
		stack.push(subType);
		
		if (clazz == matchSuperType)
			return true;
		
		Iterable<Type> superTypes = Stream.concat( //
				Stream.of(clazz.getGenericSuperclass()).filter(t -> t != null), //  
				Stream.of(clazz.getGenericInterfaces()) // 
		)::iterator;
		
		for (Type superType: superTypes) {
			if (findInheritancePath(stack, superType, matchSuperType))
				return true;
		}

		stack.pop();
		
		return false;
	}
	
	private static int indexOfGenericParameter(Class<?> rawType, String parameterName) {
		int index = 0;
		for (TypeVariable<?> typeVar: rawType.getTypeParameters()) {
			if (typeVar.getName().equals(parameterName))
				return index;
			
			index++;
		}
		
		return -1;
	}

}
