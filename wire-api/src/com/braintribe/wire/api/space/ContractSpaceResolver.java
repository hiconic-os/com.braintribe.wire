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
package com.braintribe.wire.api.space;

import com.braintribe.wire.api.context.WireContext;

/**
 * A ContractSpaceResolver is an expert to conclude from a contract {@link WireSpace} class (being an interface)
 * to the name of the actual implementing {@link WireSpace} class that would then be loaded in a special class loader 
 * that can also do Wire's enriching of the implementing class on demand if that enriching was not done a packaging time.
 * @author dirk.scheffler
 *
 */
public interface ContractSpaceResolver {
	
	default ContractResolution resolveContractSpace(WireContext<?> wireContext, Class<? extends WireSpace> contractSpaceClass) {
		return resolveContractSpace(contractSpaceClass);
	}
	
	ContractResolution resolveContractSpace(Class<? extends WireSpace> contractSpaceClass);
}
