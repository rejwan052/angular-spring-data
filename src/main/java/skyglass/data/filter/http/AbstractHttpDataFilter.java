package skyglass.data.filter.http;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Criteria;

import skyglass.data.filter.CustomJpaFilterResolver;
import skyglass.data.filter.IDataFilter;
import skyglass.data.filter.api.IFilterHelper;
import skyglass.data.filter.http.api.PermissionType;

public abstract class AbstractHttpDataFilter<T, F> extends AbstractHttpDataFilterWrapper<T, F> implements IHttpDataFilter<T, F>  {
	
	public AbstractHttpDataFilter(IDataFilter<T, ?> filter, HttpServletRequest request, IFilterHelper filterHelper) {
		super(filter, request, filterHelper);
	}
	
	@SuppressWarnings("unchecked")
	public F addHttpSearch(String... fieldNames) {
        if (searchQuery != null) {
    		addSearch(searchQuery, fieldNames);        	
        }
        return (F)this;
	}		
	
    @SuppressWarnings("unchecked")
    public F addStatuses() {
        String[] statuses = HttpRequestUtils.getStringParamValues(request, "status");
        if (statuses != null) {
            addFilters("status", statuses);        	
        } 
        return (F)this;
    }  
    
    @SuppressWarnings("unchecked")
    public <R> F addStatuses(ArrayNormalizer<R> normalizer) {
        String[] statuses = HttpRequestUtils.getStringParamValues(request, "status");
        if (statuses != null) {
        	R[] result = normalizer.normalizeArray(statuses);
            addFilters("status", result);        	
        } 
        return (F)this;
    }
    
    @SuppressWarnings("unchecked")
    public F addTypes() {
        String[] types = HttpRequestUtils.getStringParamValues(request, "type");
        if (types != null) {
            addFilters("type", types);        	
        } 
        return (F)this;
    }  
    
    @SuppressWarnings("unchecked")
    public F addSecurityFilter(final String alias, final Class<?> clazz) {
    	addCustomFilterResolver(new CustomJpaFilterResolver() {		
			@Override
			public void addCustomFilter(Criteria criteria) {
		    	filterHelper.addCustomSecurityResolver(criteria, filter.resolveAliases(alias), clazz, PermissionType.Read);				
			}
		});
        return (F)this;
    }      
	
}
