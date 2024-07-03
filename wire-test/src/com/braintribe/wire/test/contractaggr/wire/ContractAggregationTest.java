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
package com.braintribe.wire.test.contractaggr.wire;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import com.braintribe.wire.api.Wire;
import com.braintribe.wire.api.context.WireContext;
import com.braintribe.wire.test.contractaggr.wire.contract.ContractAggregationTestContract;

public class ContractAggregationTest {

	@Test
	public void testAggregation() throws Exception {
		try (WireContext<ContractAggregationTestContract> context = Wire.context(ContractAggregationTestWireModule.INSTANCE)) {
			
			ContractAggregationTestContract contract = context.contract();
			
			System.out.println(contract.toString());
			System.out.println(contract.hashCode());
			
			Assertions.assertThat(contract.value1()).isEqualTo("value1");
			Assertions.assertThat(contract.value2()).isEqualTo("value2");
			
		}
	}

}
