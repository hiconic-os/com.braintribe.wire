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
package com.braintribe.wire.test.context;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;

import org.junit.Test;

import com.braintribe.wire.api.Wire;
import com.braintribe.wire.api.context.WireContext;
import com.braintribe.wire.test.context.wirable.ConfigurableTestContext;
import com.braintribe.wire.test.context.wirable.Person;
import com.braintribe.wire.test.context.wirable.TestContext;
import com.braintribe.wire.test.context.wirable.TestLifecycleListener;
import com.braintribe.wire.test.context.wire.contract.ContextSensitivityContract;
import com.braintribe.wire.test.context.wire.contract.MultiParameterInstancesContract;


public class ContextSensitivityTest {

	
	@Test
	public void multiParamContextSensitivity() throws RuntimeException {
		WireContext<MultiParameterInstancesContract> wireCtx = Wire
				.context(MultiParameterInstancesContract.class)
				.bindContracts("com.braintribe.wire.test.context.wire")
				.build();
		
		
		MultiParameterInstancesContract contract = wireCtx.contract();
		
		Object value = contract.someInstance("Hallo", "Welt");
		
		assertThat(value).isEqualTo(Arrays.asList("Hallo", "Welt"));
	}
	
	@Test
	public void contextSensitivity() throws RuntimeException {

		WireContext<ContextSensitivityContract> wireCtx = Wire
				.context(ContextSensitivityContract.class)
				.bindContracts("com.braintribe.wire.test.context.wire")
				.build();
		

		TestContext context1 = new ConfigurableTestContext("Xsi", true, "one");
		TestContext context1Copy = new ConfigurableTestContext("Xsi", true, "one");
		TestContext context2 = new ConfigurableTestContext("Zrc", false, "two");
		
		ContextSensitivityContract contract = wireCtx.contract();
		
		Person p1 = contract.p1();
		
		
		Person person1 = contract.person(context1);
		Person person1Again1 = contract.person(context1Copy);
		Person person1Again2 = contract.person(context1);
		
		Person person2 = contract.person(context2);

		// check identities
		assertThat(person1).isSameAs(person1Again1);
		assertThat(person1).isSameAs(person1Again2);
		assertThat(person1).isNotSameAs(person2);
		
		wireCtx.close(context1);
		
		assertThat(contract.destroyedBeans().contains(person1)).isTrue();
		assertThat(contract.destroyedBeans().contains(person2)).isFalse();
		
		wireCtx.close(context2);
		
		assertThat(contract.destroyedBeans().contains(person2)).isTrue();
		
		TestLifecycleListener lifecycleListener = contract.lifecycleListener();
		
		assertThat(lifecycleListener.getDestroyedBeans().contains(lifecycleListener)).isFalse();
		
		wireCtx.shutdown();
		
		assertThat(lifecycleListener.getDestroyedBeans().contains(lifecycleListener)).isTrue();
	}
}
