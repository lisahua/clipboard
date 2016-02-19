package clipboard.model.parser;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jface.text.IDocument;
import org.eclipse.ui.editors.text.TextFileDocumentProvider;
import org.eclipse.ui.texteditor.IDocumentProvider;

import test.UTGeneralVisitor;
import ut.seal.plugins.utils.ast.UTASTNodeConverter;
import ut.seal.plugins.utils.ast.UTASTParser;
import ch.uzh.ifi.seal.changedistiller.treedifferencing.Node;

/**
 * Identify located method, convert methods to nodes and recursively match nodes
 * 
 * @Author Lisa
 * @Date: Apr 6, 2014
 */
public class RegionParser {

	private CompilationUnit parser;
	private int idLenThreshold = 3;

	public List<Node> identifyMethod(IFile file, IDocument doc, int offset,
			int length) {

		List<MethodDeclaration> lstMethods = getAllMethods(doc.get());

		int start = 0, end = lstMethods.size() - 1;
		int endPos = offset + length;
		for (; start <= end;) {
			if (lstMethods.get(start).getStartPosition() < offset) {
				start++;
				continue;
			} else
				// start = (start>0)? --start:0;
				break;
		}
		for (; start <= end;) {
			if (lstMethods.get(end).getStartPosition() > endPos) {
				end--;
				continue;
			} else
				break;
		}

		List<MethodDeclaration> selectedMethodList = lstMethods.subList(start,
				end + 1);

		return convertMethodToNode(selectedMethodList, file);
	}

	/**
	 * set parser
	 * 
	 * @param doc
	 * @return
	 */
	public List<MethodDeclaration> getAllMethods(String fileS) {

		UTASTParser astParser = new UTASTParser();
		UTGeneralVisitor<MethodDeclaration> mVisitor = new UTGeneralVisitor<MethodDeclaration>() {
			public boolean visit(MethodDeclaration node) {
				results.add(node);
				return true;
			}
		};

		parser = astParser.parse(fileS);
		parser.accept(mVisitor);

		return mVisitor.getResults();
	}

	/**
	 * use parser
	 * 
	 * @param mList
	 * @return
	 */
	private List<Node> convertMethodToNode(List<MethodDeclaration> mList,
			IFile file) {
		UTASTNodeConverter nodeConverter = new UTASTNodeConverter();
		List<Node> nodes = new ArrayList<Node>();
		File ioFile = new File(file.getFullPath().toOSString());
		for (MethodDeclaration m : mList) {

			nodes.add(nodeConverter.convertMethod(m, parser));
		}
		for (Node node : nodes) {
			node.setFilePath(ioFile);
		}
		return nodes;
	}

	public List<Node> getAllNodes(IFile file) {
		List<MethodDeclaration> lstMethods = getAllMethods(convertFileToDoc(
				file).get());
		return convertMethodToNode(lstMethods, file);
	}

	public int recurMatch(Node template, Node method) {
		// TODO should change to longest common match, match from leaf nodes
		int matchNode = 0;
		List<Node> templateNodes = template.getChildren();
		List<Node> methodNodes = method.getChildren();

		for (int i = 0; i < Math.min(templateNodes.size(), methodNodes.size()); i++) {
			Node templateN = templateNodes.get(i);
			Node methodN = methodNodes.get(i);
			if (templateN.getNodeType().equals(methodN.getNodeType())) {
				matchNode++;
				String tValue = templateN.getValue()
						.replaceAll("[a-zA-Z]+", "");
				String mValue = methodN.getValue().replaceAll("[a-zA-Z]+", "");
				if (tValue.contains(mValue))
					matchNode += mValue.length();
				matchNode += recurMatch(templateNodes.get(i),
						methodNodes.get(i));
			}
		}
		return matchNode;
	}

	public static IDocument convertFileToDoc(IFile file) {
		IDocumentProvider provider = new TextFileDocumentProvider();
		IDocument doc = null;
		try {
			provider.connect(file);
			doc = provider.getDocument(file);
			provider.disconnect(file);
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return doc;
	}

	/*
	 * public Node LCSmatch(Node root1, Node root2) { // TODO longest common
	 * subtree //List<Node> childList = new ArrayList<Node>(); List<Node>
	 * children1 = root1.getChildren(); List<Node> children2 =
	 * root2.getChildren(); if (children1 != null && children1.size() > 0) { for
	 * (Node node1 : children1) { Node n = LCSmatch(node1, root2); if (n==null)
	 * continue; node1.addAssociatedNode(n); System.out.println("root1 "+n); } }
	 * if (children2 != null && children2.size() > 0) { for (Node node2 :
	 * children2) { Node n = LCSmatch(root1, node2); // if (n==null) continue;
	 * // root1.addAssociatedNode(n); // System.out.println("root2 "+n); } } if
	 * (root1.getNodeType().equals(root2.getNodeType())) { String value1 =
	 * root1.getValue().replaceAll("[a-zA-Z]+", ""); String value2 =
	 * root2.getValue().replaceAll("[a-zA-Z]+", ""); if (value1.contains(value2)
	 * || value2.contains(value1) || value1.length() -
	 * longestCommonSubstring(value1, value2).length() < idLenThreshold) { //
	 * assume threshold<2 return root1; } }
	 * 
	 * return null; }
	 */
	private String longestCommonSubstring(String s1, String s2) {
		int start = 0;
		int max = 0;
		for (int i = 0; i < s1.length(); i++) {
			for (int j = 0; j < s2.length(); j++) {
				int x = 0;
				while (s1.charAt(i + x) == s2.charAt(j + x)) {
					x++;
					if (((i + x) >= s1.length()) || ((j + x) >= s2.length()))
						break;
				}
				if (x > max) {
					max = x;
					start = i;
				}
			}
		}
		return s1.substring(start, (start + max));
	}
}
