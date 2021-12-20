
/*
 * 
 * This code is derived from the 
 * Eclipse IDE 2021-09 (4.21) Documentation (HTML Help Center)
 *   - JDT Plug-in Developer Guide
 *      - Programmer's Guide
 *          - JDT Core
 * 
 * URL: http://help.eclipse.org/2021-09/index.jsp
 * 
 * SPDX-FileCopyrightText: Copyright (c) IBM Corporation and others 2000, 2020.
 *
 * SPDX-License-Identifier: EPL-1.0
 */

/*
 * SPDX-FileCopyrightText: 2021 Jenny Strømmen
 *
 * SPDX-License-Identifier: EPL-1.0
 */

package tests;

import java.util.ArrayList;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import analyse.CodeAnalyser;
import errors.BaseError;
import errors.BitwiseOperatorError;
import errors.EqualsOperatorError;
import errors.FieldDeclarationWithoutInitializerError;
import errors.IfWithoutBracketsError;
import errors.IgnoringReturnError;
import errors.MissingEqualsMethodError;
import errors.SemiColonAfterIfError;

public class AnalyserTest {
	
	ASTParser parser;
	CodeAnalyser analyser;
	
	@BeforeEach
	public void setUp() {
		parser = ASTParser.newParser(AST.JLS17);
		parser.setUnitName("test");
		parser.setEnvironment(null, null, null, true);
		parser.setResolveBindings(true);
		analyser = new CodeAnalyser();
	}
	
	@Test 
	public void simpleTest() {
		String example = "public class Main {"
				+ "public void method() {"
				+ "int a = 0;"
				+ "if (a == 0) ; {}"
				+ "}"
				+ "}";
		
		parser.setSource(example.toCharArray());
		CompilationUnit ast = (CompilationUnit) parser.createAST(null);
		ast.accept(analyser);
		
		ArrayList<BaseError> errors =  analyser.getErrors();
		Assertions.assertEquals(2, errors.size());
	}
	
	@Test 
	public void emptyClassNotImplementingEqualsMethod() {
		String emptyClass = "public class Main {}";
		parser.setSource(emptyClass.toCharArray());
		CompilationUnit ast = (CompilationUnit) parser.createAST(null);
		ast.accept(analyser);
		
		ArrayList<BaseError> errors =  analyser.getErrors();
		Assertions.assertEquals(1, errors.size());
		Assertions.assertTrue(errors.get(0) instanceof MissingEqualsMethodError);
	}
	
	@Test 
	public void classNotImplementingEqualsMethod() {
		String classWithMethods = "public class Main {"
				+ "		public void method() {}"
				+ "}";
		parser.setSource(classWithMethods.toCharArray());
		CompilationUnit ast = (CompilationUnit) parser.createAST(null);
		ast.accept(analyser);
		
		ArrayList<BaseError> errors =  analyser.getErrors();
		Assertions.assertEquals(1, errors.size());
		Assertions.assertTrue(errors.get(0) instanceof MissingEqualsMethodError);
	}
	
	@Test 
	public void classImplementingEqualsMethod() {
		String classWithMethods = "public class Main {"
				+ "		public void method() {}"
				+ "		public boolean equals(Object o) {return false;}"
				+ "}";
		parser.setSource(classWithMethods.toCharArray());
		CompilationUnit ast = (CompilationUnit) parser.createAST(null);
		ast.accept(analyser);
		
		ArrayList<BaseError> errors =  analyser.getErrors();
		Assertions.assertEquals(0, errors.size());
	}
	
	@Test 
	public void classNotInititializingFieldNoConstructor() {
		String classWithMethods = "public class Main {"
				+ "		ArrayList<Integer> list;"
				+ "		public boolean equals(Object o) {return false;}"
				+ "}";
		parser.setSource(classWithMethods.toCharArray());
		CompilationUnit ast = (CompilationUnit) parser.createAST(null);
		ast.accept(analyser);
		
		ArrayList<BaseError> errors =  analyser.getErrors();
		Assertions.assertEquals(1, errors.size());
		Assertions.assertTrue(errors.get(0) instanceof FieldDeclarationWithoutInitializerError);
	}
	
