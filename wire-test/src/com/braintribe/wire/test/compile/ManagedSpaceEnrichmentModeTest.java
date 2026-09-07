// ============================================================================
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
package com.braintribe.wire.test.compile;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.braintribe.wire.api.context.WireContext;
import com.braintribe.wire.impl.compile.ManagedSpaceEnrichmentMode;
import com.braintribe.wire.impl.context.WireContextBuilderImpl;
import com.braintribe.wire.test.basic.contract.MainContract;

public class ManagedSpaceEnrichmentModeTest {
	@Test
	public void selectsBackendPerContext() {
		assertContextWorks(ManagedSpaceEnrichmentMode.asm);
		assertContextWorks(ManagedSpaceEnrichmentMode.classFile);
		assertContextWorks(ManagedSpaceEnrichmentMode.compare);
	}

	private static void assertContextWorks(ManagedSpaceEnrichmentMode mode) {
		WireContextBuilderImpl<MainContract> builder = new WireContextBuilderImpl<>(MainContract.class);
		builder.managedSpaceEnrichmentMode(mode);
		builder.bindContracts("com.braintribe.wire.test.basic");

		try (WireContext<MainContract> context = builder.build()) {
			assertThat(context.contract().bean1()).isNotNull();
			assertThat(context.contract().bean1()).isSameAs(context.contract().bean1());
		}
	}
}
