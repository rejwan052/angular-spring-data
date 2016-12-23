package skyglass.data.filter.http;

import javax.servlet.http.HttpServletRequest;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import skyglass.data.common.model.DataConstants;
import skyglass.data.common.util.StringUtil;
import skyglass.data.filter.IBaseDataFilter;
import skyglass.data.filter.api.IFilterHelper;
import skyglass.data.query.QueryResult;

public abstract class AbstractBaseHttpDataFilter<T, F> extends AbstractBaseHttpDataFilterWrapper<T, F> 
	implements IBaseHttpDataFilter<T, F> {
	
	protected IFilterHelper filterHelper;
	
	protected String searchQuery;
	
	public AbstractBaseHttpDataFilter(IBaseDataFilter<T, ?> filter, 
			HttpServletRequest request, IFilterHelper filterHelper) {
		super(filter, request);	
		this.filterHelper = filterHelper;
		parseSearchQuery(request);
	}	
	
	private void parseSearchQuery(HttpServletRequest request) {
		this.searchQuery = HttpRequestUtils.getStringParamValue(request, DataConstants.SEARCH_QUERY);	
		if (StringUtil.isEmpty(this.searchQuery)) {
			this.searchQuery = null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public F addHttpPostSearch(String... fieldNames) {
        if (searchQuery != null) {
       		addPostSearch(searchQuery, fieldNames);  
        }
        return (F)this;
	}	
	
	public JSONObject getJsonResult() throws Exception {
		return getJsonResult(null);
	}
	
	public JSONObject getEmptyJsonResult() throws JSONException {
        JSONObject result = new JSONObject();
        result.put(DataConstants.TOTAL_RECORDS, 0);
        result.put(DataConstants.RECORDS, new JSONArray());
        return result;		
	}
	
	public JSONObject getJsonResult(QueryEventHandler<T> handler) throws Exception {
		QueryResult<T> queryResult = getResult();
		if (handler != null) {
			handler.doBeforeJson(queryResult);
		}
        JSONObject result = new JSONObject();        
        result.put(DataConstants.TOTAL_RECORDS, queryResult.getTotalRecords());
        result.put(DataConstants.RECORDS, queryResult.getResults()); 
		if (handler != null) {
			handler.doAfterJson(queryResult, result);
		} 
        return result; 
	}
	
    @SuppressWarnings("unchecked")
    public F addPostStatuses() {
    	addPostField("status");
        String[] statuses = HttpRequestUtils.getStringParamValues(request, "status");
        if (statuses != null) {
            addPostFilters("status", statuses);        	
        } 
        return (F)this;
    }  
    
    @SuppressWarnings("unchecked")
    public <R> F addPostStatuses(ArrayNormalizer<R> normalizer) {
    	addPostField("status");
        String[] statuses = HttpRequestUtils.getStringParamValues(request, "status");
        if (statuses != null) {
        	R[] result = normalizer.normalizeArray(statuses);
            addPostFilters("status", result);        	
        } 
        return (F)this;
    }  	
	

}
