package skyglass.demo.service.release;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import skyglass.data.model.security.User;
import skyglass.data.service.AbstractNameService;
import skyglass.data.service.ServiceException;
import skyglass.demo.data.release.PublisherData;
import skyglass.demo.model.release.Publisher;
import skyglass.demo.model.release.ReleaseUser;

@Service
public class PublisherService extends AbstractNameService<Publisher, Long, PublisherData> {
	
	@Autowired
	protected ReleaseUserService releaseUserService;
	
	public Publisher findByUser(User user) {
		return repository.findOne(user.getId());
	}
	
	@Override
	@Transactional
	public void saveAll(Long[] entityIds) {
		Iterable<ReleaseUser> releaseUsers = releaseUserService.findAll(entityIds);
		for (ReleaseUser releaseUser: releaseUsers) {
			repository.save(new Publisher(releaseUser, releaseUser.getUser().getName()));
		}
	}	
	
	@Override
	@Transactional
	public void delete(Publisher publisher) throws ServiceException {
		publisher.getReleaseUser().setPublisher(null);
		releaseUserService.save(publisher.getReleaseUser());
		repository.delete(publisher);
	}
	
	@Override
	@Transactional
	public void delete(Long id) throws ServiceException {
		delete(repository.findOne(id));
	}
}
