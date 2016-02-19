package clipboard.views;

import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IViewSite;

/**
 * @Author Lisa
 * @Date: Apr 16, 2014
 */
public abstract class ViewerBuilder {
public abstract Viewer generateViewer(Composite parent, IViewSite site) ;

protected TableViewerColumn createColumns(TableViewer viewer, String title, int size) {
	TableViewerColumn viewerColumn = new TableViewerColumn(viewer,	SWT.WRAP);
	TableColumn column = viewerColumn.getColumn();
	column.setText(title);
//	column.setAlignment(SWT.CENTER);
	column.setResizable(true);
	column.setWidth(size);
	return viewerColumn;

}

protected GridData commonPropertySetting(Viewer viewer) {
	GridData gridData = new GridData();
	gridData.verticalAlignment = GridData.FILL;
	gridData.horizontalAlignment=GridData.FILL;
	gridData.grabExcessVerticalSpace = true;
	gridData.grabExcessHorizontalSpace = true;
	viewer.getControl().setLayoutData(gridData);
	viewer.getControl().setEnabled(true);
	return gridData;
}

}

/*
 * The content provider class is responsible for providing objects to the
 * view. It can wrap existing objects in adapters or simply return objects
 * as-is. These objects may be sensitive to the current input of the view,
 * or ignore it and always show the same content (like Task List, for
 * example).
 */

class ViewContentProvider implements IStructuredContentProvider {
	Composite parent;

	public ViewContentProvider(Composite parent) {
		this.parent = parent;
	}

	public void inputChanged(Viewer v, Object oldInput, Object newInput) {
	}

	public void dispose() {
	}

	public Object[] getElements(Object parent) {
//TODO not sure how to imp..
		return null;
	}
}

class ViewLabelProvider extends LabelProvider implements
		ITableLabelProvider {
	public String getColumnText(Object obj, int index) {
		return getText(obj);
	}

	public Image getColumnImage(Object obj, int index) {
		return getImage(obj);
	}

//	public Image getImage(Object obj) {
//		return PlatformUI.getWorkbench().getSharedImages()
//				.getImage(ISharedImages.IMG_OBJ_ELEMENT);
//	}
	
	class NestedViewerLabelProvider extends TableViewer implements  IBaseLabelProvider {

		public NestedViewerLabelProvider(Table table) {
			super(table);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void addListener(ILabelProviderListener listener) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void dispose() {
			// TODO Auto-generated method stub
			
		}

		@Override
		public boolean isLabelProperty(Object element, String property) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public void removeListener(ILabelProviderListener listener) {
			// TODO Auto-generated method stub
			
		}

		
	}
}