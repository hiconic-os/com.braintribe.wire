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
package com.braintribe.wire.test.creationlistener.wire.space;

import com.braintribe.wire.api.annotation.Managed;
import com.braintribe.wire.example.TreeNode;
import com.braintribe.wire.test.creationlistener.wire.contract.CreationListenerTestContract;

@Managed
public class CreationListenerTestSpace implements CreationListenerTestContract {
	@Override
	@Managed
	public TreeNode rootInstance() {
		TreeNode bean = new TreeNode();
		bean.subNodes.add(subInstance1());
		bean.subNodes.add(subInstance2());
		return bean;
	}
	
	@Managed
	private TreeNode subInstance1() {
		TreeNode bean = new TreeNode();
		bean.subNodes.add(sub1SubInstance());
		return bean;
	}
	
	@Managed
	private TreeNode subInstance2() {
		TreeNode bean = new TreeNode();
		return bean;
	}
	
	@Managed
	private TreeNode sub1SubInstance() {
		TreeNode bean = new TreeNode();
		return bean;
	}
	
}
