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
package com.braintribe.wire.test.basic.space;

import com.braintribe.wire.api.WireException;
import com.braintribe.wire.api.annotation.Import;
import com.braintribe.wire.api.annotation.Managed;
import com.braintribe.wire.api.annotation.Scope;
import com.braintribe.wire.api.scope.InstanceConfiguration;
import com.braintribe.wire.example.BeanOriginManager;
import com.braintribe.wire.example.ExampleBean;
import com.braintribe.wire.example.Resource;
import com.braintribe.wire.test.basic.contract.MainContract;

@Managed
public class MainSpace implements MainContract {
	
	private String memberVariable = "initialized";
	
	@Import
	protected MetaSpace metaSpace;
	
	@Import
	public Space1 space1;
	
	@Import
	public Example2Space space2;
	
	public MainSpace() {
		System.out.println("Hello World");
	}
	
	@Override
	public String memberVariable() {
		return memberVariable;
	}
	
	@Override
	public BeanOriginManager beanOriginManager() {
		return metaSpace.beanOriginManager();
	}
	
	@Managed
	public ExampleBean bean1() {
		ExampleBean bean = new ExampleBean();
		bean.setBean(bean2());
		return bean;
	}
	
	@Managed
	public ExampleBean bean2() {
		ExampleBean bean = new ExampleBean();
		bean.setBean(bean1());
		return bean;
	}
	
	
	@Override
	public ExampleBean example() {
		return space1.exampleBean();
	}
	
	@Override
	public ExampleBean anotherExample() {
		return space2.anotherExampleBean();
	}
	
	@Override
	public Resource resource() {
		return space1.resource();
	}
	
	
	@Managed
	public String string() {
		String bean;
		
		try {
			bean = "Hallo";
			Integer.parseInt("1");
		}
		catch (Exception e) {
			throw new WireException("Hello World");
		}
		return bean;
	}
	
	@Managed(Scope.aggregate)
	public ExampleBean a1() {
		ExampleBean bean = new ExampleBean();
		bean.setBean(a2());
		return bean;
	}
	
	@Managed()
	public ExampleBean a1Holder() {
		ExampleBean bean = new ExampleBean();
		bean.setBean(a1());
		return bean;
	}
	
	@Managed(Scope.aggregate)
	public ExampleBean a2() {
		ExampleBean bean = new ExampleBean();
		bean.setBean(a1());
		return bean;
	}
	
	@Managed
	@Override
	public String qualification() {
		InstanceConfiguration instanceConfiguration = InstanceConfiguration.currentInstance();
		String bean = instanceConfiguration.qualification().space().getClass().getName() + "/" + instanceConfiguration.qualification().name();
		return bean;
	}
	
	@Override
	@Managed
	public String paramTest(Double param) {
		return "test-" + param;
	}
}
