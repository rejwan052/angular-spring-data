package skyglass.demo.service.release.impl;

import org.springframework.stereotype.Service;

import skyglass.demo.data.model.release.Publisher;
import skyglass.demo.data.model.security.User;
import skyglass.demo.data.release.PublisherData;
import skyglass.demo.service.GenericServiceImpl;
import skyglass.demo.service.release.PublisherService;

@Service
public class PublisherServiceImpl extends GenericServiceImpl<Publisher, Long, PublisherData> 
	implements PublisherService {

	@Override
	public Publisher findByUser(User user) {
		return repository.findOne(user.getId());
	}	



}
