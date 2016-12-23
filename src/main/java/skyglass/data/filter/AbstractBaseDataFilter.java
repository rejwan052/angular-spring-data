package skyglass.data.filter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.PatternSyntaxException;

import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.SpelEvaluationException;
import org.springframework.expression.spel.standard.SpelExpressionParser;

import skyglass.data.filter.api.IFilterHelper;
import skyglass.data.query.QueryResult;

abstract class AbstractBaseDataFilter<T, F> implements IBaseDataFilter<T, F> {
	
	protected abstract void initBeforeResult();
    
	protected abstract Iterable<T> getFullResult();
    
	protected abstract boolean shouldApplyPostOrder();
    
    private int rowsPerPage = 10;
    private int pageNumber = 1;
    private List<OrderField> orderFields = new ArrayList<OrderField>();
	
	private PostFilterItemTree<T> rootPostFilterItem;	
	
    protected List<PostFilterResolver<T>> postFilterResolvers = new ArrayList<PostFilterResolver<T>>();
    
	private Map<String, FieldResolver> fieldResolverMap = new HashMap<String, FieldResolver>();    
    
	protected Map<String, PostFieldResolver<T>> postFieldResolverMap = new HashMap<String, PostFieldResolver<T>>();
	
	private Map<String, List<FieldResolver>> postSearchMap = new HashMap<String, List<FieldResolver>>();
	
    public F addPostFieldResolver(String fieldName, final String expression) {
		PostFieldResolver<T> postFieldResolver = new PostFieldResolver<T>() {
			@Override
			public Object getValue(T element) {
				return parseExpression(element, expression);
			}
			
		}; 
		return addPostFieldResolver(fieldName, postFieldResolver);
    }	
	
	public F addPostFieldResolver(String fieldName, PostFieldResolver<T> postFieldResolver) {
		return addPostField(fieldName, postFieldResolver);	
		
	}
	
	protected AbstractBaseDataFilter(JunctionType junctionType) {
		this.rootPostFilterItem = new PostFilterItemTree<T>(junctionType);
	}
	
	@SuppressWarnings("unchecked")
	public F addPostFilterResolver(PostFilterResolver<T> postFilterResolver) {
		postFilterResolvers.add(postFilterResolver);
		return (F)this;
	}
	
	protected Object getResolvedValue(String fieldName, T object) {
		return getResolvedValue(rootPostFilterItem, fieldName, object);
	} 
	
	protected Object getResolvedValue(PostFilterItemTree<T> parent, String fieldName, T object) {
		for (PostFilterItem<T> postFilterItem: parent.getChildren()) {
			if (postFilterItem instanceof PostFilterItemTree) {
				return getResolvedValue((PostFilterItemTree<T>)postFilterItem, fieldName, object);
			} else if (postFilterItem.getFieldName().equals(fieldName)) {
				return getResolvedValue(postFilterItem, object);
			}
		}
		PostFieldResolver<T> resolver = postFieldResolverMap.get(fieldName);
		if (resolver != null) {
			return resolver.getValue(object);
		}		
		return parseExpression(object, fieldName);		
	} 	
	
	protected Object getResolvedValue(PostFilterItem<T> postFilterItem, T object) {
		if (postFilterItem.getPostFieldResolver(postFieldResolverMap) != null) {
			return (postFilterItem.getPostFieldResolver(postFieldResolverMap)).getValue(object);
		}
		return parseExpression(object, postFilterItem.getFieldName());
	} 
	
	protected Object parseExpression(Object object, String expression) {
		ExpressionParser parser = new SpelExpressionParser();
		Expression exp = parser.parseExpression(expression);
		try {
			return exp.getValue(object);			
		} catch (SpelEvaluationException e) {
			return null;
		}
	}	
	
	protected boolean resolveCustomPostFilters(T element) {
		boolean result = true;
		for (PostFilterResolver<T> customPostFilter: postFilterResolvers) {
			result = result && customPostFilter.resolveFilter(element);
		}
		return result;
	}	
	
	protected boolean returnEmptyResult() {
		return false;
	}
    
    protected int getRowsPerPage() {
        return rowsPerPage;
    }
    
    protected int getPageNumber() {
        return pageNumber;
    }    
    
    protected List<OrderField> getOrderFields() {
    	return orderFields;
    }
    
