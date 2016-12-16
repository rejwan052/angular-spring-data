package skyglass.demo.rest;

import java.io.IOException;
import java.io.Serializable;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import skyglass.demo.model.IdEntity;
import skyglass.demo.service.IGenericService;
import skyglass.demo.service.ServiceException;
import skyglass.demo.utils.rest.RestUtils;

public class AbstractResource<E extends IdEntity<ID>, ID extends Serializable,
	S extends IGenericService<E, ID, ?>> {
	
    @Autowired
    protected S service;
	
    @RequestMapping(method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('SECURITY')")
    public Iterable<E> getAllEntities() {
        return service.findAll();
    }
    
    @RequestMapping(method = RequestMethod.POST)
    @PreAuthorize("hasAuthority('SECURITY_WRITER')")
    public ResponseEntity<E> saveEntity(@RequestBody E entity, HttpServletResponse response) throws IOException {
    	try {
            return new ResponseEntity<E>(service.save(entity), HttpStatus.OK);    		
    	} catch (ServiceException se) {
    		RestUtils.sendError(response, se.getError());
    		return null;
    	}

    } 
    
    @RequestMapping(method = RequestMethod.DELETE, path  = "/{id}")
    @PreAuthorize("hasAuthority('SECURITY_WRITER')")
    public ResponseEntity<ID> deleteEntity(@PathVariable ID id) {
    	service.delete(id);
        return new ResponseEntity<ID>(id, HttpStatus.OK);
    } 

}
