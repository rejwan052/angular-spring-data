package skyglass.data.filter.api;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.TimeZone;

import org.hibernate.Criteria;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;
import org.hibernate.dialect.Dialect;
import org.hibernate.engine.spi.SessionFactoryImplementor;

import net.sf.ehcache.hibernate.management.impl.BeanUtils;
import skyglass.data.common.model.DataConstants;
import skyglass.data.common.model.HasCreatedDate;
import skyglass.data.common.model.SecurityResource;
import skyglass.data.filter.PostFieldResolver;
import skyglass.data.filter.http.api.PermissionType;
import skyglass.data.metadata.JpaMetadataUtil;

public abstract class AbstractFilterHelper implements IFilterHelper {
	
    public Date getStartDate(Integer daysAgo) {
    	if (daysAgo == null) {
    		return getThreeMonthsAgoDate();
    	}
    	Date referenceDate = new Date();
    	java.util.Calendar c = java.util.Calendar.getInstance(); 
    	c.setTime(referenceDate); 
    	c.add(Calendar.DATE, - daysAgo);
    	return c.getTime();    	
    }
    
    private Date getThreeMonthsAgoDate() {
    	Date referenceDate = new Date();
    	java.util.Calendar c = java.util.Calendar.getInstance(); 
    	c.setTime(referenceDate); 
    	c.add(java.util.Calendar.MONTH, -3);
    	return c.getTime();    	
    }     
    
    public <T> PostFieldResolver<T> getDatePostFieldResolver(Class<T> clazz, final String property, final String timezoneID) {
    	return new PostFieldResolver<T>() {
			public Object getValue(T element) {
				Date result = (Date)BeanUtils.getBeanProperty(element, property);
				return getFormattedDate(result, timezoneID);
			}
        };    	
    }

    public <T> PostFieldResolver<T> getCreatedDatePostFieldResolver(final String timezoneID) {
    	return new PostFieldResolver<T>() {
			public Object getValue(T element) {
                Date result = ((HasCreatedDate)element).getCreatedDate();
				return getFormattedDate(result, timezoneID);
			}
        };
    }

