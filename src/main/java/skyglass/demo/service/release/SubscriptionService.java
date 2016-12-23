package skyglass.demo.service.release;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.stereotype.Service;

import skyglass.demo.data.release.SubscriptionData;
import skyglass.demo.model.release.Category;
import skyglass.demo.model.release.Subscriber;
import skyglass.demo.model.release.Subscription;
import skyglass.demo.service.AbstractService;

@Service
public class SubscriptionService extends AbstractService<Subscription, Long, SubscriptionData> {

	public List<Subscription> findBySubscriber(Subscriber subscriber) {
		Iterable<Subscription> subscriptions = repository.findBySubscriber(subscriber);
		return StreamSupport.stream(subscriptions.spliterator(), false).collect(Collectors.toList());
	}
	
	public void subscribe(Subscriber subscriber, Category category) {
		Optional<Subscription> subscr = findBySubscriber(subscriber).stream()
				.filter(s -> s.getCategory().getId().equals(category.getId())).findFirst();
		if (!subscr.isPresent()) {
			Subscription s = new Subscription();
			s.setSubscriber(subscriber);
			s.setCategory(category);
			repository.save(s);
		}
	}

	@Override
	public Class<Subscription> getEntityClass() {
		return Subscription.class;
	}

}
