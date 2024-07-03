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
package com.braintribe.wire.impl.scope.singleton;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.braintribe.wire.api.context.ScopeContextHolders;
import com.braintribe.wire.api.scope.InstanceQualification;

public class ScopeContextSingletons implements ScopeContextHolders {
	private static final Logger logger = Logger.getLogger(ScopeContextSingletons.class.getName());
	protected Map<InstanceQualification, SingletonInstanceHolder> holders = new ConcurrentHashMap<InstanceQualification, SingletonInstanceHolder>();
	protected List<SingletonInstanceHolder> initializedHolders = new ArrayList<>();

	@Override
	public SingletonInstanceHolder acquireHolder(InstanceQualification keyHolder) {
		return holders.computeIfAbsent(keyHolder,
				k -> new SingletonInstanceHolder(keyHolder.space(), keyHolder.scope(), keyHolder.name(), this::append));
	}

	public void append(SingletonInstanceHolder holder) {
		synchronized (initializedHolders) {
			appendDirect(holder);
		}
	}

	private void appendDirect(SingletonInstanceHolder holder) {
		initializedHolders.add(holder);
	}

	@Override
	public void close() {
		synchronized (initializedHolders) {
			closeBeans();
		}
		holders.clear();
	}

	private void closeBeans() {
		for (ListIterator<SingletonInstanceHolder> it = initializedHolders.listIterator(initializedHolders.size()); it.hasPrevious();) {
			SingletonInstanceHolder holder = it.previous();
			try {
				logger.finest(() -> "Destroying bean " + holder.space().getClass().getName() + ":" + holder.name());
				holder.onDestroy();
			} catch (Exception e) {
				reportError(holder, e);
			} catch (IllegalAccessError e) {
				// This might occur during shutdown
				reportError(holder, e);
			} catch (Error e) {
				reportError(holder, e);
				throw e;
			}
		}
		logger.finest(() -> "Finished closing of beans: " + this.getClass().getName());
	}

	private void reportError(SingletonInstanceHolder holder, Throwable e) {
		logger.log(Level.SEVERE, "Exception while destroying bean " + holder.space().getClass().getName() + ":" + holder.name(), e);
	}
}