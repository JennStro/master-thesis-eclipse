package master.thesis.eclipse.plugin.handlers;

import java.util.ArrayList;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

import errors.BaseError;
import analyse.CodeAnalyser;
import analyse.Parser;
import editor.Editor;

import org.eclipse.jface.dialogs.MessageDialog;

public class ErrorHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		// Update editor to get possible other file in focus
		Editor.updateEditor();
		
		IFile theFile = Editor.getSelectedFile();
		CompilationUnit ast = (CompilationUnit) Parser.createAST(theFile);
		CodeAnalyser analyser = new CodeAnalyser();
		ast.accept(analyser);
		ArrayList<BaseError> errors = analyser.getErrors();
		
		Editor.printToConsole("Hello!");
		Editor.printToConsole(errors.get(0).getWhat());
		
		return null;
	}
}
