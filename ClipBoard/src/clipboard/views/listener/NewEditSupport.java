package clipboard.views.listener;

import org.eclipse.jface.viewers.TableViewer;

import clipboard.model.ClipBoardItem;
import clipboard.model.ClipBoardItemProvider;
import clipboard.model.SelectionRegion;

/**
 * @Author Lisa
 * @Date: Mar 19, 2014
 */
public class NewEditSupport extends OldEditSupport {
	TableViewer viewer;

	public NewEditSupport(TableViewer viewer) {
		super(viewer);
		this.viewer = viewer;
		// TODO Auto-generated constructor stub
	}

	@Override
	protected Object getValue(Object element) {
		SelectionRegion newReg = ((ClipBoardItem) element).getNewReg();
		return (newReg==null)? "": newReg.getRegionText();
	}

	@Override
	protected void setValue(Object element, Object userInputValue) {
		
		ClipBoardItem item = (ClipBoardItem) element;
		ClipBoardItemProvider.setNewVersion(item, userInputValue.toString());
			
		viewer.update(element, null);
	}
}