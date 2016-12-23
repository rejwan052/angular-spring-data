package skyglass.data.filter;

import org.hibernate.Criteria;

public interface CustomJpaFilterResolver extends CustomFilterResolver {
	
	public void addCustomFilter(Criteria criteria);

}
