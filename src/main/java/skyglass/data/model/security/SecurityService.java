package skyglass.data.model.security;

public interface SecurityService {
	
	public ResourceType getResourceTypeByName(Schema schema, String name);
	
	public UserRoleForResource getUserRoleForResource(User user, Role role, SecurityResource securityResource);
	
	public void addUserToRoleForResource(User user, Role role, SecurityResource securityResource);
	
	public void removeUserFromRoleForResource(User user, Role role, SecurityResource securityResource);

}

