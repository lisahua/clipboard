package clipboard.views;

import java.util.HashMap;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IViewSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import clipboard.model.CheckItem;
import clipboard.model.UndoSystematicEdit;
import clipboard.model.WeightedNode;
import clipboard.model.parser.RegionConverter;

/**
 * @Author Lisa
 * @Date: Apr 17, 2014
 */
public class PredictActionBuilder extends ActionBuilder {
	private Action applyAction, applyAllRightAction, applyAnywayAction,
			previewAction;
	private HashMap<IFile, List<WeightedNode>> fileMap = new HashMap<IFile, List<WeightedNode>>();
	private ApplyActionBuilder applyActionBuilder = new ApplyActionBuilder();

	public PredictActionBuilder(TableViewer viewer, IViewSite site) {
		super(viewer, site);

		// TODO Auto-generated constructor stub
	}

	@Override
	protected void makeActions() {
		// ////////Apply ///////////
		applyAction = applyActionBuilder.generateApplyAction(viewer);
		// /////Apply All Right Action ///////////
		applyAllRightAction = applyActionBuilder
				.generateApplyAllRightAction(viewer);
		// //////Apply Anyway ///////
		applyAnywayAction = applyActionBuilder
				.generateApplyAnywayAction(viewer);
		// ///////////Preview Action //////////////

		previewAction = new Action() {
			public void run() {
				try {
					ISelection selection = viewer.getSelection();
					List<Object> list = ((IStructuredSelection) selection)
							.toList();
					List<CheckItem> checkList = RegionConverter
							.previewMultiEdit(list);
					IViewPart viewer = PlatformUI.getWorkbench()
							.getActiveWorkbenchWindow().getActivePage()
							.showView("clipboard.views.SpeculativeView");
					TableViewer tableViewer = ((SpeculativeView) viewer)
							.getViewer();
					tableViewer.setInput(checkList);
					tableViewer.refresh();

				} catch (PartInitException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		previewAction.setText("Inspect all previewes  ");
		previewAction
				.setToolTipText("Inspect the preview for each selected location");
		// ////End of init actions
	}

	@Override
	protected void hookActions() {
		viewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				applyAction.run();
			}
		});
	}

	@Override
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

	@Override
	protected void fillContextMenu(IMenuManager manager) {
		manager.add(applyAction);
		manager.add(applyAllRightAction);
		manager.add(applyAnywayAction);
		manager.add(previewAction);
	}

	public void generateDialog(UndoSystematicEdit edit) {

	}
}