	@SuppressWarnings("unchecked")
    public F setPaging(Integer rowsPerPage, Integer pageNumber) {
    	this.rowsPerPage = rowsPerPage;
    	this.pageNumber = pageNumber;
		return (F)this;
    }
	
	@SuppressWarnings("unchecked")
    public F addOrder(String orderField, OrderType orderType) {
		OrderField order = new OrderField(getFieldResolver(orderField, null), orderType);
    	this.orderFields.add(order);
		return (F)this;
    }
    
    public F setOrder(String orderField, OrderType orderType) {
		this.orderFields.clear();
		return addOrder(orderField, orderType);
    }    
	
	protected FieldResolver getFieldResolver(String fieldName, FieldType fieldType) {
		FieldResolver fieldResolver = fieldResolverMap.get(fieldName);
		if (fieldResolver == null) {
			fieldResolver = new FieldResolver(fieldName, fieldType);
			fieldResolverMap.put(fieldName, fieldResolver);
		}
		if (fieldResolver.getFieldType() == null && fieldType != null) {
			fieldResolver.setFieldType(fieldType);
		}
		return fieldResolver;
	}
	
	@SuppressWarnings("unchecked")
    public F setDefaultOrder(String orderField, OrderType orderType) {
    	if (this.orderFields.size() == 0) {
    		setOrder(orderField, orderType);
    	}
		return (F)this;
    }	
    
	@Override
	public QueryResult<T> getResult() {
    	if (returnEmptyResult()) {
    		return getEmptyResult();
    	}
    	initBeforeResult();
		List<T> unpagedResult = applyPostFilters(); 
		applyPostOrder(unpagedResult);
    	List<T> pagedResult = new ArrayList<T>();
    	int startIndex = (getPageNumber() - 1) * getRowsPerPage();
    	int endIndex = Math.min(startIndex + getRowsPerPage(), unpagedResult.size());
    	for (int i = startIndex; i < endIndex; i++) {
    		pagedResult.add(unpagedResult.get(i));
    	}
        QueryResult<T> result = new QueryResult<T>();
        result.setTotalRecords(unpagedResult.size());
        result.setResults(pagedResult);
        return result;  
  	
    } 
	
	@Override    
	public List<T> getUnpagedList() {
    	if (returnEmptyResult()) {
    		return getEmptyResult().getResults();
    	}
    	initBeforeResult();
		List<T> unpagedResult = applyPostFilters(); 
		applyPostOrder(unpagedResult);
		return unpagedResult;
    } 	
	
	@Override
	public long getResultCount() {
    	if (returnEmptyResult()) {
    		return 0;
    	}
    	initBeforeResult();
		List<T> unpagedResult = applyPostFilters(); 
		return unpagedResult.size();  	
    } 	
	
    @SuppressWarnings("unchecked")
	protected QueryResult<T> getEmptyResult() {
        QueryResult<T> result = new QueryResult<T>();
        result.setTotalRecords(0);
        result.setResults((List<T>)Collections.emptyList()); 
        return result;
    }
    
	@SuppressWarnings("unchecked")
	public F addPostField(String fieldName) {
    	if (postFieldResolverMap.get(fieldName) == null) {
    		postFieldResolverMap.put(fieldName, null);	    		
    	}
		return (F)this;
    } 
    
	@SuppressWarnings("unchecked")
	private F addPostField(String fieldName, PostFieldResolver<T> postFieldResolver) {
		postFieldResolverMap.put(fieldName, postFieldResolver);	
		return (F)this;
	}    
    
	public F addPostFilter(String fieldName, Object filterValue) {
		return addPostFilter(fieldName, FilterType.EQ, filterValue);
	}    
    
    public F addPostFilter(String fieldName, FilterType filterType, Object filterValue) {
    	PostFilterItem<T> postFilterItem = createPostFilterItem(fieldName, filterType, filterValue);
    	return addPostFilterChild(rootPostFilterItem, postFilterItem);
    }   
    
	@SuppressWarnings("unchecked")
	public F addPostFilters(String fieldName, FilterType filterType, Object[] filterValues) {
    	addPostField(fieldName); 
		PostFilterItemTree<T> orFilter = new PostFilterItemTree<T>(JunctionType.OR);
		addPostFilterChild(rootPostFilterItem, orFilter);
		for (Object filterValue: filterValues) {
			PostFilterItem<T> postFilterItem = createPostFilterItem(fieldName, FilterType.EQ, filterValue);	
			addPostFilterChild(orFilter, postFilterItem);
		}
		return (F)this;
	}

