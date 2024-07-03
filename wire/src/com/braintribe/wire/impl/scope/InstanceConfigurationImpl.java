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
package com.braintribe.wire.impl.scope;

import java.util.logging.Level;
import java.util.logging.Logger;

import com.braintribe.wire.api.scope.InstanceConfiguration;
import com.braintribe.wire.api.scope.InstanceQualification;
import com.braintribe.wire.impl.util.Exceptions;

public class InstanceConfigurationImpl implements InstanceConfiguration {
	private static Logger logger = Logger.getLogger(InstanceConfigurationImpl.class.getName());
	public InstanceConfigurationImpl(InstanceQualification qualification) {
		super();
		this.qualification = qualification;
	}

	private Runnable destroyCallback;
	private InstanceQualification qualification;
	
	@Override
	public void onDestroy(Runnable function) {
		destroyCallback = new ExceptionSafeLinkRunnable(destroyCallback, function);
	}
	
	@Override
	public void closeOnDestroy(AutoCloseable closable) {
		onDestroy((Runnable)() -> closeAutoCloseable(closable));
	}
	
	private static void closeAutoCloseable(AutoCloseable closable) {
		try {
			closable.close();
		}
		catch (Exception e) {
			throw Exceptions.unchecked(e);
		}
	}
	
	public Runnable destroyCallback() {
		return destroyCallback;
	}

	@Override
	public InstanceQualification qualification() {
		return qualification;
	}
	
	private class ExceptionSafeLinkRunnable implements Runnable {
		private Runnable predecessor;
		private Runnable runnable;
		
		public ExceptionSafeLinkRunnable(Runnable predecessor, Runnable runnable) {
			super();
			this.predecessor = predecessor;
			this.runnable = runnable;
		}
		
		@Override
		public void run() {
			if (predecessor != null)
				predecessor.run();
			
			try {
				runnable.run();
			}
			catch (Exception e) {
				logger.log(Level.SEVERE,"Error while calling destroy callback [" + runnable + "] of bean " + qualification.space().getClass() + "/" + qualification.name(), e);
			}
		}
	}
}
