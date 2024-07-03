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
package com.braintribe.wire.test.creationlistener;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import com.braintribe.wire.api.Wire;
import com.braintribe.wire.api.context.WireContext;
import com.braintribe.wire.test.creationlistener.wire.CreationListenerTestWireModule;
import com.braintribe.wire.test.creationlistener.wire.contract.CreationListenerTestContract;

public class WireCreationListenerTest {

	@Test
	public void testCreationListeners() {
		InstanceCollector instanceCollector = new InstanceCollector();
		
		try (WireContext<CreationListenerTestContract> context = Wire.contextBuilder(CreationListenerTestWireModule.INSTANCE).creationListener(instanceCollector).build()) {
			
			context.contract().rootInstance();
			
			Assertions.assertThat(instanceCollector.beforeBeanNames).containsExactly("rootInstance", "subInstance1", "sub1SubInstance", "subInstance2");
			Assertions.assertThat(instanceCollector.afterBeanNames).containsExactly("sub1SubInstance", "subInstance1", "subInstance2", "rootInstance");
		}
	}
}
