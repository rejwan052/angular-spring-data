package skyglass.demo.rest.release;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import skyglass.demo.model.release.Publisher;
import skyglass.demo.rest.AbstractResource;
import skyglass.demo.service.ServiceException;
import skyglass.demo.service.release.PublisherService;

@RestController
@RequestMapping("/rest/release/publisher")
public class PublisherResource extends AbstractResource<Publisher, Long, PublisherService> {
	
	@Override
    @RequestMapping(method = RequestMethod.POST, path ="/saveAll")
    @PreAuthorize("hasAuthority('SECURITY_WRITER')")
    public ResponseEntity<String> saveAll(
    		@RequestBody EntitiesWrapper<Long> entitiesWrapper, HttpServletResponse response)
    				throws IOException, ServiceException {
    	service.saveAll(entitiesWrapper.ids);
        return new ResponseEntity<String>("{}", HttpStatus.OK);

    }  	

}