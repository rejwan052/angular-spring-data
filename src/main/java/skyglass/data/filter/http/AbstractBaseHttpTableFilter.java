package skyglass.data.filter.http;

import javax.servlet.http.HttpServletRequest;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import skyglass.data.common.model.DataConstants;
import skyglass.data.common.util.StringUtil;
import skyglass.data.filter.IBaseTableFilter;
import skyglass.data.filter.api.AbstractFilterSpecification;
import skyglass.data.model.security.UserFactory;
import skyglass.data.query.QueryResult;

public abstract class AbstractBaseHttpTableFilter<T, F> extends AbstractBaseHttpTableFilterWrapper<T, F> 
	implements IBaseHttpTableFilter<T, F> {
	
	protected UserFactory userFactory;
	
	protected AbstractFilterSpecification filterSpecification;
	
	protected String searchQuery;
	
	public AbstractBaseHttpTableFilter(IBaseTableFilter<T, ?> filter, 
			HttpServletRequest request, UserFactory userFactory, AbstractFilterSpecification filterSpecification) {
		super(filter, request);	
		this.userFactory = userFactory;
		this.filterSpecification = filterSpecification;
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
	
	public <O> JSONObject getJsonResult(QueryEventHandler<T> handler) throws Exception {
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
