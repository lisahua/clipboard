package clipboard.views;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;

import clipboard.views.listener.ClipBoardSelectionChangeListener;
import clipboard.views.listener.DropListener;
import clipboard.views.listener.PredictionSelectionChangeListener;

/**
 * This sample class demonstrates how to plug-in a new workbench view. The view
 * shows data obtained from the model. The sample creates a dummy model on the
 * fly, but a real implementation would connect to the model available either in
 * this or another plug-in (e.g. the workspace). The view is connected to the
 * model using a content provider.
 * <p>
 * The view uses a label provider to define how model objects should be
 * presented in the view. Each view can present the same model objects using
 * different labels and icons, if needed. Alternatively, a single label provider
 * can be shared between views in order to ensure that objects of the same type
 * are presented in the same way everywhere.
 * <p>
 */

public class SampleView extends ViewPart {

	/**
	 * The ID of the view as specified by the extension.
	 */
	public static final String ID = "clipboard.views.SampleView";
	private TableViewer inputViewer;

	// private TableViewer mainViewer;

	/**
	 * The constructor.
	 */
	public SampleView() {
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	public void createPartControl(Composite parent) {

		inputViewer = (TableViewer) new InputViewerBuilder().generateViewer(
				parent, getViewSite());
		int operations = DND.DROP_COPY | DND.CLIPBOARD;
		Transfer[] transferTypes = new Transfer[] { TextTransfer.getInstance() };
		// oldViewer.addDragSupport(operations, transferTypes, new DragListener(
		// oldViewer));
		Viewer predictViewer = new PredictViewerBuilder().generateViewer(
				parent, getViewSite());
		
		Viewer previewViewer = new PreviewViewerBuilder().generateViewer(
				parent, getViewSite());
		GridLayout layout = new GridLayout(3, false);
		layout.horizontalSpacing = 1;
		// layout.makeColumnsEqualWidth = false;
		parent.setLayout(layout);

		inputViewer.addDropSupport(operations, transferTypes, new DropListener(
				inputViewer, predictViewer, previewViewer));
		inputViewer
				.addSelectionChangedListener(new ClipBoardSelectionChangeListener(
						predictViewer, previewViewer));
		predictViewer
		.addSelectionChangedListener(new PredictionSelectionChangeListener(previewViewer));
		ActionBarBuilder builder = new ActionBarBuilder(inputViewer,
				getViewSite());
		builder.generateActions(parent);
		builder.setViewer(predictViewer, previewViewer);
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		inputViewer.getControl().setFocus();
	}

}