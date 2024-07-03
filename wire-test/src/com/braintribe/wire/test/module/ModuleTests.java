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
package com.braintribe.wire.test.module;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import com.braintribe.wire.api.Wire;
import com.braintribe.wire.api.context.WireContext;
import com.braintribe.wire.test.module.wire.ExampleModule;
import com.braintribe.wire.test.module.wire.contract.ExampleContract;

public class ModuleTests {

	@Test
	public void testSimple() throws Exception {
		try (WireContext<ExampleContract> context = Wire.context(ExampleModule.INSTANCE)) {
			ExampleContract exampleContract = context.contract();
			Assertions.assertThat(exampleContract.text()).as("Could not get the excepted managed instance").isEqualTo("Hello World!");
			Assertions.assertThat(context.contract().text("pre")).as("Could not get the excepted managed instance").isEqualTo("pre-foobar");
		}
	}
}