	public F addPostFilters(String fieldName, Object[] fieldValues) {
		return addPostFilters(fieldName, FilterType.EQ, fieldValues);		
	}
	
	public F addPostFilter(PostFilterItem<T> postFilterItem) {
    	return addPostFilterChild(rootPostFilterItem, postFilterItem);
    }
	
	public F addPostFilter(PostFilterItemTree<T> parent, PostFilterItem<T> postFilterItem) {
    	addPostFilterChild(parent, postFilterItem);
    	return addPostFilterChild(rootPostFilterItem, parent);    	
    }
	
	@SuppressWarnings("unchecked")
	public F addPostFilters(PostFilterItem<T>... postFilterItems) {
    	for (PostFilterItem<T> postFilterItem: postFilterItems) {
    		addPostFilter(postFilterItem);
    	}
		return (F)this;
    }
	
	@SuppressWarnings("unchecked")
	public F addOrPostFilters(PostFilterItem<T>... postFilterItems) {
    	return addPostFilters(new PostFilterItemTree<T>(JunctionType.OR), postFilterItems);
    }
	
	@SuppressWarnings("unchecked")
	public F addAndPostFilters(PostFilterItem<T>... postFilterItems) {
    	return addPostFilters(new PostFilterItemTree<T>(JunctionType.AND), postFilterItems);    	
    }
	
	@SuppressWarnings("unchecked")
	public F addPostFilters(PostFilterItemTree<T> parent, PostFilterItem<T>... postFilterItems) {
    	for (PostFilterItem<T> postFilterItem: postFilterItems) {
    		addPostFilterChild(parent, postFilterItem);
    	}
    	return addPostFilterChild(rootPostFilterItem, parent);
    }

	public PostFilterItem<T> createPostFilterItem(String fieldName, Object filterValue) {
		return new PostFilterItem<T>(fieldName, FilterType.EQ, filterValue);
	}

	public PostFilterItem<T> createPostFilterItem(String fieldName, FilterType filterType, Object filterValue) {
		return new PostFilterItem<T>(fieldName, filterType, filterValue);
	}
	
	public F addPostSearch(String filterValue, final String... fieldNames) {
		return addSearch(postSearchMap, filterValue, FieldType.Path, fieldNames);
	}
	
	protected void initPostSearch() {
		if (!postSearchMap.isEmpty()) {
	    	PostFilterItemTree<T> orFilter = new PostFilterItemTree<T>(JunctionType.OR);
	    	addPostFilterChild(rootPostFilterItem, orFilter);
	    	for (String fieldValue: postSearchMap.keySet()) {
	    		List<FieldResolver> fieldResolvers = postSearchMap.get(fieldValue);
	    		for (FieldResolver fieldResolver: fieldResolvers) {
	            	PostFilterItem<T> postFilterItem = createPostFilterItem(fieldResolver.getFieldName(), FilterType.LIKE, fieldValue);
	            	addPostFilterChild(orFilter, postFilterItem); 			
	    		}    		
	    	}			
		}
	}	
	
	protected List<T> applyPostFilters() {
		List<T> result = new ArrayList<T>();
		Iterable<T> fullResult = getFullResult();
		for (T element: fullResult) {
			if (applyPostFilters(element)) {
				result.add(element);
			}
		}
		return result;
	}
	
	protected boolean shouldApplyPostFilters() {
		return rootPostFilterItem.getChildren().size() > 0;
	}
	
    @SuppressWarnings({ "rawtypes", "unchecked" })
	protected boolean applyPostFilters(T element) {
    	boolean result = applyPostFilters(rootPostFilterItem, element);	
    	for (PostFilterResolver postFilterResolver: postFilterResolvers) {
    		result = result && postFilterResolver.resolveFilter(element);
    	}
    	return result;
    }
    
