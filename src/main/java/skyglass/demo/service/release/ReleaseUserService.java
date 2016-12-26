package skyglass.demo.service.release;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import skyglass.data.model.security.User;
import skyglass.data.query.QueryResult;
import skyglass.data.service.AbstractService;
import skyglass.data.service.security.UserService;
import skyglass.demo.data.release.ReleaseUserData;
import skyglass.demo.model.release.ReleaseUser;

@Service
public class ReleaseUserService extends AbstractService<ReleaseUser, Long, ReleaseUserData> {
	
	@Autowired
	protected UserService userService;
	
	public QueryResult<User> findNotPublishers(HttpServletRequest request) {
		return convertToUsers(
				filterBuilder.jpaDataFilter(request, entityClass)
				.addFilter("publisher.id", null)
		        .addFieldResolvers("name", "user.firstName", "user.lastName")
				.addHttpSearch("name")
				.getResult()
		);
	}
	
	public QueryResult<User> findNotSubscribers(HttpServletRequest request) {
		return convertToUsers(filterBuilder.jpaDataFilter(request, entityClass)
				.addFilter("subscriber.id", null)
		        .addFieldResolvers("name", "user.firstName", "user.lastName")
				.addHttpSearch("name")
				.getResult()
		);
	}
	
	private QueryResult<User> convertToUsers(QueryResult<ReleaseUser> queryResult) {
		List<User> results = new ArrayList<User>();
		queryResult.getResults().stream().forEach(s -> results.add(s.getUser()));
		QueryResult<User> result = new QueryResult<User>();
		result.setResults(results);
		result.setTotalRecords(queryResult.getTotalRecords());
		return result;
	}

	@Override
	public QueryResult<ReleaseUser> findEntities(HttpServletRequest request) {
		return filterBuilder.jpaDataFilter(request, entityClass)
				.addHttpSearch("name")
				.getResult();
	}

}
