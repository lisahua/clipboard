package clipboard.model;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.runtime.CoreException;

/**
 * @Author Lisa
 * @Date: Apr 14, 2014
 */
public class JavaFileVisitor implements IResourceVisitor {
	private List<IFile> results = new ArrayList<IFile>();
	String path = "";

	public JavaFileVisitor(String pkg) {
		path = pkg;
	}

	@Override
	public boolean visit(IResource resource) throws CoreException {
		return getPackageFiles(resource);
		// return getAllFiles(resource);
	}

	public List<IFile> getFiles() {
		return results;
	}

	private boolean getAllFiles(IResource resource) {
		if (resource.getName().contains(".java")) {
			results.add((IFile) resource);
		}
		return true;
	}

	private boolean getPackageFiles(IResource resource) {
		String resourceLoc = resource.getFullPath().toOSString();
		if (resourceLoc.contains(path) && resource.getName().contains(".java")) {
			results.add((IFile) resource);
		}
		return true;
	}
}
