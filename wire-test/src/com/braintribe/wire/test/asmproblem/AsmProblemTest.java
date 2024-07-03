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
package com.braintribe.wire.test.asmproblem;

import org.junit.Test;

import com.braintribe.wire.api.Wire;
import com.braintribe.wire.api.context.WireContext;
import com.braintribe.wire.test.asmproblem.wire.AsmProblemWireModule;
import com.braintribe.wire.test.asmproblem.wire.contract.AsmProblemTestContract;

public class AsmProblemTest {
	@Test
	public void testAsmPassthrough() {
		try (WireContext<AsmProblemTestContract> wireContext = Wire.context(AsmProblemWireModule.INSTANCE)) {
			AsmProblemTestContract contract = wireContext.contract();
			
			contract.helloWorld();
		}
	}
}
