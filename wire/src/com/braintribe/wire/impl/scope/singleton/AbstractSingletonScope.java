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
import java.util.logging.Level;
import java.util.logging.Logger;

import com.braintribe.wire.impl.scope.AbstractWireScope;

public abstract class AbstractSingletonScope extends AbstractWireScope {

	private static final Logger logger = Logger.getLogger(AbstractSingletonScope.class.getName());

	protected List<AbstractSingletonInstanceHolder> holders = new ArrayList<>();

	@Override
	public void close() throws Exception {
		for (ListIterator<AbstractSingletonInstanceHolder> it = holders.listIterator(holders.size()); it.hasPrevious();) {
			AbstractSingletonInstanceHolder holder = it.previous();
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
		logger.finest(() -> "Finished closing of scope: " + this.getClass().getName());
	}

	private void reportError(AbstractSingletonInstanceHolder holder, Throwable e) {
		logger.log(Level.SEVERE, "Exception while destroying bean " + holder.space().getClass().getName() + ":" + holder.name(), e);
	}

	public void appendBean(AbstractSingletonInstanceHolder singletonBeanHolder) {
		holders.add(singletonBeanHolder);
	}
}
