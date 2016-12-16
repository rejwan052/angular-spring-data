package skyglass.demo.data.release;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import skyglass.demo.model.release.Subscriber;
import skyglass.demo.model.release.Subscription;

public interface SubscriptionData extends JpaRepository<Subscription, Long> {
	
    public Collection<Subscription> findBySubscriber(Subscriber subscriber);

    public List<Subscriber> findByCategoryIdIn(List<Long> ids);

}
