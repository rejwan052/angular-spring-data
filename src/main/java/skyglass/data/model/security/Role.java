package skyglass.data.model.security;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
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
@Table(name = "sec_role", uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "sec_schema_id"})})
public class Role extends AbstractEntity<UUID> implements SimpleObject<UUID> {

    //******************************************************************************************************************
    // CLASS
    //******************************************************************************************************************
    @Transient
    static private final long serialVersionUID = 6737471368141221261L;

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
    @Length(max = 1024)
    @Column(name = "description")
    private String description;

    @Basic
    @NotNull
    @Column(name = "enabled")
    @Type(type="yes_no")
    private boolean enabled = true;

    @ManyToOne
    @JoinColumn(name = "sec_schema_id")
    private Schema schema;

    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="securityServerCache")
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "sec_role_to_action", joinColumns = @JoinColumn(name = "sec_role_id"),
        inverseJoinColumns = @JoinColumn(name = "sec_action_id"))
    private Set<Action> actions = new HashSet<Action>();
    
    /*@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="securityServerCache")
    @OneToMany(mappedBy = "role", fetch = FetchType.LAZY)
    private Set<GroupRoleForResource> groupRoles = new HashSet<GroupRoleForResource>();   
    
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="securityServerCache")
    @OneToMany(mappedBy = "role", fetch = FetchType.LAZY)
    private Set<UserRoleForResource> userRoles = new HashSet<UserRoleForResource>();  */    

    //------------------------------------------------------------------------------------------------------------------
    @SuppressWarnings("unused")
    private Role() {
    }

    //------------------------------------------------------------------------------------------------------------------
    protected Role(String name, Schema schema) {
    	this(name, schema, null, true);
    }

    public Role(String name, Schema schema, String description, boolean enabled) {
        this(null, name,schema, description, enabled);
    }

    public Role(UUID id, String name, Schema schema, String description, boolean enabled) {
        if(id != null){
            this.id = id;
        }
        if (schema == null) {
            throw new NullPointerException("Schema is required.");
        }
        if (name == null) {
            throw new NullPointerException("Name is required.");
        }
        this.name = name;
        this.schema = schema;
        this.description = description;
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
    public String getDescription() {
        return description;
    }

    //------------------------------------------------------------------------------------------------------------------
    public void setDescription(String description) {
        this.description = description;
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
    public Schema getSchema() {
        return schema;
    }

    //------------------------------------------------------------------------------------------------------------------
    @SuppressWarnings("unused")
    private void setSchema(Schema schema) {
        this.schema = schema;
    }

    //------------------------------------------------------------------------------------------------------------------
    public void addAction(Action action)
    throws SecurityException {
        if (action == null) {
            throw new NullPointerException("Action is required.");
        }
        if (!action.getSchema().equals(schema)) {
            throw new SecurityException("Can't add Action: " + action.getName() + " because it is from "
                + "a different schema.");
        }

        if (hasAction(action)) {
            // I assume no exception should be thrown here
            return;
        }

        actions.add(action);
    }

    //------------------------------------------------------------------------------------------------------------------
    public void removeAction(Action action) {
        actions.remove(action);
    }

    //------------------------------------------------------------------------------------------------------------------
    public void removeAllActions() {
        actions.clear();
    }

    //------------------------------------------------------------------------------------------------------------------
    public boolean hasAction(Action action) {
        return actions.contains(action);
    }

    //------------------------------------------------------------------------------------------------------------------
    public Set<Action> getActions() {
        return actions;
    }

    //------------------------------------------------------------------------------------------------------------------
    @SuppressWarnings("unused")
    private void setActions(Set<Action> actions) {
        this.actions = actions;
    }    

    //------------------------------------------------------------------------------------------------------------------
    boolean allowsAction(Action action) {
        return actions.contains(action);
    }
    
    /*public Set<UserRoleForResource> getUserRoles() {
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
    }   */ 

    //------------------------------------------------------------------------------------------------------------------
    @Override
    public String toString() {
        String string = "Role: " + getName() + " (schema: " + getSchema().getName() + ")";

        return string;
    }

}
