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
package com.braintribe.wire.impl.module;

import java.util.LinkedHashSet;

import com.braintribe.wire.api.context.WireContextBuilder;
import com.braintribe.wire.api.module.WireModule;
import com.braintribe.wire.impl.util.Exceptions;

public abstract class TransitiveModuleConfigurer {
	public static void configure(WireContextBuilder<?> contextBuilder, WireModule primaryModule, WireModule... modules) {
		LinkedHashSet<WireModule> collectedModules = new LinkedHashSet<>();
		
		collect(contextBuilder, primaryModule, collectedModules);

		for (WireModule module: modules)
			collect(contextBuilder, module, collectedModules);
		
		for (WireModule currentModule: collectedModules) {
			contextBuilder.registerModule(currentModule);
			currentModule.configureContext(contextBuilder);
		}
	}
	
	public static void configure(WireContextBuilder<?> contextBuilder, WireModule module) {
		LinkedHashSet<WireModule> modules = new LinkedHashSet<>();
		
		collect(contextBuilder, module, modules);
		
		for (WireModule currentModule: modules) {
			contextBuilder.registerModule(currentModule);
			currentModule.configureContext(contextBuilder);
		}
	}

	private static void collect(WireContextBuilder<?> contextBuilder, WireModule module, LinkedHashSet<WireModule> modules) {
		if (modules.contains(module))
			return;
		
		for (WireModule moduleDependency: module.dependencies()) {
			try {
				collect(contextBuilder, moduleDependency, modules);
			} catch(Throwable e) {
				String depName = ""+moduleDependency;
				if (moduleDependency != null) {
					depName = depName + "(" + moduleDependency.getClass().getName() + ")";
				}
				String moduleName = ""+module + "(" + module.getClass().getName() + ")";
				throw Exceptions.unchecked(e, "Error while collecting module dependency "+depName+" of module "+moduleName);
			}
		}
		
		modules.add(module);
	}
	
}
