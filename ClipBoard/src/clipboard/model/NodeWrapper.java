package clipboard.model;

import ch.uzh.ifi.seal.changedistiller.model.classifiers.EntityType;
import ch.uzh.ifi.seal.changedistiller.treedifferencing.Node;

/**
 * @Author Lisa
 * @Date: Apr 12, 2014
 */
public class NodeWrapper extends Node {
	private static final long serialVersionUID = 1L;
	private int matchWeight = 0;

	public NodeWrapper(EntityType label, String value) {
		super(label, value);
		// TODO Auto-generated constructor stub
	}

	public int getMatchWeight() {
		return matchWeight;
	}

	public void setMatchWeight(int matchWeight) {
		this.matchWeight = matchWeight;
	}
	public void addMatchWeight() {
		matchWeight ++;
	}

}
