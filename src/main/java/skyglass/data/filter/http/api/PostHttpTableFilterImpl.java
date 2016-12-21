package skyglass.data.filter.http.api;

import javax.servlet.http.HttpServletRequest;

import skyglass.data.filter.IBaseTableFilter;
import skyglass.data.filter.api.AbstractFilterSpecification;
import skyglass.data.filter.http.AbstractBaseHttpTableFilter;
import skyglass.data.model.security.UserFactory;

public class PostHttpTableFilterImpl<T> extends AbstractBaseHttpTableFilter<T, PostHttpTableFilter<T>> implements PostHttpTableFilter<T> {

	public PostHttpTableFilterImpl(IBaseTableFilter<T, ?> filter, HttpServletRequest request, UserFactory userFactory, AbstractFilterSpecification filterUtils) {
		super(filter, request, userFactory, filterUtils);
	}

}
