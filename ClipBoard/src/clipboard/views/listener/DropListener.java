package clipboard.views.listener;

import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.DropTargetListener;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;

import clipboard.model.ClipBoardItem;
import clipboard.model.ClipBoardItemProvider;
import clipboard.model.EditorDropTargetItem;
import clipboard.model.SelectionRegion;
import clipboard.model.WeightedNode;
import clipboard.model.parser.NewContextGenerator;

/**
 * @Author Lisa
 * @Date: Mar 17, 2014
 */
public class DropListener implements DropTargetListener {
	private Viewer viewer, predictViewer, preViewer;
	private IDocument currentDoc;

	public DropListener(TableViewer viewer, Viewer predictViewer,
			Viewer previewViewer) {
		this.viewer = viewer;
		this.predictViewer = predictViewer;
		preViewer = previewViewer;
	}

	@Override
	public void dragEnter(DropTargetEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void dragOperationChanged(DropTargetEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void dragOver(DropTargetEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void drop(DropTargetEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void dropAccept(DropTargetEvent event) {
		// TODO Auto-generated method stub
		System.out.println("dropaccept");
	}

	@Override
	public void dragLeave(DropTargetEvent event) {
		// TODO Auto-generated method stub
		IEditorPart editor = PlatformUI.getWorkbench()
				.getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		IEditorInput input = editor.getEditorInput();

		if (!(editor instanceof ITextEditor))
			return;

		ITextEditor ite = (ITextEditor) editor;

		IDocumentProvider provider = ite.getDocumentProvider();

		addEditorListener(provider.getDocument(ite.getEditorInput()));

		ISelection sel = ite.getSelectionProvider().getSelection();

		if (!(sel instanceof TextSelection))
			return;
		// doc.get();
		TextSelection textSel = (TextSelection) sel;
		int offset = textSel.getOffset();
		int length = textSel.getLength();

		FileEditorInput finput = (FileEditorInput) input;
		// String path = finput.getPath().toString();

		IFile file = finput.getFile();
		// String fileName = ite.getTitle();
		SelectionRegion region = new SelectionRegion(offset, length, file,
				currentDoc, textSel.getText());

		updateClipBoard(region);

		viewer.refresh();
		predictViewer.refresh();
		preViewer.refresh();
	}

	/**
	 * Get Iselection start position and see whether has match, if so, add new
	 * version to existed item, if not
	 */
	private void updateClipBoard(SelectionRegion region) {

		ClipBoardItemProvider.dropOnClipBoard(region);
		List<ClipBoardItem> list = ClipBoardItemProvider.getItems();
		viewer.setInput(list);
		ClipBoardItem item = list.get(list.size() - 1);
		EditorDropTargetItem.setItem(item);
		List<WeightedNode> nodeList = item.getMatchNodes();
		predictViewer.setInput(nodeList);
		String previewS = new NewContextGenerator().getNewMethodString(
				nodeList.get(0).getNode(), item);
		preViewer.setInput(new Document(previewS));
	}

	private void addEditorListener(IDocument doc) {
		if (doc.equals(currentDoc))
			return;
		currentDoc = doc;
		doc.addDocumentListener(new EditorInputListener());
	}
}
