package skyglass.demo.service.release.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.apache.log4j.Logger;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import skyglass.demo.data.model.release.Category;
import skyglass.demo.data.model.release.Publisher;
import skyglass.demo.data.model.release.Release;
import skyglass.demo.data.model.release.Subscriber;
import skyglass.demo.data.model.release.Subscription;
import skyglass.demo.data.release.ReleaseData;
import skyglass.demo.service.GenericServiceImpl;
import skyglass.demo.service.error.ServiceException;
import skyglass.demo.service.release.ReleaseService;

@Service
public class ReleaseServiceImpl extends GenericServiceImpl<Release, Long, ReleaseData> 
			implements ReleaseService {
	
	private final static Logger log = Logger.getLogger(ReleaseServiceImpl.class);

	@Override
	public List<Release> findByPublisher(Publisher publisher) {
		Iterable<Release> releases = repository.findByPublisher(publisher);
		return StreamSupport.stream(releases.spliterator(), false).collect(Collectors.toList());
	}

	@Override
	public List<Release> findBySubscriber(Subscriber subscriber) {
		List<Subscription> subscriptions = subscriber.getSubscriptions();
		if (subscriptions != null) {
			List<Long> ids = new ArrayList<>(subscriptions.size());
			subscriptions.stream().forEach(s -> ids.add(s.getCategory().getId()));
			return repository.findByCategoryIdIn(ids);
		} else {
			return Collections.emptyList();
		}
	}

	@Override
	public Release publish(Publisher publisher, Release release, Category category) throws ServiceException {
		if(category == null) {
			throw new ServiceException("releasePublishError", "Category not found");
		}
		release.setPublisher(publisher);
		release.setCategory(category);
		try {
			return repository.save(release);
		} catch (DataIntegrityViolationException e) {
			throw new ServiceException("releasePublishError", e.getMessage());
		}
	}

	@Override
	public void unpublish(Publisher publisher, Release release) throws ServiceException {
		if (release == null) {
			throw new ServiceException("ReleaseService.unpublish", "Release doesn't exist");
		}
		String filePath = ReleaseService.getFileName(publisher.getId(), release.getUuid());
		File file = new File(filePath);
		if (file.exists()) {
			boolean deleted = file.delete();
			if (!deleted) {
				log.error("File " + filePath + " cannot be deleted");
			}
		}
		if (!release.getPublisher().getId().equals(publisher.getId())) {
			throw new ServiceException("ReleaseService.unpublish", "Release cannot be removed");
		}
		repository.delete(release);
	}	
	
}
