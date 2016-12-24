package skyglass.demo.service;

import java.io.Serializable;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import skyglass.data.model.INameEntity;
import skyglass.data.query.QueryResult;
import skyglass.demo.data.INameData;
import skyglass.demo.service.filter.HttpFilterBuilder;

public abstract class AbstractNameService<E extends INameEntity<ID>, 
	ID extends Serializable, D extends INameData<E, ID>> 
	extends AbstractService<E, ID, D>
	implements IGenericNameService<E, ID, D> {
	
	@Autowired
	HttpFilterBuilder filterBuilder;
	
	@Override
	public QueryResult<E> findEntities(HttpServletRequest request) {
		return filterBuilder.jpaDataFilter(request, entityClass)
				.addHttpSearch("name")
				.getResult();
	}
	
	@Override
	@Transactional
	public E save(E entity) throws ServiceException {
    	if (entity.getId() != null) {
    		E oldEntity = findOne(entity.getId());
    		if (!oldEntity.getName().equals(entity.getName())) {
    			checkNameExists(entity);
    		}
    	} else {
			checkNameExists(entity);    		
    	}
    	return super.save(entity);
	}
	
	private void checkNameExists(E entity) throws ServiceException {
		E test = repository.findByName(entity.getName());
		if (test != null) {
	        throw new ServiceException("saveEntityError",
	        		"Entity with the name '" + entity.getName() + "' already exists");
		}		
	}

}
