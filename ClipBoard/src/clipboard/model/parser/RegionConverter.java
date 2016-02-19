package clipboard.model.parser;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;

import clipboard.contentassist.ClipBoardAssistAdapter;
import clipboard.model.CheckItem;
import clipboard.model.UndoEditManager;
import clipboard.model.UndoSystematicEdit;

/**
 * @Author Lisa
 * @Date: Apr 8, 2014
 */
public class RegionConverter {
	private static UndoEditManager manager = new UndoEditManager();
	private static UndoSystematicEdit edit;

	/**
	 * apply to content assist : given single template, single method, apply
	 * 
	 * @param document
	 * @param additionInfo
	 */
	public void applyTemplates(IDocument document, String additionInfo) {

		String replacement = additionInfo.replace("<p>", "");
		MethodDeclaration m = ClipBoardAssistAdapter.getLocatedMethod()
				.getMethodDeclaration();
		// TODO check compile error
		try {
			document.replace(m.getStartPosition(), m.getLength(), replacement);
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * predict all locations and apply to those locations
	 * 
	 * @param node
	 */
	// private CheckItem applyTemplates(WeightedNode node) {
	// CheckItem item = new CheckItem(node);
	//
	// String replace = new NewContextGenerator().getNewMethodString(
	// node.getNode(), EditorDropTargetItem.getItem());
	// IFile file = node.getFile();
	// // IDocument doc = new RegionParser().convertFileToDoc(file);
	// MethodDeclaration md = node.getNode().getMethodDeclaration();
	// previewChecking(doc,replace,file.getName(),startPos,length);
	// return PreviewSyntaxChecker.checkFromProject(file);
	// CheckItem item = PreviewSyntaxChecker.checkCompileError(new
	// CheckItem(node));
	// CheckItemProvider.addItem(item);
	// return item;
	// }
	public static UndoSystematicEdit applyTemplates(Object node) {
		// List<WeightedNode> itemList = new ArrayList<WeightedNode>();
		edit = new UndoSystematicEdit(node);
		manager.addEdit(edit);
		
		return edit;
	}


	public static void undoEdit(UndoSystematicEdit edit) {
		manager.undoChange(edit);
	}
	public static void clearOffsetMap() {
		manager.clearOffsetMap();
	}
	
	public static List<CheckItem> previewMultiEdit(List<Object> nodes) {
		List<CheckItem> editList = new ArrayList<CheckItem>();
		for (Object node: nodes) {
			UndoSystematicEdit edit = new UndoSystematicEdit(node);
			manager.addEdit(edit);
			manager.undoChange(edit);
			editList.add(edit.getItem());
		}
		return editList;
	}
	
	
}
