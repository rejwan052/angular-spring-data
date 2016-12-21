package skyglass.data.filter.api;

import skyglass.data.filter.AbstractObjectTableFilter;
import skyglass.data.filter.JunctionType;

public class ObjectTableFilter<T> extends AbstractObjectTableFilter<T, PostTableFilter<T>> implements PostTableFilter<T> {

	public ObjectTableFilter(Iterable<T> fullResult, JunctionType junctionType) {
		super(fullResult, junctionType);
	}

}
