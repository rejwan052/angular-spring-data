package skyglass.demo.service.release;

import skyglass.demo.data.model.release.Category;
import skyglass.demo.data.model.release.Subscriber;
import skyglass.demo.data.model.security.User;
import skyglass.demo.data.release.SubscriberData;
import skyglass.demo.service.GenericService;

public interface SubscriberService extends GenericService<Subscriber, Long, SubscriberData> {

	public Subscriber findByUser(User user);
	
	public void subscribe(User user, Category category);

}

