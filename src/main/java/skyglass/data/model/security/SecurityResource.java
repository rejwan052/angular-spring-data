package skyglass.data.model.security;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PostPersist;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.Length;

@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="securityServerCache")
@Entity
@Table(name = "sec_resource")
public class SecurityResource extends AbstractEntity<UUID> {

    //******************************************************************************************************************
    // CLASS
    //******************************************************************************************************************
    @Transient
    static private final long serialVersionUID = -8419495961298789741L;

    //******************************************************************************************************************
    // INSTANCE
    //******************************************************************************************************************
    @Id
    @Column(name = "id")
    @Type(type = "uuid-char")
    private UUID id = UUID.randomUUID();

    @Basic
    @NotNull
    @Length(min = 1, max = 255)
    @Column(name = "name")
    private String name;

    @Basic
    @NotNull
    @Column(name = "enabled")
    @Type(type="yes_no")
    private boolean enabled = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sec_resource_type_id")
    private ResourceType type;
    
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="securityServerCache")
    @OneToMany(mappedBy = "resource", fetch = FetchType.LAZY)
    private Set<GroupRoleForResource> groupRoles = new HashSet<GroupRoleForResource>();   
    
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="securityServerCache")
    @OneToMany(mappedBy = "resource", fetch = FetchType.LAZY)
    private Set<UserRoleForResource> userRoles = new HashSet<UserRoleForResource>();      

    //------------------------------------------------------------------------------------------------------------------
    protected SecurityResource() {

    }

    //------------------------------------------------------------------------------------------------------------------
    public SecurityResource(ResourceType type, String name) {
        if (type == null) {
            throw new NullPointerException("Resource Type is required.");
        }
        if (name == null) {
            throw new NullPointerException("Name is required.");
        }

        this.type = type;
        this.name = name;
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

    public Schema getSchema() {
        return getResourceType().getSchema();
    }

    //------------------------------------------------------------------------------------------------------------------
    public ResourceType getResourceType() {
        return type;
    }

    //------------------------------------------------------------------------------------------------------------------
    @SuppressWarnings("unused")
    private void setResourceType(ResourceType type) {
        this.type = type;
    }

    //------------------------------------------------------------------------------------------------------------------
    @Override
    public String toString() {
        String string = "Resource: " + getName() + " (type: " + getResourceType().getName() + ")";

        return string;
    }

    //------------------------------------------------------------------------------------------------------------------
    public void addUserToRole(User user, Role role, SecurityService securityService) throws SecurityException {
        if (!role.getSchema().equals(getResourceType())) {
            throw new SecurityException("Role schema does not match this resource schema.");
        }
        // ignore adding a user to a role multiple times
        if (securityService.getUserRoleForResource(user, role, this) == null) {
            securityService.addUserToRoleForResource(user, role, this);
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    public void removeUserFromRole(User user, Role role, SecurityService securityService) {
        securityService.removeUserFromRoleForResource(user, role, this);
    }

    public Set<UserRoleForResource> getUserRoles() {
    	return userRoles;
    }
    
    public Set<GroupRoleForResource> getGroupRoles() {
    	return groupRoles;
    }
    
    public void addUserRole(UserRoleForResource userRoleForResource) {
    	userRoles.add(userRoleForResource);
    }
    
    public void addGroupRole(GroupRoleForResource groupRoleForResource) {
    	groupRoles.add(groupRoleForResource);
    }
    
    public void removeUserRole(UserRoleForResource userRoleForResource) {
    	userRoles.remove(userRoleForResource);
    }
    
    public void removeGroupRole(GroupRoleForResource groupRoleForResource) {
    	groupRoles.remove(groupRoleForResource);
    }      

    /**
     * Method for persistence, do not use.
     */
    @PostPersist
    public void postPersist() {
        // maintain the resource structure
    }
}
