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
package com.braintribe.wire.test.spaceresolution;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.braintribe.wire.api.Wire;
import com.braintribe.wire.api.context.WireContext;
import com.braintribe.wire.test.spaceresolution.wire.ProxyResolutionTestModule;
import com.braintribe.wire.test.spaceresolution.wire.contract.AlternativeSpaceResolutionFirstContract;
import com.braintribe.wire.test.spaceresolution.wire.contract.ContractResolutionFirstContract;
import com.braintribe.wire.test.spaceresolution.wire.contract.ExampleProxyContract;
import com.braintribe.wire.test.spaceresolution.wire.contract.ProxyResolutionTestContract;
import com.braintribe.wire.test.spaceresolution.wire.contract.SpaceResolutionContract;
import com.braintribe.wire.test.spaceresolution.wire.contract.SpaceResolutionFirstContract;


public class SpaceResolutionTest {
	
	@Test
	public void contractVsSpaceResolution() throws RuntimeException {

		WireContext<SpaceResolutionContract> context = Wire
				.contextWithStandardContractBinding(SpaceResolutionContract.class)
				.bindContract(AlternativeSpaceResolutionFirstContract.class, "com.braintribe.wire.test.spaceresolution.wire.space.SpaceResolutionFirstSpace")
				.build();

		SpaceResolutionContract contract = context.contract();
		
		ContractResolutionFirstContract importedByContract1 = contract.importedByContract1();
		ContractResolutionFirstContract importedBySpace1 = contract.importedBySpace1();
		SpaceResolutionFirstContract importedByContract2 = contract.importedByContract2();
		SpaceResolutionFirstContract importedBySpace2 = contract.importedBySpace2();
		AlternativeSpaceResolutionFirstContract alternativeImportedByContract2 = contract.importedByAlterativeContract2();

		String instanceFromContract1 = importedByContract1.instance();
		String instanceFromSpace1 = importedBySpace1.instance();
		String instanceFromContract2 = importedByContract2.instance();
		String instanceFromSpace2 = importedBySpace2.instance();
		String instanceFromAlternativeContract2 = alternativeImportedByContract2.instance();
		
		assertThat(importedByContract1).isSameAs(importedBySpace1);
		assertThat(instanceFromContract1).isSameAs(instanceFromSpace1);
		assertThat(importedByContract2).isSameAs(importedBySpace2);
		assertThat(instanceFromContract2).isSameAs(instanceFromSpace2);
		assertThat(instanceFromContract2).isSameAs(instanceFromAlternativeContract2);
		
		context.shutdown();
	}
	
	@Test
	public void testProxyContractResolution() {
		WireContext<ProxyResolutionTestContract> context = Wire.context(ProxyResolutionTestModule.INSTANCE);

		ProxyResolutionTestContract contract = context.contract();
		
		ExampleProxyContract example = contract.example();
		assertThat(example.one()).isEqualTo("one");
		assertThat(example.two()).isEqualTo("two");
		assertThat(example.three()).isEqualTo("three");
		
		context.shutdown();
	}
}
