package skyglass.data.filter.http;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import skyglass.data.filter.IBaseTableFilter;

public interface IBaseHttpTableFilter<T, F> extends IBaseTableFilter<T, F> {
	
	public JSONObject getJsonResult() throws Exception;
	
	public <O> JSONObject getJsonResult(QueryEventHandler<T> handler) throws Exception;
	
	public JSONObject getEmptyJsonResult() throws JSONException;
    
    public F addHttpPostSearch(String... fieldNames);
    
    public F addPostStatuses();
    
    public <R> F addPostStatuses(ArrayNormalizer<R> normalizer);

}
