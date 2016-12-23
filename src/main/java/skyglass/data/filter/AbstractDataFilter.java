package skyglass.data.filter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import skyglass.data.query.QueryResult;

public abstract class AbstractDataFilter<T, F> extends AbstractBaseDataFilter<T, F> implements IDataFilter<T, F> {
	
	protected abstract long getRowCount();
	
	protected abstract QueryResult<T> getPagedResult();
	
	protected abstract List<T> getUnpagedResult();
	
	protected abstract void applyOrder(List<OrderField> orderFields);
	
	protected abstract void resolveCustomFilter(CustomFilterResolver filterResolver);
	
    private List<CustomFilterResolver> customFilterResolvers = new ArrayList<CustomFilterResolver>();
    
	protected FilterItemTree rootFilterItem;  
	
	private Map<String, List<FieldResolver>> searchMap = new HashMap<String, List<FieldResolver>>();
	
    protected void resolveCustomFilters() {
    	for (CustomFilterResolver customFilterResolver: customFilterResolvers) {
    		resolveCustomFilter(customFilterResolver);
    	}
    }
    
	@SuppressWarnings("unchecked")
	public F addCustomFilterResolver(CustomFilterResolver customFilterResolver) {
		customFilterResolvers.add(customFilterResolver);
		return (F)this;
	}     
	
	protected AbstractDataFilter(JunctionType junctionType) {
		super(junctionType);
		this.rootFilterItem = new FilterItemTree(junctionType);		
	}  
    
    private boolean returnFullResult() {
    	return postFilterResolvers.size() > 0 || shouldApplyPostFilters() || shouldApplyPostOrder();   	
    }    
    
	public QueryResult<T> getResult() {
    	if (returnEmptyResult()) {
    		return getEmptyResult();
    	}
    	initBeforeResult();
    	if (returnFullResult()) {
    		List<T> unpagedResult = applyPostFilters(); 
    		applyPostOrder(unpagedResult);
        	List<T> pagedResult = new ArrayList<T>();
        	int startIndex = (getPageNumber() - 1) * getRowsPerPage();
        	int endIndex = Math.min(startIndex + getRowsPerPage(), unpagedResult.size());
        	for (int i = startIndex; i < endIndex; i++) {
        		pagedResult.add(unpagedResult.get(i));
        	}
            QueryResult<T> result = new QueryResult<T>();
            result.setTotalRecords(unpagedResult.size());
            result.setResults(pagedResult);
            return result;  
   		
    	} else {
           return getPagedResult();      		
    	}    	
    } 
	
	public List<T> getUnpagedList() {
    	if (returnEmptyResult()) {
    		return getEmptyResult().getResults();
    	}
    	initBeforeResult();
    	if (returnFullResult()) {
    		List<T> unpagedResult = applyPostFilters(); 
    		applyPostOrder(unpagedResult);
    		return unpagedResult;
   		
    	} else {
           return getUnpagedResult();      		
    	}    	
    } 	
	
	public long getResultCount() {
    	if (returnEmptyResult()) {
    		return 0;
    	}
    	initBeforeResult();
    	if (returnFullResult()) {
    		List<T> unpagedResult = applyPostFilters();   
    		return unpagedResult.size();
    	} else {
           return getRowCount();      		
    	}    	
    }	
    
    protected boolean shouldApplyPostOrder() {
    	if (getOrderFields().size() == 0) {
    		return false;
    	}
    	for (String fieldName: getPostProcessFields()) {
    		for (OrderField orderField: getOrderFields()) {
    			for (String orderFieldName: orderField.getOrderField().getResolvers()) {
            		if (fieldName.equals(orderFieldName)) {
            			return true;
            		}    				
    			}  			
    		}
    	}
    	return false;
    }
    
    public boolean shouldNotApplyPostOrder() {
    	return !shouldApplyPostOrder();
    }
    
	private Collection<String> getPostProcessFields() {
		return postFieldResolverMap.keySet();
	} 

	
    protected boolean doApplyOrder() {
    	List<OrderField> orderFields = getOrderFields();
        if (orderFields.size() > 0 && shouldNotApplyPostOrder()) {
       		applyOrder(orderFields);
        	return true;
        }  
        return false;
    }     

	@SuppressWarnings("unchecked")
	public F addSearch(final String filterValue, final String... fieldNames) {
		addSearch(searchMap, filterValue, FieldType.Path, fieldNames);
		return (F)this;
	}
	
	@SuppressWarnings("unchecked")
	public F addSearch(final String filterValue, FieldType fieldType, final String... fieldNames) {
		addSearch(searchMap, filterValue, fieldType, fieldNames);
		return (F)this;
	}	
	
	protected void initSearch() {
		if (!searchMap.isEmpty()) {
	    	FilterItemTree orFilter = new FilterItemTree(JunctionType.OR);
	    	rootFilterItem.addChild(orFilter);
	    	for (String fieldValue: searchMap.keySet()) {
	    		List<FieldResolver> fieldResolvers = searchMap.get(fieldValue);
	    		for (FieldResolver fieldResolver: fieldResolvers) {
	            	FilterItem filterItem = createFilterItem(fieldResolver.getFieldName(), fieldResolver.getFieldType(), fieldValue, FilterType.LIKE, FilterClass.STRING);
	            	orFilter.addChild(filterItem); 			
	    		}  
	    	}
    	}
	}	
	
