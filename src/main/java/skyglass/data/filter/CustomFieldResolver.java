package skyglass.data.filter;

import org.hibernate.criterion.Criterion;

public interface CustomFieldResolver {
	
	public Criterion getCriteria();

}
