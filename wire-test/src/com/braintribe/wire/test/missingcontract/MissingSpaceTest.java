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
package com.braintribe.wire.test.missingcontract;

import com.braintribe.wire.api.Wire;
import com.braintribe.wire.api.context.WireContext;
import com.braintribe.wire.test.missingcontract.wire.MissingSpaceWireModule;
import com.braintribe.wire.test.missingcontract.wire.contract.MissingSpaceMainContract;

public class MissingSpaceTest {

	public void testSimple() throws Exception {
		try (WireContext<MissingSpaceMainContract> context = Wire.context(MissingSpaceWireModule.INSTANCE)) {
			context.contract().foo();
//			Assertions.assertThat(context.contract().text()).as("Could not get the excepted managed instance").isEqualTo("Hello World!");
		}
	}
}
