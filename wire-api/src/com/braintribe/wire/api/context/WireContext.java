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

import java.util.NoSuchElementException;

import com.braintribe.cfg.ScopeContext;
import com.braintribe.wire.api.annotation.Import;
import com.braintribe.wire.api.module.WireModule;
import com.braintribe.wire.api.space.WireSpace;

/**
 * A WireContext manages a set of {@link WireSpace ManagedSpaces}. There is a master {@link WireSpace} that transitively {@link Import imports}
 * other {@link WireSpace ManagedSpaces} that together make up the set of spaces. Using the Wire library one can build WireContext with a {@link WireContextBuilder}
 * coming from com.braintribe.wire.api.Wire.context(Class&lt;S&gt;) 
 * @author dirk.scheffler
 *
 * @param <S> the type of the master {@link WireSpace contract}
 */
public interface WireContext<S extends WireSpace> extends AutoCloseable {
	
	/**
	 * @return the space for the master {@link WireSpace contract} of this context
	 */
	S contract();
	
	/**
	 * @return The space for the given {@link WireSpace contract} of this context.
	 * @param wireContractClass the class of the contract to be resolved
	 * @throws NoSuchElementException if the contract is not mapped to a space in this context.
	 */
	<C extends WireSpace> C contract(Class<C> wireContractClass);

	/**
	 * finds spaces that are transitively found in the {@link #contract() master} {@link WireSpace}
	 * @param wireSpace The type of the requested {@link WireSpace}
	 * @return The space identified by the type
	 */
	<T extends WireSpace> T findContract(Class<T> wireSpace);
	
	/**
	 * finds the module that hosts the given {@link WireSpace} if there is any otherwise null is returned.
	 */
	<T extends WireSpace> WireModule findModuleFor(Class<T> wireSpace);
	
	/**
	 * Shuts down the context and therefore closes scopes which will lead to destruction of the related managed instances.
	 */
	void shutdown();
	
	@Override
	default void close() {
		shutdown();
	}
	
	void close(ScopeContext scopeContext);
	
	InstancePath currentInstancePath();
	

	/**
	 * @deprecated use {@link #contract()} instead
	 */
	@Deprecated
	default S beans() {
		return contract();
	}
	

}
