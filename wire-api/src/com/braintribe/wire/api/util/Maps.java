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

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public interface Maps {

	static <K, V> HashMap<K, V> map() {
		return new HashMap<K, V>();
	}

	@SafeVarargs
	static <K, V> HashMap<K, V> map(Entry<K, V> ... entries) {
		HashMap<K, V> map = new HashMap<K, V>();
		put(map, entries);
		return map;
	}

	static <K, V> LinkedHashMap<K, V> linkedMap() {
		return new LinkedHashMap<K, V>();
	}

	@SafeVarargs
	static <K, V> LinkedHashMap<K, V> linkedMap(Entry<K, V> ... entries) {
		LinkedHashMap<K, V> map = new LinkedHashMap<K, V>();
		put(map, entries);
		return map;
	}

	@SafeVarargs
	static <K, V> void put(Map<K, V> map, Entry<K, V> ... entries) {
		if (map != null && entries != null) {
			for (Entry<K, V> entry : entries) {
				map.put(entry.key, entry.value);
			}
		}
	}
	
	static <K, V> Entry<K, V> entry(K key, V value) {
		Entry<K, V> entry = new Entry<K, V>();
		entry.key  = key;
		entry.value  = value;
		return entry;
	}
	
	class Entry<K, V> {
		K key;
		V value;
	}

}
