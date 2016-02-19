package clipboard.model;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.PlatformUI;

import ch.uzh.ifi.seal.changedistiller.treedifferencing.Node;
import clipboard.model.parser.RegionParser;

/**
 * @Author Lisa
 * @Date: Apr 14, 2014
 */
public class ClipBoardEditLocator implements Runnable {
	RegionParser nodeGenerator = new RegionParser();
	Node template;

	public ClipBoardEditLocator(Node template) {
		this.template = template;
	}

	/**
	 * Locate all suitable functions in the open project
	 * 
	 * @param template
	 * @param threshold
	 * @return
	 */
	private List<WeightedNode> locateAllMatchNodes() {
		IProject[] projects = getTargetProjects();
		List<WeightedNode> matchNodes = new ArrayList<WeightedNode>();
		// TreeSet<WeightedNode> nodeSet = new TreeSet<WeightedNode> ();
		for (IProject project : projects) {
			matchNodes
					.addAll(getMatchMethodsInSingleProject(template, project));
		}
		return matchNodes;
	}

	public List<WeightedNode> locateMatchNodesBeyondThreshold() {
		List<WeightedNode> nodes = locateAllMatchNodes();
		List<WeightedNode> results = new ArrayList<WeightedNode>();
		for (WeightedNode node : nodes) {
			if (node.getMatchNodeWeight() >= ClipBoardItemProvider
					.getMATCH_THRESHOLD()) {
				results.add(node);
			}
		}
		Collections.sort(results, new Comparator<WeightedNode>() {
			public int compare(WeightedNode a, WeightedNode b) {
				return b.getMatchNodeWeight() - a.getMatchNodeWeight();
			}
		});
		return results;
	}

	private IProject[] getTargetProjects() {
		ISelectionService ss = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getSelectionService();
		IProject[] projectList;
		try {
			projectList = new IProject[1];
			projectList[0] = ((IResource) ((IStructuredSelection) ss
					.getSelection()).getFirstElement()).getProject();

			return projectList;
		} catch (Exception e) {

		}
		projectList = ResourcesPlugin.getWorkspace().getRoot().getProjects();
		return projectList;
	}

	private List<WeightedNode> getMatchMethodsInSingleFile(Node template,
			IFile file) {
		List<WeightedNode> nodeSet = new ArrayList<WeightedNode>();
		List<Node> nodes = nodeGenerator.getAllNodes(file);
		// List<Node> results = new ArrayList<Node>();
		for (Node node : nodes) {
			nodeSet.add(new WeightedNode(node, nodeGenerator.recurMatch(node,
					template), file));
		}
		return nodeSet;
	}

	private List<WeightedNode> getMatchMethodsInSingleProject(Node template,
			IProject project) {
		String[] filePath = template.getFilePath().getAbsolutePath().split("/");
		String filePkg = "";
		for (int i = 0; i < filePath.length - 1; i++)
			filePkg += filePath[i]+"/";
		JavaFileVisitor fVisitor = new JavaFileVisitor(filePkg);
		List<WeightedNode> resultNodes = new ArrayList<WeightedNode>();
		try {
			project.accept(fVisitor);
			List<IFile> fileList = fVisitor.getFiles();
			for (IFile file : fileList) {
				resultNodes.addAll(getMatchMethodsInSingleFile(template, file));
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultNodes;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		locateMatchNodesBeyondThreshold();
	}

	// private List<WeightedNode> getMatchMethodsInSinglePackage(Node template,
	// IProject project) {
	//
	// JavaFileVisitor fVisitor = new JavaFileVisitor();
	// List<WeightedNode> resultNodes = new ArrayList<WeightedNode>();
	// try {
	// project.accept(fVisitor);
	// List<IFile> fileList = fVisitor.getFiles();
	// for (IFile file : fileList) {
	// resultNodes.addAll(getMatchMethodsInSingleFile(template, file));
	// }
	// } catch (Exception e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// return resultNodes;
	// }

}
