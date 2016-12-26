package skyglass.demo.service.release;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import skyglass.data.service.AbstractNameService;
import skyglass.data.service.ServiceException;
import skyglass.demo.data.release.SubscriberData;
import skyglass.demo.model.release.ReleaseUser;
import skyglass.demo.model.release.Subscriber;

@Service
public class SubscriberService extends AbstractNameService<Subscriber, Long, SubscriberData> {
	
	@Autowired
	protected ReleaseUserService releaseUserService;
	
	public Subscriber findByReleaseUser(ReleaseUser releaseUser) {
		return repository.findOne(releaseUser.getId());
	}
	
	@Override
	@Transactional
	public void saveAll(Long[] entityIds) {
		Iterable<ReleaseUser> releaseUsers = releaseUserService.findAll(entityIds);
		for (ReleaseUser releaseUser: releaseUsers) {
			repository.save(new Subscriber(releaseUser, releaseUser.getUser().getName()));
		}
	}	
	
	@Override
	@Transactional
	public void delete(Subscriber subscriber) throws ServiceException {
		subscriber.getReleaseUser().setSubscriber(null);
		releaseUserService.save(subscriber.getReleaseUser());
		repository.delete(subscriber);
	}
	
	@Override
	@Transactional
	public void delete(Long id) throws ServiceException {
		delete(repository.findOne(id));
	}
}
