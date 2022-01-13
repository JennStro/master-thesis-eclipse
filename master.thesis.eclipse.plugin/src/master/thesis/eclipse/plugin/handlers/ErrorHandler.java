package master.thesis.eclipse.plugin.handlers;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;

import editor.Editor;

public class ErrorHandler extends AbstractHandler {
	
	private static final String CONTAINING_CLASS_KEY = "\"containingClass\":";
	private static final String ERRORS = "\"status\":\"errors\"";
	private static final String LINENUMBER_KEY = "\"lineNumber\":";
	private static final String EXPLANATION_KEY = "\"explanation\":";
	private static final String SUGGESTION_KEY = "\"suggestion\":";

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		// Update editor to get possible other file in focus
		Editor.updateEditor();
		Editor.clearConsole();
		
		IFile file = Editor.getSelectedFile();
		
		try {
            URL url = new URL("https://master-thesis-web-backend-prod.herokuapp.com/analyse");
            URLConnection con = url.openConnection();
            HttpURLConnection http = (HttpURLConnection) con;
            http.setRequestMethod("POST");
            http.setDoOutput(true);
            http.setRequestProperty("Content-Type", "text/plain; charset=UTF-8");
            http.connect();

            try(OutputStream os = http.getOutputStream()) {
                try {
					os.write(file.getContents().readAllBytes());
				} catch (CoreException e) {
					e.printStackTrace();
				}
            }
            String response = new String(http.getInputStream().readAllBytes());
            
            if (response.contains(ERRORS)) {
                String result = "In class " + getValue(CONTAINING_CLASS_KEY, response);
                if (Integer.parseInt(getValue(LINENUMBER_KEY, response)) != -1) {
                   result += ", on line number " + getValue(LINENUMBER_KEY, response) + "\n";
                }
                result += getValue(EXPLANATION_KEY, response) + "\n";
                if (response.contains(SUGGESTION_KEY)) {
                    result += getValue(SUGGESTION_KEY, response);
                }
                Editor.printToConsole(result);
            } else {
            	Editor.printToConsole("Found no errors!");
            }
            



        } catch (IOException ex) {
            ex.printStackTrace();
        }
		return null;
	}
	
	private String getValue(String KEY, String obj) {
		String sub = obj.substring(obj.indexOf(KEY)+KEY.length());
		String val = sub.substring(0, sub.indexOf(","));
		String valStripped = val.replace('"', ' ');
		return valStripped;
	}
}
