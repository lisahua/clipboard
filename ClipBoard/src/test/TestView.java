//package test;
//
//import java.io.File;
//import java.util.List;
//
//import org.eclipse.core.resources.IProject;
//import org.eclipse.core.resources.IWorkspace;
//import org.eclipse.core.resources.IWorkspaceRoot;
//import org.eclipse.core.resources.ResourcesPlugin;
//import org.eclipse.core.runtime.CoreException;
//import org.eclipse.jdt.core.ICompilationUnit;
//import org.eclipse.jdt.core.IJavaProject;
//import org.eclipse.jdt.core.IMethod;
//import org.eclipse.jdt.core.IPackageFragment;
//import org.eclipse.jdt.core.IPackageFragmentRoot;
//import org.eclipse.jdt.core.IType;
//import org.eclipse.jdt.core.JavaCore;
//import org.eclipse.jdt.core.JavaModelException;
//import org.eclipse.jdt.core.dom.Block;
//import org.eclipse.jdt.core.dom.CompilationUnit;
//import org.eclipse.jdt.core.dom.MethodDeclaration;
//import org.eclipse.jface.action.IMenuManager;
//import org.eclipse.jface.action.IToolBarManager;
//import org.eclipse.jface.text.Document;
//import org.eclipse.swt.SWT;
//import org.eclipse.swt.events.MouseAdapter;
//import org.eclipse.swt.events.MouseEvent;
//import org.eclipse.swt.widgets.Button;
//import org.eclipse.swt.widgets.Composite;
//import org.eclipse.ui.part.ViewPart;
//
//import ut.seal.plugins.utils.ast.UTASTNodeConverter;
//import ut.seal.plugins.utils.ast.UTASTParser;
//import ut.seal.plugins.utils.change.UTChangeDistiller;
//import ch.uzh.ifi.seal.changedistiller.treedifferencing.Node;
//
//public class TestView extends ViewPart {
//
//	public static final String	ID				= "test.MyView2";			//$NON-NLS-1$
//	UTASTNodeConverter			nodeConverter	= new UTASTNodeConverter();
//
//	public TestView() {
//	}
//
//	/**
//	 * Create contents of the view part.
//	 * 
//	 * @param parent
//	 */
//	@Override
//	public void createPartControl(Composite parent) {
//		Composite container = new Composite(parent, SWT.NONE);
//
//		Button button = new Button(container, SWT.NONE);
//		button.addMouseListener(new MouseAdapter() {
//			@Override
//			public void mouseDown(MouseEvent e) {
//
//				start();
//
//			}
//		});
//		button.setBounds(10, 10, 94, 28);
//		button.setText("New Button");
//
//		createActions();
//		initializeToolBar();
//		initializeMenu();
//	}
//
//	void start() {
//		System.out.println("******** START **********");
//		// Get the root of the workspace
//		IWorkspace workspace = ResourcesPlugin.getWorkspace();
//		IWorkspaceRoot root = workspace.getRoot();
//		// Get all projects in the workspace
//		IProject[] projects = root.getProjects();
//		// Loop over all projects
//		for (IProject project : projects) {
//			try {
//				printProjectInfo(project);
//			} catch (CoreException e) {
//				e.printStackTrace();
//			}
//		}
//	}
//
//	void printProjectInfo(IProject project) throws CoreException, JavaModelException {
//		System.out.println("Working in project " + project.getName());
//		// check if we have a Java project
//		if (project.isNatureEnabled("org.eclipse.jdt.core.javanature")) {
//			IJavaProject javaProject = JavaCore.create(project);
//			printPackageInfos(javaProject);
//		}
//	}
//
//	void printPackageInfos(IJavaProject javaProject) throws JavaModelException {
//		IPackageFragment[] packages = javaProject.getPackageFragments();
//		for (IPackageFragment mypackage : packages) {
//			if (mypackage.getKind() == IPackageFragmentRoot.K_SOURCE) {
//				System.out.println("Package " + mypackage.getElementName());
//				printICompilationUnitInfo(mypackage);
//
//			}
//		}
//	}
//
//	void printICompilationUnitInfo(IPackageFragment mypackage) throws JavaModelException {
//		for (ICompilationUnit unit : mypackage.getCompilationUnits()) {
//			printCompilationUnitDetails(unit);
//			printNode(unit);
//		}
//	}
//
//	private void printNode(ICompilationUnit unit) {
//		UTIGeneralVisitor<MethodDeclaration> mVisitor = new UTGeneralVisitor<MethodDeclaration>() {
//			public boolean visit(MethodDeclaration node) {
//				results.add(node);
//				return true;
//			}
//		};
//		File filePath = unit.getPath().toFile().getAbsoluteFile();
//		UTASTParser astParser = new UTASTParser();
//		CompilationUnit parser = astParser.parse(unit);
//		List<MethodDeclaration> lstMethods = mVisitor.getResults();
//		lstMethods.clear();
//		parser.accept(mVisitor);
//
//		if (lstMethods.size() > 1) {
//			Node node1 = nodeConverter.convertMethod(lstMethods.get(0), parser);
//			Node node2 = nodeConverter.convertMethod(lstMethods.get(1), parser);
//			UTChangeDistiller changeDistiller = new UTChangeDistiller();
//			changeDistiller.diffMethod(node1, node2);
//			changeDistiller.printChanges();
//		}
//
//		for (int j = 0; j < lstMethods.size(); j++) {
//			MethodDeclaration iMethod = lstMethods.get(j);
//			Block body = iMethod.getBody();
//			if (body == null) {
//				continue;
//			}
//			Node iNode = nodeConverter.convertMethod(iMethod, parser);
//			iNode.print();
//			System.out.println("--------------");
//			System.out.println("* " + filePath);
//			System.out.println("--------------");
//		}
//	}
//
//	void printCompilationUnitDetails(ICompilationUnit unit) throws JavaModelException {
//		System.out.println("Source file " + unit.getElementName());
//		Document doc = new Document(unit.getSource());
//		System.out.println("Has number of lines: " + doc.getNumberOfLines());
//		printIMethods(unit);
//	}
//
//	void printIMethods(ICompilationUnit unit) throws JavaModelException {
//		IType[] allTypes = unit.getAllTypes();
//		for (IType type : allTypes) {
//			printIMethodDetails(type);
//		}
//	}
//
//	void printIMethodDetails(IType type) throws JavaModelException {
//		IMethod[] methods = type.getMethods();
//		for (IMethod method : methods) {
//			System.out.println("Method name " + method.getElementName());
//			System.out.println("Signature " + method.getSignature());
//			System.out.println("Return Type " + method.getReturnType());
//		}
//	}
//
//	/**
//	 * Create the actions.
//	 */
//	private void createActions() {
//		// Create the actions
//	}
//
//	/**
//	 * Initialize the toolbar.
//	 */
//	private void initializeToolBar() {
//		@SuppressWarnings("unused")
//		IToolBarManager toolbarManager = getViewSite().getActionBars().getToolBarManager();
//	}
//
//	/**
//	 * Initialize the menu.
//	 */
//	private void initializeMenu() {
//		@SuppressWarnings("unused")
//		IMenuManager menuManager = getViewSite().getActionBars().getMenuManager();
//	}
//
//	@Override
//	public void setFocus() {
//		// Set the focus
//	}
//
//}
