// ============================================================================
// Copyright BRAINTRIBE TECHNOLOGY GMBH, Austria, 2002-2026
//
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

import static java.lang.classfile.ClassFile.ACC_BRIDGE;
import static java.lang.classfile.ClassFile.ACC_PRIVATE;
import static java.lang.classfile.ClassFile.ACC_PROTECTED;
import static java.lang.classfile.ClassFile.ACC_PUBLIC;

import java.lang.classfile.AccessFlags;
import java.lang.classfile.Annotation;
import java.lang.classfile.AnnotationElement;
import java.lang.classfile.AnnotationValue;
import java.lang.classfile.Attributes;
import java.lang.classfile.ClassBuilder;
import java.lang.classfile.ClassElement;
import java.lang.classfile.ClassFile;
import java.lang.classfile.ClassFile.ClassHierarchyResolverOption;
import java.lang.classfile.ClassFile.StackMapsOption;
import java.lang.classfile.ClassHierarchyResolver;
import java.lang.classfile.ClassModel;
import java.lang.classfile.ClassTransform;
import java.lang.classfile.CodeBuilder;
import java.lang.classfile.CodeElement;
import java.lang.classfile.CodeModel;
import java.lang.classfile.CodeTransform;
import java.lang.classfile.FieldModel;
import java.lang.classfile.Interfaces;
import java.lang.classfile.Label;
import java.lang.classfile.MethodModel;
import java.lang.classfile.MethodTransform;
import java.lang.classfile.Opcode;
import java.lang.classfile.TypeKind;
import java.lang.classfile.attribute.RuntimeVisibleAnnotationsAttribute;
import java.lang.classfile.instruction.InvokeInstruction;
import java.lang.classfile.instruction.LoadInstruction;
import java.lang.classfile.instruction.ReturnInstruction;
import java.lang.classfile.instruction.StoreInstruction;
import java.lang.classfile.instruction.SwitchCase;
import java.lang.constant.ClassDesc;
import java.lang.constant.MethodTypeDesc;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;

import com.braintribe.cfg.ScopeContext;
import com.braintribe.wire.api.EnrichedWireSpace;
import com.braintribe.wire.api.ImportFieldRecorder;
import com.braintribe.wire.api.annotation.Bean;
import com.braintribe.wire.api.annotation.Beans;
import com.braintribe.wire.api.annotation.Enriched;
import com.braintribe.wire.api.annotation.Import;
import com.braintribe.wire.api.annotation.Managed;
import com.braintribe.wire.api.annotation.Scope;
import com.braintribe.wire.api.context.InternalWireContext;
import com.braintribe.wire.api.scope.BeanConfiguration;
import com.braintribe.wire.api.scope.DefaultScope;
import com.braintribe.wire.api.scope.InstanceConfiguration;
import com.braintribe.wire.api.scope.InstanceHolder;
import com.braintribe.wire.api.scope.InstanceHolderSupplier;
import com.braintribe.wire.api.scope.InstanceParameterization;
import com.braintribe.wire.api.scope.WireScope;
import com.braintribe.wire.api.space.WireSpace;
import com.braintribe.wire.impl.scope.caller.CallerScope;
import com.braintribe.wire.impl.scope.prototype.PrototypeScope;
import com.braintribe.wire.impl.scope.referee.AggregateScope;
import com.braintribe.wire.impl.scope.singleton.SingletonScope;

final class ClassFileManagedSpaceEnricher implements ManagedSpaceEnricher {
	private static final String CONTEXT_FIELD = "$context$";

	private static final ClassDesc CD_OBJECT = ClassDesc.of(Object.class.getName());
	private static final ClassDesc CD_VOID = ClassDesc.ofDescriptor("V");
	private static final ClassDesc CD_BOOLEAN = ClassDesc.ofDescriptor("Z");
	private static final ClassDesc CD_INT = ClassDesc.ofDescriptor("I");
	private static final ClassDesc CD_STRING = ClassDesc.of(String.class.getName());
	private static final ClassDesc CD_CLASS = ClassDesc.of(Class.class.getName());
	private static final ClassDesc CD_THROWABLE = ClassDesc.of(Throwable.class.getName());
	private static final ClassDesc CD_ILLEGAL_ARGUMENT_EXCEPTION = ClassDesc.of(IllegalArgumentException.class.getName());
	private static final ClassDesc CD_ARRAY_LIST = ClassDesc.of(ArrayList.class.getName());
	private static final ClassDesc CD_LIST = ClassDesc.of(List.class.getName());

