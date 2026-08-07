// ============================================================================
// Copyright BRAINTRIBE TECHNOLOGY GMBH, Austria, 2002-2026
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
// ============================================================================
package com.braintribe.wire.impl.compile;

/**
 * Temporary migration switch for proving the JDK Class-File-API based Wire
 * space enrichment against the established ASM implementation.
 * <p>
 * This is deliberately an implementation type and not part of wire-api.
 */
public enum ManagedSpaceEnrichmentMode {
	asm,
	classFile,
	compare;

	public static final String SYSTEM_PROPERTY = "com.braintribe.wire.enrichment";

	public static ManagedSpaceEnrichmentMode configuredDefault() {
		String value = System.getProperty(SYSTEM_PROPERTY);
		if (value == null || value.isBlank())
			return asm;

		return switch (value.trim().toLowerCase()) {
			case "asm" -> asm;
			case "class-file", "classfile" -> classFile;
			case "compare" -> compare;
			default -> throw new IllegalArgumentException(
					"Unsupported Wire managed-space enrichment mode '" + value + "' configured via " + SYSTEM_PROPERTY);
		};
	}
}
