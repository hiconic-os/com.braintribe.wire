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
import com.braintribe.wire.api.space.WireSpace;

/**
 * An InstanceConfiguration stands for the additional configuration than can be made on a managed instance in construction.
 * If you are within a {@link Managed} annotated method in a {@link WireSpace} you can easily get the managed instance
 * to the according InstanceConfiguration by calling {@link #currentInstance()}. 
 * @author dirk.scheffler
 */
public interface InstanceConfiguration {
	/**
	 * Configures a callback method that will be called on destruction of the managed instance associated with the InstanceConfiguration
	 */
	void onDestroy(Runnable function);
	void closeOnDestroy(AutoCloseable closable);
	
	InstanceQualification qualification();
	
	/**
	 * This call will give access to the InstanceConfiguration (facet of {@link InstanceHolder}) to allow for further configuration.
	 * 
	 * The call is intentionally implemented by an exception here as the calling of it in {@link Managed} annotated methods
	 * will be replaced by the Wire's bytecode enricher to return the current {@link InstanceConfiguration} associated
	 * with the {@link InstanceHolder} that is managing the identity of the constructed instance. 
	 * @return
	 */
	static InstanceConfiguration currentInstance() { 
		throw new UnsupportedOperationException("this method call should have been replaced by Wire's bytecode enriching");
	};
}
