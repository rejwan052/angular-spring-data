package skyglass.demo.data.release;

import java.util.Optional;

import skyglass.demo.data.INameData;
import skyglass.demo.model.release.Subscriber;
import skyglass.demo.model.security.User;

public interface SubscriberData extends INameData<Subscriber, Long> {

    Optional<Subscriber> findByUser(User user);

}
