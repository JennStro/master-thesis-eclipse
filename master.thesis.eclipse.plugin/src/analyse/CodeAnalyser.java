package analyse;

import java.lang.reflect.Method;

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


import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTMatcher;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.ChildListPropertyDescriptor;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.EmptyStatement;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.InfixExpression;
import org.eclipse.jdt.core.dom.InfixExpression.Operator;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.StructuralPropertyDescriptor;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

import errors.BaseError;
import errors.BitwiseOperatorError;
import errors.EqualsOperatorError;
import errors.FieldDeclarationWithoutInitializerError;
import errors.IfWithoutBracketsError;
import errors.IgnoringReturnError;
import errors.MissingEqualsMethodError;
import errors.SemiColonAfterIfError;
import errors.StaticAsNormalError;

public class CodeAnalyser extends ASTVisitor {
	
	private HashMap<String, String> classInstances = new HashMap<>();
	
	private ArrayList<BaseError> errors = new ArrayList<>();

	/**
	 * Check if a class is missing the equals-method.
	 * Check for uninitialized fieldvariables.
	 */
	@Override
	public boolean visit(final TypeDeclaration declaration) {
		if (declaration.resolveBinding().isClass()) {
			List<BodyDeclaration> body = declaration.bodyDeclarations();
			
			if(!declaration.resolveBinding().getSuperclass().getName().equals("Object")) {
				
				IMethodBinding[] superMethods = declaration.resolveBinding().getSuperclass().getDeclaredMethods();
				System.out.println("Super:" + Arrays.toString(superMethods));
				
				for (IMethodBinding method : declaration.resolveBinding().getDeclaredMethods()) {
					if (Arrays.asList(superMethods).contains(method)) {
						System.out.println(method + " should be @override");
					}
				}
			}
			
			
			
			boolean hasEqualsMethod = false;
			boolean overridesEqualMethod = false;
			
			if (body.isEmpty()) {
				errors.add(new MissingEqualsMethodError(declaration.getStartPosition(), declaration.getLength()));
			} else {
				for (BodyDeclaration decl : body) {
					
					if (decl instanceof MethodDeclaration) {
						IMethodBinding binding = ((MethodDeclaration) decl).resolveBinding();
						boolean methodIsEqualsMethod = binding.getName().equals("equals") && binding.getReturnType().getName().equals("boolean") && binding.getParameterTypes().length == 1 && binding.getParameterTypes()[0].getName().equals("Object");
						if (methodIsEqualsMethod) {
							hasEqualsMethod = true;
							if (!(binding.getAnnotations().length < 1) && binding.getAnnotations()[0].getName().equals("Override")) {
								overridesEqualMethod = true;
							}
						}
					}
					
					if (decl instanceof FieldDeclaration) {
						Object maybeChildren = decl.getStructuralProperty(FieldDeclaration.FRAGMENTS_PROPERTY);
						System.out.println(maybeChildren);
						
						if (maybeChildren != null) {
							List<ASTNode> children = (List<ASTNode>) maybeChildren;
							System.out.println(children);
							System.out.println(ASTNode.nodeClassForType(children.get(0).getNodeType()));
							for (ASTNode child : children) {
								if (child instanceof VariableDeclarationFragment) {
									if (((VariableDeclarationFragment) child).getInitializer() == null) {
										errors.add(new FieldDeclarationWithoutInitializerError(decl.getStartPosition(), decl.getLength()));
									}
								}
							}
						}
							
					}
					
				}
			}
			if (!hasEqualsMethod) {
				errors.add(new MissingEqualsMethodError(declaration.getStartPosition(), declaration.getLength()));
			}
			
			
			
		}
		return super.visit(declaration);
	}
	
