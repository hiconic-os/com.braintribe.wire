// ============================================================================
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
// ============================================================================
// Copyright BRAINTRIBE TECHNOLOGY GMBH, Austria, 2002-2022
// 
// This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General Public
// License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.
// 
// This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
// 
// You should have received a copy of the GNU Lesser General Public License along with this library; See http://www.gnu.org/licenses/.
// ============================================================================
package com.braintribe.wire.test.basic.space;

import static com.braintribe.wire.api.scope.InstanceConfiguration.currentInstance;

import com.braintribe.wire.api.annotation.Import;
import com.braintribe.wire.api.annotation.Managed;
import com.braintribe.wire.api.annotation.Scope;
import com.braintribe.wire.example.AnnoBean;
import com.braintribe.wire.example.ExampleBean;
import com.braintribe.wire.example.Resource;
import com.braintribe.wire.test.basic.contract.Example2Contract;

@Managed 
public class Space1 extends AbstractSpace {
	
	@Import
	private Example2Contract space2;
	
	@Managed
	public ExampleBean exampleBean() {
		ExampleBean bean = new ExampleBean();
		bean.setBean(space2.exampleBean());
		bean.setResource(resource());
		bean.setDeferredBean(() -> annoBean());
		bean.setAnnoBean(annoBean());
		bean.setSomething(something());
		bean.setResourceProvider(this::resource);
		return bean;
	}
	
	@Managed
	public AnnoBean annoBean() {
		AnnoBean bean = new AnnoBean();
		bean.setName("some anno bean");

		currentInstance().onDestroy(bean::extraDestroy);
		
		return bean;
	}
	
	@Managed(Scope.prototype)
	public Resource resource() {
		Resource resource = new Resource();
		
		return resource;
	}
	
}
