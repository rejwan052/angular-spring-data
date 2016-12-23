package skyglass.data.filter.http;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import skyglass.data.filter.IBaseDataFilter;

public interface IBaseHttpDataFilter<T, F> extends IBaseDataFilter<T, F> {
	
	public JSONObject getJsonResult() throws Exception;
	
	public JSONObject getJsonResult(QueryEventHandler<T> handler) throws Exception;
	
	public JSONObject getEmptyJsonResult() throws JSONException;
    
    public F addHttpPostSearch(String... fieldNames);
    
    public F addPostStatuses();
    
    public <R> F addPostStatuses(ArrayNormalizer<R> normalizer);

}
