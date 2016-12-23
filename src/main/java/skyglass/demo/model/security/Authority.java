package skyglass.demo.model.security;

import javax.persistence.Entity;
import javax.persistence.Table;

import skyglass.data.model.AbstractNameEntity;


@Entity
@Table(name = "authority")
public class Authority extends AbstractNameEntity<Long> {

	private static final long serialVersionUID = 4013338954919148047L;


}
