// ============================================================================
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
// ============================================================================
// Copyright BRAINTRIBE TECHNOLOGY GMBH, Austria, 2002-2022
// 
// This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
// 
// This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
// 
// You should have received a copy of the GNU Lesser General Public License along with this library; See http://www.gnu.org/licenses/.
// ============================================================================
package com.braintribe.wire.impl.compile;

import java.util.ArrayList;
import java.util.List;

import com.braintribe.asm.MethodVisitor;
import com.braintribe.asm.tree.MethodInsnNode;

class MethodDescriptorImpl implements MethodDescriptor {
	List<TypeInfo> parameters = new ArrayList<>();
	TypeInfo returnType;
	TypeInfo owner;
	String name;
	String desc;
	int opcode;
	
	@Override
	public String asmDesc() {
		if (desc == null) {
			StringBuilder builder = new StringBuilder();
			builder.append('(');
			final int paramCount = parameters.size();
			for (int i = 0; i < paramCount; i++) {
				builder.append(parameters.get(i).desc);
			}
			
			builder.append(')');
			builder.append(returnType.desc);
			
			desc = builder.toString();
		}
		
		return desc;
	}

	@Override
	public TypeInfo owner() {
		return owner;
	}

	@Override
	public String name() {
		return name;
	}
	
	@Override
	public MethodInsnNode invoke() {
		return new MethodInsnNode(opcode, owner.internal, name, asmDesc(), owner.isInterface);
	}
	
	@Override
	public void invoke(MethodVisitor methodVisitor) {
		methodVisitor.visitMethodInsn(opcode, owner.internal, name, asmDesc(), owner.isInterface);
	}
	
	@Override
	public boolean matches(MethodInsnNode methodInsnNode) {
		return methodInsnNode.name.equals(name) && 
				methodInsnNode.owner.equals(owner.internal) && 
				methodInsnNode.desc.equals(asmDesc());
	}
}
