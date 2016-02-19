package clipboard.views;

import org.eclipse.jface.text.TextViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IViewSite;

/**
 * @Author Lisa
 * @Date: Apr 16, 2014
 */
public class PreviewViewerBuilder extends ViewerBuilder {
	private TextViewer previewViewer;
	private Composite parent;
	@Override
	public Viewer generateViewer(Composite parent, IViewSite site) {
		// TODO Auto-generated method stub
		this.parent  = parent;
		createpredictViewer(parent);
		return previewViewer;
	}

	private void createpredictViewer(Composite parent) {
		previewViewer = new TextViewer(parent, SWT.MULTI | SWT.H_SCROLL
				| SWT.V_SCROLL | SWT.FULL_SELECTION | SWT.BORDER);
//		previewViewer
//				.addSelectionChangedListener(new PredictionSelectionListener());
		createGridTable(parent);
		
		
	}

	/**
	 * Author: Lisa Create GridTable
	 * 
	 * @param parent
	 */
	private void createGridTable(Composite parent) {
	GridData gridData=	commonPropertySetting(previewViewer);
		gridData.minimumWidth=150;
	}

}
