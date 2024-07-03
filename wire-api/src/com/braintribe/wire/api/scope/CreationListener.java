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
import com.braintribe.wire.api.context.WireContextConfiguration;

/**
 * CreationListener are used to intercept the whole creation of a bean including post construction and conditional logic before the actual instantiation.
 * It uses the before and after pointcut AOP logic projected on the methods of CreationListener.
 * 
 * @see WireContextConfiguration#addCreationListener(CreationListener)
 * @author dirk.scheffler
 * @author peter.gazdik
 */
public interface CreationListener {
	/**
	 * This method will be called at the beginning of a {@link Managed managed} method.
	 */
	void onBeforeCreate(InstanceHolder instanceHolder);

	/**
	 * This method will be called right before any exit (i.e. return or uncaught exception) from a {@link Managed managed} method.
	 */
	void onAfterCreate(InstanceHolder instanceHolder);
}
