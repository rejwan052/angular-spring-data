package skyglass.demo.model;

import java.io.Serializable;

public interface IdEntity<ID extends Serializable> {
	
	public ID getId();

}
