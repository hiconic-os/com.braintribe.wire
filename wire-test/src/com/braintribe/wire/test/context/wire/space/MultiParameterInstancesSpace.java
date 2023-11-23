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
