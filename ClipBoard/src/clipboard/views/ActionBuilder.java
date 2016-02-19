package clipboard.views;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IViewSite;

/**
 * @Author Lisa
 * @Date: Apr 17, 2014
 */
public abstract class ActionBuilder {
	protected TableViewer viewer;
	protected IViewSite site;

	public ActionBuilder(TableViewer viewer, IViewSite site) {
		this.viewer = viewer;
		this.site = site;

	}

	public void generateActions(Composite parent) {
		makeActions();
		hookActions();
		hookContextMenu();
		// hookActionBar() ;
	}

	protected abstract void makeActions();

	protected abstract void hookActions();

	protected abstract MenuManager hookContextMenu();

	// protected abstract void hookActionBar() ;
	protected void showMessage(String message) {
		MessageDialog.openInformation(viewer.getControl().getShell(),
				"ClipBoard: ", message);
	}

	protected abstract void fillContextMenu(IMenuManager manager);

	
}
