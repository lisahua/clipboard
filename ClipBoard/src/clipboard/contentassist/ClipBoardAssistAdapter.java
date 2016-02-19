package clipboard.contentassist;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jface.text.IDocument;

import test.UTGeneralVisitor;
import ut.seal.plugins.utils.ast.UTASTNodeConverter;
import ut.seal.plugins.utils.ast.UTASTParser;
import ch.uzh.ifi.seal.changedistiller.treedifferencing.Node;
import clipboard.model.ClipBoardItem;
import clipboard.model.ClipBoardItemProvider;
import clipboard.model.parser.NewContextGenerator;
import clipboard.model.parser.RegionParser;

/**
 * @Author Lisa
 * @Date: Mar 20, 2014
 */
public class ClipBoardAssistAdapter {

	private static ArrayList<String> proposals;
	private static List<String> contextInfos;
	private static UTASTNodeConverter nodeConverter = new UTASTNodeConverter();
	private static List<ClipBoardItem> templates;
	// private static int methodStart = 0,methodLength = 0;
	private static Node method;
	private static ClipBoardItem selectedItem;

	public static ClipBoardItem getSelectedItem() {
		return selectedItem;
	}

	public static Node getLocatedMethod() {
		return method;
	}

	// private static IDocument currentDoc;
	public static ArrayList<String> getProposals(IDocument document,
			CharSequence prefix, int firstPosition) {

		// step1: retrieve current method
		method = locateMethod(document, firstPosition);
		// step2: match template
		templates = matchTemplates(document);
		// step3: apply the transformation
		editTemplate(applyTransformation());

		return proposals;
	}

	public static String getTemplateInfo(String proposal) {
		int index = proposals.indexOf(proposal);
		if (index < 0)
			return "";
		selectedItem = templates.get(index);
		return contextInfos.get(index);
	}

	// public static ClipBoardItem getTemplateItem(String proposal) {
	// int index = proposals.indexOf(proposal);
	// return index >= 0 ? templates.get(index) : null;
	// // }
	// public static int[] getReplaceOffsetLength () {
	// return new int[] {methodStart,methodLength};
	// }
	private static void editTemplate(List<String> contexts) {
		proposals = new ArrayList<String>();
		contextInfos = new ArrayList<String>();
		for (int i = 0; i < templates.size(); i++) {
			proposals
					.add("Template "
							+ templates.get(i).getOldReg().getNodes().get(0)
									.getValue());
			contextInfos.add(contexts.get(i).replace("\n", "\n<p>"));
		}
	}

	private static Node locateMethod(IDocument doc, int firstPosition) {

		UTASTParser astParser = new UTASTParser();
		UTGeneralVisitor<MethodDeclaration> mVisitor = new UTGeneralVisitor<MethodDeclaration>() {
			public boolean visit(MethodDeclaration node) {
				results.add(node);
				return true;
			}
		};
		CompilationUnit parser = astParser.parse(doc.get());
		parser.accept(mVisitor);
		List<MethodDeclaration> lstMethods = mVisitor.getResults();
		int start = 0, end = lstMethods.size() - 1;
		for (; start <= end; start++) {
			// System.out.println(lstMethods.get(start).getStartPosition());
			if (lstMethods.get(start).getStartPosition() < firstPosition)
				continue;
			else
				break;
		}

		start = (start > 0) ? --start : 0;

		// methodStart = lstMethods.get(start).getStartPosition();
		// methodLength = lstMethods.get(start).getLength();

		Node contextNode = nodeConverter.convertMethod(lstMethods.get(start),
				parser);

		return contextNode;

	}

	/**
	 * TODO need to be changed to weightedNode old_current match old_template
	 * TODO: prefix_current match new_template
	 * 
	 * @param doc
	 *            : current
	 * @param method
	 *            : template
	 * @return a list of matched scripts
	 */
	private static List<ClipBoardItem> matchTemplates(IDocument doc) {

		List<ClipBoardItem> result = new ArrayList<ClipBoardItem>();
		List<ClipBoardItem> mList = ClipBoardItemProvider.getItems();
		for (int i = 0; i < mList.size(); i++) {
			List<Node> methodList = mList.get(i).getOldReg().getNodes();
			for (Node m1 : methodList) {
				int matchWeight = new RegionParser().recurMatch(m1, method);
				if (matchWeight > 0) {
					ClipBoardItem item = mList.get(i);
					item.setMatchWeight(matchWeight);
					result.add(item);
					break;
				}
			}
		}
		return result;
	}

	/**
	 * Step 1: changedistill old_current, new_template.
	 * 
	 * @param method
	 *            old_current
	 * @param template
	 *            old_tempalte+ new_template: use the new template_text->
	 */
	private static List<String> applyTransformation() {
		List<String> newContexts = new ArrayList<String>();
		for (ClipBoardItem template : templates) {
			newContexts.add(new NewContextGenerator().getNewMethodString(
					method, template));
		}
		return newContexts;
	}

	public static String getFileName() {
		if (method == null)
			return "";
		return method.getFilePath().getName();
	}

}
