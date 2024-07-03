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
package com.braintribe.wire.impl.reflect;

import java.lang.reflect.Type;

import com.braintribe.wire.api.annotation.Scope;
import com.braintribe.wire.api.reflect.ManagedInstanceReflection;
import com.braintribe.wire.api.space.WireSpace;

public class ManagedInstanceReflectionImpl implements ManagedInstanceReflection {

	@Override
	public Scope scope() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String name() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public WireSpace space() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isPublic() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isParameterized() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Type[] parameterTypes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Type returnType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T instance() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T instance(Object... args) {
		// TODO Auto-generated method stub
		return null;
	}

}
