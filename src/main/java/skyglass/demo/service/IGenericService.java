package skyglass.demo.service;

import java.io.Serializable;

import org.springframework.data.repository.Repository;

public interface IGenericService <E, ID extends Serializable, R extends Repository<E, ID>> {
    
    E findOne(ID id);
    
    Iterable<E> findAll();
    
    E save(E entity) throws ServiceException;
    
    boolean exists(ID id);
    
    void delete(E entity);
    
    void delete(ID id);

}
