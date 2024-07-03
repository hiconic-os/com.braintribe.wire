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
package com.braintribe.wire.api.util;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

public interface Sets {

	static <E> HashSet<E> set() {
		return new HashSet<E>();
	}

	@SafeVarargs
	static <E> HashSet<E> set(E... entries) {
		HashSet<E> set = new HashSet<E>();
		add(set, entries);
		return set;
	}

	static <E> LinkedHashSet<E> linkedSet() {
		return new LinkedHashSet<E>();
	}

	@SafeVarargs
	static <E> LinkedHashSet<E> linkedSet(E... entries) {
		LinkedHashSet<E> set = new LinkedHashSet<E>();
		add(set, entries);
		return set;
	}

	@SafeVarargs
	static <E> void add(Set<E> set, E... entries) {
		if (set != null && entries != null) {
			for (E entry : entries) {
				set.add(entry);
			}
		}
	}

}
