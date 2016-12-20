package skyglass.demo.service.release;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Service;

import skyglass.demo.data.release.SubscriberData;
import skyglass.demo.model.release.Subscriber;
import skyglass.demo.model.security.User;
import skyglass.demo.service.AbstractService;

@Service
public class SubscriberService extends AbstractService<Subscriber, Long, SubscriberData> {
	
	@PersistenceContext
    private EntityManager em;
	
	public Subscriber findByUser(User user) {
		return repository.findOne(user.getId());
	}	
	
}
