package master.thesis.eclipse.plugin.handlers;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;

import master.thesis.backend.analyser.Analyser;
import editor.Editor;
import master.thesis.backend.errors.BaseError;
import master.thesis.backend.errors.BugReport;

public class ErrorHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		// Update editor to get possible other file in focus
		Editor.updateEditor();
		Editor.clearConsole();
		
		IFile file = Editor.getSelectedFile();
		
		String code = "";
		try {
			code = new String(file.getContents().readAllBytes(), StandardCharsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (CoreException e) {
			e.printStackTrace();
		}
		
		Analyser analyser = new Analyser();
		BugReport bugReport = analyser.analyse(code);
		
		if (!bugReport.getBugs().isEmpty()) {
			BaseError error = bugReport.getBugs().get(0);
			if (error.getLineNumber() != -1) {
				Editor.printToConsole("In file: " + file.getName() + ", in class " + error.getContainingClass() + ", at line " + error.getLineNumber() + ":");
			} else {
				Editor.printToConsole("In file: " + file.getName() + "in class " + error.getContainingClass()  + ":");
			}
			
			Editor.printToConsole(error.getWhat());
			
			if (error.getSuggestion().isPresent()) {
				Editor.printToConsole(" ");
				Editor.printToConsole(error.getSuggestion().get());
			}
			
			Editor.openFile(file);
			Editor.findAndMarkText(error.getOffset(), error.getLength());
			return null;
		} 
	
		
		Editor.printToConsole("Found no errors!");
		return null;
	}
}
