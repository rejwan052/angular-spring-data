package skyglass.data.filter.api;

import org.hibernate.Criteria;

import skyglass.data.filter.CustomHibernateFilterResolver;
import skyglass.data.filter.JunctionType;
import skyglass.data.filter.http.api.PermissionType;
import skyglass.data.model.security.Secured;

public abstract class AbstractSecuredHibernateTableFilter<T> extends AbstractHibernateTableFilter<T> {
	
	public AbstractSecuredHibernateTableFilter(final Class<? extends Secured> clazz, JunctionType junctionType, 
			AbstractFilterSpecification filterSpecification, final PermissionType permissionType) {
		super(clazz, junctionType, filterSpecification);
	    
    	addCustomFilterResolver(new CustomHibernateFilterResolver() {			
			@Override
			public void addCustomFilter(Criteria criteria) {
				filterSpecification.addCustomSecurityResolver(criteria, "root", clazz, permissionType);				
			}
		});	    

	}

}
