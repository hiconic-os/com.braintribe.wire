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
package com.braintribe.wire.impl.scope.prototype;

import com.braintribe.wire.api.scope.InstanceHolder;
import com.braintribe.wire.api.scope.InstanceHolderSupplier;
import com.braintribe.wire.api.scope.WireScope;
import com.braintribe.wire.api.space.WireSpace;
import com.braintribe.wire.impl.scope.AbstractInstanceHolder;

public class PrototypeInstanceHolder extends AbstractInstanceHolder implements InstanceHolderSupplier {
	public PrototypeInstanceHolder(WireSpace space, WireScope scope, String name) {
		super(space, scope, name);
	}
	
	@Override
	public InstanceHolder getHolder(Object context) {
		return this;
	}
	
	@Override
	public Object get() {
		return null;
	}

	@Override
	public void publish(Object bean) {
		// nop
	}
	
	@Override
	public void onCreationFailure(Throwable t) {
		// nop
	}

	@Override
	public boolean lockCreation() {
		return true;
	}

	@Override
	public void unlockCreation() {
		// nop
	}
	
}
