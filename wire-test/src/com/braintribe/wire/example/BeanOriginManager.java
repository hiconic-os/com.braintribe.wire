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
package com.braintribe.wire.example;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.braintribe.wire.api.scope.InstanceHolder;
import com.braintribe.wire.api.scope.LifecycleListener;

public class BeanOriginManager implements LifecycleListener {
	private Map<Object, InstanceHolder> originMap = new ConcurrentHashMap<>();
	
	@Override
	public void onPostConstruct(InstanceHolder beanHolder, Object bean) {
		originMap.put(bean, beanHolder);
	}
	
	@Override
	public void onPreDestroy(InstanceHolder beanHolder, Object bean) {
		originMap.remove(bean);
	}
	
	public InstanceHolder resolveBeanHolder(Object instance) {
		return originMap.get(instance);
	}
}