    private boolean applyPostFilters(PostFilterItemTree<T> parent, T element) {
    	Boolean totalResult = null;    	
    	for (PostFilterItem<T> postFilterItem: parent.getChildren()) {
    		Boolean result = null;
    		if (postFilterItem instanceof PostFilterItemTree) {
    			result = applyPostFilters((PostFilterItemTree<T>) postFilterItem, element);
    		} else {
        		result = applyPostFilter(postFilterItem, element);    			
    		}
    		if (totalResult == null) {
    			totalResult = result;
    		} else {
	    		if (parent.getJunctionType() == JunctionType.AND) {
	    			totalResult = totalResult && result;				
	    		} else {
	    			totalResult = totalResult || result;				
	    		}  
    		}
    	}
    	if (totalResult == null) {
    		return true;
    	}
    	return totalResult;
    }    
	
	private boolean applyPostFilter(PostFilterItem<T> postFilterItem, T element) {
		if (postFilterItem.getFilterType() == FilterType.LIKE) {
			return applyLikePostFilter(postFilterItem, element);
		} else {
			return applyEqualsPostFilter(postFilterItem, element);
		}
	}
	
	private boolean applyEqualsPostFilter(PostFilterItem<T> postFilterItem, T element) {
		Object value = getResolvedValue(postFilterItem, element);
		if (value != null) {
			if (value instanceof Enum<?> || value.getClass().isEnum()) {
				return (value.toString().equalsIgnoreCase(postFilterItem.getStringFilterValue()));
			}			
			if (value.equals(postFilterItem.getFilterValue())) {
				return true;
			}
			return false;	
		}
		if (value == null && postFilterItem.getFilterValue() == null) {
			return true;
		}
        return false;
	}
	
	private boolean applyLikePostFilter(PostFilterItem<T> postFilterItem, T element) {
        String filterString = IFilterHelper.convertToRegexp(IFilterHelper.processFilterString(postFilterItem.getFilterValue().toString()));					
		Object value = getResolvedValue(postFilterItem, element);
		try {
			return value != null && value.toString().toLowerCase().matches(filterString.toLowerCase());			
		} catch (PatternSyntaxException e) {
			return false;
		}
	}
	
	@SuppressWarnings("unchecked")
	protected F applyPostOrder(List<T> unsortedResult) {
		if (shouldApplyPostOrder()) { 
			Collections.sort(unsortedResult, new Comparator<T>() {   				
				@Override
				public int compare(T t1, T t2) {
					return applyPostOrder(t1, t2);
				}
			});
		}
		return (F)this;
	}

	private int applyPostOrder(T element1, T element2) {
		int result = 0;
		for (OrderField orderField: getOrderFields()) {
			for (String resolver: orderField.getOrderField().getResolvers()) {
				Object value1Object = getResolvedValue(resolver, element1);
				Object value2Object = getResolvedValue(resolver, element2);
				if (value1Object instanceof Number || value2Object instanceof Number) {
					if (value1Object == null) {
						value1Object = 0;
					}
					if (value2Object == null) {
						value2Object = 0;
					}
					result = new Long(((Number) value1Object).longValue() - ((Number) value2Object).longValue()).intValue();
				} else {
					String value1 = value1Object == null ? "" : value1Object.toString();
					String value2 = value2Object == null ? "" : value2Object.toString();
					result = value1.compareToIgnoreCase(value2);
				}
				
				if (orderField.isDescending()) {
					result = -result;
				}
				
				//first comparance which returned non-zero result, which means that comparance should
				//be finished because we know the result for sure: For example: Ben Ali Baba > Ben Aali Baba (Ben == Ben, but Ali > Aali)
				if (result != 0) {
					return result;
				}				
			}
		}



		return result;
	}
	
	@SuppressWarnings("unchecked")
	private F addPostFilterChild(PostFilterItemTree<T> parent, PostFilterItem<T> child) {
		if (child.getFieldName() != null) {
			addPostField(child.getFieldName());			
		}
		parent.addChild(child);
		return (F)this;
	}
	
	@SuppressWarnings("unchecked")
	protected F addSearch(Map<String, List<FieldResolver>> searchMap, final String filterValue, FieldType fieldType, final String... fieldNames) {
		if (fieldNames.length > 0) {
			List<FieldResolver> searchList = searchMap.get(filterValue);
			if (searchList == null) {
				searchList = new ArrayList<FieldResolver>();
				searchMap.put(filterValue, searchList);
			}
			for (String fieldName: fieldNames) {
				searchList.add(getFieldResolver(fieldName, fieldType));
			}			
		}
		return (F)this;
	}
	
}
