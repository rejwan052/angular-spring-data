package skyglass.demo.service.release;

import skyglass.demo.data.model.release.Publisher;
import skyglass.demo.data.model.security.User;
import skyglass.demo.data.release.PublisherData;
import skyglass.demo.service.GenericService;

public interface PublisherService extends GenericService<Publisher, Long, PublisherData> {

	public Publisher findByUser(User user);
	
}