package skyglass.data.filter;

import java.util.Map;



public class PostFilterItem<T> {
	
	private String fieldName;
	
	private FilterType filterType;
	
	private Object filterValue;
	
	private PostFieldResolver<T> postFieldResolver;
	
	public PostFilterItem(String fieldName, FilterType filterType, Object filterValue) {
		this.fieldName = fieldName;
		this.filterType = filterType;
		this.filterValue = filterValue;
	}
	
	public String getFieldName() {
		return fieldName;
	}

	public FilterType getFilterType() {
		return filterType;
	}

	public Object getFilterValue() {
		return filterValue;
	}
	
	public String getStringFilterValue() {
		return filterValue == null ? null : filterValue.toString();
	}

	public PostFieldResolver<T> getPostFieldResolver(Map<String, PostFieldResolver<T>> resolverMap) {
		PostFieldResolver<T> result = resolverMap.get(fieldName);
		if (result != null) {
			return result;
		}
		return postFieldResolver;
	}
	
	public void setPostFieldResolver(PostFieldResolver<T> postFieldResolver) {
		this.postFieldResolver = postFieldResolver;
	}

}
