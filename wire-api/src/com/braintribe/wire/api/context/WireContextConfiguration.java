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

import com.braintribe.wire.api.scope.CreationListener;
import com.braintribe.wire.api.scope.LifecycleListener;
import com.braintribe.wire.api.space.WireSpace;

/**
 * This interface allows a {@link WireSpace} to influence the way a {@link WireContext} works. 
 * It is passed to the {@link WireSpace#onLoaded(WireContextConfiguration)} method in order to allow for eager configuration. 
 * @author dirk.scheffler
 *
 */
public interface WireContextConfiguration {
	/**
	 * configures a {@link LifecycleListener} that will be called on post construction and on destruction of managed instances
	 */
	void addLifecycleListener(LifecycleListener listener);
	
	/**
	 * removes a configured {@link LifecycleListener} from this context.
	 */
	void removeLifecycleListener(LifecycleListener listener);
	
	/**
	 * configures a {@link CreationListener} that will be called before and after construction of managed instances
	 */
	void addCreationListener(CreationListener listener);

	/**
	 * removes a configured {@link CreationListener} from this context.
	 */
	void removeCreationListener(CreationListener listener);
}
