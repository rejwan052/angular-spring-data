package skyglass.demo.service.filter;

import skyglass.data.filter.JunctionType;
import skyglass.data.filter.api.AbstractFilterSpecification;
import skyglass.data.filter.api.AbstractHibernateTableFilter;

public class HibernateTableFilter<T> extends AbstractHibernateTableFilter<T> {
	
    public HibernateTableFilter(Class<?> clazz, JunctionType junctionType, AbstractFilterSpecification filterSpecification) {
        super(clazz, junctionType, filterSpecification);
    }   

}
