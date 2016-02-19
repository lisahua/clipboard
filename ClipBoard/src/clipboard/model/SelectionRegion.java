package clipboard.model;

import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.Region;

import ch.uzh.ifi.seal.changedistiller.treedifferencing.Node;
import clipboard.model.parser.RegionParser;

/**
 * @Author Lisa
 * @Date: Mar 20, 2014
 */
public class SelectionRegion extends Region {
	// private int offset = 0;
	// private int endPosition = 0;
	private IFile file = null;
	private String regionText = "";
	private IDocument doc;
	private List<Node> nodes;

	public SelectionRegion(int offset, int length, String text) {
		super(offset, length);
		this.regionText = text;
	}

	public SelectionRegion(int offset, int length, IFile file,
			IDocument currentDoc, String text) {
		super(offset, length);
		this.file = file;

		this.regionText = text;
		doc = currentDoc;
		nodes = new RegionParser().identifyMethod(file,doc, offset, length);
	}

	public List<Node> getNodes() {
		return nodes;
	}

	public IDocument getDoc() {
		return doc;
	}

	public void setDoc(IDocument doc) {
		this.doc = doc;
	}

	// public CompilationUnit getParser() {
	// return parser;
	// }

	// public void setParser(CompilationUnit parser) {
	// this.parser = parser;
	// }

	public String getRegionText() {
		return regionText;
	}

	public void setRegionText(String regionText) {
		this.regionText = regionText;
	}

	// public List<MethodDeclaration> getSelectedMethodList() {
	// return selectedMethodList;
	// }

	public IFile getFile() {
		return file;
	}

	public void setFile(IFile file) {
		this.file = file;
	}

	/**
	 * startPosition and file name are the same
	 */
	public boolean equals(Object element) {
		if (!(element instanceof SelectionRegion))
			return false;
		SelectionRegion item = (SelectionRegion) element;
		return this.getOffset() == item.getOffset()
				&& file.getName().equals(item.getFile().getName());
	}

	public int hashCode() {
		return 0;
	}

}
