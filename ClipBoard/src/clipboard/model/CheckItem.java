package clipboard.model;

import org.eclipse.core.resources.IFile;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.compiler.IProblem;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.ui.text.java.IJavaCompletionProposal;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;

import clipboard.model.parser.NewContextGenerator;
import clipboard.model.parser.RegionParser;

public class CheckItem {
	private String oldMethod;
	private String preview;
	private WeightedNode node;
	private IProblem[] errorList;
	private MethodDeclaration md;
	private IFile file;
	private IDocument doc;
	private int startPos = 0;
	private int length = 0;
	private String[] errorMsg;
	
	private int maxLine = 0;
	private int offset = 0;
	private ICompilationUnit cu;
	private IJavaCompletionProposal[] proposals;
	private String[] proposalString;

	public IJavaCompletionProposal[] getProposals() {
		return proposals;
	}

	public String[] getProposalString() {
		return proposalString;
	}

	public void setProposals(IJavaCompletionProposal[] proposals) {
		this.proposals = proposals;
		proposalString = new String[proposals.length];
		for (int i = 0; i < proposals.length; i++) {
			IJavaCompletionProposal p = proposals[i];
			proposalString[i] = p.getDisplayString();
		}

	}

	public CheckItem(WeightedNode node) {
		this.node = node;
		preview = new NewContextGenerator().getNewMethodString(node.getNode(),
				EditorDropTargetItem.getItem());
		file = node.getFile();
		doc = RegionParser.convertFileToDoc(file);
		md = node.getNode().getMethodDeclaration();
		startPos = md.getStartPosition();
		length = md.getLength();

		try {
			oldMethod = doc.get(startPos, length);
			maxLine = oldMethod.split("\n").length;
			offset = preview.length() - oldMethod.length();
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public ICompilationUnit getCu() {
		return cu;
	}

	public void setCu(ICompilationUnit cu) {
		this.cu = cu;
	}

	public int getOffset() {
		return offset;
	}

	public int getMaxLine() {
		return maxLine;
	}

	public void setMaxLine(int maxLine) {
		this.maxLine = maxLine;
	}

	public String[] getErrorMsg() {
		return errorMsg;
	}

	public String getOldMethod() {
		return oldMethod;
	}

	public void setOldMethod(String oldMethod) {
		this.oldMethod = oldMethod;
	}

	public MethodDeclaration getMd() {
		return md;
	}

	public void setMd(MethodDeclaration md) {
		this.md = md;
	}

	public IFile getFile() {
		return file;
	}

	public void setFile(IFile file) {
		this.file = file;
	}

	public IDocument getDoc() {
		return doc;
	}

	public void setDoc(IDocument doc) {
		this.doc = doc;
	}

	public int getStartPos() {
		return startPos;
	}

	public void setStartPos(int startPos) {
		this.startPos = startPos;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public String getPreview() {
		return preview;
	}

	public void setPreview(String preview) {
		this.preview = preview;
	}

	public WeightedNode getnode() {
		return node;
	}

	public void setnode(WeightedNode node) {
		this.node = node;
	}

	public IProblem[] getErrorList() {
		return errorList;
	}

	public void setErrorList(IProblem[] errorList) {
		this.errorList = errorList;
		errorMsg = new String[errorList.length];
		for (int i = 0; i < errorList.length; i++) {
			IProblem p = errorList[i];
			errorMsg[i] = p.getMessage() + " at line" + p.getSourceLineNumber();
		}
	}

	public String toString() {
		return node.toString();
	}


}
