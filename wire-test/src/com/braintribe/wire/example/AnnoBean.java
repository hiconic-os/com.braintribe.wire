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
package com.braintribe.wire.example;

import com.braintribe.cfg.DestructionAware;
import com.braintribe.cfg.InitializationAware;

public class AnnoBean implements InitializationAware, DestructionAware {
	private String name;
	
	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public void preDestroy() {
		System.out.println("AnnoBean " + name + " destroyed");
	}
	
	public void extraDestroy() {
		System.out.println("AnnoBean " + name + " extra destroyed");
	}
	
	public void anomalDestroy(String s) {
		System.out.println("AnnoBean " + name + " extra destroyed");
	}

	@Override
	public void postConstruct() {
		System.out.println("AnnoBean " + name + " constructed");
	}
}
