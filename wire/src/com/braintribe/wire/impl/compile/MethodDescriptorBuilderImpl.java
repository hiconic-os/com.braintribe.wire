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

class MethodDescriptorBuilderImpl implements MethodDescriptorBuilder {
	MethodDescriptorImpl methodDescriptor = new MethodDescriptorImpl();
	
	public MethodDescriptorBuilderImpl(TypeInfo owner, String name, int opcode) {
		methodDescriptor.owner = owner;
		methodDescriptor.name = name;
		methodDescriptor.opcode = opcode;
	}
	
	public MethodDescriptorBuilderImpl(TypeInfo owner, String name) {
		methodDescriptor.owner = owner;
		methodDescriptor.name = name;
		methodDescriptor.opcode = owner.isInterface? Opcodes.INVOKEINTERFACE: Opcodes.INVOKEVIRTUAL;
	}

	@Override
	public MethodDescriptorBuilder par(TypeInfo typeInfo) {
		methodDescriptor.parameters.add(typeInfo);
		return this;
	}

	@Override
	public MethodDescriptorBuilder par(Class<?> type) {
		methodDescriptor.parameters.add(new TypeInfo(type));
		return this;
	}

	@Override
	public MethodDescriptor ret(Class<?> type) {
		methodDescriptor.returnType = new TypeInfo(type);
		return methodDescriptor;
	}

	@Override
	public MethodDescriptor ret(TypeInfo typeInfo) {
		methodDescriptor.returnType = typeInfo;
		return methodDescriptor;
	}

	@Override
	public MethodDescriptorBuilder opcode(int opcode) {
		methodDescriptor.opcode = opcode;
		return this;
	}
}
