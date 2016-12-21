package skyglass.data.filter;

public interface PostFilterResolver<T> {
	
	public boolean resolveFilter(T element);

}
