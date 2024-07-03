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
package com.braintribe.wire.impl.context;

import java.util.Collections;
import java.util.Iterator;

import com.braintribe.wire.api.context.InstancePath;
import com.braintribe.wire.api.scope.InstanceHolder;

public class EmptyInstancePath implements InstancePath {
	
	private static final EmptyInstancePath INSTANCE = new EmptyInstancePath();

	@Override
	public Iterator<InstanceHolder> iterator() {
		return Collections.emptyIterator();
	}

	@Override
	public int length() {
		return 0;
	}

	@Override
	public InstanceHolder lastElement() {
		return null;
	}

	@Override
	public InstanceHolder firstElement() {
		return null;
	}

	@Override
	public boolean isEmpty() {
		return true;
	}

	@Override
	public Iterator<InstanceHolder> descendingIterator() {
		return Collections.emptyIterator();
	}

}
