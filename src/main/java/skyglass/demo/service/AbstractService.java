package skyglass.demo.service;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;

public class AbstractService<E, ID extends Serializable, R extends CrudRepository<E, ID>> 
	implements IGenericService<E, ID, R> {

    @Autowired
    protected R repository;

    protected Class<E> entityClass;

    @SuppressWarnings("unchecked")
    public AbstractService() {
        ParameterizedType genericSuperclass = (ParameterizedType) getClass().getGenericSuperclass();
        this.entityClass = (Class<E>) genericSuperclass.getActualTypeArguments()[0];
    }

	@Override
    public E findOne(ID id) {
        return repository.findOne(id);
    }

	@Override
    public Iterable<E> findAll() {
		return repository.findAll();
    }
    
	@Override
    public E save(E entity) throws ServiceException {
        return repository.save(entity);
    }

	@Override
	public boolean exists(ID id) {
		return repository.exists(id);
	}
	
	@Override
	public void delete(E entity) {
		repository.delete(entity);
	}
	
	@Override
	public void delete(ID id) {
		repository.delete(id);
	}

}
