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
package com.braintribe.wire.api.scope;

import com.braintribe.wire.api.context.InternalWireContext;
import com.braintribe.wire.api.context.WireContext;
import com.braintribe.wire.api.space.WireSpace;

/**
 * WireScopes control {@link InstanceHolder instance holders} and the lifecycle management of the instances associated with them. 
 * Wire users can write custom scopes.
 * @author dirk.scheffler
 *
 */
public interface WireScope extends AutoCloseable {
	InstanceHolderSupplier createHolderSupplier(WireSpace managedSpace, String name, InstanceParameterization parameterization);
	
	/**
	 * Will be called by the {@link WireContext} controlling this scope to link itself to the scope 
	 */
	void attachContext(InternalWireContext context);
	
	/**
	 * Returns the associated {@link WireContext} as {@link InternalWireContext} 
	 */
	InternalWireContext	getContext();
}
