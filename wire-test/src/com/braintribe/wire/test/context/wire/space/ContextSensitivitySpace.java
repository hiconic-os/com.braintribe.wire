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
package com.braintribe.wire.test.context.wire.space;

import static com.braintribe.wire.api.scope.InstanceConfiguration.currentInstance;

import java.util.Set;

import com.braintribe.wire.api.annotation.Managed;
import com.braintribe.wire.api.context.WireContextConfiguration;
import com.braintribe.wire.test.context.wirable.Person;
import com.braintribe.wire.test.context.wirable.TestContext;
import com.braintribe.wire.test.context.wirable.TestLifecycleListener;
import com.braintribe.wire.test.context.wire.contract.ContextSensitivityContract;

@Managed
public class ContextSensitivitySpace implements ContextSensitivityContract {
	
	@Override
	public void onLoaded(WireContextConfiguration configuration) {
		configuration.addLifecycleListener(lifecycleListener());
	}
	
	@Override
	@Managed
	public TestLifecycleListener lifecycleListener() {
		TestLifecycleListener bean = new TestLifecycleListener();
		return bean;
	}
	
	@Override
	@Managed
	public Person person(final TestContext context) {
		Person bean = new Person();
		bean.setName(context.name());
		bean.setImportant(context.important());
		currentInstance().onDestroy(() -> System.out.println("person " + bean.getName() + " (id=" + System.identityHashCode(bean) + ") destroyed"));
		return bean;
	}
	
	@Override
	public Set<Object> destroyedBeans() {
		return lifecycleListener().getDestroyedBeans();
	}
	
	@Managed
	public Person p1() {
		Person bean = new Person();
		bean.setName("p1");
		bean.setPartner(p2());
		return bean;
	}
	
	@Managed
	public Person p2() {
		Person bean = new Person();
		bean.setName("p2");
		bean.setPartner(p1());
		return bean;
	}
}
