package clipboard.model.parser;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.PlatformUI;

import test.UTGeneralVisitor;
import ut.seal.plugins.utils.ast.UTASTParser;
import clipboard.model.CheckItem;

/**
 * @Author Lisa
 * @Date: Apr 7, 2014
 */
public class PreviewSyntaxChecker {

	public static CheckItem checkFromProject(CheckItem item) {

		IProject[] project = getTargetProjects();
		IJavaProject javaProject = JavaCore.create(project[0]);
		String message = "";
		UTASTParser astParser = new UTASTParser();
		UTGeneralVisitor<MethodDeclaration> mVisitor = new UTGeneralVisitor<MethodDeclaration>() {
			public boolean visit(MethodDeclaration node) {
				results.add(node);
				return true;
			}
		};

		try {
			IPackageFragment[] packages = javaProject.getPackageFragments();

			for (IPackageFragment pkg : packages) {

				ICompilationUnit[] compilationUnits = pkg.getCompilationUnits();
				for (ICompilationUnit cu : compilationUnits) {
					if (cu.getPath().equals(item.getFile().getFullPath())) {
						item.setCu(cu);
						CompilationUnit parser = astParser.parse(cu);
						IProblem[] list = parser.getProblems();
					
						item.setErrorList(list);
						return item;
					}
				}
			}
		} catch (JavaModelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return item;
	}

	private static IProject[] getTargetProjects() {
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
}
