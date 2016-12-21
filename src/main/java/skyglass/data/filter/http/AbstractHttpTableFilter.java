package skyglass.data.filter.http;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Criteria;

import skyglass.data.filter.CustomHibernateFilterResolver;
import skyglass.data.filter.ITableFilter;
import skyglass.data.filter.api.AbstractFilterSpecification;
import skyglass.data.filter.http.api.PermissionType;
import skyglass.data.model.security.Secured;
import skyglass.data.model.security.UserFactory;

public abstract class AbstractHttpTableFilter<T, F> extends AbstractHttpTableFilterWrapper<T, F> implements IHttpTableFilter<T, F>  {
	
	public AbstractHttpTableFilter(ITableFilter<T, ?> filter, HttpServletRequest request, UserFactory userFactory, AbstractFilterSpecification filterUtils) {
		super(filter, request, userFactory, filterUtils);
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
    public F addSecurityFilter(final String alias, final Class<? extends Secured> clazz) {
    	addCustomFilterResolver(new CustomHibernateFilterResolver() {		
			@Override
			public void addCustomFilter(Criteria criteria) {
		    	filterSpecification.addCustomSecurityResolver(criteria, filter.resolveAliases(alias), clazz, PermissionType.Read);				
			}
		});
        return (F)this;
    }      
	
}
