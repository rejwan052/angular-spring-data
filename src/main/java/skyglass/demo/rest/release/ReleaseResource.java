package skyglass.demo.rest.release;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.Principal;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.StreamSupport;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import skyglass.demo.model.release.Category;
import skyglass.demo.model.release.Publisher;
import skyglass.demo.model.release.Release;
import skyglass.demo.model.release.Subscriber;
import skyglass.demo.model.release.Subscription;
import skyglass.demo.model.security.User;
import skyglass.demo.rest.AbstractResource;
import skyglass.demo.service.Error;
import skyglass.demo.service.release.CategoryService;
import skyglass.demo.service.release.PublisherService;
import skyglass.demo.service.release.ReleaseService;
import skyglass.demo.service.release.SubscriberService;
import skyglass.demo.service.release.SubscriptionService;
import skyglass.demo.utils.rest.RestUtils;

@RestController
@RequestMapping("/rest/release/release")
public class ReleaseResource extends AbstractResource<Release, Long, ReleaseService> {
	
	@Autowired
	private PublisherService publisherService;

	@Autowired
	private SubscriberService subscriberService;
	
	private SubscriptionService subscriptionService;
	
	@Autowired
	private CategoryService categoryService;

	@SuppressWarnings("rawtypes")
	@ResponseBody
	@RequestMapping(value = "/view/{id}", method = RequestMethod.GET, produces = "application/pdf")
	public ResponseEntity view(@AuthenticationPrincipal Principal principal, @PathVariable("id") Long id)
			throws IOException {
		Release release = service.findOne(id);
		Category category = release.getCategory();
		User user = (User) ((Authentication) principal).getPrincipal();
		Subscriber subscriber = subscriberService.findOne(user.getId());
		Iterable<Subscription> subscriptions = subscriptionService.findBySubscriber(subscriber);
		Optional<Subscription> subscription = StreamSupport.stream(subscriptions.spliterator(), false)
				.filter(s -> s.getCategory().getId().equals(category.getId())).findFirst();
		if (subscription.isPresent() || release.getPublisher().getId().equals(user.getId())) {
			File file = new File(ReleaseService.getFileName(release.getPublisher().getId(), release.getUuid()));
			InputStream in = new FileInputStream(file);
			return ResponseEntity.ok(IOUtils.toByteArray(in));
		} else {
			return ResponseEntity.notFound().build();
		}

	}
	
	
	@RequestMapping(method = RequestMethod.POST, value = "/publish")
	@PreAuthorize("hasRole('PUBLISHER')")
	public ResponseEntity<Release> publish(@RequestParam("name") String name, @RequestParam("category")Long categoryId, @RequestParam("file") MultipartFile file,
			RedirectAttributes redirectAttributes, @AuthenticationPrincipal Principal principal,
			HttpServletResponse response) throws IOException {
		
		Category category = categoryService.findOne(categoryId);

		User user = (User) ((Authentication) principal).getPrincipal();
		Publisher publisher = publisherService.findByUser(user);

		String uuid = UUID.randomUUID().toString();
		File dir = new File(ReleaseService.getDirectory(publisher.getId()));
		createDirectoryIfNotExist(dir);

		File f = new File(ReleaseService.getFileName(publisher.getId(), uuid));
		Release release = null;
		if (!file.isEmpty()) {
			try {
				BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(f));
				FileCopyUtils.copy(file.getInputStream(), stream);
				stream.close();
				release = new Release();
				release.setUuid(uuid);
				release.setName(name);
				service.publish(publisher, release, category);
			} catch (Exception e) {
	    		RestUtils.sendError(response, new Error(
	    				"ReleaseController.publish", 
	    				"You failed to publish " + name + " => " + e.getMessage()));
	    		return null;
			}
		} else {
    		RestUtils.sendError(response, new Error(
    				"ReleaseController.publish", 
    				"You failed to upload " + name + " because the file was empty"));
    		return null;
		}

        return new ResponseEntity<Release>(release, HttpStatus.OK);
	}

	private boolean createDirectoryIfNotExist(File dir) {
		if (!dir.exists()) {
			boolean created = dir.mkdirs();
			if (!created) {
				return false;
			}
		}
		return true;
	}	
}
