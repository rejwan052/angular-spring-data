package skyglass.demo.service.filter;

import static skyglass.data.common.model.DataConstants.SEARCH_QUERY;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import skyglass.data.common.model.DataConstants;
import skyglass.data.common.util.StringUtil;
import skyglass.data.filter.IBaseDataFilter;
import skyglass.data.filter.JunctionType;
import skyglass.data.filter.OrderType;
import skyglass.data.filter.api.DataFilter;
import skyglass.data.filter.api.IPostDataFilter;
import skyglass.data.filter.api.JpaDataFilter;
import skyglass.data.filter.http.HttpRequestUtils;
import skyglass.data.filter.http.api.HttpDataFilter;
import skyglass.data.filter.http.api.HttpDataFilterImpl;
import skyglass.data.filter.http.api.HttpPostDataFilter;
import skyglass.data.filter.http.api.HttpPostDataFilterImpl;
import skyglass.data.filter.http.api.PermissionType;
import skyglass.data.model.ISecuredEntity;

@Service("httpFilterBuilder")
public class HttpFilterBuilder {
	
	@Autowired
	protected FilterBuilder filterBuilder;
	
	@Autowired
	protected FilterHelper filterHelper;

	public <T extends ISecuredEntity<?>> HttpDataFilter<T> securedJpaDataFilter(
			HttpServletRequest request, Class<T> clazz, String ... searchFields) {
	    HttpDataFilter<T> filter = securedJpaDataFilter(request, clazz);
		if (searchFields.length == 0) {
			return filter;
		}
	    String searchQuery = HttpRequestUtils.getStringParamValue(request, SEARCH_QUERY);
	    if (!StringUtil.isEmpty(searchQuery)){
	        for (String searchField : searchFields){
	            filter.addPostSearch(searchQuery, searchField);
	        }
	    }
	    return filter;
	}
	
	public <T extends ISecuredEntity<?>> HttpDataFilter<T> securedJpaDataFilter(
			HttpServletRequest request, Class<T> clazz) {
		return securedJpaDataFilter(request, clazz, JunctionType.AND);
	}	

	public <T extends ISecuredEntity<?>> HttpDataFilter<T> securedJpaDataFilter(
			HttpServletRequest request, Class<T> clazz, JunctionType junctionType) {
		PermissionType permissionType = PermissionType.Read;
		if (HttpRequestUtils.getBooleanParamValue(request, DataConstants.EXECUTE) != null) {
			permissionType = PermissionType.Execute;
		}	
	    DataFilter<T> filter = filterBuilder.securedJpaDataFilter(clazz, junctionType, permissionType);
	    initDataFilter(filter, request);
	    return new HttpDataFilterImpl<T>(filter, request, filterHelper);
	}

	public <T> HttpPostDataFilter<T> postDataFilter(HttpServletRequest request, Iterable<T> fullResult, JunctionType junctionType) {
		IPostDataFilter<T> dataFilter = filterBuilder.postDataFilter(fullResult, junctionType);
		initDataFilter(dataFilter, request);
		return new HttpPostDataFilterImpl<T>(dataFilter, request, filterHelper); 
	}

	public <T> HttpDataFilter<T> jpaDataFilter(HttpServletRequest request, Class<T> clazz, JunctionType junctionType) {
		DataFilter<T> dataFilter = new JpaDataFilter<T>(clazz, junctionType, filterHelper);
	    initDataFilter(dataFilter, request); 
	    return new HttpDataFilterImpl<T>(dataFilter, request, filterHelper);
	}

	public <T> HttpDataFilter<T> jpaDataFilter(HttpServletRequest request, Class<T> clazz) {
		return jpaDataFilter(request, clazz, JunctionType.AND);
	}
	
	private static <T> void initDataFilter(IBaseDataFilter<T, ?> dataFilter, HttpServletRequest request) {
		if (request != null) {
	        String rowsPerPageString = request.getParameter(FilterBuilder.ROWS_PER_PAGE_QUERY_PARAM);
	        if (rowsPerPageString != null) {
	            String pageNumberString = request.getParameter(FilterBuilder.PAGE_NUMBER_QUERY_PARAM);
	            if (pageNumberString != null) {
	                dataFilter.setPaging(Integer.valueOf(rowsPerPageString), Integer.valueOf(pageNumberString));
	            }        	
	        }	        
    
	        String orderField = request.getParameter(FilterBuilder.ORDER_FIELD_QUERY_PARAM);
	        if (orderField != null) {
		        OrderType orderType = OrderType.ASC;
		        String sortTypeString = request.getParameter(FilterBuilder.SORT_TYPE_QUERY_PARAM);
		        if (sortTypeString != null && sortTypeString.equals("desc")) {
		            orderType = OrderType.DESC;
		        }	        	
		        dataFilter.setOrder(orderField, orderType);	        	
	        }
		}
	}

}
