package skyglass.demo.service;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import skyglass.data.query.QueryResult;
import skyglass.demo.service.filter.HttpFilterBuilder;

@Transactional(readOnly = true)
public abstract class AbstractService<E, ID extends Serializable, R extends CrudRepository<E, ID>> 
	implements IGenericService<E, ID, R> {

    @Autowired
    protected R repository;
    
	@Autowired
	HttpFilterBuilder filterBuilder;

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
    public Iterable<E> findAll(ID[] ids) {
		return repository.findAll(Arrays.asList(ids));
    }
    
	@Override
	@Transactional
    public E save(E entity) throws ServiceException {
        return repository.save(entity);
    }
	
	@Override
	@Transactional
	public void saveAll(ID[] entityIds) {
		repository.save(repository.findAll(Arrays.asList(entityIds)));
	}	

	@Override
	public boolean exists(ID id) {
		return repository.exists(id);
	}
	
	@Override
	@Transactional
	public void delete(E entity) throws ServiceException {
		repository.delete(entity);
	}
	
	@Override
	@Transactional
	public void delete(ID id) throws ServiceException {
		repository.delete(id);
	}
	
	@Override
	public QueryResult<E> findEntities(HttpServletRequest request) {
		return filterBuilder.jpaDataFilter(request, entityClass)
				.getResult();
	}

}
