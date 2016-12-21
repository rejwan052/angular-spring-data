package skyglass.data.filter;

import java.util.ArrayList;
import java.util.List;


public class PostFilterItemTree<T> extends PostFilterItem<T> {
	
	private List<PostFilterItem<T>> children = new ArrayList<PostFilterItem<T>>();
	
	private JunctionType junctionType;
	
	public PostFilterItemTree(JunctionType junctionType) {
		super(null, null, null);
		this.junctionType = junctionType;		
	}
	
	public void addChild(PostFilterItem<T> filterItem) {
		this.children.add(filterItem);
	}

	public List<PostFilterItem<T>> getChildren() {
		return children;
	}
	
	public JunctionType getJunctionType() {
		return junctionType;
	}
	
	

}
