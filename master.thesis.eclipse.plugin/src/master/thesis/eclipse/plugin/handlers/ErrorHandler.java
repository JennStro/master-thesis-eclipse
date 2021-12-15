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
		Editor.clearConsole();
		
		ArrayList<IFile> files = Editor.getProjectFiles();
		
		for (IFile file : files) {
			CompilationUnit ast = (CompilationUnit) Parser.createAST(file);
			CodeAnalyser analyser = new CodeAnalyser();
			ast.accept(analyser);
			ArrayList<BaseError> errors = analyser.getErrors();
			
			
			if (!errors.isEmpty()) {
				Editor.printToConsole(file.getName());
				BaseError error = errors.get(0);
				Editor.printToConsole(error.getWhat());
				Editor.openFile(file);
				Editor.findAndMarkText(error.getOffset(), error.getLength());
				return null;
			} 
		}
		
		Editor.printToConsole("Found no errors!");
		
		return null;
	}
}
