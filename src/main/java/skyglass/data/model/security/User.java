package skyglass.data.model.security;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.security.GeneralSecurityException;
import java.security.Principal;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang.StringUtils;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.Length;

import skyglass.data.common.util.CryptStringUtil;

@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="securityServerCache")
@Entity
@Table(name = "sec_user", uniqueConstraints = {@UniqueConstraint(columnNames = { "name", "sec_authentication_realm_id" })})
public class User extends AbstractEntity<UUID> implements Principal, SimpleObject<UUID>, Comparable<User> {

    //******************************************************************************************************************
    // CLASS
    //******************************************************************************************************************
    //**********************************************************************************************
    // CLASS
    //**********************************************************************************************

    static private final long serialVersionUID = 5213848455014653270L;

    static private final int ROLE_CACHE_SIZE = 16;

    static private final int ACCESS_CACHE_SIZE = 16;    

    //******************************************************************************************************************
    // INSTANCE
    //******************************************************************************************************************
    @Id
    @Column(name = "id")
    @Type(type = "uuid-char")
    private UUID id = UUID.randomUUID();

    @Basic
    @NotNull
    @Length(min = 1, max = 256)
    @Column(name = "name")
    private String name;

    @Basic
    @Length(max = 256)
    @Column(name = "password", updatable = false)
    private String password;

    @Basic
    @Column(name = "enabled")
    @Type(type="yes_no")
    private boolean enabled = true;

    @Basic
    @Length(max = 256)
    @Column(name = "actual_name")
    private String actualName;

    @Basic
    @Length(max = 256)
    @Column(name = "email")
    private String email;

    @Basic
    @NotNull
    @Column(name = "failed_attempts")
    private int failedAttempts;

    @Basic
    @NotNull
    @Column(name = "ghosted_date", updatable = false)
    private long ghostedDate;

