package skyglass.demo.data.release;

import java.util.Optional;

import skyglass.demo.data.INameData;
import skyglass.demo.model.release.Publisher;
import skyglass.demo.model.security.User;

public interface PublisherData extends INameData<Publisher, Long> {

    Optional<Publisher> findByUser(User user);

}