	@SuppressWarnings("unchecked")
    public F addCustomFieldResolver(String fieldName, CustomFieldResolver customFieldResolver) {
		FieldResolver fieldResolver = getFieldResolver(fieldName, FieldType.Criteria);
		fieldResolver.setCustomFieldResolver(customFieldResolver);
		addFilter(fieldName, null);
		return (F)this;
    } 	
    
	@SuppressWarnings("unchecked")
    public F addFieldResolver(String fieldName, FieldType fieldType, String expression) {
		FieldResolver fieldResolver = getFieldResolver(fieldName, fieldType);
		fieldResolver.addResolvers(expression);
		return (F)this;
    } 
	
	@SuppressWarnings("unchecked")
    public F addFieldResolvers(String fieldName, FieldType fieldType, String... expressions) {
		FieldResolver fieldResolver = getFieldResolver(fieldName, fieldType);
		fieldResolver.addResolvers(expressions);
		return (F)this;
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
	
    
	@SuppressWarnings("unchecked")
    public F addFilter(String fieldName, FieldType fieldType, Object filterValue) {
    	addFilter(fieldName, fieldType, filterValue, FilterType.EQ, FilterClass.STRING);
		return (F)this;
    }   
    
	@SuppressWarnings("unchecked")
    public F addFilter(String fieldName, FieldType fieldType, Object filterValue, FilterType filterType) {
    	addFilter(fieldName, fieldType, filterValue, filterType, FilterClass.STRING);
		return (F)this;
    }  
    
	@SuppressWarnings("unchecked")
    public F addFilter(String fieldName, FieldType fieldType, Object filterValue, FilterClass filterClass) {
    	addFilter(fieldName, fieldType, filterValue, FilterType.EQ, filterClass);
		return (F)this;
    }     
   
	@SuppressWarnings("unchecked")
    public F addFilter(String fieldName, FieldType fieldType, Object filterValue, FilterType filterType, FilterClass filterClass) {
    	FilterItem filterItem = createFilterItem(fieldName, fieldType, filterValue, filterType, filterClass);
    	rootFilterItem.addChild(filterItem);
		return (F)this;
    }
    
	@SuppressWarnings("unchecked")
    public F addFilters(String fieldName, FieldType fieldType, Object[] filterValues) {
    	addFilters(fieldName, fieldType, filterValues, FilterType.EQ, FilterClass.STRING);
		return (F)this;
    }   
    
	@SuppressWarnings("unchecked")
    public F addFilters(String fieldName, FieldType fieldType, Object[] filterValues, FilterType filterType) {
    	addFilters(fieldName, fieldType, filterValues, filterType, FilterClass.STRING);
		return (F)this;
    }  
    
	@SuppressWarnings("unchecked")
    public F addFilters(String fieldName, FieldType fieldType, Object[] filterValues, FilterClass filterClass) {
    	addFilters(fieldName, fieldType, filterValues, FilterType.EQ, filterClass);
		return (F)this;
    }     
   
	@SuppressWarnings("unchecked")
    public F addFilters(String fieldName, FieldType fieldType, Object[] filterValues, FilterType filterType, FilterClass filterClass) {
    	FilterItemTree orFilter = new FilterItemTree(JunctionType.OR);
    	rootFilterItem.addChild(orFilter);
    	for (Object filterValue: filterValues) {
        	FilterItem filterItem = createFilterItem(fieldName, fieldType, filterValue, filterType, filterClass);
        	orFilter.addChild(filterItem);    		
    	}
		return (F)this;
    } 
    
	@SuppressWarnings("unchecked")
	public F addFilter(FilterItem filterItem) {
    	rootFilterItem.addChild(filterItem);
		return (F)this;
    }
	
	@SuppressWarnings("unchecked")
	public F addFilter(FilterItemTree parent, FilterItem filterItem) {
    	parent.addChild(filterItem);
    	rootFilterItem.addChild(parent); 
		return (F)this;
    }
	
	@SuppressWarnings("unchecked")
	public F addFilters(FilterItem... filterItems) {
    	for (FilterItem filterItem: filterItems) {
    		addFilter(filterItem);
    	} 
		return (F)this;
    }
	
	public F addOrFilters(FilterItem... filterItems) {
    	return addFilters(new FilterItemTree(JunctionType.OR), filterItems);
    }
	
	public F addAndFilters(FilterItem... filterItems) {
    	return addFilters(new FilterItemTree(JunctionType.AND), filterItems);    	
    }
	
	@SuppressWarnings("unchecked")
	public F addFilters(FilterItemTree parent, FilterItem... filterItems) {
    	for (FilterItem filterItem: filterItems) {
    		parent.addChild(filterItem);
    	}
    	rootFilterItem.addChild(parent);
		return (F)this;
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

	public F addFieldResolver(String fieldName, String expression) {
		return addFieldResolver(fieldName, FieldType.Path, expression);
	}

	public F addFieldResolvers(String fieldName, String... expressions) {
		return addFieldResolvers(fieldName, FieldType.Path, expressions);
	} 	
	
}
