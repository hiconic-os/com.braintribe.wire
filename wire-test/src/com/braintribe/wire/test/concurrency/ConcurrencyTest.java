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
package com.braintribe.wire.test.concurrency;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.junit.Test;

import com.braintribe.wire.api.Wire;
import com.braintribe.wire.api.context.WireContext;
import com.braintribe.wire.test.concurrency.bean.ConcurrentlyAccessed;
import com.braintribe.wire.test.concurrency.bean.CycleBean;
import com.braintribe.wire.test.concurrency.wire.ConcurrencyTestWireModule;
import com.braintribe.wire.test.concurrency.wire.contract.ConcurrencyTestMainContract;


public class ConcurrencyTest {
	@Test
	public void concurrencyTest() throws Exception {

		WireContext<ConcurrencyTestMainContract> context = Wire.context(ConcurrencyTestWireModule.INSTANCE);

		final int numThreads = 20;
		
		ExecutorService newFixedThreadPool = Executors.newFixedThreadPool(numThreads);
		
		List<Future<String>> futures = new ArrayList<>();
		
		for (int i = 0; i < numThreads; i++) {
			Future<String> future = newFixedThreadPool.submit(() -> {
				ConcurrentlyAccessed concurrentlyAccessed = context.contract().concurrentlyAccessed();
				
				return concurrentlyAccessed.getValue();
			});
			
			futures.add(future);
		}
		
		for (Future<String> future: futures) {
			String value = future.get();
			
			assertThat(value).describedAs("bean was not properly initialized when beeing returned in a concurrent setup").isEqualTo("done");
		}
		
		// check cyclic reference
		CycleBean cycleBean = context.contract().cycleBean();
		
		CycleBean backlinkBean = cycleBean.getOther();
		CycleBean subjectBean = backlinkBean.getOther();
		
		assertThat(cycleBean).describedAs("bean cycle was not properly established").isSameAs(subjectBean);
		
		context.shutdown();
		newFixedThreadPool.shutdown();
	}
}
