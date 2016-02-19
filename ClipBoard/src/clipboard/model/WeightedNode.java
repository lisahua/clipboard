package clipboard.model;

import org.eclipse.core.resources.IFile;

import ch.uzh.ifi.seal.changedistiller.treedifferencing.Node;

/**
 * @Author Lisa
 * @Date: Apr 14, 2014
 */
public class WeightedNode extends Node implements Comparable<Object> {
	private String className = "";
	private static final long serialVersionUID = 8945272923600127523L;
	private int matchNodeWeight = 0;
	private Node node;
	private IFile file;

	public WeightedNode(Node node, int matchWeight) {
		super(node.getLabel(), node.getValue());
		this.node = node;
		matchNodeWeight = matchWeight;
	}
	
	
	public WeightedNode(Node node, int matchWeight,IFile file) {
		super(node.getLabel(), node.getValue());
		this.node = node;
		matchNodeWeight = matchWeight;
		String name = file.getName();
		className = name.substring(0, name.length() - 5);
		this.file = file;
	}

	@Override
	public int compareTo(Object arg0) {
		// TODO Auto-generated method stub
		if (!(arg0 instanceof WeightedNode))
			return 0;

		return  ((WeightedNode) arg0).getMatchNodeWeight() - matchNodeWeight ;
	}

	public int getMatchNodeWeight() {
		return matchNodeWeight;
	}

	public String getClassName() {
		return className;
	}

	public String toString() {
		
		return className + "-" + node.getValue()+" match nodes-"+matchNodeWeight;
	}
	public Node getNode() {
		return node;
	}
	public IFile getFile() {
		return file;
	}
}
