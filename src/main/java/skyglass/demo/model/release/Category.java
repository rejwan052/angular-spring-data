package skyglass.demo.model.release;

import javax.persistence.Entity;
import javax.persistence.Table;

import skyglass.data.model.AbstractSecuredNameEntity;

@Entity
@Table(name = "category")
public class Category extends AbstractSecuredNameEntity<Long> {

	private static final long serialVersionUID = -6083875106616190233L;

}
