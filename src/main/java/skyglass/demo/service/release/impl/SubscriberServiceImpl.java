package skyglass.demo.service.release.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import skyglass.demo.data.model.release.Category;
import skyglass.demo.data.model.release.Subscriber;
import skyglass.demo.data.model.release.Subscription;
import skyglass.demo.data.model.security.User;
import skyglass.demo.data.release.SubscriberData;
import skyglass.demo.service.GenericServiceImpl;
import skyglass.demo.service.release.SubscriberService;

@Service
public class SubscriberServiceImpl extends GenericServiceImpl<Subscriber, Long, SubscriberData> 
	implements SubscriberService {
	
	@Override
	public Subscriber findByUser(User user) {
		return repository.findOne(user.getId());
	}	

	@Override
	public void subscribe(User user, Category category) {
		Subscriber subscriber = findByUser(user);
		Optional<Subscription> subscr = subscriber.getSubscriptions().stream()
				.filter(s -> s.getCategory().getId().equals(category.getId())).findFirst();
		if (!subscr.isPresent()) {
			Subscription s = new Subscription();
			s.setSubscriber(subscriber);
			s.setCategory(category);
			subscriber.getSubscriptions().add(s);
			repository.save(subscriber);
		}
	};

}
