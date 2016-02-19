package clipboard.contentassist;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.contentassist.IContextInformationValidator;

/**
 * @Author Lisa
 * @Date: Mar 22, 2014
 */
public class SynditProposalProcessor implements IContentAssistProcessor {

	private static final ICompletionProposal[] NO_PROPOSALS= new ICompletionProposal[0];
	private static final IContextInformation[] NO_CONTEXTS= new IContextInformation[0];

	private final SynditCompletionEngine fEngine= new SynditCompletionEngine();

	/*
	 * @see org.eclipse.jface.text.contentassist.IContentAssistProcessor#computeCompletionProposals(org.eclipse.jface.text.ITextViewer, int)
	 */
	public ICompletionProposal[] computeCompletionProposals(ITextViewer viewer, int offset) {
		try {
			String prefix= getPrefix(viewer, offset);
//			if (prefix == null || prefix.length() == 0)
//				return NO_PROPOSALS;
			
			List<String> suggestions= getSuggestions(viewer, offset, prefix);
			
			List<ICompletionProposal> result= new ArrayList<ICompletionProposal>();
			for (Iterator<String> it= suggestions.iterator(); it.hasNext();) {
				String string= (String) it.next();
				if (string.length() > 0)
					result.add(createProposal(string, prefix, offset));
			}
			
			return result.toArray(new ICompletionProposal[result.size()]);
			
		} catch (BadLocationException x) {
			// ignore and return no proposals
			
			return NO_PROPOSALS;
		}
	}

	private String getPrefix(ITextViewer viewer, int offset) throws BadLocationException {
		IDocument doc= viewer.getDocument();
		if (doc == null || offset > doc.getLength())
			return null;
		
		int length= 0;
		while (--offset >= 0 && Character.isJavaIdentifierPart(doc.getChar(offset)))
			length++;
		
		return doc.get(offset + 1, length);
	}

	private ICompletionProposal createProposal(String string, String prefix, int offset) {
		return new SynditProposal(string, prefix, offset);
	}

	/*
	 * @see org.eclipse.jface.text.contentassist.IContentAssistProcessor#computeContextInformation(org.eclipse.jface.text.ITextViewer, int)
	 */
	public IContextInformation[] computeContextInformation(ITextViewer viewer, int offset) {
		// no context informations 
		return NO_CONTEXTS;
	}
	
	/*
	 * @see org.eclipse.jface.text.contentassist.IContentAssistProcessor#getCompletionProposalAutoActivationCharacters()
	 */
	public char[] getCompletionProposalAutoActivationCharacters() {
		return null;
	}
	
	/*
	 * @see org.eclipse.jface.text.contentassist.IContentAssistProcessor#getContextInformationAutoActivationCharacters()
	 */
	public char[] getContextInformationAutoActivationCharacters() {
		return null;
	}
	
	/*
	 * @see org.eclipse.jface.text.contentassist.IContentAssistProcessor#getContextInformationValidator()
	 */
	public IContextInformationValidator getContextInformationValidator() {
		return null;
	}

	/**
	 * Return the list of suggestions from the current document. First the
	 * document is searched backwards from the caret position and then forwards.
	 * 
	 * @param offset 
	 * @param viewer 
	 * @param prefix the completion prefix
	 * @return all possible completions that were found in the current document
	 * @throws BadLocationException if accessing the document fails
	 */
	private ArrayList<String> createSuggestionsFromOpenDocument(ITextViewer viewer, int offset, String prefix) {
		IDocument document= viewer.getDocument();
		ArrayList<String> completions= new ArrayList<String>();
		try {
		completions.addAll(fEngine.getCompletionsBackwards(document, prefix, offset));
		completions.addAll(fEngine.getCompletionsForward(document, prefix, offset - prefix.length(), true));
		completions.addAll(fEngine.getSynditCompletions(document, prefix, offset));
		}catch ( BadLocationException e) {
//			completions.addAll(fEngine.getSynditCompletions(document, prefix, offset));
		}
		
		return completions;
	}

	/**
	 * Create the array of suggestions. It scans all open text editors and
	 * prefers suggestions from the currently open editor. It also adds the
	 * empty suggestion at the end.
	 * 
	 * @param viewer 
	 * @param offset 
	 * @param prefix the prefix to search for
	 * @return the list of all possible suggestions in the currently open
	 *         editors
	 * @throws BadLocationException if accessing the current document fails
	 */
	private List<String> getSuggestions(ITextViewer viewer, int offset, String prefix) throws BadLocationException {

		ArrayList<String> suggestions= createSuggestionsFromOpenDocument(viewer, offset, prefix);
	/*	IDocument currentDocument= viewer.getDocument();

		IWorkbenchWindow window= PlatformUI.getWorkbench().getActiveWorkbenchWindow();
		IEditorReference editorReferences[]= window.getActivePage().getEditorReferences();

		for (int i= 0; i < editorReferences.length; i++) {
			IEditorPart editor= editorReferences[i].getEditor(false); // don't create!
			if (editor instanceof ITextEditor) {
				ITextEditor textEditor= (ITextEditor) editor;
				IEditorInput input= textEditor.getEditorInput();
				IDocument doc= textEditor.getDocumentProvider().getDocument(input);
				if (!currentDocument.equals(doc))
					suggestions.addAll(fEngine.getCompletionsForward(doc, prefix, 0, false));
			}
		} */
		// add the empty suggestion
		suggestions.add(""); //$NON-NLS-1$

		List<String> uniqueSuggestions= fEngine.makeUnique(suggestions);

		return uniqueSuggestions;
	}

	/*
	 * @see org.eclipse.jface.text.contentassist.ICompletionProposalComputer#getErrorMessage()
	 */
	public String getErrorMessage() {
		return null; // no custom error message
	}
	

}