	@Test 
	public void classInititializingFieldNoConstructor() {
		String classWithMethods = "public class Main {"
				+ "		ArrayList<Integer> list = new ArrayList<>();"
				+ "		public boolean equals(Object o) {return false;}"
				+ "}";
		parser.setSource(classWithMethods.toCharArray());
		CompilationUnit ast = (CompilationUnit) parser.createAST(null);
		ast.accept(analyser);
		
		ArrayList<BaseError> errors =  analyser.getErrors();
		Assertions.assertEquals(0, errors.size());
	}
	
	@Test 
	public void classInititializingFieldInConstructor() {
		String classWithMethods = "public class Main {"
				+ "		ArrayList<Integer> list;"
				
				+ "		public Main(ArrayList<Integer> list) {"
				+ "			this.list = list;"
				+ "		}"
				
				+ "		public boolean equals(Object o) {return false;}"
				+ "}";
		parser.setSource(classWithMethods.toCharArray());
		CompilationUnit ast = (CompilationUnit) parser.createAST(null);
		ast.accept(analyser);
		
		ArrayList<BaseError> errors =  analyser.getErrors();
		Assertions.assertEquals(0, errors.size());
	}
	
	@Test 
	public void classInititializingFieldInConstructorNotAccessingField() {
		String classWithMethods = "public class Main {"
				+ "		ArrayList<Integer> list;"
				
				+ "		public Main(ArrayList<Integer> list) {"
				+ "			list = list;"
				+ "		}"
				
				+ "		public boolean equals(Object o) {return false;}"
				+ "}";
		parser.setSource(classWithMethods.toCharArray());
		CompilationUnit ast = (CompilationUnit) parser.createAST(null);
		ast.accept(analyser);
		
		ArrayList<BaseError> errors =  analyser.getErrors();
		Assertions.assertEquals(0, errors.size());
	}
	
	@Test 
	public void classNotInititializingFieldInConstructor() {
		String classWithMethods = "public class Main {"
				+ "		ArrayList<Integer> list;"
				
				+ "		public Main(ArrayList<Integer> list) {"
				+ ""
				+ "		}"
				
				+ "		public boolean equals(Object o) {return false;}"
				+ "}";
		parser.setSource(classWithMethods.toCharArray());
		CompilationUnit ast = (CompilationUnit) parser.createAST(null);
		ast.accept(analyser);
		
		ArrayList<BaseError> errors =  analyser.getErrors();
		Assertions.assertEquals(1, errors.size());
		Assertions.assertTrue(errors.get(0) instanceof FieldDeclarationWithoutInitializerError);
	}
	
	@Test 
	public void ifWithoutBrackets() {
		String code = "public class Main {"
				+ "		public Main(int a) {"
				+ "			if (a == 0)"
				+ "				System.out.println(a);"
				+ "		}"
				+ "		public boolean equals(Object o) {return false;}"
				+ "}";
		parser.setSource(code.toCharArray());
		CompilationUnit ast = (CompilationUnit) parser.createAST(null);
		ast.accept(analyser);
		
		ArrayList<BaseError> errors =  analyser.getErrors();
		Assertions.assertEquals(1, errors.size());
		Assertions.assertTrue(errors.get(0) instanceof IfWithoutBracketsError);
		Assertions.assertTrue(errors.get(0).hasSuggestion());
	}
	
	@Test 
	public void ifWithBrackets() {
		String code = "public class Main {"
				+ "		public Main(int a) {"
				+ "			if (a == 0) {"
				+ "				System.out.println(a);"
				+ "			}"
				+ "		}"
				+ "		public boolean equals(Object o) {return false;}"
				+ "}";
		parser.setSource(code.toCharArray());
		CompilationUnit ast = (CompilationUnit) parser.createAST(null);
		ast.accept(analyser);
		
		ArrayList<BaseError> errors =  analyser.getErrors();
		Assertions.assertEquals(0, errors.size());
	}
	
	@Test 
	public void usingAndBitwiseOperator() {
		String code = "public class Main {"
				+ "		public Main(int a, int b) {"
				+ "			if (a == 0 & b == 0) {"
				+ "				System.out.println(a);"
				+ "			}"
				+ "		}"
				+ "		public boolean equals(Object o) {return false;}"
				+ "}";
		parser.setSource(code.toCharArray());
		CompilationUnit ast = (CompilationUnit) parser.createAST(null);
		ast.accept(analyser);
		
		ArrayList<BaseError> errors =  analyser.getErrors();
		Assertions.assertEquals(1, errors.size());
		Assertions.assertTrue(errors.get(0) instanceof BitwiseOperatorError);
		Assertions.assertTrue(errors.get(0).hasSuggestion());
	}
	
