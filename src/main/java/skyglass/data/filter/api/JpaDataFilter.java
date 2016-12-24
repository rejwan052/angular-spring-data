package skyglass.data.filter.api;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.hibernate.Criteria;
import org.hibernate.EntityMode;
import org.hibernate.HibernateException;
import org.hibernate.criterion.CriteriaQuery;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.IlikeExpression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.dialect.DB2Dialect;
import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.PostgreSQL81Dialect;
import org.hibernate.dialect.PostgreSQLDialect;
import org.hibernate.engine.spi.TypedValue;
import org.hibernate.internal.util.StringHelper;
import org.hibernate.internal.util.collections.ArrayHelper;
import org.hibernate.sql.JoinType;
import org.hibernate.type.StandardBasicTypes;
import org.hibernate.type.StringType;
import org.hibernate.type.Type;

import skyglass.data.common.model.IdentifiableObject;
import skyglass.data.filter.AbstractDataFilter;
import skyglass.data.filter.CustomFilterResolver;
import skyglass.data.filter.CustomJpaFilterResolver;
import skyglass.data.filter.FieldResolver;
import skyglass.data.filter.FieldType;
import skyglass.data.filter.FilterClass;
import skyglass.data.filter.FilterItem;
import skyglass.data.filter.FilterItemTree;
import skyglass.data.filter.FilterType;
import skyglass.data.filter.JpaFilterItem;
import skyglass.data.filter.JunctionType;
import skyglass.data.filter.OrderField;
import skyglass.data.query.QueryResult;

public class JpaDataFilter<T> extends AbstractDataFilter<T, DataFilter<T>> implements DataFilter<T> {
	
	protected IFilterHelper filterHelper;
	
	private Criteria rootCriteria;
	
    protected Class<?> clazz;
    
    private Set<String> aliases = new HashSet<String>();
    
    private Set<String> aliasPaths = new HashSet<String>();
    
    private Map<String, String> orderAliases = new HashMap<String, String>();
    
	public FilterItem createFilterItem(String fieldName, FieldType fieldType, Object filterValue) {
		return new JpaFilterItem(filterHelper, clazz, getFieldResolver(fieldName, fieldType), filterValue);
	}     
    
	public FilterItem createFilterItem(String fieldName, FieldType fieldType, Object filterValue, FilterType filterType) {
		return new JpaFilterItem(filterHelper, clazz, getFieldResolver(fieldName, fieldType), filterValue, filterType);
	}     
    
	public FilterItem createFilterItem(String fieldName, FieldType fieldType, Object filterValue, FilterClass filterClass) {
		return new JpaFilterItem(filterHelper, clazz, getFieldResolver(fieldName, fieldType), filterValue, filterClass);
	}      
    
	public FilterItem createFilterItem(String fieldName, FieldType fieldType, Object filterValue, FilterType filterType, FilterClass filterClass) {
		return new JpaFilterItem(filterHelper, clazz, getFieldResolver(fieldName, fieldType), filterValue, filterType, filterClass);
	}    
    
    public JpaDataFilter(Class<?> clazz, JunctionType junctionType, 
    		IFilterHelper filterHelper) {
        super(junctionType);
        this.clazz = clazz;
        this.filterHelper = filterHelper;
    }    
	
    public Criteria createRootCriteria() {
    	return filterHelper.createRootCriteria(clazz);
    }
    
    @Override
    @SuppressWarnings("unchecked")
	public List<T> getFullResult() {
        doApplyFilter();
        resolveCustomFilters();
        doApplyOrder();
        List<T> result = rootCriteria.list();
		return result;
    } 
    
    @Override
    protected void initBeforeResult() {
    	initSearch();
    	initPostSearch();
    	this.rootCriteria = createRootCriteria();    
    }
    
    @Override
    protected long getRowCount() {
        doApplyFilter();
        resolveCustomFilters();
        if (isDistinctCriteria()) {
        	setDistinctRowCount();
        } else {
        	setRowCount();
        }   
        long rowCount = (Long)rootCriteria.uniqueResult();
        return rowCount;   	
    }