	private static final ClassDesc CD_MANAGED = ClassDesc.of(Managed.class.getName());
	private static final ClassDesc CD_BEANS = ClassDesc.of(Beans.class.getName());
	private static final ClassDesc CD_BEAN = ClassDesc.of(Bean.class.getName());
	private static final ClassDesc CD_ENRICHED = ClassDesc.of(Enriched.class.getName());
	private static final ClassDesc CD_IMPORT = ClassDesc.of(Import.class.getName());

	private static final ClassDesc CD_WIRE_SPACE = ClassDesc.of(WireSpace.class.getName());
	private static final ClassDesc CD_ENRICHED_WIRE_SPACE = ClassDesc.of(EnrichedWireSpace.class.getName());
	private static final ClassDesc CD_IMPORT_FIELD_RECORDER = ClassDesc.of(ImportFieldRecorder.class.getName());
	private static final ClassDesc CD_INTERNAL_WIRE_CONTEXT = ClassDesc.of(InternalWireContext.class.getName());
	private static final ClassDesc CD_WIRE_SCOPE = ClassDesc.of(WireScope.class.getName());
	private static final ClassDesc CD_INSTANCE_HOLDER = ClassDesc.of(InstanceHolder.class.getName());
	private static final ClassDesc CD_INSTANCE_HOLDER_SUPPLIER = ClassDesc.of(InstanceHolderSupplier.class.getName());
	private static final ClassDesc CD_INSTANCE_PARAMETERIZATION = ClassDesc.of(InstanceParameterization.class.getName());
	private static final ClassDesc CD_INSTANCE_CONFIGURATION = ClassDesc.of(InstanceConfiguration.class.getName());
	private static final ClassDesc CD_BEAN_CONFIGURATION = ClassDesc.of(BeanConfiguration.class.getName());

	private static final ClassDesc CD_DEFAULT_SCOPE = ClassDesc.of(DefaultScope.class.getName());
	private static final ClassDesc CD_SINGLETON_SCOPE = ClassDesc.of(SingletonScope.class.getName());
	private static final ClassDesc CD_PROTOTYPE_SCOPE = ClassDesc.of(PrototypeScope.class.getName());
	private static final ClassDesc CD_AGGREGATE_SCOPE = ClassDesc.of(AggregateScope.class.getName());
	private static final ClassDesc CD_CALLER_SCOPE = ClassDesc.of(CallerScope.class.getName());

