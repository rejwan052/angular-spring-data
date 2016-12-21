package skyglass.data.filter;

import java.util.UUID;

import org.hibernate.criterion.Criterion;

import skyglass.data.filter.api.AbstractFilterSpecification;

public class FilterItem {	
	
	private Class<?> rootClass;
	
	private FieldResolver fieldResolver;	
	private FilterType filterType;
	private Object filterValue;
	private Object resolvedFilterValue;
	
	private FilterClass filterClass;
	
    //should be overriden to define different behaviour
    protected Object convertObject(String fieldName, Object value) {
    	return value;
    }	
	
	protected FilterItem(Class<?> rootClass, FieldResolver fieldResolver, Object filterValue) {
		this(rootClass, fieldResolver, filterValue, FilterType.EQ, FilterClass.STRING);
	}
	
	protected FilterItem(Class<?> rootClass, FieldResolver fieldResolver, Object filterValue, FilterType filterType) {
		this(rootClass, fieldResolver, filterValue, filterType, FilterClass.STRING);
	}
	
	protected FilterItem(Class<?> rootClass, FieldResolver fieldResolver, Object filterValue, FilterClass filterClass) {
		this(rootClass, fieldResolver, filterValue, FilterType.EQ, filterClass);
	}	
	
	protected FilterItem(Class<?> rootClass, FieldResolver fieldResolver, Object filterValue, FilterType filterType, FilterClass filterClass) {
		this.rootClass = rootClass;
		this.fieldResolver = fieldResolver;
		this.filterType = filterType;
		this.filterClass = filterClass;
		this.filterValue = filterValue;
	}
	
	public Class<?> getRootClass() {
		return rootClass;
	}
	
	public FieldResolver getFieldResolver() {
		return fieldResolver;
	}
	
	public FilterType getFilterType() {
		return filterType;
	}
	
	public Object getFilterValue() {
		if (filterValue == null) {
			return null;
		}
		if (resolvedFilterValue == null) {
			this.resolvedFilterValue = resolveFilterValue();				
		}
		return resolvedFilterValue;
	}
	
	public FilterClass getFilterClass() {
		return filterClass;
	}
	
    private Object resolveFilterValue() {
    	if (filterValue instanceof Criterion) {
	    	return filterValue;
    	}        	
    	
    	if (filterType == FilterType.LIKE) {
	    	return AbstractFilterSpecification.processFilterString(filterValue);
    	}   
    	
        Object result = null;       
        String stringValue = filterValue.toString();
        
        
        if (filterClass == FilterClass.BOOLEAN) {
            result = Boolean.valueOf(stringValue);
        }
        else if (filterClass == FilterClass.LONG) {
            result = Long.valueOf(stringValue);
        }
        else if (filterClass == FilterClass.STRING) {
            result = convertObject(fieldResolver.getResolver(), filterValue);
        }
        else if (filterClass == FilterClass.UUID) {
            result = UUID.fromString(stringValue);
        }
        else {
            throw new IllegalArgumentException("Unsupported filter classname: "+filterClass);
        }
        
        return result;
    }  
	
	

}
