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
package com.braintribe.wire.impl.compile;

import java.lang.classfile.ClassFile;
import java.lang.classfile.ClassModel;
import java.util.List;
import java.util.TreeSet;

final class ComparingManagedSpaceEnricher implements ManagedSpaceEnricher {
	private static final ClassFile CLASS_FILE = ClassFile.of();

	private final ManagedSpaceEnricher asm;
	private final ManagedSpaceEnricher classFile;

	ComparingManagedSpaceEnricher(ManagedSpaceEnricher asm, ManagedSpaceEnricher classFile) {
		this.asm = asm;
		this.classFile = classFile;
	}

	@Override
	public byte[] enrich(String className, byte[] classBytes) {
		byte[] asmBytes = asm.enrich(className, classBytes);
		byte[] classFileBytes = classFile.enrich(className, classBytes);

		verify(className, "ASM", asmBytes);
		verify(className, "Class File API", classFileBytes);

		ClassShape asmShape = ClassShape.of(asmBytes);
		ClassShape classFileShape = ClassShape.of(classFileBytes);
		if (!asmShape.equals(classFileShape)) {
			throw new IllegalStateException("Wire enrichment structure differs for " + className
					+ " between ASM and Class File API.\nASM: " + asmShape + "\nClass File API: " + classFileShape);
		}

		// Shadow mode deliberately keeps the established implementation active.
		return asmBytes;
	}

	private static void verify(String className, String backend, byte[] bytes) {
		List<java.lang.VerifyError> errors = CLASS_FILE.verify(bytes);
		if (!errors.isEmpty())
			throw new IllegalStateException(backend + " generated invalid bytecode for " + className + ": " + errors);
	}

	private record ClassShape(String className, String superClass, TreeSet<String> interfaces,
			TreeSet<String> fields, TreeSet<String> methods) {
		static ClassShape of(byte[] bytes) {
			ClassModel model = CLASS_FILE.parse(bytes);
			TreeSet<String> interfaces = new TreeSet<>();
			model.interfaces().forEach(entry -> interfaces.add(entry.asInternalName()));
			TreeSet<String> fields = new TreeSet<>();
			model.fields().forEach(field -> fields.add(field.fieldName().stringValue() + ':'
					+ field.fieldType().stringValue() + ':' + field.flags().flagsMask()));
			TreeSet<String> methods = new TreeSet<>();
			model.methods().forEach(method -> methods.add(method.methodName().stringValue()
					+ method.methodType().stringValue() + ':' + method.flags().flagsMask()));
			return new ClassShape(model.thisClass().asInternalName(),
					model.superclass().map(entry -> entry.asInternalName()).orElse(null), interfaces, fields, methods);
		}
	}
}
