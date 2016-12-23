package skyglass.data.filter.api;

import java.util.Set;

import javax.persistence.EntityManager;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.dialect.Dialect;

import skyglass.data.filter.http.api.PermissionType;

public interface IFilterHelper {
	
	public EntityManager getEntityManager();
	
	public Session getSession();
	
	public Criteria createRootCriteria(Class<?> clazz);
	
	public Dialect getDialect();
	
	public Object convertObject(Class<?> rootClass, String property, Object value, boolean isCollection);
	
	public Set<Long> getExpectedActionIds(Class<?> clazz, PermissionType permissionType);
	
	public Long getCurrentUserId();
	
	public void addCustomSecurityResolver(Criteria criteria, String alias, 
    		Class<?> clazz, PermissionType permissionType);	
	
	public boolean isNumericField(Class<?> rootClass, String propertyName);
	
    public static String convertToRegexp(String filterString) {
        String result = filterString.replace("*", "\\*");
        result = result.replace("%", ".*");
        if (!result.endsWith(".*")) {
            result += ".*";
        }
        if (!result.startsWith(".*")) {
            result = ".*"+result;
        }

        return result;
    }
    
    public static String processFilterString(Object filterString) {
        String result = filterString.toString().replace("\\*", "*");
        result = result.replace('*', '%');
        if (!result.endsWith("%")) {
            result += "%";
        }
        if (!result.startsWith("%")) {
            result = "%"+result;
        }
        
        return result;
    }
    
	public static String normalizeFieldName(String expression) {
    	String[] values1 = expression.split("\\.");
    	if (values1.length <= 1) {
    		return expression;
    	}
    	String[] values = new String[values1.length - 1];   	
    	for (int i = 0; i < values1.length - 1; i++) {
    		values[i] = values1[i];
    	}
    	StringBuilder sb = new StringBuilder();    	
    	for (String value: values) {
    		sb.append(value);
    		sb.append("_");
    	}
    	sb.deleteCharAt(sb.length()-1);
    	sb.append(".").append(values1[values1.length-1]);
    	return sb.toString();
	}

}
