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
package com.braintribe.wire.api;

import com.braintribe.wire.api.context.WireContext;
import com.braintribe.wire.api.context.WireContextBuilder;
import com.braintribe.wire.api.module.WireModule;
import com.braintribe.wire.api.module.WireTerminalModule;
import com.braintribe.wire.api.space.WireSpace;
import com.braintribe.wire.impl.context.WireContextBuilderImpl;
import com.braintribe.wire.impl.module.TransitiveModuleConfigurer;

public interface Wire {
	public static <S extends WireSpace> WireContext<S> context(WireTerminalModule<S> module) {		
		return contextBuilder( module).build();
	}

	public static <S extends WireSpace> WireContext<S> context(WireTerminalModule<S> module, WireModule... additionalModules) {		
		return contextBuilder( module, additionalModules).build();
	}
	
	public static <S extends WireSpace> WireContextBuilder<S> context(Class<S> wireSpace) {
		if (!wireSpace.isInterface())
			throw new IllegalArgumentException("only contracts (WireSpace interfaces) can be used as top level space for WireContexts. " + wireSpace + " is a class");
		
		return new WireContextBuilderImpl<>(wireSpace);
	}
	
	/**
	 * Calls {@link #context(Class)} and {@link WireContextBuilder#bindContracts(String)} with the parent package of the
	 * given {@code wireSpace}.
	 */
	public static <S extends WireSpace> WireContextBuilder<S> contextWithStandardContractBinding(Class<S> wireSpace) {
		String packageName = wireSpace.getPackage().getName();
		String basePackage = packageName.substring(0, packageName.lastIndexOf('.'));

		return context(wireSpace).bindContracts(basePackage);

	}
	
	public static <S extends WireSpace> WireContextBuilder<S> contextBuilder(WireTerminalModule<S> module) {
		WireContextBuilder<S> contextBuilder = context(module.contract());
		TransitiveModuleConfigurer.configure(contextBuilder, module);
		return contextBuilder;
	}
	
	public static <S extends WireSpace> WireContextBuilder<S> contextBuilder(WireTerminalModule<S> module, WireModule... additionalModules) {
		WireContextBuilder<S> contextBuilder = context(module.contract());
		TransitiveModuleConfigurer.configure(contextBuilder, module, additionalModules);
		return contextBuilder;
	}
}
