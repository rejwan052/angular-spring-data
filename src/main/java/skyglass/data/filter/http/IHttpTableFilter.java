package skyglass.data.filter.http;

import skyglass.data.filter.ITableFilter;
import skyglass.data.model.security.Secured;

public interface IHttpTableFilter<T, F> extends ITableFilter<T, F>, IBaseHttpTableFilter<T, F> {
	
	public F addHttpSearch(String... fieldNames);
    
    public F addStatuses();
    
    public <R> F addStatuses(ArrayNormalizer<R> normalizer);    
    
    public F addTypes();
    
    public F addSecurityFilter(String alias, Class<? extends Secured> clazz);

}
