package skyglass.demo.rest;

import java.io.IOException;
import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import skyglass.data.model.IdEntity;
import skyglass.data.query.QueryResult;
import skyglass.demo.service.IGenericService;
import skyglass.demo.service.ServiceException;
import skyglass.demo.service.filter.HttpFilterBuilder;
import skyglass.demo.utils.rest.RestUtils;

public abstract class AbstractResource<E extends IdEntity<ID>, ID extends Serializable,
	S extends IGenericService<E, ID, ?>> implements IGenericResource<E, ID, S> {
	
    @Autowired
    protected S service;
    
    @Autowired
    protected HttpFilterBuilder filterBuilder;  
    
    @RequestMapping(method = RequestMethod.GET)
    @PreAuthorize("hasAuthority('SECURITY')")
    public QueryResult<E> findEntities(HttpServletRequest request) {
    	return service.findEntities(request);
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
    
    @RequestMapping(method = RequestMethod.POST, path ="/saveAll")
    @PreAuthorize("hasAuthority('SECURITY_WRITER')")
    public ResponseEntity<String> saveAll(
    		@RequestBody EntitiesWrapper<ID> entitiesWrapper, HttpServletResponse response)
    				throws IOException, ServiceException {
    	service.saveAll(entitiesWrapper.ids);
        return new ResponseEntity<String>("", HttpStatus.OK);

    }     
    
    @RequestMapping(method = RequestMethod.DELETE, path  = "/{id}")
    @PreAuthorize("hasAuthority('SECURITY_WRITER')")
    public ResponseEntity<ID> deleteEntity(@PathVariable ID id, HttpServletResponse response) throws IOException {
    	try {
        	service.delete(id);
            return new ResponseEntity<ID>(id, HttpStatus.OK);		
    	} catch (ServiceException se) {
    		RestUtils.sendError(response, se.getError());
    		return null;
    	}

    } 
    
    protected static class EntitiesWrapper<ID> {
		public ID[] ids;
    }

}
