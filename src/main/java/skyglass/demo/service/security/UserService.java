package skyglass.demo.service.security;

import skyglass.demo.data.model.security.User;
import skyglass.demo.data.security.UserData;
import skyglass.demo.service.GenericService;

public interface UserService extends GenericService<User, Long, UserData> {
	
	public User setAuthorities(Long userId, Long[] authorityIds);

}
