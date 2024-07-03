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
package com.braintribe.wire.test.basic.space;

import com.braintribe.wire.api.annotation.Import;
import com.braintribe.wire.api.annotation.Managed;
import com.braintribe.wire.api.annotation.Scope;
import com.braintribe.wire.example.ExampleBean;
import com.braintribe.wire.test.basic.contract.Example2Contract;

@Managed(Scope.prototype)
public class Example2Space extends AbstractSpace implements Example2Contract {
	
	@Import
	protected Space1 space1;
	
	@Managed(Scope.singleton)
	public ExampleBean exampleBean() {
		ExampleBean bean = new ExampleBean();
		bean.setBean(space1.exampleBean());
		bean.setSomething(something());
		return bean;
	}
	
	@Managed(Scope.inherit)
	public ExampleBean anotherExampleBean() {
		ExampleBean bean = new ExampleBean();
		return bean;
	}
	
}
