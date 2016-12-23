package skyglass.data.filter.http.api;

import javax.servlet.http.HttpServletRequest;

import skyglass.data.filter.IBaseDataFilter;
import skyglass.data.filter.api.IFilterHelper;
import skyglass.data.filter.http.AbstractBaseHttpDataFilter;

public class HttpPostDataFilterImpl<T> extends AbstractBaseHttpDataFilter<T, HttpPostDataFilter<T>> implements HttpPostDataFilter<T> {

	public HttpPostDataFilterImpl(IBaseDataFilter<T, ?> filter, HttpServletRequest request, IFilterHelper filterUtils) {
		super(filter, request, filterUtils);
	}

}
