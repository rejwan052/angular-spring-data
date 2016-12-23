package skyglass.data.filter.http;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import skyglass.data.common.model.DataConstants;
import skyglass.data.filter.FilterType;
import skyglass.data.filter.IBaseDataFilter;
import skyglass.data.filter.OrderType;
import skyglass.data.filter.PostFieldResolver;
import skyglass.data.filter.PostFilterItem;
import skyglass.data.filter.PostFilterItemTree;
import skyglass.data.filter.PostFilterResolver;
import skyglass.data.query.QueryResult;

public class AbstractBaseHttpDataFilterWrapper<T, F> implements IBaseDataFilter<T, F> {
	
	private IBaseDataFilter<T, ?> filter;
	
	protected HttpServletRequest request;
	
	protected String[] searchFields;	
	
	public AbstractBaseHttpDataFilterWrapper(IBaseDataFilter<T, ?> filter, HttpServletRequest request) {
		this.filter = filter;		
		this.request = request;
		parseSearchFields(request);		
	}
	
	private void parseSearchFields(HttpServletRequest request) {
		String param = HttpRequestUtils.getStringParamValue(request, DataConstants.SEARCH_FIELDS);
		if (param == null) {
			this.searchFields = new String[0];
		} else {
			this.searchFields = param.split(",");
		}
	}
	
	protected String[] filterSearchFields(String[] fields) {
		if (this.searchFields.length == 0) {
			return fields;
		}
		List<String> result = new ArrayList<String>();
		for (String field: fields) {
			if (isSearchField(field)) {
				result.add(field);
			}
		}
		return result.toArray(new String[0]);
	}	
	
	protected boolean isSearchField(String field) {
		if (this.searchFields.length == 0) {
			return true;
		}
		for (int i = 0; i < this.searchFields.length; i++) {
			if (this.searchFields[i].equals(field)) {
				return true;
			}
		}
		return false;
	}	
	
	@SuppressWarnings("unchecked")
	public F addPostSearch(String filterValue, String... fieldNames) {
   		filter.addPostSearch(filterValue, filterSearchFields(fieldNames));        	
        return (F)this;
	}	

	
	@SuppressWarnings("unchecked")
	public F addPostFilter(String fieldName, Object filterValue) {
		filter.addPostFilter(fieldName, filterValue);
        return (F)this;
	}

	@SuppressWarnings("unchecked")
	public F addPostFilter(String fieldName, FilterType filterType, Object filterValue) {
		filter.addPostFilter(fieldName, filterType, filterValue);
        return (F)this;		
	}

	@SuppressWarnings("unchecked")
	public F addPostFilters(String fieldName, Object[] filterValues) {
		filter.addPostFilters(fieldName, filterValues);
        return (F)this;
	}

	@SuppressWarnings("unchecked")
	public F addPostFilters(String fieldName, FilterType filterType, Object[] filterValues) {
		filter.addPostFilters(fieldName, filterType, filterValues);
        return (F)this;
	}

	@SuppressWarnings("unchecked")
	public F addPostFilter(PostFilterItem<T> filterItem) {
		filter.addPostFilter(filterItem);
        return (F)this;
	}

	@SuppressWarnings("unchecked")
	public F addPostFilter(PostFilterItemTree<T> parent, PostFilterItem<T> postFilterItem) {
		filter.addPostFilter(parent, postFilterItem);
        return (F)this;
	}

	@SuppressWarnings("unchecked")
	public F addPostFilters(PostFilterItem<T>... postFilterItems) {
		filter.addPostFilters(postFilterItems);
        return (F)this;
	}

	@SuppressWarnings("unchecked")
	public F addOrPostFilters(PostFilterItem<T>... postFilterItems) {
		filter.addOrPostFilters(postFilterItems);
        return (F)this;
	}

	@SuppressWarnings("unchecked")
	public F addAndPostFilters(PostFilterItem<T>... postFilterItems) {
		filter.addAndPostFilters(postFilterItems);
        return (F)this;
	}

	@SuppressWarnings("unchecked")
	public F addPostFilters(PostFilterItemTree<T> parent, PostFilterItem<T>... postFilterItems) {
		filter.addPostFilters(parent, postFilterItems);
        return (F)this;
	}

	public PostFilterItem<T> createPostFilterItem(String fieldName, Object filterValue) {
		return filter.createPostFilterItem(fieldName, filterValue);
	}

	public PostFilterItem<T> createPostFilterItem(String fieldName, FilterType filterType, Object filterValue) {
		return filter.createPostFilterItem(fieldName, filterType, filterValue);
	}

	@SuppressWarnings("unchecked")
	public F addPostField(String fieldName) {
		filter.addPostField(fieldName);
        return (F)this;
	}

	@SuppressWarnings("unchecked")
	public F addPostFieldResolver(String fieldName, String expression) {
		filter.addPostFieldResolver(fieldName, expression);
        return (F)this;
	}

	@SuppressWarnings("unchecked")
	public F addPostFieldResolver(String fieldName, PostFieldResolver<T> postFieldResolver) {
		filter.addPostFieldResolver(fieldName, postFieldResolver);
        return (F)this;
	}

	@SuppressWarnings("unchecked")
	public F addPostFilterResolver(PostFilterResolver<T> postFilterResolver) {
		filter.addPostFilterResolver(postFilterResolver);
        return (F)this;
	}

	public QueryResult<T> getResult() {
		return filter.getResult();
	}

	public long getResultCount() {
		return filter.getResultCount();
	}

	public List<T> getUnpagedList() {
		return filter.getUnpagedList();
	}
	
	@SuppressWarnings("unchecked")
	public F addOrder(String orderField, OrderType orderType) {
		filter.addOrder(orderField, orderType);
        return (F)this;
	}	

	@SuppressWarnings("unchecked")
	public F setOrder(String orderField, OrderType orderType) {
		filter.setOrder(orderField, orderType);
        return (F)this;
	}

	@SuppressWarnings("unchecked")
	public F setDefaultOrder(String orderField, OrderType orderType) {
		filter.setDefaultOrder(orderField, orderType);
        return (F)this;
	}

	@SuppressWarnings("unchecked")
	public F setPaging(Integer rowsPerPage, Integer pageNumber) {
		filter.setPaging(rowsPerPage, pageNumber);
        return (F)this;
	}
	
	protected IBaseDataFilter<T, ?> getParentFilter() {
		return filter;
	}

}