	private static final MethodTypeDesc MTD_VOID = MethodTypeDesc.of(CD_VOID);
	private static final MethodTypeDesc MTD_CONTEXT_CONSTRUCTOR = MethodTypeDesc.of(CD_VOID, CD_INTERNAL_WIRE_CONTEXT);
	private static final MethodTypeDesc MTD_GET_SCOPE = MethodTypeDesc.of(CD_WIRE_SCOPE, CD_CLASS);
	private static final MethodTypeDesc MTD_CREATE_HOLDER_SUPPLIER = MethodTypeDesc.of(CD_INSTANCE_HOLDER_SUPPLIER,
			CD_WIRE_SPACE, CD_STRING, CD_INSTANCE_PARAMETERIZATION);
	private static final MethodTypeDesc MTD_GET_HOLDER = MethodTypeDesc.of(CD_INSTANCE_HOLDER, CD_OBJECT);
	private static final MethodTypeDesc MTD_LOCK_CREATION = MethodTypeDesc.of(CD_BOOLEAN, CD_INSTANCE_HOLDER);
	private static final MethodTypeDesc MTD_UNLOCK_CREATION = MethodTypeDesc.of(CD_VOID, CD_INSTANCE_HOLDER);
	private static final MethodTypeDesc MTD_HOLDER_GET = MethodTypeDesc.of(CD_OBJECT);
	private static final MethodTypeDesc MTD_HOLDER_OBJECT = MethodTypeDesc.of(CD_VOID, CD_OBJECT);
	private static final MethodTypeDesc MTD_HOLDER_THROWABLE = MethodTypeDesc.of(CD_VOID, CD_THROWABLE);
	private static final MethodTypeDesc MTD_HOLDER_CONFIG = MethodTypeDesc.of(CD_INSTANCE_CONFIGURATION);
	private static final MethodTypeDesc MTD_CURRENT_INSTANCE = MethodTypeDesc.of(CD_INSTANCE_CONFIGURATION);
	private static final MethodTypeDesc MTD_CURRENT_BEAN = MethodTypeDesc.of(CD_BEAN_CONFIGURATION);
	private static final MethodTypeDesc MTD_ADAPT_BEAN = MethodTypeDesc.of(CD_BEAN_CONFIGURATION, CD_INSTANCE_CONFIGURATION);
	private static final MethodTypeDesc MTD_ARRAY_LIST_CONSTRUCTOR = MethodTypeDesc.of(CD_VOID, CD_INT);
	private static final MethodTypeDesc MTD_LIST_ADD = MethodTypeDesc.of(CD_BOOLEAN, CD_OBJECT);
	private static final MethodTypeDesc MTD_LIST_IMPORT_FIELDS = MethodTypeDesc.of(CD_VOID, CD_IMPORT_FIELD_RECORDER);
	private static final MethodTypeDesc MTD_SET_IMPORT_FIELD = MethodTypeDesc.of(CD_VOID, CD_CLASS, CD_INT, CD_OBJECT);
	private static final MethodTypeDesc MTD_GET_SUPERCLASS = MethodTypeDesc.of(CD_CLASS);
	private static final MethodTypeDesc MTD_IS_ASSIGNABLE_FROM = MethodTypeDesc.of(CD_BOOLEAN, CD_CLASS);
	private static final MethodTypeDesc MTD_RECORD_IMPORT = MethodTypeDesc.of(CD_VOID, CD_CLASS, CD_CLASS, CD_INT);
	private static final MethodTypeDesc MTD_STRING_CONSTRUCTOR = MethodTypeDesc.of(CD_VOID, CD_STRING);

	private final ClassLoader classLoader;
	private final ClassFile classFile;

	ClassFileManagedSpaceEnricher(ClassLoader classLoader) {
		this.classLoader = classLoader;
		ClassHierarchyResolver hierarchyResolver = ClassHierarchyResolver.ofResourceParsing(classLoader)
				.orElse(ClassHierarchyResolver.defaultResolver());
		classFile = ClassFile.of(StackMapsOption.GENERATE_STACK_MAPS, ClassHierarchyResolverOption.of(hierarchyResolver));
	}

	@Override
	public byte[] enrich(String className, byte[] classBytes) {
		ClassModel classModel = classFile.parse(classBytes);
		List<Annotation> annotations = annotations(classModel);

		boolean wireSpace = hasAnnotation(annotations, CD_MANAGED) || hasAnnotation(annotations, CD_BEANS);
		boolean enriched = hasAnnotation(annotations, CD_ENRICHED);
		if (!wireSpace || enriched)
			return classBytes;

		return enrichWireSpace(className, classModel);
	}

	private byte[] enrichWireSpace(String className, ClassModel classModel) {
		ClassDesc targetType = classModel.thisClass().asSymbol();
		ClassDesc superType = classModel.superclass().orElseThrow().asSymbol();
		List<FactoryMethod> factoryMethods = getFactoryMethods(classModel);
		List<FieldModel> importFields = classModel.fields().stream().filter(this::isImportField).toList();

		ClassTransform transform = (builder, element) -> transformElement(builder, element, targetType, factoryMethods);
		transform = transform.andThen(ClassTransform.endHandler(builder -> {
			writeConstructor(builder, targetType, superType, factoryMethods);
			writeImportFieldReflection(builder, targetType, superType, importFields);
		}));

		byte[] enriched = classFile.transformClass(classModel, transform);
		List<java.lang.VerifyError> errors = classFile.verify(enriched);
		if (!errors.isEmpty())
			throw new IllegalStateException("Class File API generated invalid bytecode for " + className + ": " + errors);
		return enriched;
	}

