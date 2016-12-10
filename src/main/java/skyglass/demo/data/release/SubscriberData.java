package skyglass.demo.data.release;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import skyglass.demo.data.model.release.Subscriber;
import skyglass.demo.data.model.security.User;

public interface SubscriberData extends JpaRepository<Subscriber, Long> {

    Optional<Subscriber> findByUser(User user);

}
