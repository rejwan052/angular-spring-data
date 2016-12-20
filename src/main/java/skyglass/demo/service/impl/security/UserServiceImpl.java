package skyglass.demo.service.impl.security;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import skyglass.demo.data.security.AuthorityData;
import skyglass.demo.data.security.UserData;
import skyglass.demo.model.security.Authority;
import skyglass.demo.model.security.User;
import skyglass.demo.service.AbstractService;
import skyglass.demo.service.ServiceException;
import skyglass.demo.service.security.UserService;

@Service
public class UserServiceImpl extends AbstractService<User, Long, UserData> implements UserService {
	
	@Autowired
	protected AuthorityData authorityData;
	
	@PersistenceContext
    private EntityManager em;
	
	@Override
	public User save(User user) throws ServiceException {
    	if (user.getId() != null) {
    		User oldUser = findOne(user.getId());
    		if (!oldUser.getLogin().equals(user.getLogin())) {
    			checkLoginExists(user);
    		}
    	} else {
			checkLoginExists(user);    		
    	}
    	return super.save(user);
	}
	
	private void checkLoginExists(User user) throws ServiceException {
		User test = repository.findByLogin(user.getLogin());
		if (test != null) {
	        throw new ServiceException("saveUserError",
	        		"User with login '" + user.getLogin() + "' already exists");
		}		
	}

	@Override
	public User setAuthorities(Long userId, Long[] authorityIds) {
    	User user = findOne(userId);
		Iterable<Authority> authorities = authorityData.findAll(Arrays.asList(authorityIds));
		Set<Authority> result = new HashSet<Authority>();
		for (Authority authority: authorities) {
			result.add(authority);
		}
		user.setAuthorities(result);
		return repository.save(user);
	}
	
	@Override
	public Iterable<User> findNotPublishers() {
	    TypedQuery<User> query = em.createQuery(
	    		"select u from User u left join u.publisher p where p.id is null", User.class);
	    return query.getResultList();
	}
	
	@Override
	public Iterable<User> findNotSubscribers() {
	    TypedQuery<User> query = em.createQuery(
	    		"select u from User u left join u.subscriber s where s.id is null", User.class);
	    return query.getResultList();
	}

}