    @Override
    @SuppressWarnings("unchecked")
    protected QueryResult<T> getPagedResult() {
    	long rowCount = getRowCount(); 
        QueryResult<T> result = new QueryResult<T>();
        result.setTotalRecords(rowCount);
        if (rowCount == 0) {
    		result.setResults(Collections.EMPTY_LIST);
    		return result;
        }
      
        if (isDistinctCriteria()) {
        	List<UUID> orderedSubList = setDistinctRootResult();
            result.setResults(sortResult(rootCriteria.list(), orderedSubList));        		
        } else {
        	setRootResult();
            result.setResults(rootCriteria.list());
        }


        return result;
    } 
    
    @SuppressWarnings("unchecked")
	private List<T> sortResult(List<T> result, List<UUID> sortedIds) {
    	Map<UUID, T> resultMap = new HashMap<UUID, T>();
    	for (T element: result) {
    		resultMap.put(((IdentifiableObject<UUID>)element).getId(), element);
    	}
    	List<T> sortedResult = new ArrayList<T>();
    	for (UUID uuid: sortedIds) {
    		sortedResult.add(resultMap.get(uuid));
    	}
    	return sortedResult;
    }
    
    private void setDistinctRowCount() {
        rootCriteria.setProjection(Projections.countDistinct("id"));     	
    }
    
    private List<UUID> setDistinctRootResult() {
        int rowsPerPage = getRowsPerPage(); 	
        rootCriteria.setFirstResult((getPageNumber()-1)*rowsPerPage);
        rootCriteria.setMaxResults(rowsPerPage); 
        List<UUID> uniqueSubList = null;
        if (doApplyOrder()) {
        	ProjectionList projectionList = Projections.projectionList().add(Projections.distinct(Projections.id()));
        	for (String orderAlias: orderAliases.keySet()) {
        		String orderExpression = orderAliases.get(orderAlias);
        		projectionList.add(RestrictionsExt.sqlProjection(orderExpression, orderAlias));     		
        	}
        	for (OrderField orderField: getOrderFields()) {
        		if (orderField.isSingle()) {
            		for (String resolver: orderField.getOrderField().getResolvers()) {
                		projectionList.add(Projections.property(resolver), resolver);        			
            		}        			
        		}
        	}
        	rootCriteria.setProjection(projectionList);
        	uniqueSubList = convertArrayToIds(rootCriteria.list());  
        } else {
        	rootCriteria.setProjection(Projections.distinct(Projections.id()));
        	uniqueSubList = convertToIds(rootCriteria.list());  
        }   
        if (uniqueSubList.size() == 0) {
        	return uniqueSubList;
        }
        rootCriteria = createRootCriteria();
        rootCriteria.add(Restrictions.in("id", uniqueSubList));
        rootCriteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
        return uniqueSubList;
    }
    
    private List<UUID> convertArrayToIds(List<?> list) {
    	List<UUID> result = new ArrayList<UUID>();
    	@SuppressWarnings("unchecked")
		List<Object> arrayList = (List<Object>) list;
		for (Object array: arrayList) {
			if (array instanceof Object[]) {
				UUID uuid = (UUID)((Object[])array)[0];
				result.add(uuid);				
			} else {
				UUID uuid = (UUID)array;
				result.add(uuid);					
			}
		}
		return result;
    }    
    
    private List<UUID> convertToIds(List<?> list) {
    	List<UUID> result = new ArrayList<UUID>();
    	@SuppressWarnings("unchecked")
		List<UUID> arrayList = (List<UUID>) list;
		for (UUID uuid: arrayList) {
			result.add(uuid);
		}
		return result;
    }
    
    private void setRowCount() {
        rootCriteria.setProjection(Projections.rowCount());     	
    }
    
