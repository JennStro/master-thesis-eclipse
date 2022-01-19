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
import org.json.JSONObject;

import editor.Editor;

public class ErrorHandler extends AbstractHandler {

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
            JSONObject obj = new JSONObject(response);
            
            
            if (obj.has("hasException") && obj.getBoolean("hasException")) {
                String result = "Uh oh, could not analyse your code! :(";
                Editor.printToConsole(result);
            } else {
                if (obj.get("status").equals("errors")) {
                    String result = "In class " + obj.getString("containingClass");
                    if (obj.getInt("lineNumber") != -1) {
                        result += ", on line number " + obj.get("lineNumber");
                    }
                    result += "\n\n" + obj.getString("explanation");
                    if (obj.has("suggestion")) {
                        result += "\n \nYou should try \n" + obj.getString("suggestion");
                    }
                    if (obj.has("moreInfoLink")) {
                        result += "\n\nMore info? Check out " + obj.get("moreInfoLink");
                    }
                    if (obj.has("tip")) {
                        result += "\n\n" + obj.get("tip");
                    }
                    Editor.printToConsole(result);
                } else {
                	Editor.printToConsole("Found no errors!");
                }
            }
            



        } catch (IOException ex) {
            ex.printStackTrace();
        }
		return null;
	}
}
