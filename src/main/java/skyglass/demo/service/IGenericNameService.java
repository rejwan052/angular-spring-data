package skyglass.demo.service;

import java.io.Serializable;

import org.springframework.data.repository.CrudRepository;

import skyglass.data.model.INameEntity;

public interface IGenericNameService<E extends INameEntity<ID>, 
	ID extends Serializable, D extends CrudRepository<E, ID>> extends IGenericService<E, ID, D> {

}
