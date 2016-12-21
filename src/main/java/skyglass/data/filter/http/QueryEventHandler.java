package skyglass.data.filter.http;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import skyglass.data.query.QueryResult;

public abstract class QueryEventHandler<T> {
	
	public void doBeforeJson(QueryResult<T> resultData) {
		
	}
	
	public void doAfterJson(QueryResult<T> queryResult, JSONObject result) throws JSONException {
		
	}
	
	public JSONArray createCustomJsonArray(QueryResult<T> queryResult) throws Exception {
		return null;
	}

}
