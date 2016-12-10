package skyglass.demo.data.release;

import org.springframework.data.jpa.repository.JpaRepository;

import skyglass.demo.data.model.release.Publisher;
import skyglass.demo.data.model.security.User;

import java.util.Optional;

public interface PublisherData extends JpaRepository<Publisher, Long> {

    Optional<Publisher> findByUser(User user);

}
