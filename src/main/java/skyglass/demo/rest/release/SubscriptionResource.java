package skyglass.demo.rest.release;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import skyglass.demo.model.release.Subscription;
import skyglass.demo.rest.AbstractResource;
import skyglass.demo.service.release.SubscriptionService;

@RestController
@RequestMapping("/rest/release/subscription")
public class SubscriptionResource extends AbstractResource<Subscription, Long, SubscriptionService> {

}