	@Test 
	public void usingOrBitwiseOperator() {
		String code = "public class Main {"
				+ "		public Main(int a, int b) {"
				+ "			if (a == 0 | b == 0) {"
				+ "				System.out.println(a);"
				+ "			}"
				+ "		}"
				+ "		public boolean equals(Object o) {return false;}"
				+ "}";
		parser.setSource(code.toCharArray());
		CompilationUnit ast = (CompilationUnit) parser.createAST(null);
		ast.accept(analyser);
		
		ArrayList<BaseError> errors =  analyser.getErrors();
		Assertions.assertEquals(1, errors.size());
		Assertions.assertTrue(errors.get(0) instanceof BitwiseOperatorError);
		Assertions.assertTrue(errors.get(0).hasSuggestion());
	}
	
	@Test 
	public void usingConditionalOperator() {
		String code = "public class Main {"
				+ "		public Main(int a, int b) {"
				+ "			if (a == 0 && b == 0) {"
				+ "				System.out.println(a);"
				+ "			}"
				+ "		}"
				+ "		public boolean equals(Object o) {return false;}"
				+ "}";
		parser.setSource(code.toCharArray());
		CompilationUnit ast = (CompilationUnit) parser.createAST(null);
		ast.accept(analyser);
		
		ArrayList<BaseError> errors =  analyser.getErrors();
		Assertions.assertEquals(0, errors.size());
	}
	
	@Test 
	public void usingEqualsOperatorOnObject() {
		String code = "public class Main {"
				+ "		public Main(Object a, Object b) {"
				+ "			if (a == b) {"
				+ "				System.out.println(a);"
				+ "			}"
				+ "		}"
				+ "		public boolean equals(Object o) {return false;}"
				+ "}";
		parser.setSource(code.toCharArray());
		CompilationUnit ast = (CompilationUnit) parser.createAST(null);
		ast.accept(analyser);
		
		ArrayList<BaseError> errors =  analyser.getErrors();
		Assertions.assertEquals(1, errors.size());
		Assertions.assertTrue(errors.get(0) instanceof EqualsOperatorError);
		Assertions.assertTrue(errors.get(0).hasSuggestion());
	}
	
	@Test 
	public void ignoringReturnValue() {
		String code = "public class Main {"
				+ "		public Main(String a) {"
				+ "			a.toLowerCase();"
				+ "		}"
				+ "		public boolean equals(Object o) {return false;}"
				+ "}";
		parser.setSource(code.toCharArray());
		CompilationUnit ast = (CompilationUnit) parser.createAST(null);
		ast.accept(analyser);
		
		ArrayList<BaseError> errors =  analyser.getErrors();
		Assertions.assertEquals(1, errors.size());
		Assertions.assertTrue(errors.get(0) instanceof IgnoringReturnError);
		Assertions.assertTrue(errors.get(0).hasSuggestion());
	}
	
	@Test 
	public void semiColonAfterIf() {
		String code = "public class Main {"
				+ "		public Main(String a) {"
				+ "			if (a.equals(\"a\")); {}"
				+ "		}"
				+ "		public boolean equals(Object o) {return false;}"
				+ "}";
		parser.setSource(code.toCharArray());
		CompilationUnit ast = (CompilationUnit) parser.createAST(null);
		ast.accept(analyser);
		
		ArrayList<BaseError> errors =  analyser.getErrors();
		Assertions.assertEquals(1, errors.size());
		Assertions.assertTrue(errors.get(0) instanceof SemiColonAfterIfError);
		Assertions.assertTrue(errors.get(0).hasSuggestion());
	}
	
	@Test 
	public void noSemiColonAfterIfButEmptyThenBranch() {
		String code = "public class Main {"
				+ "		public Main(String a) {"
				+ "			if (a.equals(\"a\")){}"
				+ "		}"
				+ "		public boolean equals(Object o) {return false;}"
				+ "}";
		parser.setSource(code.toCharArray());
		CompilationUnit ast = (CompilationUnit) parser.createAST(null);
		ast.accept(analyser);
		
		ArrayList<BaseError> errors =  analyser.getErrors();
		Assertions.assertEquals(0, errors.size());
	}
	
}