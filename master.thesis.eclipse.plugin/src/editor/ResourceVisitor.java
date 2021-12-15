package editor;

import java.util.ArrayList;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.runtime.CoreException;

public class ResourceVisitor implements IResourceVisitor {
	
	private ArrayList<IFile> files = new ArrayList<>();

	@Override
	public boolean visit(IResource resource) throws CoreException {
		System.out.println(resource);
		if (resource.getType() == IResource.FILE) {
			IFile file = (IFile) resource;
			if (file.getFileExtension().equals("java")) {
				files.add(file);
			}
			
			return false;
		}
		return true;
	}
	
	public ArrayList<IFile> getFiles() {
		return files;
	}

}
