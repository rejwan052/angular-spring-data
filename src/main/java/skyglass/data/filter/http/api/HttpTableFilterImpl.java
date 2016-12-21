package skyglass.data.filter.http.api;

import javax.servlet.http.HttpServletRequest;

import skyglass.data.filter.ITableFilter;
import skyglass.data.filter.api.AbstractFilterSpecification;
import skyglass.data.filter.http.AbstractHttpTableFilter;
import skyglass.data.model.security.UserFactory;

public class HttpTableFilterImpl<T> extends AbstractHttpTableFilter<T, HttpTableFilter<T>> implements HttpTableFilter<T>  {

	public HttpTableFilterImpl(ITableFilter<T, ?> filter, HttpServletRequest request, UserFactory userFactory, AbstractFilterSpecification filterUtils) {
		super(filter, request, userFactory, filterUtils);
	}

}
