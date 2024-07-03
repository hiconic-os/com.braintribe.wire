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
package com.braintribe.wire.api;

import java.util.ArrayList;
import java.util.List;

public class ImportFieldRecorder {
	private List<ImportField> importFields = new ArrayList<>();
	private EnrichedWireSpace enhancedWireSpace;
	
	public ImportFieldRecorder(EnrichedWireSpace enhancedWireSpace) {
		super();
		this.enhancedWireSpace = enhancedWireSpace;
	}

	public void record(Class<?> origin, Class<?> type, int key) {
		importFields.add(new RecorderImportField(origin, type, key));
	}
	
	public List<ImportField> getImportFields() {
		return importFields;
	}
	
	private class RecorderImportField implements ImportField {
		Class<?> origin;
		Class<?> type;
		int key;

		public RecorderImportField(Class<?> origin, Class<?> type, int key) {
			super();
			this.origin = origin;
			this.type = type;
			this.key = key;
		}

		@Override
		public void set(Object value) {
			enhancedWireSpace.__setImportField(origin, key, value);
		}
		
		@Override
		public Class<?> type() {
			return type;
		}
	}
}
