package skyglass.demo.service.filter;

import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Service;

import skyglass.data.filter.api.AbstractFilterHelper;
import skyglass.data.filter.http.api.PermissionType;

@Service("filterHelper")
public class FilterHelper extends AbstractFilterHelper {	
	
	@PersistenceContext
    protected EntityManager entityManager;

	@Override
	public Set<Long> getExpectedActionIds(Class<?> clazz, PermissionType permissionType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	//TODO: implement
	public Long getCurrentUserId() {
		return null;
	}

	@Override
	public EntityManager getEntityManager() {
		return entityManager;
	}
	
	@Override
	public Session getSession() {
		return entityManager.unwrap(Session.class);
	}

	
}
