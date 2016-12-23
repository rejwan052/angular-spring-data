package skyglass.demo.service.security;

import javax.servlet.http.HttpServletRequest;

import skyglass.data.query.QueryResult;
import skyglass.demo.data.security.UserData;
import skyglass.demo.model.security.User;
import skyglass.demo.service.IGenericService;

public interface UserService extends IGenericService<User, Long, UserData> {
	
	public User setAuthorities(Long userId, Long[] authorityIds);
	
	public QueryResult<User> findNotSubscribers(HttpServletRequest request);
	
	public QueryResult<User> findNotPublishers(HttpServletRequest request);

}
