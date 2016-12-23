package skyglass.demo.service.release;

import org.springframework.stereotype.Service;

import skyglass.demo.data.release.PublisherData;
import skyglass.demo.model.release.Publisher;
import skyglass.demo.model.security.User;
import skyglass.demo.service.AbstractNameService;

@Service
public class PublisherService extends AbstractNameService<Publisher, Long, PublisherData> {
	
	public Publisher findByUser(User user) {
		return repository.findOne(user.getId());
	}

	@Override
	public Class<Publisher> getEntityClass() {
		return Publisher.class;
	}	

}
