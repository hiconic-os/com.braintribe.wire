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
package com.braintribe.wire.api.context;

import com.braintribe.cfg.ScopeContext;
import com.braintribe.wire.api.scope.InstanceHolder;
import com.braintribe.wire.api.scope.WireScope;
import com.braintribe.wire.api.space.WireSpace;

/**
 * This interface is used internally by {@link InstanceHolder} implementations
 * @author dirk.scheffler
 *
 */
public interface InternalWireContext {
	/**
	 * Resolves a {@link WireSpace} by its class. If it is already loaded it will be returned 
	 * otherwise it will be loaded and then returned.
	 */
	<T extends WireSpace> T resolveSpace(Class<T> class1);
	
	/**
	 * Resolves a {@link WireScope} by its class. If it is already instantiated it will be returned 
	 * otherwise it will be instantiated and then returned.
	 */
	WireScope getScope(Class<? extends WireScope> scopeClass);
	
	/**
	 * {@link InstanceHolder} implementations should forward their according notifications to this method
	 * @param instanceHolder the holder that is used to manage the instance
	 * @param instance The instance being destroyed
	 */
	void onPostConstruct(InstanceHolder instanceHolder, Object instance);
	
	/**
	 * {@link InstanceHolder} implementations should forward their according notifications to this method
	 * @param instanceHolder the holder that is used to manage the instance
	 * @param instance The instance being destroyed
	 */
	void onPreDestroy(InstanceHolder instanceHolder, Object instance);

	boolean lockCreation(InstanceHolder instanceHolder);

	void unlockCreation(InstanceHolder instanceHolder);
	
	InstancePath currentInstancePath();
	
	ScopeContextHolders getScopeForContext(ScopeContext context);
	
	void close(ScopeContext scopeContext);
}
