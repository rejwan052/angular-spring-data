package skyglass.data.model;

import java.io.Serializable;

public interface INameEntity<ID extends Serializable> extends IdEntity<ID> {
	
	public String getName();

}
