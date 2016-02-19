package clipboard.views.listener;

import org.eclipse.jface.text.Document;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;

import clipboard.model.ClipBoardItem;
import clipboard.model.EditorDropTargetItem;
import clipboard.model.WeightedNode;
import clipboard.model.parser.NewContextGenerator;

/**
 * @Author Lisa
 * @Date: Apr 8, 2014
 */
public class ClipBoardSelectionChangeListener implements
		ISelectionChangedListener {
	StructuredSelection currentSelect;
	Viewer viewer,preViewer;

	public ClipBoardSelectionChangeListener(Viewer predictViewer, Viewer preViewer) {
		viewer = predictViewer;
		this.preViewer = preViewer;
	}

	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		StructuredSelection item = (StructuredSelection) event.getSelection();
		if (item.equals(currentSelect))
			return;
		Object element = item.getFirstElement();
		if (element instanceof ClipBoardItem) {
			ClipBoardItem cbitem = (ClipBoardItem) element;
			EditorDropTargetItem.setItem(cbitem);
			viewer.setInput((cbitem).getMatchNodes());
			viewer.refresh();
				WeightedNode node = cbitem.getMatchNodes().get(0);
				String previewS = new NewContextGenerator().getNewMethodString(node.getNode(),
						EditorDropTargetItem.getItem());
				preViewer.setInput(new Document(previewS));
				preViewer.refresh();
			
		}
		// System.out.println(event.getSource());

	}

}
