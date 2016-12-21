package skyglass.data.model.security;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import javax.persistence.CascadeType;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.Length;

@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="securityServerCache")
@Entity
@Table(name = "sec_schema", uniqueConstraints = {@UniqueConstraint(columnNames = "name")})
public class Schema extends AbstractEntity<UUID> implements SimpleObject<UUID> {

    //******************************************************************************************************************
    // CLASS
    //******************************************************************************************************************
    @Transient
    static private final long serialVersionUID = -6594102948975922280L;

    //******************************************************************************************************************
    // INSTANCE
    //******************************************************************************************************************
    @Id
    @Column(name = "id")
    @Type(type = "uuid-char")
    private UUID id = UUID.randomUUID();

    @Basic
    @NotNull
    @Length(min = 1, max = 64)
    @Column(name = "name")
    private String name;

    @Basic
    @NotNull
    @Column(name = "enabled")
    @Type(type="yes_no")
    private boolean enabled = true;

    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="securityServerCache")
    @OneToMany(mappedBy = "schema", cascade = CascadeType.ALL)
    private Set<Role> roles = new HashSet<Role>();

    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="securityServerCache")
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "schema", cascade = CascadeType.ALL)
    private Set<ResourceType> resourceTypes = new HashSet<ResourceType>();

    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="securityServerCache")
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "schema", cascade = CascadeType.ALL)
    private Set<Action> actions = new HashSet<Action>();

    //------------------------------------------------------------------------------------------------------------------
    protected Schema() {

    }

    //------------------------------------------------------------------------------------------------------------------
    public Schema(String name) {
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
    @SuppressWarnings("unused")
    private void setName(String name) {
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
    public Set<Role> getRoles() {
        return roles;
    }
    
    //------------------------------------------------------------------------------------------------------------------
    public Role createRole(String name) throws SecurityException {
    	return createRole(null, name);
    }    

    //------------------------------------------------------------------------------------------------------------------
    public Role createRole(String id, String name) throws SecurityException {
        if (name == null) {
            throw new NullPointerException("Name is required.");
        }
        if (roleExists(name)) {
            throw new SecurityException("Role '" + name + "' already exists in schema '" + getName() + "'");
        }

        Role role = new Role(name, this);
        roles.add(role);

        return role;
    }

    //------------------------------------------------------------------------------------------------------------------
    public Set<ResourceType> getResourceTypes() {
        return resourceTypes;
    }

    //------------------------------------------------------------------------------------------------------------------
    /*public ResourceType createResourceType(String name)
    throws SecurityException {
        if (name == null) {
            throw new NullPointerException("Name is required.");
        }
        if (resourceTypeExists(name)) {
            throw new SecurityException("Resource Type '" + name + "' already exists in schema '" + getName() + "'");
        }

        ResourceType type = new ResourceType(this, name);
        getResourceTypes().add(type);

        return type;
    } */

    //------------------------------------------------------------------------------------------------------------------
    public boolean resourceTypeExists(SecurityService securityService, String name) {
        boolean result = false;

        ResourceType resourceType = securityService.getResourceTypeByName(this, name);
        if (resourceType != null) {
            result = true;
        }

        return result;
    }

    //------------------------------------------------------------------------------------------------------------------
    @Override
    public String toString() {
        String string = "Schema: " + getName();

        return string;
    }

    //------------------------------------------------------------------------------------------------------------------
    public boolean roleExists(String name) {
        boolean result = false;

        Role role = getRoleByName(name);
        if (role != null) {
            result = true;
        }

        return result;
    }

    //------------------------------------------------------------------------------------------------------------------
    public Role getRoleByName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Name is required.");
        }

        Role result = null;

        for (Role role : roles) {
            if (role.getName().equals(name)) {
                result = role;
            }
        }

        return result;
    }

    //------------------------------------------------------------------------------------------------------------------
    public Set<Action> getActions() {
        return actions;
    }
    
    public void removeRole(Role role) {
    	roles.remove(role);
    }

    //------------------------------------------------------------------------------------------------------------------
    public Action createAction(String name)
    throws SecurityException {
        if (name == null) {
            throw new NullPointerException("Name is required.");
        }
        if (actionExists(name)) {
            throw new SecurityException("Action '" + name + "' already exists in resource type '" + getName() + "'");
        }

        Action action = new Action(this, name);
        actions.add(action);

        return action;
    }

    //------------------------------------------------------------------------------------------------------------------
    public boolean actionExists(String name) {
        return getActionByName(name) != null;
    }

    //------------------------------------------------------------------------------------------------------------------
    public Action getActionByName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Name is required.");
        }

        Action result = null;

        for (Action action : getActions()) {
            if (action.getName().equals(name)) {
                result = action;
            }
        }

        return result;
    }
    
    public void addRole(Role role) {
    	getRoles().add(role);
    }
}
