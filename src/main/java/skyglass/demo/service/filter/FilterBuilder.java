package skyglass.demo.service.filter;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import skyglass.data.filter.JunctionType;
import skyglass.data.filter.api.DataFilter;
import skyglass.data.filter.api.IPostDataFilter;
import skyglass.data.filter.api.JpaDataFilter;
import skyglass.data.filter.api.PostDataFilter;
import skyglass.data.filter.api.SecuredJpaDataFilter;
import skyglass.data.filter.http.api.PermissionType;
import skyglass.data.model.ISecuredEntity;

@Service("filterBuilder")
public class FilterBuilder {
	
	@Autowired
	protected FilterHelper filterService;
	
    static final public String ROWS_PER_PAGE_QUERY_PARAM = "rowsPerPage";
    static final public String PAGE_NUMBER_QUERY_PARAM = "pageNumber";
    static final public String ORDER_FIELD_QUERY_PARAM = "orderField";
    static final public String SORT_TYPE_QUERY_PARAM = "sortType";
    static final public String FILTER_FIELDS_QUERY_PARAM = "filterFields";
    static final public String FILTER_VALUE_PREFIX = "filterValue_";
    static final public String FILTER_TYPE_PREFIX = "filterType_";
    static final public String FILTER_CLASS_PREFIX = "filterClass_";	

    public <T> DataFilter<T> dataFilter(Class<T> clazz) {
    	return new JpaDataFilter<T>(clazz, JunctionType.AND, filterService);
    }     
    
    public <T> IPostDataFilter<T> postDataFilter(Collection<T> fullResult) {
    	return new PostDataFilter<T>(fullResult, JunctionType.AND);
    }
    
    public <T extends ISecuredEntity<?>> DataFilter<T> securedJpaDataFilter(
    		Class<T> clazz, PermissionType permissionType) {
    	return securedJpaDataFilter(clazz, JunctionType.AND, permissionType);
    }     
    
    public <T extends ISecuredEntity<?>> DataFilter<T> securedJpaDataFilter(
    		Class<T> clazz, boolean ghostedDate) {
    	return securedJpaDataFilter(clazz, JunctionType.AND, PermissionType.Read);
    }     
    
	public <T> IPostDataFilter<T> postDataFilter(Iterable<T> fullResult, JunctionType junctionType) {
		return new PostDataFilter<T>(fullResult, junctionType);
	}
	
	public <T extends ISecuredEntity<?>> DataFilter<T> securedJpaDataFilter(Class<T> clazz, 
			JunctionType junctionType, PermissionType permissionType) {
	    DataFilter<T> filter = new SecuredJpaDataFilter<T>(
	    		clazz, junctionType, filterService, permissionType);  
		return filter;
	}	

}
