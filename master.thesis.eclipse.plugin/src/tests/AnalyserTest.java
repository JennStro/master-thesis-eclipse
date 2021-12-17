
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
import errors.MissingEqualsMethodError;

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
	public void classNotImplementingEqualsMethod() {
		String emptyClass = "public class Main {}";
		parser.setSource(emptyClass.toCharArray());
		CompilationUnit ast = (CompilationUnit) parser.createAST(null);
		ast.accept(analyser);
		
		ArrayList<BaseError> errors =  analyser.getErrors();
		System.out.println(errors);
		Assertions.assertEquals(1, errors.size());
		Assertions.assertTrue(errors.get(0) instanceof MissingEqualsMethodError);
		
	}
	
}