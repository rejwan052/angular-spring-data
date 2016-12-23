package skyglass.demo.rest;

import java.io.Serializable;

import skyglass.data.model.ISecuredEntity;
import skyglass.demo.service.IGenericService;

public abstract class AbstractSecuredResource<E extends ISecuredEntity<ID>, ID extends Serializable,
	S extends IGenericService<E, ID, ?>>  extends AbstractResource<E, ID, S> {


}
