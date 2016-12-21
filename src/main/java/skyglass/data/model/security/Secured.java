package skyglass.data.model.security;

/**
 * Interface for a secured object.
 */
public interface Secured {

	/**
	 * Get the Schema identifier this Secured's Resource is in the security server.
	 */
	public Schema getSchema(SecuritySchemaHelper securitySchemaHelper);
	
	/**
	 * Get the ResourceType this Secured's Resource is a instance of in the the security server.
	 */
	public ResourceType getResourceType(SecuritySchemaHelper securitySchemaHelper);
	
	/**
	 * Get the Resource of this Secured in the security server.
	 */
	public SecurityResource getResource(SecuritySchemaHelper securitySchemaHelper);
	
	public boolean hasRead(SecuritySchemaHelper securitySchemaHelper);
	
	public boolean hasWrite(SecuritySchemaHelper securitySchemaHelper);
	
	public boolean hasSecurity(SecuritySchemaHelper securitySchemaHelper);
	
	public void assertRead(UserFactory userFactory, SecuritySchemaHelper securitySchemaHelper)
	throws SecurityException;
	
	public void assertWrite(UserFactory userFactory, SecuritySchemaHelper securitySchemaHelper)
	throws SecurityException;
	
	public void assertSecurity(UserFactory userFactory, SecuritySchemaHelper securitySchemaHelper)
	throws SecurityException;
}
