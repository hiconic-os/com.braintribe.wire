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
package com.braintribe.wire.example;

import java.util.function.Supplier;

public class ExampleBean implements AutoCloseable {
	private ExampleBean bean;
	private String name;
	private AnnoBean annoBean;
	private Supplier<Resource> resourceProvider; 
	private Something something;
	private Resource resource;
	private Supplier<AnnoBean> supplier;
	
	public void setSomething(Something something) {
		this.something = something;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setBean(ExampleBean bean) {
		this.bean = bean;
	}
	
	public void setDeferredBean(Supplier<AnnoBean> supplier) {
		this.supplier = supplier;
	}
	
	public Something getSomething() {
		return something;
	}
	
	public ExampleBean getBean() {
		return bean;
	}
	
	public AnnoBean getDeferredBean() {
		AnnoBean bean = supplier.get();
		return bean;
	}

	public void setResource(Resource resource) {
		this.resource = resource;
	}
	
	public Resource getResource() {
		return resource;
	}
	
	public void setAnnoBean(AnnoBean annoBean) {
		this.annoBean = annoBean;
	}
	
	public AnnoBean getAnnoBean() {
		return annoBean;
	}
	
	public void setResourceProvider(Supplier<Resource> resourceProvider) {
		this.resourceProvider = resourceProvider;
	}
	
	public Supplier<Resource> getResourceProvider() {
		return resourceProvider;
	}
	
	@Override
	public void close() throws Exception {
		System.out.println("Example bean closed");
		
	}
	
	
}
