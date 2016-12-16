package skyglass.demo.service.security;

import skyglass.demo.data.security.UserData;
import skyglass.demo.model.security.User;
import skyglass.demo.service.IGenericService;

public interface UserService extends IGenericService<User, Long, UserData> {
	
	public User setAuthorities(Long userId, Long[] authorityIds);

}
