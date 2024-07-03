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

import com.braintribe.wire.api.annotation.Managed;
import com.braintribe.wire.api.context.WireContext;
import com.braintribe.wire.api.context.WireContextConfiguration;
import com.braintribe.wire.api.space.WireSpace;

/**
 * LifecycleListeners can be used to call specific lifecycle methods on managed instances that are defined in interfaces or by annotations. 
 * The calling of LifecycleListeners is triggered by {@link InstanceHolder} and {@link WireScope} associated with a {@link Managed} annotated method. 
 * LifecycleListeners can be configured on a {@link WireContext} during {@link WireSpace#onLoaded(com.braintribe.wire.api.context.WireContextConfiguration)}
 * 
 * @see WireContextConfiguration#addLifecycleListener(LifecycleListener)
 * @author dirk.scheffler
 *
 */
public interface LifecycleListener {
	/**
	 * This method will be called on a registered LifecycleListener right before a managed instance is returned from a {@link Managed} annotated method
	 * @param instanceHolder the holder that holds the managed instance
	 * @param instance The managed instance that has been constructed
	 */
	void onPostConstruct(InstanceHolder instanceHolder, Object instance);
	
	/**
	 * This method will be called on a registered LifecycleListener when the owning {@link WireScope} is closed 
	 * @param instanceHolder the holder that holds the managed instance
	 * @param instance The managed instance that will be destroyed
	 */
	void onPreDestroy(InstanceHolder instanceHolder, Object instance);
}