	private void transformElement(ClassBuilder builder, ClassElement element, ClassDesc targetType, List<FactoryMethod> factoryMethods) {
		if (element instanceof Interfaces interfaces) {
			List<ClassDesc> interfaceTypes = interfaces.interfaces().stream().map(entry -> entry.asSymbol()).collect(java.util.stream.Collectors.toList());
			interfaceTypes.add(CD_ENRICHED_WIRE_SPACE);
			builder.withInterfaceSymbols(interfaceTypes);
			return;
		}

		if (element instanceof FieldModel field && isImportField(field)) {
			builder.transformField(field, (fieldBuilder, fieldElement) -> {
				if (fieldElement instanceof AccessFlags flags) {
					int mask = flags.flagsMask() & ~(ACC_PRIVATE | ACC_PROTECTED);
					fieldBuilder.withFlags(mask | ACC_PUBLIC);
				} else {
					fieldBuilder.with(fieldElement);
				}
			});
			return;
		}

		if (element instanceof MethodModel method) {
			FactoryMethod factoryMethod = factoryMethods.stream().filter(candidate -> candidate.method == method).findFirst().orElse(null);
			if (factoryMethod != null) {
				builder.transformMethod(method, managedMethodTransform(targetType, factoryMethod));
				return;
			}
		}

		builder.with(element);
	}

	private MethodTransform managedMethodTransform(ClassDesc targetType, FactoryMethod factoryMethod) {
		return (methodBuilder, element) -> {
			if (element instanceof CodeModel codeModel)
				methodBuilder.transformCode(codeModel, new ManagedMethodCodeTransform(targetType, factoryMethod, codeModel));
			else
				methodBuilder.with(element);
		};
	}

	private final class ManagedMethodCodeTransform implements CodeTransform {
		private final ClassDesc targetType;
		private final FactoryMethod factoryMethod;
		private final Set<Integer> publishedVariableSlots;
		private final Map<ReturnInstruction, Boolean> variableReturns;
		private int returnSlot;
		private int holderSlot;
		private Label beginTry;
		private Label beginCatch;
		private Label finale;

		ManagedMethodCodeTransform(ClassDesc targetType, FactoryMethod factoryMethod, CodeModel codeModel) {
			this.targetType = targetType;
			this.factoryMethod = factoryMethod;
			this.publishedVariableSlots = new HashSet<>();
			this.variableReturns = new IdentityHashMap<>();

			java.lang.classfile.Instruction previousInstruction = null;
			for (CodeElement element : codeModel) {
				if (!(element instanceof java.lang.classfile.Instruction instruction))
					continue;
				if (instruction instanceof ReturnInstruction returnInstruction && returnInstruction.opcode() == Opcode.ARETURN) {
					boolean variableReturn = previousInstruction instanceof LoadInstruction load && load.typeKind() == TypeKind.REFERENCE;
					variableReturns.put(returnInstruction, variableReturn);
					if (variableReturn)
						publishedVariableSlots.add(((LoadInstruction) previousInstruction).slot());
				}
				previousInstruction = instruction;
			}
		}

		@Override
		public void atStart(CodeBuilder builder) {
			returnSlot = builder.allocateLocal(TypeKind.REFERENCE);
			holderSlot = builder.allocateLocal(TypeKind.REFERENCE);
			beginTry = builder.newLabel();
			beginCatch = builder.newLabel();
			finale = builder.newLabel();

			initializeHolder(builder);
			pushLockCallParameters(builder);
			builder.invokeinterface(CD_INTERNAL_WIRE_CONTEXT, "lockCreation", MTD_LOCK_CREATION);
			builder.ifne(beginTry);
			builder.aload(holderSlot);
			builder.invokeinterface(CD_INSTANCE_HOLDER, "get", MTD_HOLDER_GET);
			builder.checkcast(factoryMethod.returnType);
			builder.areturn();
			builder.labelBinding(beginTry);
		}

