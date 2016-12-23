package skyglass.data.filter.api;

import skyglass.data.filter.AbstractPostDataFilter;
import skyglass.data.filter.JunctionType;

public class PostDataFilter<T> extends AbstractPostDataFilter<T, IPostDataFilter<T>> implements IPostDataFilter<T> {

	public PostDataFilter(Iterable<T> fullResult, JunctionType junctionType) {
		super(fullResult, junctionType);
	}

}
