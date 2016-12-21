package skyglass.data.filter;

import java.util.List;

import skyglass.data.query.QueryResult;

public interface IBaseTableFilter<T, F> {
	
	public QueryResult<T> getResult();
	
	public long getResultCount();
	
	public List<T> getUnpagedList();
	
	public F addOrder(String orderField, OrderType orderType);	

	public F setOrder(String orderField, OrderType orderType);
	
	public F setDefaultOrder(String orderField, OrderType orderType);
	
	public F setPaging(Integer rowsPerPage, Integer pageNumber);	
	
	public F addPostSearch(String filterValue, String... fieldNames);	
	
	public F addPostFilter(String fieldName, Object filterValue);
	
	public F addPostFilter(String fieldName, FilterType filterType, Object filterValue);
	
	public F addPostFilters(String fieldName, Object[] filterValues);
	
	public F addPostFilters(String fieldName, FilterType filterType, Object[] filterValues);	

	public F addPostFilter(PostFilterItem<T> filterItem);
	
	public F addPostFilter(PostFilterItemTree<T> parent, PostFilterItem<T> postFilterItem);
	
	@SuppressWarnings("unchecked")
	public F addPostFilters(PostFilterItem<T>... postFilterItems);
	
	@SuppressWarnings("unchecked")
	public F addOrPostFilters(PostFilterItem<T>... postFilterItems);
	
	@SuppressWarnings("unchecked")
	public F addAndPostFilters(PostFilterItem<T>... postFilterItems);
	
	@SuppressWarnings("unchecked")
	public F addPostFilters(PostFilterItemTree<T> parent, PostFilterItem<T>... postFilterItems);
	
	public PostFilterItem<T> createPostFilterItem(String fieldName, Object filterValue);
	
	public PostFilterItem<T> createPostFilterItem(String fieldName, FilterType filterType, Object filterValue);
	
	public F addPostField(String fieldName);	
	
	public F addPostFieldResolver(String fieldName, String expression);
	
	public F addPostFieldResolver(String fieldName, PostFieldResolver<T> postFieldResolver);
	
	public F addPostFilterResolver(PostFilterResolver<T> postFilterResolver);	

}
