package skyglass.data.filter.api;

import org.hibernate.Criteria;

import skyglass.data.filter.CustomJpaFilterResolver;
import skyglass.data.filter.JunctionType;
import skyglass.data.filter.http.api.PermissionType;

public class SecuredJpaDataFilter<T> extends JpaDataFilter<T> {
	
	public SecuredJpaDataFilter(final Class<?> clazz, JunctionType junctionType, 
			IFilterHelper filterHelper, final PermissionType permissionType) {
		super(clazz, junctionType, filterHelper);
	    
    	addCustomFilterResolver(new CustomJpaFilterResolver() {			
			@Override
			public void addCustomFilter(Criteria criteria) {
				filterHelper.addCustomSecurityResolver(criteria, "root", clazz, permissionType);				
			}
		});	    

	}

}
