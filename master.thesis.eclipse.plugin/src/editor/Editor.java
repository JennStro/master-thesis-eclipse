


/*
 * 
 * This code is derived from the 
 * Eclipse IDE 2021-09 (4.21) Documentation (HTML Help Center)
 *   - Platform Plug-in Developer Guide 
 *      - Plugging into the workbench
 * 
 * URL: http://help.eclipse.org/2021-09/index.jsp
 * 
 * SPDX-FileCopyrightText: Copyright (c) Eclipse contributors 2000, 2020
 *
 * SPDX-License-Identifier: EPL-1.0
 */

/*
 * 
 * The methods findConsole and printToConsole are from
 * FAQ How do I write to the console from a plug-in?
 * 
 * URL: https://wiki.eclipse.org/FAQ_How_do_I_write_to_the_console_from_a_plug-in%3F
 * 
 * SPDX-FileCopyrightText: Copyright © Eclipse Foundation, Inc., 2011 by Simon Linden. Based on work by Volker Stolz, John J. Barton and Chris Laffra.
 *
 * SPDX-License-Identifier: EPL-2.0
 */

/*
 * SPDX-FileCopyrightText: 2021 Jenny Strømmen
 *
 * SPDX-License-Identifier: EPL-2.0
 */

package editor;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleConstants;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.IConsoleView;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;
import org.eclipse.ui.ide.IDE;
import org.eclipse.ui.texteditor.ITextEditor;

public class Editor {
	
	private static IEditorPart editor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
	
	public static IEditorPart getEditor() {
		return editor;
	}
	
	/**
	 * Update the editor. For example, when a user changes the selected file, the editor needs to be updated
	 * in order to get this new file such that it can be analyzed.
	 */
	public static void updateEditor() {
		editor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
	}
	
	/**
	 * 
	 * @param editor
	 * @return the current file open in the editor
	 */
	public static IFile getSelectedFile() {
		IFileEditorInput input = (IFileEditorInput) editor.getEditorInput();
		return input.getFile();
	}
	
	public static IWorkbenchPage getPage() {
		return PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
	}
	
	/**
	 * Taken from https://wiki.eclipse.org/FAQ_How_do_I_write_to_the_console_from_a_plug-in%3F
	 * @param name
	 * @return a console
	 */
	private static MessageConsole findConsole(String name) {
	      ConsolePlugin plugin = ConsolePlugin.getDefault();
	      IConsoleManager consoleManager = plugin.getConsoleManager();
	      IConsole[] existing = consoleManager.getConsoles();
	      for (int i = 0; i < existing.length; i++)
	         if (name.equals(existing[i].getName()))
	            return (MessageConsole) existing[i];
	      MessageConsole myConsole = new MessageConsole(name, null);
	      consoleManager.addConsoles(new IConsole[]{myConsole});
	      return myConsole;
	   }
	
	/**
	 * Taken from https://wiki.eclipse.org/FAQ_How_do_I_write_to_the_console_from_a_plug-in%3F
	 * 
	 */
	public static void printToConsole(String text) {
		MessageConsole myConsole = findConsole("Errors");
		MessageConsoleStream out = myConsole.newMessageStream();
		
		out.print(text);
		out.println();
		
		IWorkbenchPage page = Editor.getPage();
		String id = IConsoleConstants.ID_CONSOLE_VIEW;
		IConsoleView view;
		try {
			view = (IConsoleView) page.showView(id);
			view.display(myConsole);
		} catch (PartInitException e) {
			e.printStackTrace();
		}
	}
	
	public static void clearConsole() {
		MessageConsole myConsole = findConsole("Errors");
		myConsole.clearConsole();
	}
	
	public static void findAndMarkText(int start, int length) {
		((ITextEditor) editor).selectAndReveal(start, length);
	}
	
	public static int getLineNumber(int offset) {
		IDocument doc = ((ITextEditor) editor).getDocumentProvider().getDocument(editor.getEditorInput());
		int lineNumber = 0;
		try {
			lineNumber = doc.getLineOfOffset(offset) + 1;
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		return lineNumber;
	}

	public static void openFile(IFile file) {
		try {
			IDE.openEditor(getPage(), file);
		} catch (PartInitException e) {
			e.printStackTrace();
		}
	}

}