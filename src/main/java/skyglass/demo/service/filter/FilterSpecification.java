package skyglass.demo.service.filter;

import java.util.Set;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import skyglass.data.filter.api.AbstractFilterSpecification;
import skyglass.data.filter.http.api.PermissionType;
import skyglass.data.model.security.UserFactory;

@Service("filterSpecification")
public class FilterSpecification extends AbstractFilterSpecification {	
	
	@Autowired
	protected UserFactory userFactory;
	
	@Autowired
	protected SessionFactory sessionFactory;

	@Override
	protected Set<Long> getExpectedActionIds(Class<?> clazz, PermissionType permissionType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected Long getCurrentUserId() {
		return userFactory.getCurrentUserId();
	}

	@Override
	protected SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	
}
