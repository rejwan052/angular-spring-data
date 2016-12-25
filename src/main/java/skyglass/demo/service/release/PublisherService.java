package skyglass.demo.service.release;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import skyglass.demo.data.release.PublisherData;
import skyglass.demo.model.release.Publisher;
import skyglass.demo.model.security.User;
import skyglass.demo.service.AbstractNameService;
import skyglass.demo.service.ServiceException;
import skyglass.demo.service.security.UserService;

@Service
public class PublisherService extends AbstractNameService<Publisher, Long, PublisherData> {
	
	@Autowired
	protected UserService userService;
	
	public Publisher findByUser(User user) {
		return repository.findOne(user.getId());
	}
	
	@Override
	@Transactional
	public void saveAll(Long[] entityIds) {
		Iterable<User> users = userService.findAll(entityIds);
		for (User user: users) {
			repository.save(new Publisher(user, user.getName()));
		}
	}	
	
	@Override
	@Transactional
	public void delete(Publisher publisher) throws ServiceException {
		publisher.getUser().setPublisher(null);
		userService.save(publisher.getUser());
		repository.delete(publisher);
	}
	
	@Override
	@Transactional
	public void delete(Long id) throws ServiceException {
		delete(repository.findOne(id));
	}
}