    @ManyToOne
    @JoinColumn(name = "sec_authentication_realm_id")
    private AuthenticationRealm authenticationRealm;
    
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="securityServerCache")
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "users")
    private Set<Group> groups = new HashSet<Group>();

    @Transient
    private String authenticationRealmId;

    @Transient
    transient private LinkedList<RoleCheck> roleCheckCache = new LinkedList<RoleCheck>();

    @Transient
    transient private LinkedList<AccessCheck> accessCheckCache = new LinkedList<AccessCheck>();

    //------------------------------------------------------------------------------------------------------------------
    protected User() {
    }

    //------------------------------------------------------------------------------------------------------------------
    protected User(AuthenticationRealm authenticationRealm, String name)
    throws SecurityException {
        this(authenticationRealm, name, null);
    }    

    /**
     * Create a SecurityUser in the given AuthenticationRealm with the name unique to the realm and the clear text
     * password.
     * @param authenticationRealm the AuthenticationRealm to create the user in
     * @param name the name of the user unique to the AuthenticationRealm
     * @param password the password in clear text if this security server is maintaining the password. empty if the
     *                 user is authenticated externally
     * @throws SecurityException
     */
    public User(AuthenticationRealm authenticationRealm, String name, String password)
    throws SecurityException {
    	this(authenticationRealm, name, password, true);
    }
    
    //----------------------------------------------------------------------------------------------
    public User(AuthenticationRealm authenticationRealm, String name, String password, boolean enabled)  throws SecurityException  {
        this.authenticationRealm = authenticationRealm;
        this.name = name;
        if (StringUtils.isNotEmpty(password)) {
            try {
                this.password = CryptStringUtil.encrypt(password);
            }
            catch (Exception e) {
                throw new SecurityException("Unable to encrypt new user password.", e);
            }
        }
        this.enabled = enabled;
    }    

    //------------------------------------------------------------------------------------------------------------------
    @Override
    public UUID getId() {
        return id;
    }

    //------------------------------------------------------------------------------------------------------------------
    public String getName() {
        return name;
    }

    //------------------------------------------------------------------------------------------------------------------
    public void setName(String name) {
        this.name = name;
    }

    //------------------------------------------------------------------------------------------------------------------
    public boolean isEnabled() {
        return enabled;
    }

    //------------------------------------------------------------------------------------------------------------------
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    //------------------------------------------------------------------------------------------------------------------
    public String getActualName() {
        return actualName;
    }
    
    //------------------------------------------------------------------------------------------------------------------
    public void setActualName(String actualName) {
        this.actualName = actualName;
    }

    //------------------------------------------------------------------------------------------------------------------
    public String getEmail() {
        return email;
    }

    //------------------------------------------------------------------------------------------------------------------
    public void setEmail(String email) {
        this.email = email;
    }

    //------------------------------------------------------------------------------------------------------------------
    public AuthenticationRealm getAuthenticationRealm() {
        return authenticationRealm;
    }

    //----------------------------------------------------------------------------------------------
    public int getFailedAttempts() {
        return failedAttempts;
    }

    //----------------------------------------------------------------------------------------------
    public void setFailedAttempts(int failedAttempts) {
        this.failedAttempts += failedAttempts;
    }

    //----------------------------------------------------------------------------------------------
    public void addFailedAttempt() {
        failedAttempts += 1;
    }

    //----------------------------------------------------------------------------------------------
    public void clearFailedAttempts() {
        failedAttempts = 0;
    }

    //------------------------------------------------------------------------------------------------------------------
    public boolean isGhosted() {
        return getGhostedDate() != 0;
    }

    //----------------------------------------------------------------------------------------------
    public long getGhostedDate() {
        return ghostedDate;
    }

    //----------------------------------------------------------------------------------------------
    public void setGhostedDate(long ghostedDate) {
        if (ghostedDate == 0 && this.ghostedDate != 0) {
            throw new IllegalStateException("Cannot unghost a user");
        }
        this.ghostedDate = ghostedDate;
    }

    //------------------------------------------------------------------------------------------------------------------
    public Set<Group> getGroups() {
        return groups;
    }
    
    //------------------------------------------------------------------------------------------------------------------
    /**
     * Returns a cached {@link RoleCheck}, or <code>null</code> if the role
     * check is not cached.
     *
     * @param roleId
     *        a role ID. Non-<code>null</code>.
     * @param resourceId
     *        a resource ID. Non-<code>null</code>.
     * @return a <code>RoleCheck</code> instance. <code>null</code> if not
     *         cached.
     */
    RoleCheck getCachedRoleCheck(String roleId, String resourceId) {
        if (roleId == null) {
            throw new NullPointerException("roleId");
        }
        if (resourceId == null) {
            throw new NullPointerException("resourceId");
        }

        RoleCheck target = RoleCheck.newInstance(roleId, resourceId, false);
        for (Iterator<RoleCheck> i = roleCheckCache.iterator(); i.hasNext();) {
            RoleCheck roleCheck = i.next();
            if (target.equals(roleCheck)) {
                i.remove();
                roleCheckCache.addFirst(roleCheck);
                return roleCheck;
            }
        }
        return null;
    }

    //------------------------------------------------------------------------------------------------------------------
    /**
     * Caches the result of a role check and returns the {@link RoleCheck}
     * object. This method may not actually update the cache, depending on the
     * cache policy, but a <code>RoleCheck</code> object is always returned.
     * Adding a new element to the cache may invalidate other elements.
     *
     * @param roleId
     *        a role ID. Non-<code>null</code>.
     * @param resourceId
     *        a resource ID. Non-<code>null</code>.
     * @param inRole
     *        <code>true</code> if the user is in the role, <code>false</code>
     *        otherwise.
     * @return a <code>RoleCheck</code>. Non-<code>null</code>.
     */
    RoleCheck cacheRoleCheck(String roleId, String resourceId, boolean inRole) {
        if (roleId == null) {
            throw new NullPointerException("roleId");
        }
        if (resourceId == null) {
            throw new NullPointerException("resourceId");
        }

        RoleCheck roleCheck = RoleCheck.newInstance(roleId, resourceId, inRole);
        roleCheckCache.addFirst(roleCheck);
        if (roleCheckCache.size() > ROLE_CACHE_SIZE) {
            roleCheckCache.removeLast();
        }
        return roleCheck;
    }

    //------------------------------------------------------------------------------------------------------------------
    /**
     * Returns a cached {@link AccessCheck}, or <code>null</code> if the access
     * check is not cached.
     *
     * @param resource
     *        a resource. Non-<code>null</code>.
     * @return an <code>AccessCheck</code> instance, or <code>null</code> if not
     *         cached.
     */
    AccessCheck getCachedAccessCheck(SecurityResource resource) {
        if (resource == null) {
            throw new NullPointerException("resourceId");
        }

        AccessCheck target = AccessCheck.newInstance(resource, null);
        for (Iterator<AccessCheck> i = accessCheckCache.iterator(); i.hasNext();) {
            AccessCheck accessCheck = i.next();
            if (target.equals(accessCheck)) {
                i.remove();
                accessCheckCache.addFirst(accessCheck);
                return accessCheck;
            }
        }
        return null;
    }

    //------------------------------------------------------------------------------------------------------------------
    /**
     * Caches the result of an access check and returns the {@link AccessCheck}
     * object. This method may not actually update the cache, depending on the
     * cache policy, but an <code>AccessCheck</code> object is always returned.
     * Adding a new element to the cache may invalidate other elements. Action
     * IDs must be distinct, non-<code>null</code>, and in order by smallest to
     * largest.
     *
     * @param resource
     *        a resource. Non-<code>null</code>.
     * @param actionIds
     *        a list of distint, ordered, non-<code>null</code>, valid action
     *        IDs. Non-<code>null</code>.
     * @return an <code>AccessCheck</code>. Non-<code>null</code>.
     */
    AccessCheck cacheAccessCheck(SecurityResource resource, List<String> actionIds) {
        if (resource == null) {
            throw new NullPointerException("resourc");
        }
        if (actionIds == null) {
            throw new NullPointerException("actionIds");
        }

        AccessCheck accessCheck = AccessCheck.newInstance(resource, actionIds);
        accessCheckCache.addFirst(accessCheck);
        if (accessCheckCache.size() > ACCESS_CACHE_SIZE) {
            accessCheckCache.removeLast();
        }
        return accessCheck;
    }

    //------------------------------------------------------------------------------------------------------------------
    /**
     * Performs custom deserialization. Transient caches are reinitialized, but
     * all other deserialization is performed by
     * {@link ObjectInputStream#defaultReadObject()}.
     */
    private void readObject(ObjectInputStream in)
    throws IOException, ClassNotFoundException {
        roleCheckCache = new LinkedList<RoleCheck>();
        accessCheckCache = new LinkedList<AccessCheck>();
        in.defaultReadObject();
    }

    //------------------------------------------------------------------------------------------------------------------
    @Override
    public String toString() {
        return "User:"+getDisplayName()+" ("+getId()+")";
    }

    /**
     * Check if the value is the user's password.
     * @param decryptedPassword the clear text password
     * @return true if it matches, else false
     * @throws GeneralSecurityException
     */
    public boolean isPassword(String decryptedPassword)
    throws GeneralSecurityException {
        return decryptedPassword.equals(CryptStringUtil.decrypt(password));
    }
    
    public boolean isDeleted() {
        return isGhosted();
    }  
    
    //----------------------------------------------------------------------------------------------
    public boolean isLockedOut() {
    	AuthenticationRealm realm = getAuthenticationRealm();
        int allowedAttempts = realm.getAllowedAttempts();
        return allowedAttempts > 0 && getFailedAttempts() >= allowedAttempts;
    }

    //----------------------------------------------------------------------------------------------
    public long getDeletedDate() {
        return getGhostedDate();
    } 
    
  
    /**
     * @return a reasonable display name for this user. Will include both the actual name and user
     *         name, or only the name if they are identical.
     */
    public String getDisplayName() {
        String result = getName();

        if (!StringUtils.isEmpty(getActualName())
                && !getName().equals(getActualName())) {
            result = getActualName()+" ("+getName()+")";
        }

        return result;
    }  
    
    public String getNameOrActualName() {
        String result = getActualName();
        if (result == null || result.length() == 0) {
            result = getName();
        }
        return result;
    }      
    
    //----------------------------------------------------------------------------------------------
    public boolean isAdmin() {
        return getId().equals(SecuritySchemaHelper.ADMIN_USER_ID);
    }    

    public int compareTo(User other) {
        return getDisplayName().compareTo(other.getDisplayName());
    }
    
    public void addGroup(Group group) {
    	groups.add(group);
    }
    
    public void removeGroup(Group group) {
    	groups.remove(group);
    }
    
    public void removeAllGroups() {
    	groups.clear();
    }   
    
}
