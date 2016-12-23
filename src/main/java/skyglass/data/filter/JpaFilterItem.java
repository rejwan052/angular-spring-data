package skyglass.data.filter;

import skyglass.data.filter.api.IFilterHelper;

public class JpaFilterItem extends FilterItem {
	
	protected IFilterHelper filterHelper;
	
    @Override
    protected Object convertObject(String fieldName, Object value) {
    	return filterHelper.convertObject(getRootClass(), fieldName, value, false);
    }
    
    public JpaFilterItem(IFilterHelper filterHelper, Class<?> rootClass, FieldResolver fieldResolver, Object filterValue) {
		this(filterHelper, rootClass, fieldResolver, filterValue, FilterType.EQ, FilterClass.STRING);
	}
	
	public JpaFilterItem(IFilterHelper filterHelper, Class<?> rootClass, FieldResolver fieldResolver, Object filterValue, FilterType filterType) {
		this(filterHelper, rootClass, fieldResolver, filterValue, filterType, FilterClass.STRING);
	}
	
	public JpaFilterItem(IFilterHelper filterHelper, Class<?> rootClass, FieldResolver fieldResolver, Object filterValue, FilterClass filterClass) {
		this(filterHelper, rootClass, fieldResolver, filterValue, null, filterClass);
	}	
	
	public JpaFilterItem(IFilterHelper filterHelper, Class<?> rootClass, FieldResolver fieldResolver, Object filterValue, FilterType filterType, FilterClass filterClass) {
		super(rootClass, fieldResolver, filterValue, filterType, filterClass);
		this.filterHelper = filterHelper;
	}

}
