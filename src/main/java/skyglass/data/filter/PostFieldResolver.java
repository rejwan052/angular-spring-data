package skyglass.data.filter;

public interface PostFieldResolver<T> {
	
	public Object getValue(T element);

}
