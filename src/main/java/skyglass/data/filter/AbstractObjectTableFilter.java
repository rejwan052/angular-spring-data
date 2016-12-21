package skyglass.data.filter;


public abstract class AbstractObjectTableFilter<T, F> extends AbstractBaseTableFilter<T, F> {
    
    private Iterable<T> fullResult;
    
    protected AbstractObjectTableFilter(Iterable<T> fullResult, JunctionType junctionType) {
    	super(junctionType);
    	this.fullResult = fullResult;
    }
    
    @Override
    public void initBeforeResult() {
    	initPostSearch();    	
    }

	@Override
	protected Iterable<T> getFullResult() {
		return fullResult;
	}

	@Override
	protected boolean shouldApplyPostOrder() {
		return true;
	}    
    

}
