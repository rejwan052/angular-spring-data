package skyglass.data.filter.http;

import skyglass.data.filter.IDataFilter;

public interface IHttpDataFilter<T, F> extends IDataFilter<T, F>, IBaseHttpDataFilter<T, F> {
	
	public F addHttpSearch(String... fieldNames);
    
    public F addStatuses();
    
    public <R> F addStatuses(ArrayNormalizer<R> normalizer);    
    
    public F addTypes();
    
    public F addSecurityFilter(String alias, Class<?> clazz);

}
