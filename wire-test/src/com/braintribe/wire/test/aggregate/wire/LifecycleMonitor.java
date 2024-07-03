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
package com.braintribe.wire.test.aggregate.wire;

import java.util.LinkedHashSet;
import java.util.Set;

import com.braintribe.wire.api.scope.InstanceHolder;
import com.braintribe.wire.api.scope.LifecycleListener;

public class LifecycleMonitor implements LifecycleListener {
	private Set<Object> destroyedInstances = new LinkedHashSet<>();

	@Override
	public void onPostConstruct(InstanceHolder instanceHolder, Object instance) {
		// noop
		
	}

	@Override
	public void onPreDestroy(InstanceHolder instanceHolder, Object instance) {
		destroyedInstances.add(instance);
	}
	
	public Set<Object> getDestroyedInstances() {
		return destroyedInstances;
	}

}
