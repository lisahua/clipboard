package clipboard.model;

import java.util.List;
import java.util.TreeSet;

import ut.seal.plugins.utils.change.UTChangeDistiller;

public class ClipBoardItem {

	private SelectionRegion oldReg = null, newReg = null;
	private int maxLineNo = 0;
	private UTChangeDistiller distiller = new UTChangeDistiller();
	private int matchWeight = 0;
	private List<WeightedNode> matchedNodes;
	private int groupID = 0;

	public ClipBoardItem(SelectionRegion oldReg, SelectionRegion newReg) {
		this.oldReg = oldReg;
		this.newReg = newReg;
		// this.newReg.setRegionText("");
		maxLineNo = oldReg.getRegionText().split("\n").length;
		// TODO trigger locator, start new thread
		ClipBoardEditLocator locator = new ClipBoardEditLocator(oldReg
				.getNodes().get(0));
		matchedNodes = locator.locateMatchNodesBeyondThreshold();
	}

	public int getGroupID() {
		return groupID;
	}

	public void setGroupID(int groupID) {
		this.groupID = groupID;
	}

	public UTChangeDistiller getDistiller() {
		return distiller;
	}

	public int getMaxLineNo() {
		return maxLineNo;
	}

	public SelectionRegion getOldReg() {
		return oldReg;
	}

	public void setOldReg(SelectionRegion oldReg) {
		this.oldReg = oldReg;
	}

	public SelectionRegion getNewReg() {
		return newReg;
	}

	public void setNewReg(SelectionRegion newReg) {
		this.newReg = newReg;
		// extractEditScript();
		// ClipBoardAssistAdapter.hasTemplateChange();
	}

	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append(oldReg.getFile().getName());
		builder.append("-" + oldReg.getNodes().get(0).getValue() + "\n");
		builder.append(oldReg.getRegionText() + "\n");
		if (newReg != null) {
			builder.append(oldReg.getRegionText());
		}
		return builder.toString();
	}

	public int getMatchWeight() {
		return matchWeight;
	}

	public void setMatchWeight(int matchWeight) {
		this.matchWeight = matchWeight;
	}

	public String getMatchNodesString() {
		StringBuilder result = new StringBuilder();
		for (WeightedNode node : matchedNodes) {
			result.append(node.toString() + "\n");
		}
		return result.toString();
	}

	public List<WeightedNode> getMatchNodes() {
		return matchedNodes;
	}

}
