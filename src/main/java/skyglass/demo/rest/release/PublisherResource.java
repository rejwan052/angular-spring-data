package skyglass.demo.rest.release;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import skyglass.demo.model.release.Publisher;
import skyglass.demo.rest.AbstractResource;
import skyglass.demo.service.release.PublisherService;

@RestController
@RequestMapping("/rest/release/publisher")
public class PublisherResource extends AbstractResource<Publisher, Long, PublisherService> {



}