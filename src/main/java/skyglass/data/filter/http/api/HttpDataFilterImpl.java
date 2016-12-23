package skyglass.data.filter.http.api;

import javax.servlet.http.HttpServletRequest;

import skyglass.data.filter.IDataFilter;
import skyglass.data.filter.api.IFilterHelper;
import skyglass.data.filter.http.AbstractHttpDataFilter;

public class HttpDataFilterImpl<T> extends AbstractHttpDataFilter<T, HttpDataFilter<T>> implements HttpDataFilter<T>  {

	public HttpDataFilterImpl(IDataFilter<T, ?> filter, HttpServletRequest request, IFilterHelper filterHelper) {
		super(filter, request, filterHelper);
	}

}
