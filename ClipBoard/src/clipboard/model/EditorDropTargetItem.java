package clipboard.model;

import org.eclipse.jface.text.IDocument;

import clipboard.model.parser.RegionConverter;

/**
 * @Author Lisa
 * @Date: Apr 8, 2014
 */
public class EditorDropTargetItem {
	private static ClipBoardItem item;
	
	public static ClipBoardItem getItem() {
		return item;
	}

	public static void setItem(ClipBoardItem i) {
		item = i;
	}

	public static String getSelectedItemTarget(IDocument currentDoc,
			int eventOffset) {
		if (item == null)
			return currentDoc.get();
		// RegionConverter.checkSyntaxError(currentDoc,eventOffset,item);

		return "";
	}

}
