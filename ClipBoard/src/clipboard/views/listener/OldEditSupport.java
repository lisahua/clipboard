package clipboard.views.listener;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TextCellEditor;

import clipboard.model.ClipBoardItem;
import clipboard.model.ClipBoardItemProvider;

/**
 * @Author Lisa
 * @Date: Mar 19, 2014
 */

public class OldEditSupport extends EditingSupport {

	private final TableViewer viewer;
	private final CellEditor editor;

	public OldEditSupport(TableViewer viewer) {
		super(viewer);
		this.viewer = viewer;
		this.editor = new TextCellEditor(viewer.getTable());
	}

	@Override
	protected CellEditor getCellEditor(Object element) {
		return editor;
	}

	@Override
	protected boolean canEdit(Object element) {
		return true;
	}

	@Override
	protected Object getValue(Object element) {
		return ((ClipBoardItem) element).getOldReg().getRegionText();
	}

	@Override
	protected void setValue(Object element, Object userInputValue) {
		ClipBoardItem item = (ClipBoardItem) element;
		ClipBoardItemProvider.setNewVersion(item, userInputValue.toString());
		viewer.update(element, null);
	}
}


