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

import java.util.Deque;
import java.util.Iterator;

import com.braintribe.wire.api.context.InstancePath;
import com.braintribe.wire.api.scope.InstanceHolder;

public class DequeInstancePath implements InstancePath {
	private Deque<InstanceHolder> stack;
	
	public DequeInstancePath(Deque<InstanceHolder> stack) {
		super();
		this.stack = stack;
	}

	@Override
	public Iterator<InstanceHolder> iterator() {
		return stack.iterator();
	}
	
	@Override
	public Iterator<InstanceHolder> descendingIterator() {
		return stack.descendingIterator();
	}

	@Override
	public int length() {
		return stack.size();
	}
	
	@Override
	public boolean isEmpty() {
		return stack.isEmpty();
	}

	@Override
	public InstanceHolder lastElement() {
		return stack.peekLast();
	}

	@Override
	public InstanceHolder firstElement() {
		return stack.peekFirst();
	}

}
