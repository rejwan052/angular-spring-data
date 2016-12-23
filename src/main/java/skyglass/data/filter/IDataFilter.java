package skyglass.data.filter;


public interface IDataFilter<T, F> extends IBaseDataFilter<T, F> {	
	
	public F addSearch(String filterValue, FieldType fieldType, String... fieldNames);		
	
	public F addSearch(String filterValue, String... fieldNames);	
	
	public F addFilter(String fieldName, Object filterValue);
	
	public F addFilter(String fieldName, Object filterValue, FilterType filterType);
	
	public F addFilter(String fieldName, Object filterValue, FilterClass filterClass);

	public F addFilter(String fieldName, Object filterValue, FilterType filterType, FilterClass filterClass);
	
	public F addFilters(String fieldName, Object[] filterValues);
	
	public F addFilters(String fieldName, Object[] filterValues, FilterType filterType);	
	
	public F addFilters(String fieldName, Object[] filterValues, FilterClass filterClass);	

	public F addFilters(String fieldName, Object[] filterValues, FilterType filterType, FilterClass filterClass);
	
	public F addFilter(String fieldName, FieldType fieldType, Object filterValue);
	
	public F addFilter(String fieldName, FieldType fieldType, Object filterValue, FilterType filterType);
	
	public F addFilter(String fieldName, FieldType fieldType, Object filterValue, FilterClass filterClass);

	public F addFilter(String fieldName, FieldType fieldType, Object filterValue, FilterType filterType, FilterClass filterClass);
	
	public F addFilters(String fieldName, FieldType fieldType, Object[] filterValues);
	
	public F addFilters(String fieldName, FieldType fieldType, Object[] filterValues, FilterType filterType);	
	
	public F addFilters(String fieldName, FieldType fieldType, Object[] filterValues, FilterClass filterClass);	

	public F addFilters(String fieldName, FieldType fieldType, Object[] filterValues, FilterType filterType, FilterClass filterClass);	
	
	public F addFilter(FilterItem filterItem);
	
	public F addFilter(FilterItemTree parent, FilterItem filterItem);
	
	public F addFilters(FilterItem... filterItems);
	
	public F addOrFilters(FilterItem... filterItems);
	
	public F addAndFilters(FilterItem... filterItems);
	
	public F addFilters(FilterItemTree parent, FilterItem... filterItems);
	
	public FilterItem createFilterItem(String fieldName, Object filterValue);
	
	public FilterItem createFilterItem(String fieldName, Object filterValue, FilterType filterType);
	
	public FilterItem createFilterItem(String fieldName, Object filterValue, FilterClass filterClass);
	
	public FilterItem createFilterItem(String fieldName, Object filterValue, FilterType filterType, FilterClass filterClass);	
	
	public FilterItem createFilterItem(String fieldName, FieldType fieldType, Object filterValue);
	
	public FilterItem createFilterItem(String fieldName, FieldType fieldType, Object filterValue, FilterType filterType);
	
	public FilterItem createFilterItem(String fieldName, FieldType fieldType, Object filterValue, FilterClass filterClass);
	
	public FilterItem createFilterItem(String fieldName, FieldType fieldType, Object filterValue, FilterType filterType, FilterClass filterClass);	
	
	public F addFieldResolver(String fieldName, String expression);	
	
	public F addFieldResolver(String fieldName, FieldType fieldType, String expression);		
	
	public F addFieldResolvers(String fieldName, String... expressions);	
	
	public F addFieldResolvers(String fieldName, FieldType fieldType, String... expressions);	
	
	public F addCustomFieldResolver(String fieldName, CustomFieldResolver customFieldResolver);

	public F addCustomFilterResolver(CustomFilterResolver customFilterResolver);
	
	public String resolveAliases(String expression);
	
}
    


