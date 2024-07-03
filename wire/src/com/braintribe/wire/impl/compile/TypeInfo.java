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
package com.braintribe.wire.impl.compile;

import com.braintribe.asm.Opcodes;
import com.braintribe.asm.Type;

class TypeInfo {
	public final String desc;
	public final String internal;
	public final Type type;
	public final boolean isInterface;
	
	public TypeInfo(Class<?> clazz) {
		type = Type.getType(clazz);
		desc = type.getDescriptor();
		internal = clazz.isPrimitive()? null: type.getInternalName();
		isInterface = clazz.isInterface();
	}
	
	public MethodDescriptorBuilder constructor() {
		return new MethodDescriptorBuilderImpl(this, "<init>", Opcodes.INVOKESPECIAL);
	}
	
	public MethodDescriptorBuilder staticMethod(String name) {
		return new MethodDescriptorBuilderImpl(this, name, Opcodes.INVOKESTATIC);
	}
	
	public MethodDescriptorBuilder method(String name) {
		return new MethodDescriptorBuilderImpl(this, name);
	}
}
