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
package com.braintribe.wire.test.aggregate.wire.space;

import com.braintribe.wire.api.annotation.Managed;
import com.braintribe.wire.api.annotation.Scope;
import com.braintribe.wire.api.context.WireContextConfiguration;
import com.braintribe.wire.test.aggregate.payload.TestContext;
import com.braintribe.wire.test.aggregate.payload.TestNode;
import com.braintribe.wire.test.aggregate.wire.LifecycleMonitor;
import com.braintribe.wire.test.aggregate.wire.contract.AggregateScopeTestContract;

@Managed
public class AggregateScopeTestSpace implements AggregateScopeTestContract {
	
	@Override
	public void onLoaded(WireContextConfiguration configuration) {
		configuration.addLifecycleListener(monitor());
	}
	
	@Managed @Override
	public TestNode root(TestContext context) {
		TestNode bean = new TestNode("root");
		bean.setNext(sub("sub"));
		bean.setAltNext(sub("sub"));
		bean.setExtraNext(sub("subextra"));
		return bean;
	}
	
	@Managed(Scope.caller)
	private TestNode sub(String name) {
		TestNode bean = new TestNode(name);
		bean.setNext(sub1());
		return bean;
	}
	
	@Override
	@Managed(Scope.caller)
	public TestNode sub1() {
		TestNode bean = new TestNode("sub1");
		bean.setNext(sub2());
		return bean;
	}
	
	@Managed(Scope.caller)
	private TestNode sub2() {
		TestNode bean = new TestNode("sub2");
		bean.setNext(sub1());
		return bean;
	}
	
	@Override
	@Managed
	public LifecycleMonitor monitor() {
		LifecycleMonitor bean = new LifecycleMonitor();
		return bean;
	}
}
