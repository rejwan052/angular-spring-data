package skyglass.data.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public class AbstractNameEntity<ID extends Serializable> extends AbstractEntity<ID> implements INameEntity<ID> {
	
	private static final long serialVersionUID = -6711889725534266076L;
	
	@Column(name = "name", nullable = false)
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}	

}