	@Override 
	public boolean visit(final MethodInvocation method) {
		if (!(methodReturnsVoid(method) || methodReturnsBoolean(method))) {
			if (method.getParent().getNodeType() == ASTNode.EXPRESSION_STATEMENT) {
				errors.add(new IgnoringReturnError(method.getStartPosition(), method.getLength()));
				String suggestion =  "The call of this method has no effect unless you store it in a variable. \n"
						+ "This method returns " + method.resolveMethodBinding().getReturnType().getName() + " and you should consider doing this: \n \n" +
						method.resolveMethodBinding().getReturnType().getName() + " variableName = " + method.toString();
			} 
		}
		if (Modifier.isStatic(method.resolveMethodBinding().getModifiers()) && method.getExpression() != null) {
			if(method.getExpression().getNodeType() == ASTNode.SIMPLE_NAME) {
				SimpleName simpleName = (SimpleName) method.getExpression();
				if(simpleName.resolveBinding() instanceof IVariableBinding) {
					
					if (classInstances.containsKey(simpleName.getIdentifier())) {
						errors.add(new StaticAsNormalError(method.getStartPosition(), method.getLength()));
						String suggestion = "You may want to try to make the method non-static., or try: " + classInstances.get(simpleName.getIdentifier()) + method.toString().substring(method.toString().indexOf('.'));
						
					} else {
						errors.add(new StaticAsNormalError(method.getStartPosition(), method.getLength()));
						String suggestion = "You may want to try to make the method non-static. \n \n ";
									
					}
				}
			}
			
		}
		return super.visit(method);
	}
	
	private boolean methodReturnsVoid(MethodInvocation method) {
		return method.resolveMethodBinding().getReturnType().getName().equals("void");
	}
	
	private boolean methodReturnsBoolean(MethodInvocation method) {
		return method.resolveMethodBinding().getReturnType().getName().equals("boolean");
	}
	
	@Override 
	public boolean visit(final InfixExpression expression) {
		if (expression.getOperator() == Operator.AND || expression.getOperator() == Operator.OR) {
			if (expression.getLeftOperand().resolveTypeBinding().getName().equals("boolean") && expression.getRightOperand().resolveTypeBinding().getName().equals("boolean")) {
				errors.add(new BitwiseOperatorError(expression.getStartPosition(), expression.getLength()));
				String suggestion = "You may want to try " + expression.getLeftOperand().toString() + " " + expression.getOperator() + " " + expression.getRightOperand().toString();
			} 
		}
		if (expression.getOperator() == Operator.EQUALS && !expression.getLeftOperand().resolveTypeBinding().isPrimitive() && !expression.getRightOperand().resolveTypeBinding().isPrimitive() && !expression.getRightOperand().resolveTypeBinding().getName().equals("null")) {
			System.out.println("Type " + expression.getRightOperand().resolveTypeBinding().getName());
			System.out.println("Type " + expression.getLeftOperand().resolveTypeBinding().getName());
			if (!(expression.getLeftOperand().getNodeType() == ASTNode.STRING_LITERAL && expression.getRightOperand().getNodeType() == ASTNode.STRING_LITERAL)) {
				errors.add(new EqualsOperatorError(expression.getStartPosition(), expression.getLength()));
				String suggestion = "You should try \n "+ expression.getLeftOperand().toString() + ".equals("+ expression.getRightOperand().toString()  +")";
			}
		}
		return super.visit(expression);
	}
	
	@Override 
	public boolean visit(final IfStatement ifStatement) {
		if (ifStatement.getThenStatement() instanceof EmptyStatement) {
			errors.add(new SemiColonAfterIfError(ifStatement.getStartPosition(), ifStatement.getLength()));
			
		}
		if (!(ifStatement.getThenStatement().getNodeType() == ASTNode.BLOCK)) {
			errors.add(new IfWithoutBracketsError(ifStatement.getStartPosition(), ifStatement.getLength()));
		}
		return super.visit(ifStatement);
	}
	
	@Override
	public boolean visit(final ClassInstanceCreation classInstanceCreation) {
		if (classInstanceCreation.getParent().getNodeType() == ASTNode.VARIABLE_DECLARATION_FRAGMENT) {
			VariableDeclarationFragment varDecl = (VariableDeclarationFragment) classInstanceCreation.getParent();
			classInstances.put(varDecl.getName().getIdentifier(), classInstanceCreation.resolveTypeBinding().getName());
		}
		return super.visit(classInstanceCreation);
	}

	
	
	public ArrayList<BaseError> getErrors() {
		return this.errors;
	}
	
}
