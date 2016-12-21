package skyglass.data.filter;

public enum FilterType {
	EQ("eq"),
	LIKE("like"),
	GE("ge"),
	GT("gt"),
	LE("le"),
	LT("lt");
	
	private String name;
	
	private FilterType(String name) {
		this.name = name;
	}
	
	public static FilterType nameOf(String name) {
		for (FilterType filterType: FilterType.values()) {
			if (filterType.name.equals(name)) {
				return filterType;
			}
		}
		return FilterType.EQ;
	}
}
