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
package com.braintribe.wire.impl.contract;

import com.braintribe.wire.api.context.WireContext;
import com.braintribe.wire.api.space.ContractResolution;
import com.braintribe.wire.api.space.ContractSpaceResolver;
import com.braintribe.wire.api.space.WireSpace;

public class NameConventionContractSpaceResolver implements ContractSpaceResolver {

	private String contractPrefix;
	private String contractSuffix;
	
	private String implementationPrefix;
	private String implementationSuffix;
	
	private boolean lenient;
	
	public NameConventionContractSpaceResolver(String contractPrefix, String contractSuffix,
			String implementationPrefix, String implementationSuffix, boolean lenient) {
		this.contractPrefix = contractPrefix;
		this.contractSuffix = contractSuffix;
		this.implementationPrefix = implementationPrefix;
		this.implementationSuffix = implementationSuffix;
		this.lenient = lenient;
	}
	
	@Override
	public ContractResolution resolveContractSpace(WireContext<?> wireContext, Class<? extends WireSpace> contractSpaceClass) {
		return resolveContractSpace(contractSpaceClass);
	}

	@Override
	public ContractResolution resolveContractSpace(Class<? extends WireSpace> contractSpaceClass) {
		String contractName = contractSpaceClass.getName();
		if (!(contractName.startsWith(contractPrefix) && contractName.endsWith(contractSuffix))) {
			if (lenient) 
				return null;
			throw new IllegalStateException("Contract [" + contractSpaceClass.getName() + "] is not suitable for the naming pattern " + contractPrefix + "*" + contractSuffix);
		}
		
		String implementationName = implementationPrefix + 
				contractName.substring(contractPrefix.length(), contractName.length() - contractSuffix.length()) +
				implementationSuffix; 
		
		return new StandardContractResolution(implementationName);
	}
}
