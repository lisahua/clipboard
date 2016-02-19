package clipboard.views.listener;

import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentListener;

import clipboard.model.EditorDropTargetItem;

/**
 * @Author Lisa
 * @Date: Apr 8, 2014
 */
public class EditorInputListener implements IDocumentListener {
	IDocument currentDoc;

	@Override
	public void documentAboutToBeChanged(DocumentEvent event) {
		// TODO Auto-generated method stub

	}

	@Override
	public void documentChanged(DocumentEvent event) {
		if (event.getOffset() == 0 || event.getLength() == 0)
			return;
		currentDoc = event.getDocument();
		String newVersion = EditorDropTargetItem.getSelectedItemTarget(
				currentDoc, event.getOffset());

//		currentDoc.set(newVersion);

	}

}
