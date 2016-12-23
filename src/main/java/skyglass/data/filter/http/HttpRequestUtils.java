package skyglass.data.filter.http;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import skyglass.data.common.model.DataConstants;
import skyglass.data.filter.api.IFilterHelper;

public class HttpRequestUtils {
    public static Boolean getBooleanParamValue(HttpServletRequest request, String paramName) {
    	String result = getStringParamValue(request, paramName);
    	if (result != null) {
    		return Boolean.valueOf(result);
    	}
    	return null;
    }
    
    public static UUID getUUIDParamValue(HttpServletRequest request, String paramName) {
    	String result = getStringParamValue(request, paramName);
    	if (result != null) {
    		return UUID.fromString(result);
    	}
    	return null;
    }
    
    public static Integer getIntegerParamValue(HttpServletRequest request, String paramName) {
        return getIntegerParamValue(request, paramName, null);
    }

    public static Integer getIntegerParamValue(HttpServletRequest request, String paramName, Integer defValue) {
        String result = getStringParamValue(request, paramName);
        if (result != null) {
            try {
                return Integer.parseInt(result);
            } catch (NumberFormatException e) {
                return defValue;
            }
        }
        return defValue;
    }
    
    public static String getStringParamValue(HttpServletRequest request, String paramName) {
	    String[] paramValues = getStringParamValues(request, paramName);
	    if (paramValues != null && paramValues.length > 0) { 
	    	return paramValues[0].trim().equals("") ? null : paramValues[0];
	    }
	    return null;
    } 
    
    public static String getRegexpParamValue(HttpServletRequest request) {
        String searchQuery = HttpRequestUtils.getStringParamValue(request, DataConstants.SEARCH_QUERY);
    	if (searchQuery != null) {
    		searchQuery = IFilterHelper.convertToRegexp(searchQuery);
    	} 
    	return searchQuery;
    }     
    
    public static String[] getStringParamValues(HttpServletRequest request, String paramName) {
	    return request.getParameterValues(paramName);
    }     
}