    public String getFormattedDate(Date date, String timezoneID) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm a");
        if(timezoneID == null){
            timezoneID = TimeZone.getDefault().getID();
        }
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone(timezoneID));
        return simpleDateFormat.format(date);
    }
    
    public boolean isMatchesSearchQuery(String result, String searchQuery) {
    	return searchQuery != null && result.toLowerCase().matches(searchQuery.toLowerCase());
    }
    
    public boolean areMatchSearchQuery(String searchQuery, String... results) {
    	if (searchQuery == null) {
    		return false;
    	}
    	for (String result: results) {
    		if (result != null && result.toLowerCase().matches(searchQuery.toLowerCase())) {
    			return true;
    		}
    	}
    	return false;
    }    
    
    public boolean isAutoExpand(String result, String searchQuery) {
    	return searchQuery != null && !result.toLowerCase().matches(searchQuery.toLowerCase());
    }
    
    @Override
    public void addCustomSecurityResolver(Criteria criteria, String alias, 
    		Class<?> clazz, PermissionType permissionType) {	
		Set<Long> expectedActionIds = getExpectedActionIds(clazz, permissionType);	
	    String prefix = alias + "_resource";
        
		if (alias.equals("root")) {
		    criteria.createAlias("resource", prefix);					
		} else {
		    criteria.createAlias(alias + ".resource", prefix);
		}	
		
	    criteria.setCacheable(false);
	    
	    for (Long expectedActionId: expectedActionIds) {
		    DetachedCriteria subQuery1 = DetachedCriteria.forClass(SecurityResource.class, "groupResource")
		    	    .setProjection(Projections.distinct(Projections.id()))
		    	    .createAlias("groupRoles", "groupRole")
		    	    .createAlias("groupRole.role", "role")
		    	    .createAlias("role.actions", "action")
		    	    .createAlias("groupRole.group", "group")
		    	    .createAlias("group.users", "user")
		    	    .add(Restrictions.eqProperty("groupResource.id", prefix + ".id"))	    	    
		    	    .add(Restrictions.and(
		    				Restrictions.eq("action.id", expectedActionId), 
		    				Restrictions.eq("user.id", getCurrentUserId())));	

	   
			DetachedCriteria subQuery2 = DetachedCriteria.forClass(SecurityResource.class, "userResource")
		    	    .setProjection(Projections.distinct(Projections.id()))
		    	    .createAlias("userRoles", "userRole")
		    	    .createAlias("userRole.role", "role")
		    	    .createAlias("role.actions", "action")
		    	    .add(Restrictions.eqProperty("userResource.id", prefix + ".id"))	    	    
		    	    .add(Restrictions.and( 
		    				Restrictions.eq("userRole.user.id", getCurrentUserId()),
		    				Restrictions.eq("action.id", expectedActionId)));	


		    
			criteria.add(Restrictions.or(
			        Subqueries.propertyEq(prefix + ".id", subQuery1),
			        Subqueries.propertyEq(prefix + ".id", subQuery2)));	    	
	    }
   

		
    }
    
    /*public Criterion getLastComponentExecutionStatusResolver(Application application, String[] statuses) {
    	ExecutionResult[] executionResults = deployRuntimeFactory.resolveProcessExecutionResults(statuses);
    	if (executionResults.length == 0) {
    		return null;
    	}
        DetachedCriteria requestCriteria = DetachedCriteria.forClass(ComponentProcessRequest.class, "request");
        
	    DetachedCriteria lastExecDate = DetachedCriteria.forClass(ComponentProcessRequest.class, "lastExecDate")
	    	    .createAlias("calendarEntry", "calendarEntry")	    		
	    	    .setProjection(Projections.max("calendarEntry.scheduledDate"))	    	    
	    	    .add(Restrictions.eqProperty("lastExecDate.component.id", "request.component.id"))
	        	.add(Restrictions.eq("calendarEntry.fired", true));		    	    
	    
        if (application != null) {
            lastExecDate.add(Restrictions.eq("application.id", application.getId()));   
            requestCriteria.add(Restrictions.eq("application.id", application.getId()));   
        }
        
		requestCriteria
	    .createAlias("calendarEntry", "calendarEntry")	  
		.add(Subqueries.propertyEq("calendarEntry.scheduledDate", lastExecDate))
		.add(Restrictions.eqProperty("request.component.id", DataConstants.ROOT_CRITERIA_ALIAS + ".id"))
		.add(Restrictions.in("result", executionResults))
	    .setProjection(Property.forName("component.id"));
	    
		
		return Subqueries.propertyEq("id", requestCriteria);	    	    
      
    } 
    
    public Criterion getLastApplicationExecutionStatusResolver(String[] statuses) {
    	ExecutionResult[] executionResults = deployRuntimeFactory.resolveProcessExecutionResults(statuses);
    	if (executionResults.length == 0) {
    		return null;
    	}
        DetachedCriteria requestCriteria = DetachedCriteria.forClass(ApplicationProcessRequest.class, "request");
        
	    DetachedCriteria lastExecDate = DetachedCriteria.forClass(ApplicationProcessRequest.class, "lastExecDate")
	    	    .createAlias("calendarEntry", "calendarEntry")	    		
	    	    .setProjection(Projections.max("calendarEntry.scheduledDate"))	    	    
	    	    .add(Restrictions.eqProperty("lastExecDate.application.id", "request.application.id"))
	        	.add(Restrictions.eq("calendarEntry.fired", true));	    	    
        
		requestCriteria
	    .createAlias("calendarEntry", "calendarEntry")	  
		.add(Subqueries.propertyEq("calendarEntry.scheduledDate", lastExecDate))
		.add(Restrictions.eqProperty("request.application.id", DataConstants.ROOT_CRITERIA_ALIAS + ".id"))
		.add(Restrictions.in("result", executionResults))
	    .setProjection(Property.forName("application.id"));
	    
		
		return Subqueries.propertyEq("id", requestCriteria);	    	    
      
    }    */
	
	private Set<String> getExpectedActionNames(PermissionType permissionType) {
		Set<String> result = new HashSet<String>();
		//result.add(SecuritySchemaHelper.READ_ACTION_NAME);
		if (permissionType == PermissionType.Execute) {
			//result.add(SecuritySchemaHelper.EXECUTE_ACTION_NAME);
		}	
		return result;
	}
	
	@Override
    public Criteria createRootCriteria(Class<?> clazz) {
        Criteria result = getSession().createCriteria(clazz, DataConstants.ROOT_CRITERIA_ALIAS);
        result.setCacheable(true);
        return result;
    }
    
    public Dialect getDialect() {
    	return ((SessionFactoryImplementor) getSession().getSessionFactory()).getDialect();    	
    }
    
    @Override
    public boolean isNumericField(Class<?> rootClass, String propertyName) {
        return JpaMetadataUtil.isNumericField(getSession().getSessionFactory(), rootClass, propertyName);    	
    }
    
    @Override
    public Object convertObject(Class<?> rootClass, String property, Object value, boolean isCollection) {
    	return JpaMetadataUtil.convertValue(
    			getSession().getSessionFactory(), rootClass, property, value, isCollection);    	
    }
    

	
}
