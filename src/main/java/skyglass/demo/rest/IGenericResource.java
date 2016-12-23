package skyglass.demo.rest;

import java.io.IOException;
import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import skyglass.data.model.IdEntity;
import skyglass.data.query.QueryResult;
import skyglass.demo.service.IGenericService;

public interface IGenericResource<E extends IdEntity<ID>, 
	ID extends Serializable, S extends IGenericService<E, ID, ?>> {
	
	public QueryResult<E> findEntities(HttpServletRequest request);
	
	public ResponseEntity<E> saveEntity(@RequestBody E entity, HttpServletResponse response) throws IOException;
	
	public ResponseEntity<ID> deleteEntity(ID id); 

}
