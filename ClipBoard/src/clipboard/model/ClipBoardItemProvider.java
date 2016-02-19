package clipboard.model;

import java.util.ArrayList;
import java.util.List;

import ch.uzh.ifi.seal.changedistiller.treedifferencing.Node;
import clipboard.model.parser.RegionParser;

/**
 * @Author Lisa
 * @Date: Mar 17, 2014
 */
public class ClipBoardItemProvider {
	private static List<ClipBoardItem> itemList = new ArrayList<ClipBoardItem>();
	private static int MATCH_THRESHOLD = 10;
	private static int maxGroupID = 0;

	public static int getMATCH_THRESHOLD() {
		return MATCH_THRESHOLD;

	}

	public static void setMATCH_THRESHOLD(int mATCH_THRESHOLD) {
		MATCH_THRESHOLD = mATCH_THRESHOLD;
	}

	private ClipBoardItemProvider() {

	}

	public static List<ClipBoardItem> getItems() {
		// itemList.add(new ClipBoardItem("aa", "bb", 0, 0, ""));
		return itemList;
	}

	public static void setNewVersion(ClipBoardItem item, String newText) {
		int id = itemList.indexOf(item);
		if (id < 0)
			return;
		if (itemList.get(id).getNewReg() == null)
			return;
		itemList.get(id).getNewReg().setRegionText(newText);
	}

	public static void setOldVersion(ClipBoardItem item, String newText) {
		int id = itemList.indexOf(item);
		if (id < 0)
			return;
		itemList.get(id).getOldReg().setRegionText(newText);
	}

	public static void clearClipBoard() {
		itemList.clear();

	}

	public static void dropOnClipBoard(SelectionRegion region) {
		// TODO Auto-generated method stub
		for (int i = 0; i < itemList.size(); i++) {
			String oldVersion = itemList.get(i).getOldReg().getRegionText();
			if (itemList.get(i).getNewReg() != null)
				continue;
			// SelectionRegion newReg = itemList.get(i).getNewReg();
			String[] oldTexts = oldVersion.split("\n");
			for (String txt : oldTexts) {
				txt = txt.trim();
				if (txt.length() > 8
						&& region.getRegionText().contains(txt.trim())) {
					// similar,
					itemList.get(i).setNewReg(region);
					return;
				}
			}
		}
		itemList.add(new ClipBoardItem(region, null));

	}

	// public static void deleteItem(ClipBoardItem item) {
	// itemList.remove(item);
	// }
	public static void deleteItem(Object item) {
		itemList.remove(item);
	}

	public static void genearteScript(List<Object> list) {
		// TODO dummy function
		for (Object o : list) {
			int id = itemList.indexOf(o);
			if (itemList.get(id).getGroupID() > 0)
				return; // color already set
			if (id > -1) {
				itemList.get(id).setGroupID(maxGroupID);
			}
		}
		maxGroupID++;
		if (list.size() > 1) {
			Node node1 = ((ClipBoardItem) list.get(0)).getOldReg().getNodes()
					.get(0);
			Node node2 = ((ClipBoardItem) list.get(1)).getOldReg().getNodes()
					.get(0);
//		System.out.print(new RegionParser().LCSmatch(node1, node2));
		}
	}

	public static List<WeightedNode> reCalculatePrediction() {
		ClipBoardItem item = EditorDropTargetItem.getItem();
		ClipBoardEditLocator locator = new ClipBoardEditLocator(item
				.getOldReg().getNodes().get(0));
		return locator.locateMatchNodesBeyondThreshold();

	}
}