		@Override
		public void accept(CodeBuilder builder, CodeElement element) {
			if (element instanceof ReturnInstruction returnInstruction && returnInstruction.opcode() == Opcode.ARETURN) {
				writeManagedReturn(builder, variableReturns.getOrDefault(returnInstruction, false));
				return;
			}

			if (element instanceof InvokeInstruction invocation && isCurrentInstanceCall(invocation)) {
				builder.aload(holderSlot);
				builder.invokeinterface(CD_INSTANCE_HOLDER, "config", MTD_HOLDER_CONFIG);
				return;
			}

			if (element instanceof InvokeInstruction invocation && isCurrentBeanCall(invocation)) {
				builder.aload(holderSlot);
				builder.invokeinterface(CD_INSTANCE_HOLDER, "config", MTD_HOLDER_CONFIG);
				builder.invokestatic(CD_BEAN_CONFIGURATION, "adapt", MTD_ADAPT_BEAN);
				return;
			}

			builder.with(element);
			if (element instanceof StoreInstruction store && store.typeKind() == TypeKind.REFERENCE
					&& publishedVariableSlots.contains(store.slot())) {
				builder.aload(holderSlot);
				builder.aload(store.slot());
				builder.invokeinterface(CD_INSTANCE_HOLDER, "publish", MTD_HOLDER_OBJECT);
			}
		}

		@Override
		public void atEnd(CodeBuilder builder) {
			builder.labelBinding(beginCatch);
			builder.dup();
			builder.aload(holderSlot);
			builder.swap();
			builder.invokeinterface(CD_INSTANCE_HOLDER, "onCreationFailure", MTD_HOLDER_THROWABLE);
			pushLockCallParameters(builder);
			builder.invokeinterface(CD_INTERNAL_WIRE_CONTEXT, "unlockCreation", MTD_UNLOCK_CREATION);
			builder.athrow();

			builder.labelBinding(finale);
			pushLockCallParameters(builder);
			builder.invokeinterface(CD_INTERNAL_WIRE_CONTEXT, "unlockCreation", MTD_UNLOCK_CREATION);
			builder.aload(returnSlot);
			builder.areturn();
			builder.exceptionCatchAll(beginTry, beginCatch, beginCatch);
		}

		private void initializeHolder(CodeBuilder builder) {
			builder.aload(0);
			builder.getfield(targetType, factoryMethod.holderSupplierName, CD_INSTANCE_HOLDER_SUPPLIER);
			List<ClassDesc> parameters = factoryMethod.parameters;
			switch (parameters.size()) {
				case 0 -> builder.aconst_null();
				case 1 -> loadBoxedParameter(builder, parameters.get(0), 0);
				default -> {
					builder.new_(CD_ARRAY_LIST);
					builder.dup();
					builder.loadConstant(parameters.size());
					builder.invokespecial(CD_ARRAY_LIST, "<init>", MTD_ARRAY_LIST_CONSTRUCTOR);
					for (int i = 0; i < parameters.size(); i++) {
						builder.dup();
						loadBoxedParameter(builder, parameters.get(i), i);
						builder.invokeinterface(CD_LIST, "add", MTD_LIST_ADD);
						builder.pop();
					}
				}
			}
			builder.invokeinterface(CD_INSTANCE_HOLDER_SUPPLIER, "getHolder", MTD_GET_HOLDER);
			builder.astore(holderSlot);
		}

		private void loadBoxedParameter(CodeBuilder builder, ClassDesc parameter, int parameterIndex) {
			TypeKind kind = TypeKind.fromDescriptor(parameter.descriptorString());
			int slot = builder.parameterSlot(parameterIndex);
			builder.loadLocal(kind, slot);
			box(builder, kind);
		}

