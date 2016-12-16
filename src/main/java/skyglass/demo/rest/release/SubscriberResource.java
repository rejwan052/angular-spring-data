package skyglass.demo.rest.release;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import skyglass.demo.model.release.Subscriber;
import skyglass.demo.rest.AbstractResource;
import skyglass.demo.service.release.SubscriberService;

@RestController
@RequestMapping("/rest/release/subscriber")
public class SubscriberResource extends AbstractResource<Subscriber, Long, SubscriberService> {

}
