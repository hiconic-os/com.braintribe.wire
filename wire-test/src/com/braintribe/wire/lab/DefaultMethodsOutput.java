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

import java.io.InputStream;
import java.io.PrintWriter;

import com.braintribe.asm.ClassReader;
import com.braintribe.asm.ClassVisitor;
import com.braintribe.asm.Opcodes;
import com.braintribe.asm.util.ASMifier;
import com.braintribe.asm.util.Textifier;
import com.braintribe.asm.util.TraceClassVisitor;



public class DefaultMethodsOutput implements Opcodes {
	public static void main(String[] args) {
	
		try {
			InputStream inputStream = Derived.class.getResourceAsStream(Derived.class.getSimpleName() + ".class");

			ClassReader reader = new ClassReader(inputStream);

			ASMifier asMifier = new ASMifier();
			Textifier textifier = new Textifier();
			ClassVisitor visitor = new TraceClassVisitor(null, textifier, new PrintWriter(System.out));
			reader.accept(visitor, ClassReader.EXPAND_FRAMES);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
