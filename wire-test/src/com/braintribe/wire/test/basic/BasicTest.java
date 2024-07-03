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
package com.braintribe.wire.test.basic;

import static org.assertj.core.api.Assertions.assertThat;

import java.awt.Button;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.braintribe.wire.api.Wire;
import com.braintribe.wire.api.context.WireContext;
import com.braintribe.wire.api.scope.InstanceHolder;
import com.braintribe.wire.api.scope.LifecycleListener;
import com.braintribe.wire.example.AnnoBean;
import com.braintribe.wire.example.ExampleBean;
import com.braintribe.wire.example.Resource;
import com.braintribe.wire.test.basic.contract.IntermediateConstructionContract;
import com.braintribe.wire.test.basic.contract.MainContract;
import com.braintribe.wire.test.basic.space.MainSpace;


public class BasicTest {
	/**
	 * tests the following features
	 * <ul>
	 * 	<li>bean space contract resolution</li>
	 * 	<li>cyclic bean space import</li>
	 * 	<li>cyclic singleton scoped bean reference</li>
	 * </ul>
	 */
	
	public boolean isX(String name) {
		return true;
	}
	
	public boolean isY(String name) {
		return true;
	}
	
	@Test
	public void basicFunctionality() throws RuntimeException {

		WireContext<MainContract> context = Wire
				.context(MainContract.class)
				.bindContracts("com.braintribe.wire.test.basic")
				.build();
		
		
		ExampleBean exampleBean = context.contract().bean1();
		Resource resourceDirect = context.contract().resource();
		ExampleBean exampleBeanWithResource = context.contract().example();
		
		Resource resource = exampleBeanWithResource.getResourceProvider().get();
		
		ExampleBean exampleBean1 = context.contract().example();
		
		ExampleBean exampleBean2 = exampleBean1.getBean();
		ExampleBean exampleBean1Candidate = exampleBean2.getBean();
		
		AnnoBean annoBean1 = exampleBeanWithResource.getAnnoBean();
		AnnoBean annoBean2 = exampleBeanWithResource.getDeferredBean();
		
		InstanceHolder beanHolder = context.contract().beanOriginManager().resolveBeanHolder(exampleBean);
		System.out.println(beanHolder.space().getClass());
		System.out.println(beanHolder.name());
		
		ExampleBean anotherExampleBean = context.contract().anotherExample();
		
		ExampleBean a1 = context.contract().a1();
		assertThat(a1).isSameAs(a1.getBean().getBean());
		
		// check cyclic reference
		assertThat(exampleBean1).isSameAs(exampleBean1Candidate);

		// check BeanSpace implementation inheritance
		assertThat(exampleBean1.getSomething()).isNotNull();
		assertThat(exampleBean2.getSomething()).isNotNull();
		assertThat(exampleBean2.getSomething()).isNotSameAs(exampleBean1.getSomething());
		assertThat(context.contract().qualification()).as("InstanceConfiguration qualification did not work").isEqualTo(MainSpace.class.getName() + "/qualification");
		
		String s1 = context.contract().paramTest(2D);
		String s2 = context.contract().paramTest(2D);
		String s3 = context.contract().paramTest(3D);
		
		assertThat(s1).isEqualTo("test-2.0");
		assertThat(s2).isEqualTo("test-2.0");
		assertThat(s1).isSameAs(s2);
		assertThat(s3).isEqualTo("test-3.0");
		
		// assertThat(context.contract().memberVariable()).isEqualTo("initialized");
		context.shutdown();
	}
	
	@Test
	public void aggregateScope() throws RuntimeException {
		
		List<Object> destroyedInstances = new ArrayList<>();
		
		LifecycleListener lifecycleListener = new LifecycleListener() {
			
			@Override
			public void onPreDestroy(InstanceHolder instanceHolder, Object instance) {
				destroyedInstances.add(instance);
			}
			
			@Override
			public void onPostConstruct(InstanceHolder instanceHolder, Object instance) {
				// TODO Auto-generated method stub
				
			}
		};
		
		try (WireContext<MainContract> context = Wire
				.context(MainContract.class)
				.bindContracts("com.braintribe.wire.test.basic")
				.lifecycleListener(lifecycleListener)
				.build()) {
			
			ExampleBean a1Holder = context.contract().a1Holder();
			ExampleBean a1 = a1Holder.getBean();
			assertThat(a1).isSameAs(a1.getBean().getBean());
			
		}
		
		System.out.println(destroyedInstances);
		
	}
	
	@Test
	public void intermediateConstructionTest() throws Exception {
		
		WireContext<IntermediateConstructionContract> context = Wire
				.context(IntermediateConstructionContract.class)
				.bindContracts("com.braintribe.wire.test.basic")
				.build();

		IntermediateConstructionContract contract = context.contract();
		ExampleBean button = contract.button();
		System.out.println("Done: "+button);
		
	}

}
