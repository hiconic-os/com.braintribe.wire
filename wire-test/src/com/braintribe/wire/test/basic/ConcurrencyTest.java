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
package com.braintribe.wire.test.basic;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

import org.junit.Assert;
import org.junit.Test;

import com.braintribe.wire.api.Wire;
import com.braintribe.wire.api.context.WireContext;
import com.braintribe.wire.test.basic.contract.ConcurrencyTestContract;

public class ConcurrencyTest {

	@Test
	public void concurrentSingletonAccessSimple() throws Exception {

		WireContext<ConcurrencyTestContract> context = Wire.contextWithStandardContractBinding(ConcurrencyTestContract.class).build();

		testConcurrently(context.contract()::simpleInstance);

	}

	@Test
	public void concurrentSingletonAccessCyclic() throws Exception {

		WireContext<ConcurrencyTestContract> context = Wire.contextWithStandardContractBinding(ConcurrencyTestContract.class).build();

		testConcurrently(context.contract()::cyclicInstance);

	}

	protected void testConcurrently(Supplier<Object> instanceSupplier) throws Exception {

		ExecutorService executor = Executors.newCachedThreadPool();

		try {

			List<Callable<Object>> tests = new ArrayList<>(5);

			for (int i = 0; i < 5; i++) {
				tests.add(new Callable<Object>() {
					@Override
					public Object call() throws Exception {
						System.out.println(Thread.currentThread().getName() + " will try to get the managed instance");
						Object result = instanceSupplier.get();
						System.out.println(Thread.currentThread().getName() + " got managed instance: " + result);
						return result;
					}
				});
			}

			Set<Object> occurences = new HashSet<>();

			for (Future<Object> future : executor.invokeAll(tests, 20, TimeUnit.SECONDS)) {
				occurences.add(future.get());
			}

			assertThat(occurences).hasSize(1);

		} catch (CancellationException c) {
			Assert.fail("The concurrent access on the managed instance deadlocked");
		} finally {
			executor.shutdownNow();
		}

	}

}
