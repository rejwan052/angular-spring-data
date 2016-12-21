package skyglass.demo.service.filter;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import skyglass.data.filter.JunctionType;
import skyglass.data.filter.api.AbstractSecuredHibernateTableFilter;
import skyglass.data.filter.http.api.PermissionType;

@Component("securedHibernateTableFilter")
@Scope("prototype")
public class SecuredHibernateTableFilter<T> extends AbstractSecuredHibernateTableFilter<T> {
	
	public SecuredHibernateTableFilter(final Class<?> clazz, JunctionType junctionType, 
			FilterSpecification filterSpecification, final PermissionType permissionType) {
		super(clazz, junctionType, filterSpecification, permissionType);
	}

}
