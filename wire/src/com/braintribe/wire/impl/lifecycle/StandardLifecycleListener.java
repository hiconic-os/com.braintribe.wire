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
package com.braintribe.wire.impl.lifecycle;

import com.braintribe.cfg.DestructionAware;
import com.braintribe.cfg.InitializationAware;
import com.braintribe.wire.api.scope.InstanceHolder;
import com.braintribe.wire.api.scope.LifecycleListener;
import com.braintribe.wire.impl.util.Exceptions;

public class StandardLifecycleListener implements LifecycleListener {

	public static final StandardLifecycleListener INSTANCE = new StandardLifecycleListener();
	
	@Override
	public void onPostConstruct(InstanceHolder beanHolder, Object bean) {
		if (bean instanceof InitializationAware) {
			((InitializationAware)bean).postConstruct();
		}
	}

	@Override
	public void onPreDestroy(InstanceHolder beanHolder, Object bean) {
		if (bean instanceof AutoCloseable) {
			try {
				((AutoCloseable)bean).close();
			} catch (Exception e) {
				throw Exceptions.unchecked(e, "Error while calling AutoClosable.close", IllegalStateException::new);
			}
		} else if (bean instanceof DestructionAware) {
			((DestructionAware)bean).preDestroy();
		}
	}

}
