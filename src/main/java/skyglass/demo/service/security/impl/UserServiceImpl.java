package skyglass.demo.service.security.impl;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import skyglass.demo.data.model.security.Authority;
import skyglass.demo.data.model.security.User;
import skyglass.demo.data.security.AuthorityData;
import skyglass.demo.data.security.UserData;
import skyglass.demo.service.GenericServiceImpl;
import skyglass.demo.service.error.ServiceException;
import skyglass.demo.service.security.UserService;

@Service
public class UserServiceImpl extends GenericServiceImpl<User, Long, UserData> implements UserService {
	
	@Autowired
	protected AuthorityData authorityData;
	
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

}
