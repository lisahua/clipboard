package clipboard.views;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.IViewSite;

import clipboard.model.WeightedNode;
import clipboard.views.listener.PredictionSelectionChangeListener;

/**
 * @Author Lisa
 * @Date: Apr 16, 2014
 */
public class PredictViewerBuilder extends ViewerBuilder {
	private TableViewer predictViewer;
	private Composite parent;

	@Override
	public Viewer generateViewer(Composite parent, IViewSite site) {
		this.parent = parent;
		createpredictViewer(parent);
		new PredictActionBuilder(predictViewer, site).generateActions(parent);
		return predictViewer;
	}

	private void createpredictViewer(Composite parent) {
		predictViewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL
				| SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
		predictViewer.setLabelProvider(new ViewLabelProvider());

		createGridTable(parent);

	}

	/**
	 * Author: Lisa Create GridTable
	 * 
	 * @param parent
	 */
	private void createGridTable(Composite parent) {
		initialColumns();
		// //////////End of Column Initilization //////////////
		Table table = predictViewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		predictViewer.setContentProvider(new ArrayContentProvider());
		predictViewer.getControl().setEnabled(true);
		commonPropertySetting(predictViewer);
	}

	private void initialColumns() {
		// ////////////Match Nodes ///////////
		TableViewerColumn col = createColumns(predictViewer,
				"Predict Match Location", 180);
		col.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				if (element instanceof WeightedNode) {
					WeightedNode node = (WeightedNode) element;
					return node.getClassName() + "-" + node.getValue();
				}
				return "";
			}

			public Image getImage(Object element) {
				return null;
			}
		});
		col = createColumns(predictViewer, "Match Weight", 10);
		col.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				return String.valueOf(((WeightedNode) element)
						.getMatchNodeWeight());
			}

			public Image getImage(Object element) {
				return null;
			}
		});
	}

}
