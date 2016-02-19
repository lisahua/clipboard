package clipboard.contentassist;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IInformationControlCreator;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.ICompletionProposalExtension;
import org.eclipse.jface.text.contentassist.ICompletionProposalExtension2;
import org.eclipse.jface.text.contentassist.ICompletionProposalExtension3;
import org.eclipse.jface.text.contentassist.ICompletionProposalExtension4;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;

import clipboard.model.parser.RegionConverter;

/**
 * @Author Lisa
 * @Date: Mar 22, 2014
 */
public class SynditProposal implements ICompletionProposal,
		ICompletionProposalExtension, ICompletionProposalExtension2,
		ICompletionProposalExtension3, ICompletionProposalExtension4 {

	private String fString;
	private String fPrefix;
	private int fOffset;
	private String additionInfo="";
	public SynditProposal(String string, String prefix, int offset) {
		fString = string;
		fPrefix = prefix;
		fOffset = offset;
	}

	public void apply(IDocument document) {
		apply(null, '\0', 0, fOffset);
	}

	public Point getSelection(IDocument document) {
		return new Point(fOffset + fString.length(), 0);
	}

	public String getAdditionalProposalInfo() {
			additionInfo = ClipBoardAssistAdapter.getTemplateInfo(fString);
		return additionInfo;
	}

	public String getDisplayString() {
		return fPrefix + fString;
	}

	public Image getImage() {
		return null;
	}

	public IContextInformation getContextInformation() {
		return null;
	}

	public void apply(IDocument document, char trigger, int offset) {
//		try {

//			String replacement = fString.substring(offset - fOffset);
			if  (fString.startsWith("Template ") ) {
				new RegionConverter().applyTemplates(document, additionInfo);
			}
			
//			document.replace(offset, 0, replacement);
//		} catch (BadLocationException x) {
//			// TODO Auto-generated catch block
//			x.printStackTrace();
//		}
	}

	public boolean isValidFor(IDocument document, int offset) {
		return validate(document, offset, null);
	}

	public char[] getTriggerCharacters() {
		return null;
	}

	public int getContextInformationPosition() {
		return 0;
	}

	public void apply(ITextViewer viewer, char trigger, int stateMask,
			int offset) {
		apply(viewer.getDocument(), trigger, offset);
	}

	public void selected(ITextViewer viewer, boolean smartToggle) {
	}

	public void unselected(ITextViewer viewer) {
	}

	public boolean validate(IDocument document, int offset, DocumentEvent event) {
		try {
			int prefixStart = fOffset - fPrefix.length();
			return offset >= fOffset
					&& offset < fOffset + fString.length()
					&& document.get(prefixStart, offset - (prefixStart))
							.equals((fPrefix + fString).substring(0, offset
									- prefixStart));
		} catch (BadLocationException x) {
			return false;
		}
	}

	public IInformationControlCreator getInformationControlCreator() {
		return null;
	}

	public CharSequence getPrefixCompletionText(IDocument document,
			int completionOffset) {
		return fPrefix + fString;
	}

	public int getPrefixCompletionStart(IDocument document, int completionOffset) {
		return fOffset - fPrefix.length();
	}

	public boolean isAutoInsertable() {
		return true;
	}
}