		private void pushLockCallParameters(CodeBuilder builder) {
			builder.aload(0);
			builder.getfield(targetType, CONTEXT_FIELD, CD_INTERNAL_WIRE_CONTEXT);
			builder.aload(holderSlot);
		}

		private void writeManagedReturn(CodeBuilder builder, boolean variableReturn) {
			if (!variableReturn) {
				builder.dup();
				builder.aload(holderSlot);
				builder.swap();
				builder.invokeinterface(CD_INSTANCE_HOLDER, "publish", MTD_HOLDER_OBJECT);
			}
			builder.aload(holderSlot);
			builder.swap();
			builder.dup();
			builder.astore(returnSlot);
			builder.invokeinterface(CD_INSTANCE_HOLDER, "onPostConstruct", MTD_HOLDER_OBJECT);
			builder.goto_(finale);
		}
	}

	private void writeConstructor(ClassBuilder builder, ClassDesc targetType, ClassDesc superType, List<FactoryMethod> factoryMethods) {
		builder.withField(CONTEXT_FIELD, CD_INTERNAL_WIRE_CONTEXT, ACC_PRIVATE);
		for (FactoryMethod method : factoryMethods)
			builder.withField(method.holderSupplierName, CD_INSTANCE_HOLDER_SUPPLIER, ACC_PRIVATE);

		builder.withMethodBody("<init>", MTD_CONTEXT_CONSTRUCTOR, ACC_PUBLIC, code -> {
			code.aload(0);
			if (superType.equals(CD_OBJECT))
				code.invokespecial(CD_OBJECT, "<init>", MTD_VOID);
			else {
				code.aload(1);
				code.invokespecial(superType, "<init>", MTD_CONTEXT_CONSTRUCTOR);
			}

			code.aload(0);
			code.aload(1);
			code.putfield(targetType, CONTEXT_FIELD, CD_INTERNAL_WIRE_CONTEXT);

			for (FactoryMethod method : factoryMethods) {
				code.aload(1);
				code.ldc(method.scopeType);
				code.invokeinterface(CD_INTERNAL_WIRE_CONTEXT, "getScope", MTD_GET_SCOPE);
				code.astore(2);

				code.aload(0);
				code.dup();
				code.aload(2);
				code.swap();
				code.ldc(method.method.methodName().stringValue());
				code.getstatic(CD_INSTANCE_PARAMETERIZATION, method.parameterization.name(), CD_INSTANCE_PARAMETERIZATION);
				code.invokeinterface(CD_WIRE_SCOPE, "createHolderSupplier", MTD_CREATE_HOLDER_SUPPLIER);
				code.putfield(targetType, method.holderSupplierName, CD_INSTANCE_HOLDER_SUPPLIER);
			}
			code.return_();
		});
	}

