package skyglass.demo.service;

import java.io.Serializable;

import skyglass.data.model.ISecuredNameEntity;
import skyglass.demo.data.INameData;

public abstract class AbstractSecuredNameService<E extends ISecuredNameEntity<ID>, 
ID extends Serializable, D extends INameData<E, ID>> 
extends AbstractNameService<E, ID, D>  {
	
}
