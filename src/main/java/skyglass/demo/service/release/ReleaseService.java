package skyglass.demo.service.release;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import skyglass.demo.data.release.ReleaseData;
import skyglass.demo.model.release.Category;
import skyglass.demo.model.release.Publisher;
import skyglass.demo.model.release.Release;
import skyglass.demo.model.release.Subscriber;
import skyglass.demo.model.release.Subscription;
import skyglass.demo.service.AbstractService;
import skyglass.demo.service.ServiceException;

@Service
public class ReleaseService extends AbstractService<Release, Long, ReleaseData> {
	
	@Autowired
	protected SubscriptionService subscriptionService;
	
	public static final String ROOT = "c://temp/upload";
	
	public static String getFileName(long publisherId, String uuid) {
		return getDirectory(publisherId) + "/" + uuid + ".pdf";
	}

	public static String getDirectory(long publisherId) {
		return ROOT + "/" + publisherId;
	}	
	
	private final static Logger log = Logger.getLogger(ReleaseService.class);
	
	public List<Release> findByPublisher(Publisher publisher) {
		Iterable<Release> releases = repository.findByPublisher(publisher);
		return StreamSupport.stream(releases.spliterator(), false).collect(Collectors.toList());
	}

	public List<Release> findBySubscriber(Subscriber subscriber) {
		List<Subscription> subscriptions = subscriptionService.findBySubscriber(subscriber);
		if (subscriptions != null) {
			List<Long> ids = new ArrayList<>(subscriptions.size());
			subscriptions.stream().forEach(s -> ids.add(s.getCategory().getId()));
			return repository.findByCategoryIdIn(ids);
		} else {
			return Collections.emptyList();
		}
	}

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
