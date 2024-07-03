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
package com.braintribe.wire.api.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.braintribe.wire.api.context.WireContext;
import com.braintribe.wire.api.scope.WireScope;
import com.braintribe.wire.api.space.WireSpace;

/**
 * The Managed annotation is to be placed on any factory method or {@link WireSpace} that should be managed by Wire.
 * A method annotated iwth will be enhanced on bytecode level by Wire in order to create the managed instance within a {@link WireScope} if
 * the {@link WireSpace} containing it is annotated as well.
 *    
 * @author dirk.scheffler
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@Inherited
public @interface Managed {
	/**
	 * The scope that should be used. If this is {@link Scope#inherit} the scope will be inherited via the following chain:
	 * <ol>
	 * 	<li>factory method</li>
	 *  <li>{@link WireSpace}</li>
	 *  <li>{@link WireContext}</li>
	 * </ol>
	 * The default of this will be {@link Scope#inherit}. 
	 */
	Scope value() default Scope.inherit;
}
