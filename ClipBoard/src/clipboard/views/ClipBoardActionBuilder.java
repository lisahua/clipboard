package clipboard.views;



import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IViewSite;

import clipboard.model.ClipBoardItem;
import clipboard.model.ClipBoardItemProvider;

/**
 * @Author Lisa
 * @Date: Apr 17, 2014
 */
public class ClipBoardActionBuilder extends ActionBuilder {
	private Action clearAction, clearAllAction;
	private Action doubleClickAction, createScriptAction;

	public ClipBoardActionBuilder(TableViewer viewer, IViewSite site) {
		super(viewer, site);
	}

	// ///////////// Init Actions//////////////////////
	protected void makeActions() {
		generateClearActions();
		generateDoubleClickActions();
		generateScriptAction();
		// hookContextMenu() ;
	}

	private void generateClearActions() {
		// ////////Clear Single Action ///////
		clearAction = new Action() {
			public void run() {
				ISelection selection = viewer.getSelection();
				List<Object> list = ((IStructuredSelection) selection).toList();
				for (Object o : list)
					ClipBoardItemProvider.deleteItem(o);
				viewer.refresh();
			}
		};
		clearAction.setText("Clear ");
		clearAction.setToolTipText("Clear ClipBoard");
		// ////////Clear all actions ///////////
		clearAllAction = new Action() {
			public void run() {
				showMessage("Clear all Items");
				ClipBoardItemProvider.clearClipBoard();
				viewer.refresh();
			}
		};
		clearAllAction.setText("Clear All Items");
		clearAllAction.setToolTipText("Clear ClipBoard");

	}

	private void generateDoubleClickActions() {
		// //////////Double click Action//////////////
		doubleClickAction = new Action() {
			public void run() {
				ISelection selection = viewer.getSelection();
				// TODO show description
				List<Object> list = ((IStructuredSelection) selection).toList();
				StringBuilder builder = new StringBuilder();
				for (Object o : list) {
					builder.append(o.toString());
				}
				showMessage("Double-click detected on " + builder.toString());
			}
		};
	}

	private void generateScriptAction() {
		createScriptAction = new Action() {
			public void run() {
				ISelection selection = viewer.getSelection();
				List<Object> list = ((IStructuredSelection) selection).toList();
				ClipBoardItemProvider.genearteScript(list);
			List<ClipBoardItem> inputTable=(List<ClipBoardItem>)	viewer.getInput();
			TableItem[] itemList=	viewer.getTable().getItems();
			 int color = SWT.COLOR_RED;
			 
			for (int i=0;i<inputTable.size();i++) {
				int groupid=inputTable.get(i).getGroupID();
				if ( itemList.length> i) {
					  itemList[i].setBackground(Display.getDefault().getSystemColor(color+groupid*2));
				}
			}
				viewer.refresh();
				
			}
		};
		createScriptAction.setText("Generate Script ");
		createScriptAction.setToolTipText("Generate script from multiple examples");
	}

	protected void hookActions() {
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				doubleClickAction.run();
			}
		});

	}

	// /////////////Context Menu //////////////////////
	protected MenuManager hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		Menu menu = menuMgr.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);

		site.registerContextMenu(menuMgr, viewer);

		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				fillContextMenu(manager);
			}
		});
		return menuMgr;
	}

	protected void fillContextMenu(IMenuManager manager) {
		 manager.add(createScriptAction);
		manager.add(clearAction);
		manager.add(clearAllAction);
		
		// Other plug-ins can contribute there actions here
		// manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}

	// /////////////End of Context Menu//////////////////////
	// private void fillLocalPullDown(IMenuManager manager) {
	// manager.add(clearAllAction);
	// manager.add(new Separator());
	// }
	//
	// private void fillLocalToolBar(IToolBarManager manager) {
	// manager.add(clearAllAction);
	// }

}
