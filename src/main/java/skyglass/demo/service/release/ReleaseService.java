package skyglass.demo.service.release;

import java.util.List;

import skyglass.demo.data.model.release.Category;
import skyglass.demo.data.model.release.Publisher;
import skyglass.demo.data.model.release.Release;
import skyglass.demo.data.model.release.Subscriber;
import skyglass.demo.data.release.ReleaseData;
import skyglass.demo.service.GenericService;
import skyglass.demo.service.error.ServiceException;

public interface ReleaseService extends GenericService<Release, Long, ReleaseData> {

	public List<Release> findBySubscriber(Subscriber subscriber);

	public List<Release> findByPublisher(Publisher publisher);
	
	public Release publish(Publisher publisher, Release release, Category category) throws ServiceException;

	public void unpublish(Publisher publisher, Release release) throws ServiceException;
	
	public static final String ROOT = "c://temp/upload";
	
	public static String getFileName(long publisherId, String uuid) {
		return getDirectory(publisherId) + "/" + uuid + ".pdf";
	}

	public static String getDirectory(long publisherId) {
		return ROOT + "/" + publisherId;
	}

}
