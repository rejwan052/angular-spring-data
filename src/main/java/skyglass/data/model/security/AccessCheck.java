package skyglass.data.model.security;

import java.util.Arrays;
import java.util.List;

/**
 * This class is the cached result of an access check performed by
 * {@link HibernateSecurityFactory#checkAccess(SecurityUser, Action, SecurityResource, Object)}. The
 * user is implied by the user which is caching the check.
 */
abstract class AccessCheck {

    //******************************************************************************************************************
    // CLASS
    //******************************************************************************************************************

    //------------------------------------------------------------------------------------------------------------------
    /**
     * Returns a new instance representing an access check. <code>null</code> is
     * permitted for <code>actionIds</code> and is treated as an empty list.
     * Action IDs must be distinct, non-<code>null</code>, and in order by
     * smallest to largest.
     *
     * @param resourceId
     *        a resource ID. Non-<code>null</code>.
     * @param actionIds
     *        a list of distinct, ordered, valid non-<code>null</code> action
     *        IDs. <code>null</code> is treated as an empty list.
     * @return a new instance. Non-<code>null</code>.
     */
    static AccessCheck newInstance(SecurityResource resource, List<String> actionIds) {
        AccessCheck result = null;

        if (resource == null) {
            throw new NullPointerException("resource");
        }

        if (actionIds == null || actionIds.size() == 0) {
            result = new AccessDenied(resource);
        }
        else {
            String[] idArray = actionIds.toArray(new String[actionIds.size()]);
            result = new AccessAllowed(resource, idArray);
        }

        return result;
    }

    //------------------------------------------------------------------------------------------------------------------
    /**
     * This class represents an access check that denies all access.
     */
    final static private class AccessDenied extends AccessCheck {

        //--------------------------------------------------------------------------------------------------------------
        /**
         * Create an instance.
         */
        AccessDenied(SecurityResource resource) {
            super(resource);
        }

        //--------------------------------------------------------------------------------------------------------------
        /**
         * Unconditionally returns false.
         */
        @Override
        public boolean isAccessAllowed(String actionId) {
            return false;
        }

        //--------------------------------------------------------------------------------------------------------------
        /**
         * Returns a string representation suitable for debugging.
         */
        @Override
        public String toString() {
            return "AccessCheck(resource=" + getResource().getId() + ") [denied]";
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    /**
     * This class represents an access check that grants access for some
     * actions.
     */
    final static private class AccessAllowed extends AccessCheck {
        final private String[] actionIds;

        //--------------------------------------------------------------------------------------------------------------
        /**
         * Creates a new instance.
         */
        AccessAllowed(SecurityResource resource, String[] actionIds) {
            super(resource);
            if (actionIds == null) {
                throw new NullPointerException("actionIds");
            }
            this.actionIds = actionIds;
        }

        //--------------------------------------------------------------------------------------------------------------
        /**
         * Returns <code>true</code> if the action is valid, <code>false</code>
         * otherwise.
         */
        @Override
        public boolean isAccessAllowed(String actionId) {
            return Arrays.binarySearch(actionIds, actionId) >= 0;
        }

        //--------------------------------------------------------------------------------------------------------------
        /**
         * Returns a string representation suitable for debugging.
         */
        @Override
        public String toString() {
            return "AccessCheck(resource=" + getResource().getId() + ") " + Arrays.toString(actionIds);
        }
    }

    //******************************************************************************************************************
    // INSTANCE
    //******************************************************************************************************************
    final private SecurityResource resource;

    //------------------------------------------------------------------------------------------------------------------
    /**
     * Creates a new instance. This constructor is private to prevent the
     * creation of sub-classes outside this class.
     */
    protected AccessCheck(SecurityResource resource) {
        this.resource = resource;
    }

    //------------------------------------------------------------------------------------------------------------------
    /**
     * Compares for equality based on resource ID.
     */
    @Override
    final public boolean equals(Object obj) {
        if (obj instanceof AccessCheck) {
            AccessCheck rhs = (AccessCheck) obj;
            return resource.equals(rhs.resource);
        }
        return false;
    }

    //------------------------------------------------------------------------------------------------------------------
    /**
     * Computes a hashcode based on resource ID.
     */
    @Override
    final public int hashCode() {
        return resource.hashCode();
    }

    //------------------------------------------------------------------------------------------------------------------
    /**
     * Returns the resource ID.
     */
    final public SecurityResource getResource() {
        return resource;
    }

    //------------------------------------------------------------------------------------------------------------------
    /**
     * Checks if access is allowed for a given action.
     */
    abstract public boolean isAccessAllowed(String actionId);
}
