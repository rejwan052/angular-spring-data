package skyglass.data.model;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class AbstractEntity<ID extends Serializable> implements Serializable, IdEntity<ID> {
	private static final long serialVersionUID = 5106608691871651682L;
	
	@Id
    @GeneratedValue
    private ID id;
	
	@Override
    public ID getId() { 
		return id; 
	}
	
    public void setId(ID id) { 
    	this.id = id; 
    }

    @Override
    public int hashCode() {
        int hash;

        if (getId() != null) {
            hash = 1;
            hash = hash * 31 + getClass().hashCode();
            hash = hash * 31 + getId().hashCode();
        }
        else {
            hash = super.hashCode();
        }

        return hash;
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "#" + getId();
    }
}
