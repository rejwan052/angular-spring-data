package skyglass.data.model.security;


/**
 * Interface for the logic of determining if a SecurityUser is in a DynamicRole for a given Resource.
 */
public interface DynamicRoleCheck {

    /**
     * Test whether a user belongs to a role for a single resource.
     * @param role
     * @param user
     * @param resource
     * @return
     */
    public boolean isUserInRole(DynamicRole role, User user, SecurityService securityService);
    
}
