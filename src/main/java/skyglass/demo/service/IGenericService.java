package skyglass.demo.service;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.repository.Repository;

import skyglass.data.query.QueryResult;

public interface IGenericService <E, ID extends Serializable, R extends Repository<E, ID>> {
	
	Class<E> getEntityClass();
	
    Iterable<E> findAll();
    
    E findOne(ID id);
    
    QueryResult<E> findEntities(HttpServletRequest request);
    
    E save(E entity) throws ServiceException;
    
    boolean exists(ID id);
    
    void delete(E entity);
    
    void delete(ID id);

}
