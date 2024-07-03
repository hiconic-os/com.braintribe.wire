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
package com.braintribe.wire.impl.contract;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.braintribe.wire.api.context.WireContext;
import com.braintribe.wire.api.space.WireSpace;
import com.braintribe.wire.impl.util.Exceptions;

public class ContractAggregationInvocationHandler implements InvocationHandler {
	private static final Map<Class<? extends WireSpace>, SpaceImplementationEntry> implementations = new ConcurrentHashMap<>();

	private Map<Method, MethodMapping> methodMappings;


	private WireContext<?> wireContext;

	private ContractAggregationInvocationHandler(WireContext<?> wireContext, Map<Method, MethodMapping> methodMappings) {
		this.wireContext = wireContext;
		this.methodMappings = methodMappings;
	}
	
	private static Object[] emptyArgs = new Object[0];
	
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		if (method.getDeclaringClass() == Object.class) {
			return method.invoke(this, args);
		}
			
		MethodMapping methodMapping = methodMappings.get(method);
		args = args != null? args: emptyArgs;
		WireSpace space = wireContext.contract(methodMapping.delegationContract);
		MethodHandle boundMethodHandle = methodMapping.methodHandle.bindTo(space);
		return boundMethodHandle.invokeWithArguments(args);
	}
	
	public static <T extends WireSpace> T create(WireContext<?> context, Class<T> contract) {
		return (T)getImplementation(contract).createInstance(context);
	}
	
	private static SpaceImplementationEntry getImplementation(Class<? extends WireSpace> contract) {
		return implementations.computeIfAbsent(contract, ContractAggregationInvocationHandler::implement);
	}
	
	private static SpaceImplementationEntry implement(Class<? extends WireSpace> contract) {
		try {
			Map<Method, MethodMapping> methodMappings = new HashMap<>();
			
			Lookup publicLookup = MethodHandles.publicLookup();
			for (Class<?> iface: contract.getInterfaces()) {
				Class<? extends WireSpace> aggregatedContract = iface.asSubclass(WireSpace.class);
				
				for (Method method: aggregatedContract.getMethods()) {
					if (!Modifier.isStatic(method.getModifiers())) {
						MethodHandle methodHandle = publicLookup.unreflect(method);
						methodMappings.put(method, new MethodMapping(methodHandle, aggregatedContract));
					}
				}
			}
			
			Class<?> proxyClass = Proxy.getProxyClass(contract.getClassLoader(), contract);
			
			Constructor<?> constructor = proxyClass.getConstructor(InvocationHandler.class);
			MethodHandle constructorHandle = publicLookup.unreflectConstructor(constructor);
			
			return new SpaceImplementationEntry(constructorHandle, methodMappings);
			
		} catch (Exception e) {
			throw Exceptions.unchecked(e, "Error while implementing ContractAggregation: " + contract);
		}
		
	}
	
	private static class SpaceImplementationEntry {
		private MethodHandle constructor;
		private Map<Method, MethodMapping> mappings;
		
		public SpaceImplementationEntry(MethodHandle constructor, Map<Method, MethodMapping> mappings) {
			super();
			this.constructor = constructor;
			this.mappings = mappings;
		}

		public Object createInstance(WireContext<?> context) {
			try {
				InvocationHandler handler = new ContractAggregationInvocationHandler(context, mappings);
				return constructor.invoke(handler);
			} catch (Throwable e) {
				throw Exceptions.unchecked(e);
			}
		}
	}
	
	private static class MethodMapping {
		MethodHandle methodHandle;
		Class<? extends WireSpace> delegationContract;
		public MethodMapping(MethodHandle methodHandle, Class<? extends WireSpace> delegationContract) {
			super();
			this.methodHandle = methodHandle;
			this.delegationContract = delegationContract;
		}
	}
}