	private void writeImportFieldReflection(ClassBuilder builder, ClassDesc targetType, ClassDesc superType, List<FieldModel> importFields) {
		builder.withMethodBody("__listImportFields", MTD_LIST_IMPORT_FIELDS, ACC_PUBLIC, code -> {
			Label localFields = code.newLabel();
			code.ldc(CD_ENRICHED_WIRE_SPACE);
			code.ldc(targetType);
			code.invokevirtual(CD_CLASS, "getSuperclass", MTD_GET_SUPERCLASS);
			code.invokevirtual(CD_CLASS, "isAssignableFrom", MTD_IS_ASSIGNABLE_FROM);
			code.ifeq(localFields);
			code.aload(0);
			code.aload(1);
			code.invokespecial(superType, "__listImportFields", MTD_LIST_IMPORT_FIELDS);
			code.labelBinding(localFields);
			for (int i = 0; i < importFields.size(); i++) {
				FieldModel field = importFields.get(i);
				code.aload(1);
				code.ldc(targetType);
				code.ldc(field.fieldTypeSymbol());
				code.loadConstant(i);
				code.invokevirtual(CD_IMPORT_FIELD_RECORDER, "record", MTD_RECORD_IMPORT);
			}
			code.return_();
		});

		builder.withMethodBody("__setImportField", MTD_SET_IMPORT_FIELD, ACC_PUBLIC, code -> {
			Label localClass = code.newLabel();
			Label invalidIndex = code.newLabel();
			code.ldc(targetType);
			code.aload(1);
			code.if_acmpeq(localClass);
			code.aload(0);
			code.aload(1);
			code.iload(2);
			code.aload(3);
			code.invokespecial(superType, "__setImportField", MTD_SET_IMPORT_FIELD);
			code.return_();
			code.labelBinding(localClass);

			if (!importFields.isEmpty()) {
				List<Label> labels = new ArrayList<>(importFields.size());
				List<SwitchCase> cases = new ArrayList<>(importFields.size());
				for (int i = 0; i < importFields.size(); i++) {
					Label label = code.newLabel();
					labels.add(label);
					cases.add(SwitchCase.of(i, label));
				}
				code.iload(2);
				code.tableswitch(invalidIndex, cases);
				for (int i = 0; i < importFields.size(); i++) {
					FieldModel field = importFields.get(i);
					code.labelBinding(labels.get(i));
					code.aload(0);
					code.aload(3);
					code.checkcast(field.fieldTypeSymbol());
					code.putfield(targetType, field.fieldName().stringValue(), field.fieldTypeSymbol());
					code.return_();
				}
			}

			code.labelBinding(invalidIndex);
			code.new_(CD_ILLEGAL_ARGUMENT_EXCEPTION);
			code.dup();
			code.ldc("index out of bounds");
			code.invokespecial(CD_ILLEGAL_ARGUMENT_EXCEPTION, "<init>", MTD_STRING_CONSTRUCTOR);
			code.athrow();
		});
	}

	private List<FactoryMethod> getFactoryMethods(ClassModel classModel) {
		ClassDesc defaultScope = scopeType(classModel, true);
		UniqueNameFunction uniqueNames = new UniqueNameFunction();
		List<FactoryMethod> result = new ArrayList<>();
		for (MethodModel method : classModel.methods()) {
			if ((method.flags().flagsMask() & ACC_BRIDGE) != 0)
				continue;
			ClassDesc scope = scopeType(method, false);
			if (scope == null)
				continue;
			if (scope.equals(CD_DEFAULT_SCOPE))
				scope = defaultScope;
			result.add(new FactoryMethod(method, scope, uniqueNames));
		}
		return result;
	}

	private final class FactoryMethod {
		final MethodModel method;
		final String holderSupplierName;
		final ClassDesc scopeType;
		final List<ClassDesc> parameters;
		final ClassDesc returnType;
		final InstanceParameterization parameterization;

		FactoryMethod(MethodModel method, ClassDesc scopeType, Function<String, String> nameMapper) {
			this.method = method;
			this.holderSupplierName = nameMapper.apply('$' + method.methodName().stringValue());
			this.scopeType = scopeType;
			MethodTypeDesc methodType = method.methodTypeSymbol();
			this.parameters = methodType.parameterList();
			this.returnType = methodType.returnType();
			this.parameterization = switch (parameters.size()) {
				case 0 -> InstanceParameterization.none;
				case 1 -> isScopeContext(parameters.get(0)) ? InstanceParameterization.context : InstanceParameterization.params;
				default -> InstanceParameterization.params;
			};
		}
	}

	private boolean isScopeContext(ClassDesc type) {
		if (!type.isClassOrInterface())
			return false;
		String binaryName = type.packageName().isEmpty() ? type.displayName() : type.packageName() + '.' + type.displayName();
		try {
			Class<?> parameterClass = Class.forName(binaryName, false, classLoader);
			return ScopeContext.class.isAssignableFrom(parameterClass);
		} catch (ClassNotFoundException e) {
			throw new IllegalStateException("Managed instance parameter type " + binaryName + " not found", e);
		}
	}

