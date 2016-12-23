package skyglass.data.model;

import java.io.Serializable;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class AbstractSecuredNameEntity<ID extends Serializable> extends AbstractNameEntity<ID> implements ISecuredNameEntity<ID> {

	private static final long serialVersionUID = 5078698635877747298L;

}
