package skyglass.demo.model.release;

import javax.persistence.Entity;
import javax.persistence.Table;

import skyglass.demo.model.AbstractNameEntity;

@Entity
@Table(name = "domain")
public class Category extends AbstractNameEntity<Long> {

	private static final long serialVersionUID = -6083875106616190233L;

}
