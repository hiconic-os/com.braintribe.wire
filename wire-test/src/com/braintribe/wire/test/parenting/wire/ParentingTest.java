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
package com.braintribe.wire.test.parenting.wire;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.braintribe.wire.api.Wire;
import com.braintribe.wire.api.context.WireContext;
import com.braintribe.wire.test.parenting.wire.derived.ParentingTestDerivedWireModule;
import com.braintribe.wire.test.parenting.wire.derived.contract.ParentingTestDerivedContract;
import com.braintribe.wire.test.parenting.wire.parent.ParentingTestParentWireModule;
import com.braintribe.wire.test.parenting.wire.parent.contract.ParentingTestParentContract;

public class ParentingTest {
	
	@Test
	public void parentingTest() throws Exception {

		try (WireContext<ParentingTestParentContract> parentContext = Wire.context(ParentingTestParentWireModule.INSTANCE)) {
			try (WireContext<ParentingTestDerivedContract> derivedContext = Wire.context(new ParentingTestDerivedWireModule(parentContext))) {

				ParentingTestDerivedContract dervivedContract = derivedContext.contract();
				String derivedText = dervivedContract.textDerived();
				String overriddenText = dervivedContract.textOverridden();
				
				assertThat(derivedText).isEqualTo("derived");
				assertThat(overriddenText).isEqualTo("overridden");
			}
		}
	}
}
