package skyglass.data.filter.http;

import javax.servlet.http.HttpServletRequest;

import skyglass.data.common.model.DataConstants;
import skyglass.data.filter.CustomFieldResolver;
import skyglass.data.filter.CustomFilterResolver;
import skyglass.data.filter.FieldType;
import skyglass.data.filter.FilterClass;
import skyglass.data.filter.FilterItem;
import skyglass.data.filter.FilterItemTree;
import skyglass.data.filter.FilterType;
import skyglass.data.filter.ITableFilter;
import skyglass.data.filter.api.AbstractFilterSpecification;
import skyglass.data.model.security.UserFactory;

public class AbstractHttpTableFilterWrapper<T, F> extends AbstractBaseHttpTableFilter<T, F>  {
	
	protected ITableFilter<T, ?> filter;
	
	@SuppressWarnings("unchecked")
	public F addSearch(String filterValue, String... fieldNames) {
   		filter.addSearch(filterValue, filterSearchFields(fieldNames));
        return (F)this;
	}	
	
	@SuppressWarnings("unchecked")
	public F addSearch(String filterValue, FieldType fieldType, String... fieldNames) {
        String searchQuery = HttpRequestUtils.getStringParamValue(request, DataConstants.SEARCH_QUERY);
        if (searchQuery != null) {
    		filter.addSearch(filterValue, fieldType, fieldNames);        	
        }   
        return (F)this;
	}		
	
	public AbstractHttpTableFilterWrapper(ITableFilter<T, ?> filter, HttpServletRequest request, UserFactory userFactory, AbstractFilterSpecification filterUtils) {
		super(filter, request, userFactory, filterUtils);
		this.filter = filter;
	}	

	@SuppressWarnings("unchecked")
	public F addFilter(String fieldName, FieldType fieldType, Object filterValue) {
		filter.addFilter(fieldName, fieldType, filterValue);
        return (F)this;
	}

	@SuppressWarnings("unchecked")
	public F addFilter(String fieldName, FieldType fieldType, Object filterValue, FilterType filterType) {
		filter.addFilter(fieldName, fieldType, filterValue, filterType);
        return (F)this;
	}

	@SuppressWarnings("unchecked")
	public F addFilter(String fieldName, FieldType fieldType, Object filterValue, FilterClass filterClass) {
		filter.addFilter(fieldName, fieldType, filterValue, filterClass);
        return (F)this;
	}

	@SuppressWarnings("unchecked")
	public F addFilter(String fieldName, FieldType fieldType, Object filterValue, FilterType filterType, FilterClass filterClass) {
		filter.addFilter(fieldName, fieldType, filterValue, filterType, filterClass);
        return (F)this;
	}

	@SuppressWarnings("unchecked")
	public F addFilters(String fieldName, FieldType fieldType, Object[] filterValues) {
		filter.addFilters(fieldName, fieldType, filterValues);
        return (F)this;
	}

	@SuppressWarnings("unchecked")
	public F addFilters(String fieldName, FieldType fieldType, Object[] filterValues, FilterType filterType) {
		filter.addFilters(fieldName, fieldType, filterValues, filterType);
        return (F)this;
	}

	@SuppressWarnings("unchecked")
	public F addFilters(String fieldName, FieldType fieldType, Object[] filterValues, FilterClass filterClass) {
		filter.addFilters(fieldName, fieldType, filterValues, filterClass);
        return (F)this;
	}

	@SuppressWarnings("unchecked")
	public F addFilters(String fieldName, FieldType fieldType, Object[] filterValues, FilterType filterType, FilterClass filterClass) {
		filter.addFilters(fieldName, fieldType, filterValues, filterType, filterClass);
        return (F)this;
	}

	@SuppressWarnings("unchecked")
	public F addFilter(FilterItem filterItem) {
		filter.addFilter(filterItem);
        return (F)this;
	}

	@SuppressWarnings("unchecked")
	public F addFilter(FilterItemTree parent, FilterItem filterItem) {
		filter.addFilter(parent, filterItem);
        return (F)this;
	}

	@SuppressWarnings("unchecked")
	public F addFilters(FilterItem... filterItems) {
		filter.addFilters(filterItems);
        return (F)this;
	}

	@SuppressWarnings("unchecked")
	public F addOrFilters(FilterItem... filterItems) {
		filter.addOrFilters(filterItems);
        return (F)this;
	}

	@SuppressWarnings("unchecked")
	public F addAndFilters(FilterItem... filterItems) {
		filter.addAndFilters(filterItems);
        return (F)this;
	}

