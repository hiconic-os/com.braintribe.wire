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
package com.braintribe.wire.api.module;

import java.util.Collections;
import java.util.List;

import com.braintribe.wire.api.context.WireContext;
import com.braintribe.wire.api.context.WireContextBuilder;
import com.braintribe.wire.api.space.WireSpace;

/**
 * A WireModule helps to build a reproducible wire context
 * 
 * @author Dirk Scheffler
 * */
public interface WireModule {
	default List<WireModule> dependencies() { 
		return Collections.emptyList(); 
	}
	
	/**
	 * This method is being called when the module is applied to some context because it was a transitive
	 * dependency of the {@link WireTerminalModule} with which the {@link WireContext} was been built.
	 * 
	 * @param contextBuilder on this builder the module can configure its aspects
	 */
	default void configureContext(WireContextBuilder<?> contextBuilder) {
		contextBuilder.bindContracts(getClass().getPackage().getName());
	}
}
