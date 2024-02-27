package com.braintribe.wire.lab;

import java.io.File;

import com.braintribe.wire.impl.compile.WireCompiler;

public class CompilerLab {
	public static void main(String[] args) {
		compile();
	}

	private static void compile() {
		File classesFolder = new File("classes");
		
		long s = System.currentTimeMillis();
		
		WireCompiler compiler = new WireCompiler();
		compiler.compile(classesFolder);
		long e = System.currentTimeMillis();
		System.out.println("scan and instrumentation took: " + (e - s) + "ms");
	}
}
