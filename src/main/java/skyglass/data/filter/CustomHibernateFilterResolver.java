package skyglass.data.filter;

import org.hibernate.Criteria;

public interface CustomHibernateFilterResolver extends CustomFilterResolver {
	
	public void addCustomFilter(Criteria criteria);

}
