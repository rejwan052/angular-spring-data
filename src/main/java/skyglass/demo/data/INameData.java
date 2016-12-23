package skyglass.demo.data;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import skyglass.data.model.INameEntity;

@NoRepositoryBean
public interface INameData<E extends INameEntity<ID>, ID extends Serializable> 
	 extends JpaRepository<E, ID> {
	
	public E findByName(String name);

}