	@SuppressWarnings("unchecked")
	public F addFilters(FilterItemTree parent, FilterItem... filterItems) {
		filter.addFilters(parent, filterItems);
        return (F)this;
	}

	public FilterItem createFilterItem(String fieldName, FieldType fieldType, Object filterValue) {
		return filter.createFilterItem(fieldName, fieldType, filterValue);
	}

	public FilterItem createFilterItem(String fieldName, FieldType fieldType, Object filterValue, FilterType filterType) {
		return filter.createFilterItem(fieldName, fieldType, filterValue, filterType);
	}

	public FilterItem createFilterItem(String fieldName, FieldType fieldType, Object filterValue, FilterClass filterClass) {
		return filter.createFilterItem(fieldName, fieldType, filterValue, filterClass);
	}

	public FilterItem createFilterItem(String fieldName, FieldType fieldType, Object filterValue, FilterType filterType, FilterClass filterClass) {
		return filter.createFilterItem(fieldName, fieldType, filterValue, filterType, filterClass);
	}

	@SuppressWarnings("unchecked")
	public F addFieldResolver(String fieldName, FieldType fieldType, String expression) {
		filter.addFieldResolver(fieldName, fieldType, expression);
        return (F)this;
	}
	
	public F addFieldResolver(String fieldName, String expression) {
		return addFieldResolver(fieldName, FieldType.Path, expression);
	}	
	
	@SuppressWarnings("unchecked")
	public F addFieldResolvers(String fieldName, FieldType fieldType, String... expressions) {
		filter.addFieldResolvers(fieldName, fieldType, expressions);
        return (F)this;
	}	
	
	public F addFieldResolvers(String fieldName, String... expressions) {
		return addFieldResolvers(fieldName, FieldType.Path, expressions);
	}
	
	@SuppressWarnings("unchecked")
	public F addCustomFieldResolver(String fieldName, CustomFieldResolver customFieldResolver) {
		filter.addCustomFieldResolver(fieldName, customFieldResolver);
		return (F)this;
	}	

	@SuppressWarnings("unchecked")
	public F addCustomFilterResolver(CustomFilterResolver customFilterResolver) {
		filter.addCustomFilterResolver(customFilterResolver);
        return (F)this;
	}
	
	public String resolveAliases(String expression) {
		return filter.resolveAliases(expression);
	}	
	
	public F addFilter(String fieldName, Object filterValue) {
		return addFilter(fieldName, FieldType.Path, filterValue);
	}

	public F addFilter(String fieldName, Object filterValue, FilterType filterType) {
		return addFilter(fieldName, FieldType.Path, filterValue, filterType);
	}

	public F addFilter(String fieldName, Object filterValue, FilterClass filterClass) {
		return addFilter(fieldName, FieldType.Path, filterValue, filterClass);
	}

	public F addFilter(String fieldName, Object filterValue, FilterType filterType, FilterClass filterClass) {
		return addFilter(fieldName, FieldType.Path, filterValue, filterType, filterClass);
	}

	public F addFilters(String fieldName, Object[] filterValues) {
		return addFilters(fieldName, FieldType.Path, filterValues);
	}

	public F addFilters(String fieldName, Object[] filterValues, FilterType filterType) {
		return addFilters(fieldName, FieldType.Path, filterValues, filterType);
	}

	public F addFilters(String fieldName, Object[] filterValues, FilterClass filterClass) {
		return addFilters(fieldName, FieldType.Path, filterValues, filterClass);
	}

	public F addFilters(String fieldName, Object[] filterValues, FilterType filterType, FilterClass filterClass) {
		return addFilters(fieldName, FieldType.Path, filterValues, filterType, filterClass);
	}

	public FilterItem createFilterItem(String fieldName, Object filterValue) {
		return createFilterItem(fieldName, FieldType.Path, filterValue);
	}

	public FilterItem createFilterItem(String fieldName, Object filterValue, FilterType filterType) {
		return createFilterItem(fieldName, FieldType.Path, filterValue, filterType);
	}

	public FilterItem createFilterItem(String fieldName, Object filterValue, FilterClass filterClass) {
		return createFilterItem(fieldName, FieldType.Path, filterValue, filterClass);
	}

	public FilterItem createFilterItem(String fieldName, Object filterValue, FilterType filterType, FilterClass filterClass) {
		return createFilterItem(fieldName, FieldType.Path, filterValue, filterType, filterClass);
	}

}
