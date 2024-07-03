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
package com.braintribe.wire.lab;

import java.util.function.Supplier;

import com.braintribe.wire.api.annotation.Enriched;
import com.braintribe.wire.api.annotation.Managed;
import com.braintribe.wire.api.context.InternalWireContext;
import com.braintribe.wire.api.scope.InstanceHolder;
import com.braintribe.wire.api.scope.InstanceHolderSupplier;
import com.braintribe.wire.api.scope.InstanceParameterization;
import com.braintribe.wire.api.scope.WireScope;
import com.braintribe.wire.example.ExampleBean;
import com.braintribe.wire.impl.scope.singleton.SingletonScope;
import com.braintribe.wire.test.basic.space.Space1;

@Managed @Enriched
public class Space1Enriched extends Space1 {
	
	private InstanceHolderSupplier $exampleBean;
	private InternalWireContext $;
	private InternalWireContext context;
	
	public Space1Enriched(InternalWireContext context) {
		WireScope scope;
		scope = context.getScope(SingletonScope.class);
		$exampleBean = scope.createHolderSupplier(this, "exampleBean", InstanceParameterization.params);
		this.context = context;
	}
	
	public ExampleBean exampleBean(Supplier<String> ctx) {
		Object $$retVal = null;
		
		InstanceHolder beanHolder = $exampleBean.getHolder(ctx);
		
		if (!context.lockCreation(beanHolder))
			return (ExampleBean)beanHolder.get();
		
		try {
			ExampleBean bean = new ExampleBean();
			
			// insert
			beanHolder.publish(bean);
			// insert_end
			
			bean.setName(ctx.get());
			
			// insert
			// $woven.publish(bean);
			beanHolder.onPostConstruct(bean);
			$$retVal = bean;
			// insert_end
		}
		catch (Throwable t) {
			beanHolder.onCreationFailure(t);
		}
		finally {
			context.unlockCreation(beanHolder);
		}
		
		return (ExampleBean)$$retVal;
	}
	

}
