package skyglass.demo.rest;

import java.io.IOException;
import java.io.Serializable;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import skyglass.demo.model.IdEntity;
import skyglass.demo.service.IGenericService;

public interface IGenericResource<E extends IdEntity<ID>, 
	ID extends Serializable, S extends IGenericService<E, ID, ?>> {
	
	public Iterable<E> getAllEntities();
	
	public ResponseEntity<E> saveEntity(@RequestBody E entity, HttpServletResponse response) throws IOException;
	
	public ResponseEntity<ID> deleteEntity(ID id); 

}
