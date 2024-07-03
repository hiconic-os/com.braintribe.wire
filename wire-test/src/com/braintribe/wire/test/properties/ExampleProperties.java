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
package com.braintribe.wire.test.properties;

import java.io.File;
import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.braintribe.cfg.Required;
import com.braintribe.wire.api.annotation.Decrypt;
import com.braintribe.wire.api.annotation.Default;
import com.braintribe.wire.api.annotation.Name;

public interface ExampleProperties {
	int SOME_NUMBER();
	boolean SOME_BOOLEAN();

	File A_FILE(File def);
	File ANOTHER_FILE(File def);

	@Required
	boolean MISSING_BOOLEAN();
	
	@Required
	String MANDATORY_PROPERTY1();

	@Required
	String MANDATORY_PROPERTY2();

	boolean BROKEN_PARAMETERIZED_BOOLEAN(Boolean def);
	
	@Default("true")
	boolean DEFAULTED_BOOLEAN();
	
	@Name("RENAMED_VAR")
	String renamed();
	
	List<File> FILES();
	
	Set<Integer> NUMBERS();
	
	Map<Integer, String> NUMBER_TEXTS();
	
	@Decrypt(secret="crushftp")
	String CONNECTION_PASSWORD();
	
	@Decrypt
	@Default("zVIpu/OIBKLU52n9psTVCz7mY6ehJ3V0yUlswNSDQNsay8Gzr4U9q69MtKUkSVzBjupbRg==")
	String CONNECTION_PASSWORD2();
	
	@Decrypt
	String CONNECTION_PASSWORD2(String def);
	
	@Default("somedef")
	String INVALID_DEFAULTING(String def);
	
	Date A_DATE();
	
	Duration A_DURATION();
	
	ExampleEnum ENUM();
}
