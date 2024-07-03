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
package com.braintribe.wire.impl.scope.singleton;

import com.braintribe.wire.api.scope.InstanceHolder;
import com.braintribe.wire.api.scope.InstanceHolderSupplier;
import com.braintribe.wire.api.space.WireSpace;

public class SingleThreadedSingletonBeanHolder extends AbstractSingletonInstanceHolder implements InstanceHolderSupplier {
	private Object bean;
	private boolean initialized;
	private SingleThreadedSingletonScope singletonScope;

	public SingleThreadedSingletonBeanHolder(WireSpace space, SingleThreadedSingletonScope scope, String name) {
		super(space, scope, name);
		singletonScope = scope;
	}
	
	@Override
	public InstanceHolder getHolder(Object context) {
		return this;
	}

	public boolean isInitialized() {
		return initialized; 
	}

	@Override
	public Object get() {
		return bean;
	}

	@Override
	public void publish(Object bean) {
		this.bean = bean;
		this.initialized = true;
		singletonScope.appendBean(this);
	}
	
	@Override
	public void onCreationFailure(Throwable t) {
		this.initialized = false;
		this.bean = null;
	}

	@Override
	public boolean lockCreation() {
		return !initialized;
	}

	@Override
	public void unlockCreation() {
		// noop
	}
	
	@Override
	public String toString() {
		return "SingleThreadedSingletonBeanHolder[name=" + name + "]";
	}

}
