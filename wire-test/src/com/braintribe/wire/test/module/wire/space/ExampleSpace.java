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
package com.braintribe.wire.test.module.wire.space;

import com.braintribe.wire.api.annotation.Managed;
import com.braintribe.wire.test.module.ConstantHolder;
import com.braintribe.wire.test.module.wire.contract.ExampleContract;

@Managed
public class ExampleSpace implements ExampleContract {

	private void foo() {
		try {
			Integer nums[] = new Integer[]{1,2,3};
			
			for (Integer element: nums) {
				System.out.println(element);
			}
		}
		catch (Exception e) {
			System.out.println("Error");
		}
		finally {
			System.out.println("unlock");
		}
	}
	
	@Managed
	public ConstantHolder constantHolderX() {
		ConstantHolder bean = new ConstantHolder();
		Integer nums[] = new Integer[]{1,2,3};
		
		for (Integer element: nums) {
			System.out.println(element);
		}
		
		return bean;
	}
	
	@Override
	@Managed
	public ConstantHolder constantHolder() {
		ConstantHolder bean = new ConstantHolder();
		Integer nums[] = new Integer[]{1,2,3};
		
		Integer element;
		int i = 0;
		int max;
		max = nums.length;
		Integer numsCopy[] = nums;
		for (; i < max; i++) {
			int J = 0;
			System.out.println(J);
			element = numsCopy[i];
			System.out.println(element);
		}
		
		return bean;
	}

	
	@Managed
	@Override
	public String text() {
		return "Hello World!";
	}

	@Managed
	@Override
	public String text(String prefix) {
		return prefix + "-foobar"; 
	}
	
	
}
