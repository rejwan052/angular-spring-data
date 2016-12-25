package skyglass.demo.service;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

import org.springframework.data.repository.Repository;

import skyglass.data.query.QueryResult;

public interface IGenericService <E, ID extends Serializable, R extends Repository<E, ID>> {
	
    Iterable<E> findAll();
    
    Iterable<E> findAll(ID[] ids);
    
    E findOne(ID id);
    
    QueryResult<E> findEntities(HttpServletRequest request);
    
    E save(E entity) throws ServiceException;
    
    void saveAll(ID[] ids) throws ServiceException;
    
    boolean exists(ID id);
    
    void delete(E entity) throws ServiceException;
    
    void delete(ID id) throws ServiceException;

}
