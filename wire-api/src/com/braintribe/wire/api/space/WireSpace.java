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

import java.util.function.Consumer;

import com.braintribe.wire.api.annotation.Managed;
import com.braintribe.wire.api.context.WireContextConfiguration;
import com.braintribe.wire.api.reflect.EmptyWireSpaceReflection;
import com.braintribe.wire.api.reflect.WireSpaceReflection;

/**
 * WireSpace subclasses are the namespaces for managed instance factory methods.
 * Such subclasses may be interfaces and in this case the WireSpace is a contract.
 * If the subclass is a class it really implements factory methods which are to be
 * annotated with the {@link Managed} annotation then.
 * 
 * The factory methods follow a certain pattern.
 * 
 * <pre>
 * <code>
 * 		
 *  {@literal @}Managed 
 *  public TypeOfInstance instanceName()
 *    // right here will be the lock and identity management done by Wire's bytecode enrichment
 *  
 *    // creation with constructor or other ways
 *    TypeOfInstance instance = new TypeOfInstance();
 *    
 *    // right here will be the eager publishing done by Wire's bytecode enrichment
 * 
 *    // preparation of the instance
 *    instance.setX("foo");
 *    instance.setY("bar");
 *    instance.addZ(1);
 *    instance.addZ(2);
 *    instance.addZ(3);
 * 
 *    // right here will be the post contruct notification done by Wire's bytecode enrichment
 *    
 *    // return the prepared
 *    return instance;
 *    
 *    // right here will be the unlocking done by Wire's bytecode enrichment
 *  }
 * </code>
 * </pre>
 * 
 * @author dirk.scheffler
 *
 */
public interface WireSpace {
	/**
	 * This method will be called right after the WireSpace was created and its imports were loaded.
	 * A WireSpace can configure additional expertise on the {@link WireContextConfiguration}. 
	 */
	default void onLoaded(@SuppressWarnings("unused") WireContextConfiguration configuration) {
		//intentionally left empty
	}
	
	default WireSpaceReflection reflect() {
		return EmptyWireSpaceReflection.INSTANCE;
	}
	
}