    private void setRootResult() {
        int rowsPerPage = getRowsPerPage();
        rootCriteria.setProjection(null);     
        rootCriteria.setFirstResult((getPageNumber()-1)*rowsPerPage);
        rootCriteria.setFetchSize(rowsPerPage);
        rootCriteria.setMaxResults(rowsPerPage);
        doApplyOrder();
        rootCriteria.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);    	
    }  
    
    protected boolean isDistinctCriteria() {
    	return false;
    }
    
    @Override
    @SuppressWarnings("unchecked")
    protected List<T> getUnpagedResult() {
        doApplyFilter();
        resolveCustomFilters();    	
        doApplyOrder();
        if (isDistinctCriteria()) {
            rootCriteria.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);        	
        } else {
            rootCriteria.setResultTransformer(Criteria.ROOT_ENTITY);          	
        }
        return rootCriteria.list();
    }      

    @Override
    protected void resolveCustomFilter(CustomFilterResolver filterResolver) {
   		((CustomJpaFilterResolver)filterResolver).addCustomFilter(rootCriteria);
    }
    
    @Override
    protected void applyOrder(List<OrderField> orderFields) {
    	for (OrderField orderField: orderFields) {
    		FieldResolver fieldResolver = orderField.getOrderField();
    		if (fieldResolver.isMultiple()) {
            	applyMultipleOrder(orderField);      			
    		} else {
    			applySingleOrder(orderField);
    		}
    	} 
    	
    }
    
    private void applyMultipleOrder(OrderField orderField) {
    	String orderAlias = addOrderAlias(orderField);
    	for (String resolver: orderField.getOrderField().getResolvers()) {
        	createAliases(resolver);    		
    	}
    	String orderExpression = getOrderSqlExpression(orderField);
    	orderAliases.put(orderAlias, orderExpression);
    	rootCriteria.addOrder(getOrder(orderField, orderExpression)); 
    }     
    
    private void applySingleOrder(OrderField orderField) {
    	createAliases(orderField.getOrderField().getResolver());
        if (orderField.isDescending()) {
        	rootCriteria.addOrder(Order.desc(orderField.getOrderField().getResolver())); 
        } else {
        	rootCriteria.addOrder(Order.asc(orderField.getOrderField().getResolver()));   
        }       	
    }
    
    private String getOrderSqlExpression(OrderField orderField) {
    	return concat(0, orderField.getOrderField().getResolvers());   	
    }
    
    private Order getOrder(OrderField orderField, String orderExpression) {
    	boolean isDescending = orderField.isDescending();
    	return new OrderBySqlFormula(orderExpression + (isDescending ? " DESC" : " ASC"));		
    }    
    
    private String addOrderAlias(OrderField orderField) {
    	StringBuilder sb = new StringBuilder();
    	for (String resolver: orderField.getOrderField().getResolvers()) {
        	sb.append(resolver).append("_");   		
    	} 
    	if (sb.length() > 0) {
    		sb.deleteCharAt(sb.length() - 1);
    		String result = sb.toString().replaceAll("\\.", "_");
    		return result;    		
    	}
    	return null;
    }    
    
    private String concat(int i, Collection<String> fieldResolvers) {
		String expression = coalesce(getFieldResolver(i, fieldResolvers));    		
		if (i < fieldResolvers.size() - 1) {
    		return concat(expression, concat(i+1, fieldResolvers));
		} else {
			return expression;
		}	
    }
    
    private String getFieldResolver(int i, Collection<String> fieldResolvers) {
    	int j = 0;
    	for (String fieldResolver: fieldResolvers) {
    		if (j == i) {
    			return fieldResolver;
    		}
    		j++;
    	}
    	return null;
    }
    
    private String coalesce(String concat) {
    	concat = formatFieldToSql(concat);
    	StringBuilder sb = new StringBuilder();
    	sb.append("CASE WHEN (").append(concat).append(" IS NULL) THEN '' ELSE ").append(concat).append(" END");
    	//sb.append("COALESCE(").append(formatFieldToSql(concat)).append(", '')");
    	return sb.toString();
    }
 
    
    private String concat(String concat1, String concat2) {
    	StringBuilder sb = new StringBuilder();
    	if (filterHelper.getDialect() instanceof DB2Dialect) {
        	sb.append("(").append(concat1).append(" || ").append(concat2).append(")");      		
    	} else {
        	sb.append("CONCAT(").append(concat1).append(", ").append(concat2).append(")");      		
    	}
    	return sb.toString();
    }
    

           

    private static class CustomIlikeExpression extends IlikeExpression
    {
    	private Object value;
    	private String propertyName;
    	private Class<?> rootClass;
    	private IFilterHelper filterHelper;    	
    	
		public CustomIlikeExpression(Class<?> rootClass, String propertyName, Object value, IFilterHelper filterHelper)
		{
			super(propertyName, value);
			this.rootClass = rootClass;
			this.propertyName = propertyName;
			this.value = value;
			this.filterHelper = filterHelper;
		}
		
		@Override
		public String toSqlString(Criteria criteria, CriteriaQuery criteriaQuery) {
			Dialect dialect = filterHelper.getDialect();
			String[] columns = criteriaQuery.findColumns(this.propertyName,
					criteria);
			if (columns.length != 1) {
				throw new HibernateException(
						"ilike may only be used with single-column properties");
			}
			if ((dialect instanceof PostgreSQLDialect)
					|| (dialect instanceof PostgreSQL81Dialect)) {
				return columns[0] + " ilike ?";
			}

			if ((dialect instanceof DB2Dialect) 
					&& filterHelper.isNumericField(rootClass, propertyName)) {
				return dialect.getLowercaseFunction() + "(TRIM(CAST(" + columns[0] + " AS CHAR(30)))) like ?";
			}
			return dialect.getLowercaseFunction() + '(' + columns[0] + ") like ?";
		}		

		@Override
		public TypedValue[] getTypedValues(Criteria criteria,
				CriteriaQuery criteriaQuery) throws HibernateException
		{
			return new TypedValue[] { 
					new TypedValue(StandardBasicTypes.STRING, value, EntityMode.POJO)
			};
		}
    } 
    
    private String formatFieldToSql(String expression) {
    	String[] values1 = expression.split("\\.");
    	if (values1.length <= 1) {
    		return camelCaseToUnderscore(expression);
    	}
    	String currentAlias = values1[0];
    	String propertyName = values1[1];
    	return filterHelper.getDialect().getLowercaseFunction() + '(' + "{" + currentAlias + "}." + camelCaseToUnderscore(propertyName) + ")";
    } 
    
    public String resolveAliases(String expression) {
    	expression = IFilterHelper.normalizeFieldName(expression);
    	String[] values1 = expression.split("\\.");
    	if (values1.length == 1) {
    		createAlias(expression, expression);
    		return expression;
    	}
    	String currentAlias = createAliases(expression); 
    	String result = currentAlias + "_" + values1[1];
		createAlias(currentAlias + "." + values1[1], result);
		return result;
    }    
    
    private String createAliases(String expression) {
    	String[] values1 = expression.split("\\.");
    	if (values1.length == 1) {
    		return values1[0];
    	}
    	String[] values = values1[0].split("_");
    	int i = 0;
    	String currentAlias = values[0]; 
    	createAlias(values[0], currentAlias);
    	for (String value: values) {
    		if (i > 0) {
       			createAlias(currentAlias + "." + value, currentAlias + "_" + value);
        		currentAlias = currentAlias + "_" + value;    				
    		}
    		i++;
    	}
    	return currentAlias;
    }
    
    public void createAlias(String path, String alias) {
    	if (aliasPaths.contains(path)) {
    		return;
    	}
    	if (aliases.contains(alias)) {
    		return;
    	}    	
    	aliasPaths.add(path);
    	aliases.add(alias);
    	rootCriteria.createAlias(path, alias, JoinType.LEFT_OUTER_JOIN);
    	//TODO: add joinType parameter to filter:
    	//rootCriteria.createAlias(path, alias, JoinType.INNER_JOIN);
    }

    protected void doApplyFilter() {
    	Criterion result = applyFilters(rootFilterItem);
    	if (result != null) {
        	rootCriteria.add(result);     		
    	} 	
    }
    
    private Criterion applyFilters(FilterItemTree parent) {
    	Criterion totalResult = null;
    	for (FilterItem filterItem: parent.getChildren()) {
    		Criterion result = null;
    		if (filterItem instanceof FilterItemTree) {
    			result = applyFilters((FilterItemTree) filterItem);
    		} else {
        		result = applyFilter(filterItem.getFieldResolver(), filterItem.getFilterType(), filterItem.getFilterValue());    			
    		}
    		if (totalResult == null) {
    			totalResult = result;
    		} else if (parent.getJunctionType() == JunctionType.AND) {
    			totalResult = Restrictions.and(result, totalResult);
    		} else {
    			totalResult = Restrictions.or(result, totalResult);
    		}
    	}
    	return totalResult;
    }    
	
	private Criterion applyFilter(FieldResolver fieldResolver, FilterType filterType, Object filterValue) {
    	Criterion totalResult = null;
    	for (String field: fieldResolver.getResolvers()) {
			createAliases(field);	
    		Criterion result = applyAtomicFilter(fieldResolver, field, filterType, filterValue);
    		if (totalResult == null) {
    			totalResult = result;
    		} else {
    			totalResult = Restrictions.or(result, totalResult);
    		}
    	}
    	return totalResult;	
	}	
	
	private Criterion applyAtomicFilter(FieldResolver fieldResolver, String fieldName, FilterType filterType, Object filterValue) {
		if (fieldResolver.getFieldType() == FieldType.Criteria) {
			return fieldResolver.getCustomFieldResolver().getCriteria();
		}
		if (filterType == FilterType.LIKE) {
			return applyLikeFilter(fieldName, filterValue);
		} else if (filterType == FilterType.EQ) {
			return applyEqualsFilter(fieldName, filterValue);
		} else if (filterType == FilterType.GE) {
			return applyGreaterOrEqualsFilter(fieldName, filterValue);
		} else if (filterType == FilterType.GT) {
			return applyGreaterFilter(fieldName, filterValue);			
		} else if (filterType == FilterType.LE) {
			return applyLessOrEqualsFilter(fieldName, filterValue);
		} else if (filterType == FilterType.LT) {
			return applyLessFilter(fieldName, filterValue);
		} else {
			return applyEqualsFilter(fieldName, filterValue);
		}		
	}
	
	private Criterion applyEqualsFilter(String fieldName, Object filterValue) {
		return filterValue == null ? Restrictions.isNull(fieldName) : Restrictions.eq(fieldName, filterValue);
	}
	
	private Criterion applyGreaterFilter(String fieldName, Object filterValue) {
		return Restrictions.gt(fieldName, filterValue);
	}
	
	private Criterion applyGreaterOrEqualsFilter(String fieldName, Object filterValue) {
		return Restrictions.ge(fieldName, filterValue);
	}
	
	private Criterion applyLessFilter(String fieldName, Object filterValue) {
		return Restrictions.lt(fieldName, filterValue);
	}
	
	private Criterion applyLessOrEqualsFilter(String fieldName, Object filterValue) {
		return Restrictions.le(fieldName, filterValue);
	}	
	
	private Criterion applyLikeFilter(String fieldName, Object filterValue) {
        return new CustomIlikeExpression(clazz, fieldName, filterValue.toString(), filterHelper); 
	}
	
    protected static class OrderBySqlFormula extends Order {
		private static final long serialVersionUID = -3928943724937182557L;
		private String sqlFormula;


        protected OrderBySqlFormula(String sqlFormula) {
            super(sqlFormula, true);
            this.sqlFormula = sqlFormula;
        }

        public String toString() {
            return sqlFormula;
        }

        public String toSqlString(Criteria criteria, CriteriaQuery criteriaQuery) throws HibernateException {
            return applyAliases(criteria, criteriaQuery, sqlFormula);
        }


        public static Order customOrder(String sqlFormula) {
            return new OrderBySqlFormula(sqlFormula);
        }
        
    }	
	
	protected static class RestrictionsExt {

	    public static Criterion likeSQLAliased(String propertyName, String value) {
	        return new SQLAliasedCriterion(propertyName + " like ?", "%" + value + "%", StringType.INSTANCE);
	    }

	    public static Criterion sqlAliased(String sql, Object[] values, Type[] types) {
	        return new SQLAliasedCriterion(sql, values, types);
	    }


	    public static Projection sqlProjection(String sql, String alias) {
	        return new SQLAliasedProjection(sql + " as " + alias,
	                new String[]{alias}, new Type[]{StringType.INSTANCE});
	    }

	    public static Projection sqlProjectionGroupedString(String sql, String alias) {
	        return new SQLAliasedProjection(sql + " as " + alias,
	                new String[]{alias}, new Type[]{StringType.INSTANCE}, sql);
	    }

	    public static Projection sqlProjectionGrouped(String sql, String alias, Type clazz) {
	        return new SQLAliasedProjection(sql + " as " + alias,
	                new String[]{alias}, new Type[]{clazz}, sql);
	    }
	    
	}
	
	protected static class SQLAliasedCriterion implements Criterion {

		private static final long serialVersionUID = -4025066009045126609L;

		private final String sql;
		private final TypedValue[] typedValues;

		public String toSqlString(Criteria criteria, CriteriaQuery criteriaQuery) throws HibernateException {
			return applyAliases(criteria, criteriaQuery);
		}

		private String applyAliases(Criteria criteria, CriteriaQuery criteriaQuery) {
			StringBuilder res = new StringBuilder();
			int i = 0;
			int cnt = this.sql.length();
			while (i < cnt) {
				int l = this.sql.indexOf('{', i);
				if (l == -1) {
					break;
				}

				String before = this.sql.substring(i, l);
				res.append(before);

				int r = this.sql.indexOf('}', l);
				String alias = this.sql.substring(l + 1, r);
				if (alias.isEmpty() || "alias".equals(alias)) { // root alias
					res.append(criteriaQuery.getSQLAlias(criteria));
				} else {
					String[] columns = criteriaQuery.getColumnsUsingProjection(criteria, alias);
					if (columns.length != 1)
						throw new HibernateException("SQLAliasedCriterion may only be used with single-column properties: "
								+ alias);
					res.append(columns[0]);
				}
				i = r + 1;
			}
			String after = this.sql.substring(i, cnt);
			res.append(after);

			return res.toString();
		}

		public TypedValue[] getTypedValues(Criteria criteria, CriteriaQuery criteriaQuery) throws HibernateException {
			return typedValues;
		}

		@Override
		public String toString() {
			return sql;
		}

		public SQLAliasedCriterion(String sql, Object[] values, Type[] types) {
			this.sql = sql;
			typedValues = new TypedValue[values.length];
			for (int i = 0; i < typedValues.length; i++) {
				typedValues[i] = new TypedValue(types[i], values[i], EntityMode.POJO);
			}
		}

		public SQLAliasedCriterion(String sql, Object value, Type type) {
			this(sql, new Object[] { value }, new Type[] { type });
		}

		public SQLAliasedCriterion(String sql) {
			this(sql, ArrayHelper.EMPTY_OBJECT_ARRAY, ArrayHelper.EMPTY_TYPE_ARRAY);
		}

	}
	
	protected static class SQLAliasedProjection implements Projection {

	    /**
	     *
	     */
	    private static final long serialVersionUID = -7028362199361547260L;
	    private final String sql;
	    private final String groupBy;
	    private final Type[] types;
	    private String[]     aliases;
	    private String[]     columnAliases;
	    private boolean      grouped;

	    public String toSqlString(Criteria criteria, int loc, CriteriaQuery criteriaQuery) throws HibernateException {
	        return applyAliases(criteria, criteriaQuery, sql);
	    }    

	    public String toGroupSqlString(Criteria criteria, CriteriaQuery criteriaQuery) throws HibernateException {
	        return applyAliases(criteria, criteriaQuery, groupBy);
	    }

	    public Type[] getTypes(Criteria crit, CriteriaQuery criteriaQuery) throws HibernateException {
	        return types;
	    }

	    @Override
	    public String toString() {
	        return sql;
	    }

	    public SQLAliasedProjection(String sql, String[] columnAliases, Type[] types) {
	        this(sql, null, columnAliases, types);
	    }

	    public SQLAliasedProjection(String sql, String[] columnAliases, Type[] types, String grouped) {
	        this(sql, grouped, columnAliases, types);
	    }

	    protected SQLAliasedProjection(String sql, String groupBy, String[] columnAliases, Type[] types) {
	        this.sql = sql;
	        this.types = types;
	        this.aliases = columnAliases;
	        this.columnAliases = columnAliases;
	        this.grouped = groupBy != null;
	        this.groupBy = groupBy;
	    }

	    public String[] getAliases() {
	        return aliases;
	    }

	    public String[] getColumnAliases(int loc) {
	        return columnAliases;
	    }

	    public boolean isGrouped() {
	        return grouped;
	    }

	    public Type[] getTypes(String alias, Criteria crit, CriteriaQuery criteriaQuery) {
	        return null; // unsupported
	    }

	    public String[] getColumnAliases(String alias, int loc) {
	        return null; // unsupported
	    }
	
	}
	
    /**
     * @param criteria
     * @param criteriaQuery
     * @return
     */
    private static String applyAliases(Criteria criteria, CriteriaQuery criteriaQuery, String sql) {
        StringBuilder res = new StringBuilder();
        String src = StringHelper.replace(sql, "{alias}", criteriaQuery.getSQLAlias(criteria));
        int i = 0;
        int cnt = src.length();
        while (i < cnt) {
            int l = src.indexOf('{', i);
            if(l == -1){
                break;
            }

            String before = src.substring(i, l);
            res.append(before);

            int r = src.indexOf('}', l);
            String alias = src.substring(l+1, r);
            if(alias.length() == 0){
                alias = "this_";
            }
            Criteria cri = null;
            // get criteria using it's path
            cri = ((org.hibernate.loader.criteria.CriteriaQueryTranslator) criteriaQuery).getCriteria(alias);
            if(cri == null){
                // if not found - get criteria using it's alias
            	cri = getAliasedCriteria(criteriaQuery, alias);
            }
            if (cri != null) {
                String sqlAlias = criteriaQuery.getSQLAlias(cri);
                res.append(sqlAlias);
            } else {
                // try to search criteria or alias in parent criteria (if we are in subcriteria)
            	CriteriaQuery outerQueryTranslator = getOuterQueryTranslator(criteriaQuery);	            	
                if(outerQueryTranslator != null){
                    // get criteria using it's path
                    cri = ((org.hibernate.loader.criteria.CriteriaQueryTranslator) outerQueryTranslator).getCriteria(alias);
                    if(cri == null){
                        // if not found - get criteria using it's alias
                    	cri = getAliasedCriteria(criteriaQuery, alias);
                    }
                    if(cri != null){
                        String sqlAlias = ((org.hibernate.loader.criteria.CriteriaQueryTranslator) outerQueryTranslator).getSQLAlias(cri);
                        res.append(sqlAlias);
                    }else{
                        res.append(alias);
                    }
                }else{
                    res.append(alias);
                }
            }
            i = r + 1;
        }
        String after = src.substring(i, cnt);
        res.append(after);

        return res.toString();
    }
    
    private static Criteria getAliasedCriteria(CriteriaQuery criteriaQuery, String alias) {
    	try {
        	Method m = ((org.hibernate.loader.criteria.CriteriaQueryTranslator) criteriaQuery).getClass().getDeclaredMethod("getAliasedCriteria", String.class);
        	m.setAccessible(true);
        	return (Criteria) m.invoke(((org.hibernate.loader.criteria.CriteriaQueryTranslator) criteriaQuery), alias);	   	    		
    	} catch (Exception e) {
    		throw new RuntimeException(e);
    	}
    }
    
    private static CriteriaQuery getOuterQueryTranslator(CriteriaQuery criteriaQuery) {
    	try {
        	Field f = ((org.hibernate.loader.criteria.CriteriaQueryTranslator) criteriaQuery).getClass().getDeclaredField("outerQueryTranslator");
        	f.setAccessible(true);
        	return (CriteriaQuery) f.get(((org.hibernate.loader.criteria.CriteriaQueryTranslator) criteriaQuery));	    	    		
    	} catch (Exception e) {
    		throw new RuntimeException(e);
    	}
    }	
    
    private static String camelCaseToUnderscore(String property) {
    	return property.replaceAll("(.)(\\p{Upper})", "$1_$2").toLowerCase();
    }
    


}
