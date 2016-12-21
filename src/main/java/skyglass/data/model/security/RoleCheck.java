package skyglass.data.model.security;

/**
 * This class is the cached result of a user-in-role check performed by
 * {@link HibernateSecurityFactory#isUserInRole(SecurityUser, Role, SecurityResource, Object)}. The
 * user is implied by the user which is caching the check.
 */
abstract class RoleCheck {

    //******************************************************************************************************************
    // CLASS
    //******************************************************************************************************************

    //------------------------------------------------------------------------------------------------------------------
    /**
     * Returns a new instance.
     *
     * @param roleId
     *        a role ID. Non-<code>null</code>.
     * @param resourceId
     *        a resource ID. Non-<code>null</code>.
     * @return a new instance. Non-<code>null</code>.
     */
    static RoleCheck newInstance(String roleId, String resourceId, boolean inRole) {
        RoleCheck result = null;

        if (roleId == null) {
            throw new NullPointerException("roleId");
        }
        if (resourceId == null) {
            throw new NullPointerException("resourceId");
        }

        if (inRole) {
            result = new InRole(roleId, resourceId);
        }
        else {
            result = new NotInRole(roleId, resourceId);
        }

        return result;
    }

    //------------------------------------------------------------------------------------------------------------------
    /**
     * This class represents a role-check that indicates the user is not in the
     * specified role.
     */
    final static private class NotInRole extends RoleCheck {

        //--------------------------------------------------------------------------------------------------------------
        /**
         * Create a new instance.
         */
        NotInRole(String roleId, String resourceId) {
            super(roleId, resourceId);
        }

        //--------------------------------------------------------------------------------------------------------------
        /**
         * Unconditional returns false.
         */
        @Override
        public boolean isInRole() {
            return false;
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    /**
     * This class represents a role-check that indicates the user is in the
     * specified role.
     */
    final static private class InRole extends RoleCheck {

        //--------------------------------------------------------------------------------------------------------------
        /**
         * Create a new instance.
         */
        InRole(String roleId, String resourceId) {
            super(roleId, resourceId);
        }

        //--------------------------------------------------------------------------------------------------------------
        /**
         * Uncoditionally returns true.
         */
        @Override
        public boolean isInRole() {
            return true;
        }
    }

    //******************************************************************************************************************
    // INSTANCE
    //******************************************************************************************************************
    final private String roleId;
    final private String resourceId;

    //------------------------------------------------------------------------------------------------------------------
    /**
     * Constructs a new instance that represents a the result of a particular role check.
     */
    protected RoleCheck(String roleId, String resourceId) {
        this.roleId = roleId;
        this.resourceId = resourceId;
    }

    //------------------------------------------------------------------------------------------------------------------
    /**
     * Computes equality based on role ID and resource ID.
     */
    @Override
    final public boolean equals(Object obj) {
        if (obj instanceof RoleCheck) {
            RoleCheck rhs = (RoleCheck) obj;
            return roleId == rhs.roleId && resourceId == rhs.resourceId;
        }
        return false;
    }

    //------------------------------------------------------------------------------------------------------------------
    /**
     * Computes hashcode based on role ID and resource ID.
     */
    @Override
    final public int hashCode() {
        return roleId.hashCode() + (7 * resourceId.hashCode());
    }

    //------------------------------------------------------------------------------------------------------------------
    /**
     * Returns the role ID.
     */
    final public String getRoleId() {
        return roleId;
    }

    //------------------------------------------------------------------------------------------------------------------
    /**
     * Returns the resource ID.
     */
    final public String getResourceId() {
        return resourceId;
    }

    //--------------------------------------------------------------------------------------------------------------
    /**
     * Returns a string representation suitable for debugging.
     */
    @Override
    public String toString() {
        return "RoleCheck(role=" + getRoleId() + ", resource=" + getResourceId() + ") [" + isInRole() + "]";
    }

    //------------------------------------------------------------------------------------------------------------------
    /**
     * Check is if the user is in a particular role.
     */
    abstract public boolean isInRole();
}
