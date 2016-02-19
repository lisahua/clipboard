package clipboard.views;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.IViewSite;

import clipboard.model.ClipBoardItem;
import clipboard.model.ClipBoardItemProvider;
import clipboard.model.SelectionRegion;
import clipboard.views.listener.NewEditSupport;
import clipboard.views.listener.OldEditSupport;

/**
 * @Author Lisa
 * @Date: Mar 19, 2014
 */
public class InputViewerBuilder extends ViewerBuilder {
	private TableViewer inputViewer;
	
	private IViewSite site;

	@Override
	public Viewer generateViewer(Composite parent, IViewSite site) {
		// TODO Auto-generated method stub
		this.site = site;
		createInputViewer(parent);
		return inputViewer;
	}

	private void createInputViewer(Composite parent) {
		inputViewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL
				| SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
		// inputViewer.setContentProvider(new ViewContentProvider(parent));
		inputViewer.setLabelProvider(new ViewLabelProvider());
		
		// viewer.setSorter(new NameSorter());
		// inputViewer.setInput();

		createGridTable(parent);
		new ClipBoardActionBuilder(inputViewer, site).generateActions(parent);
	}

	// /////////////Init Table //////////////////////
	/**
	 * Author: Lisa Create GridTable
	 * 
	 * @param parent
	 */
	private void createGridTable(Composite parent) {
		initialColumns();
		// //////////End of Column Initilization //////////////
		Table table = inputViewer.getTable();
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		inputViewer.setContentProvider(new ArrayContentProvider());
		// get the content for the viewer, setInput will call getElements in the
		// contentProvider
		inputViewer.setInput(ClipBoardItemProvider.getItems());
		// make the selection available to other views
		site.setSelectionProvider(inputViewer);
		// define layout for the viewer
		commonPropertySetting(inputViewer);

	}

	private void initialColumns() {
		String[] titles = { "Original Version", "New Version" };
		// ////////Old Version/////////////
		TableViewerColumn col = createColumns(inputViewer, titles[0], 350);
		col.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				return ((ClipBoardItem) element).getOldReg().getRegionText();
			}

			public Image getImage(Object element) {
				if (element instanceof ClipBoardItem) {
					return new Image(Display.getDefault(), 1,
							((ClipBoardItem) element).getMaxLineNo() * 15);
				} else
					return null;
			}
		});
		col.setEditingSupport(new OldEditSupport(inputViewer));
		// //////////////////////New Version Column////////////////
		col = createColumns(inputViewer, titles[1], 350);
		col.setLabelProvider(new ColumnLabelProvider() {
			public String getText(Object element) {
				SelectionRegion newReg = ((ClipBoardItem) element).getNewReg();
				return (newReg == null) ? "" : newReg.getRegionText();
			}
		});
		col.setEditingSupport(new NewEditSupport(inputViewer));
	}

	// /////////////End of Init Table//////////////////////
	
}
