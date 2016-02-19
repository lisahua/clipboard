package clipboard.views.listener;

import org.eclipse.jface.text.Document;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.Viewer;

import clipboard.model.EditorDropTargetItem;
import clipboard.model.WeightedNode;
import clipboard.model.parser.NewContextGenerator;

/**
 * @Author Lisa
 * @Date: Apr 8, 2014
 */
public class PredictionSelectionChangeListener implements
		ISelectionChangedListener {
	StructuredSelection currentSelect;
	Viewer viewer;

	public PredictionSelectionChangeListener(Viewer preViewer) {
		viewer = preViewer;
	}

	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		StructuredSelection item = (StructuredSelection) event.getSelection();
		if (item.equals(currentSelect))
			return;
		Object element = item.getFirstElement();
		if (element instanceof WeightedNode) {
			WeightedNode node = (WeightedNode) element;
			String previewS = new NewContextGenerator().getNewMethodString(node.getNode(),
					EditorDropTargetItem.getItem());
			viewer.setInput(new Document(previewS));
			viewer.refresh();
		}
		// System.out.println(event.getSource());

	}

}
