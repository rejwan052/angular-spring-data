package skyglass.data.filter;

import skyglass.data.filter.api.AbstractFilterSpecification;

public class HibernateFilterItem extends FilterItem {
	
	protected AbstractFilterSpecification filterUtils;
	
    @Override
    protected Object convertObject(String fieldName, Object value) {
    	return filterUtils.convertObject(getRootClass(), fieldName, value, false);
    }
    
    public HibernateFilterItem(AbstractFilterSpecification filterUtils, Class<?> rootClass, FieldResolver fieldResolver, Object filterValue) {
		this(filterUtils, rootClass, fieldResolver, filterValue, FilterType.EQ, FilterClass.STRING);
	}
	
	public HibernateFilterItem(AbstractFilterSpecification filterUtils, Class<?> rootClass, FieldResolver fieldResolver, Object filterValue, FilterType filterType) {
		this(filterUtils, rootClass, fieldResolver, filterValue, filterType, FilterClass.STRING);
	}
	
	public HibernateFilterItem(AbstractFilterSpecification filterUtils, Class<?> rootClass, FieldResolver fieldResolver, Object filterValue, FilterClass filterClass) {
		this(filterUtils, rootClass, fieldResolver, filterValue, null, filterClass);
	}	
	
	public HibernateFilterItem(AbstractFilterSpecification filterUtils, Class<?> rootClass, FieldResolver fieldResolver, Object filterValue, FilterType filterType, FilterClass filterClass) {
		super(rootClass, fieldResolver, filterValue, filterType, filterClass);
		this.filterUtils = filterUtils;
	}

}