	private static ClassDesc scopeType(java.lang.classfile.AttributedElement element, boolean classLevel) {
		List<Annotation> annotations = annotations(element);
		Annotation managed = findAnnotation(annotations, CD_MANAGED);
		if (managed != null) {
			AnnotationElement value = managed.elements().stream().filter(e -> e.name().equalsString("value")).findFirst().orElse(null);
			if (value == null)
				return CD_DEFAULT_SCOPE;
			String constant = ((AnnotationValue.OfEnum) value.value()).constantName().stringValue();
			return switch (Scope.valueOf(constant)) {
				case inherit -> CD_DEFAULT_SCOPE;
				case singleton -> CD_SINGLETON_SCOPE;
				case prototype -> CD_PROTOTYPE_SCOPE;
				case aggregate -> CD_AGGREGATE_SCOPE;
				case caller -> CD_CALLER_SCOPE;
			};
		}

		ClassDesc legacyAnnotation = classLevel ? CD_BEANS : CD_BEAN;
		String legacyProperty = classLevel ? "defaultScope" : "scope";
		Annotation legacy = findAnnotation(annotations, legacyAnnotation);
		if (legacy != null) {
			AnnotationElement value = legacy.elements().stream().filter(e -> e.name().equalsString(legacyProperty)).findFirst().orElse(null);
			return value == null ? CD_DEFAULT_SCOPE : ((AnnotationValue.OfClass) value.value()).classSymbol();
		}

		return classLevel ? CD_DEFAULT_SCOPE : null;
	}

	private boolean isImportField(FieldModel field) {
		return hasAnnotation(annotations(field), CD_IMPORT);
	}

	private static List<Annotation> annotations(java.lang.classfile.AttributedElement element) {
		return element.findAttribute(Attributes.runtimeVisibleAnnotations())
				.map(RuntimeVisibleAnnotationsAttribute::annotations)
				.orElse(List.of());
	}

	private static Annotation findAnnotation(List<Annotation> annotations, ClassDesc annotationType) {
		return annotations.stream().filter(annotation -> annotation.classSymbol().equals(annotationType)).findFirst().orElse(null);
	}

	private static boolean hasAnnotation(List<Annotation> annotations, ClassDesc annotationType) {
		return findAnnotation(annotations, annotationType) != null;
	}

	private static boolean isCurrentInstanceCall(InvokeInstruction invocation) {
		return invocation.opcode() == Opcode.INVOKESTATIC
				&& invocation.owner().asSymbol().equals(CD_INSTANCE_CONFIGURATION)
				&& invocation.name().equalsString("currentInstance")
				&& invocation.typeSymbol().equals(MTD_CURRENT_INSTANCE);
	}

	private static boolean isCurrentBeanCall(InvokeInstruction invocation) {
		return invocation.opcode() == Opcode.INVOKESTATIC
				&& invocation.owner().asSymbol().equals(CD_BEAN_CONFIGURATION)
				&& invocation.name().equalsString("currentBean")
				&& invocation.typeSymbol().equals(MTD_CURRENT_BEAN);
	}

	private static void box(CodeBuilder builder, TypeKind kind) {
		Class<?> wrapper = switch (kind) {
			case BOOLEAN -> Boolean.class;
			case BYTE -> Byte.class;
			case SHORT -> Short.class;
			case INT -> Integer.class;
			case LONG -> Long.class;
			case FLOAT -> Float.class;
			case DOUBLE -> Double.class;
			case CHAR -> Character.class;
			case REFERENCE -> null;
			case VOID -> throw new IllegalArgumentException("void is not a valid method parameter");
		};
		if (wrapper == null)
			return;
		ClassDesc wrapperDesc = ClassDesc.of(wrapper.getName());
		builder.invokestatic(wrapperDesc, "valueOf", MethodTypeDesc.of(wrapperDesc, kind.upperBound()));
	}

	private static final class UniqueNameFunction implements Function<String, String> {
		private final Map<String, Integer> names = new HashMap<>();

		@Override
		public String apply(String name) {
			int number = names.compute(name, (key, value) -> value == null ? 1 : value + 1);
			return number == 1 ? name : name + '-' + number;
		}
	}
}
