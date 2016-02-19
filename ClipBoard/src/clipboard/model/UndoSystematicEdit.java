package clipboard.model;

import org.eclipse.core.resources.IFile;
import org.eclipse.jdt.ui.text.java.IJavaCompletionProposal;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;

import clipboard.model.parser.PreviewSyntaxChecker;
import clipboard.model.parser.QuickFixPerformer;
import clipboard.model.parser.RegionParser;

/**
 * @Author Lisa
 * @Date: May 4, 2014
 */
public class UndoSystematicEdit   {
	CheckItem item;
	int offset = 0;

	public int getOffset() {
		return item.getOffset();
	}

	/**
	 * increase offset
	 * 
	 * @param item
	 * @param offset
	 */
	public UndoSystematicEdit(Object node) {
		this.item = new CheckItem((WeightedNode) node);
//		this.offset = offset;
	}

	public void applyChange(int preOffset) {
		IDocument doc = RegionParser.convertFileToDoc(item.getFile());
		try {
			doc.replace(item.getStartPos()+preOffset, item.getLength(), item.getPreview());
			item = PreviewSyntaxChecker.checkFromProject(item);
			item.setProposals(QuickFixPerformer.actionPerform(item));
			
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}
	public String[] getSyntaxError() {
		return item.getErrorMsg();
	}
	public String[] getProposal() {
		return item.getProposalString();
	}
	
	public IFile getFile() {
		return item.getFile();
	}
	public CheckItem getItem() {
		return item;
	}
	
}
