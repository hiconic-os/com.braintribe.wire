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
package com.braintribe.wire.test.context.wire.space;

import java.util.ArrayList;
import java.util.List;

import com.braintribe.wire.api.annotation.Managed;
import com.braintribe.wire.api.annotation.Scope;
import com.braintribe.wire.test.context.wire.contract.MultiParameterInstancesContract;

@Managed
public class MultiParameterInstancesSpace implements MultiParameterInstancesContract {

	@Managed(Scope.prototype)
	@Override
	public Object someInstance(String s1, String s2) {
		List<String> list = new ArrayList<>();
		
		list.add(s1);
		list.add(s2);
		
		return list;
	}

}
