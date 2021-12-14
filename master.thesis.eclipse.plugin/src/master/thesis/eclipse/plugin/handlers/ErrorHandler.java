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
		Editor.getProjectFiles();
		CompilationUnit ast = (CompilationUnit) Parser.createAST(theFile);
		CodeAnalyser analyser = new CodeAnalyser();
		ast.accept(analyser);
		ArrayList<BaseError> errors = analyser.getErrors();
		
		int totalErrors = errors.size();
		
		if (!errors.isEmpty()) {
			
			BaseError error = errors.get(0);
			
			
			Editor.findAndMarkText(error.getOffset(), error.getLength());
			
			Editor.printToConsole(error.getWhat());
			errors.remove(0);
			
		} else {
			Editor.printToConsole("Found no errors!");
		}
		
		return null;
	}
}
