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
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import javax.persistence.CascadeType;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.Length;
import skyglass.data.common.util.StringUtil;

@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="securityServerCache")
@Entity
@Table(name = "sec_resource_type", uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "sec_schema_id"})})
public class ResourceType extends AbstractEntity<UUID> implements SimpleObject<UUID> {

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
    @Length(min = 1, max = 256)
    @Column(name = "name")
    private String name;

    @Basic
    @NotNull
    @Column(name = "enabled")
    @Type(type="yes_no")
    private boolean enabled = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sec_schema_id")
    private Schema schema;

    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="securityServerCache")
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "resourceType", orphanRemoval = true, cascade = CascadeType.ALL)
    private Set<DynamicRole> dynamicRoles = new HashSet<DynamicRole>();

    //------------------------------------------------------------------------------------------------------------------
    protected ResourceType() {
    }

    //------------------------------------------------------------------------------------------------------------------
    public ResourceType(Schema schema, String name) {
        if (schema == null) {
            throw new NullPointerException("Schema is required.");
        }
        if (name == null) {
            throw new NullPointerException("Name is required.");
        }

        this.schema = schema;
        this.name = name;
    }

    //------------------------------------------------------------------------------------------------------------------
    @Override
    public UUID getId() {
        return id;
    }
    
    public String getStringId() {
    	return id.toString();
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
    public Schema getSchema() {
        return schema;
    }

    //------------------------------------------------------------------------------------------------------------------
    @SuppressWarnings("unused")
    private void setSchema(Schema schema) {
        this.schema = schema;
    }

    //------------------------------------------------------------------------------------------------------------------
    public Set<Action> getActions() {
        return getSchema().getActions();
    }

    //------------------------------------------------------------------------------------------------------------------
    public void addDynamicRole(DynamicRole role) {
        getDynamicRoles().add(role);
    }

    //------------------------------------------------------------------------------------------------------------------
    public void removeDynamicRole(DynamicRole role) {
        dynamicRoles.remove(role);
    }

    //------------------------------------------------------------------------------------------------------------------
    public boolean dynamicRoleExists(String name) {
        boolean result = false;

        DynamicRole dynamicRole = getDynamicRoleByName(name);
        if (dynamicRole != null) {
            result = true;
        }

        return result;
    }

    //------------------------------------------------------------------------------------------------------------------
    public DynamicRole getDynamicRoleByName(String name) {
        if (name == null) {
            throw new IllegalArgumentException("Name is required.");
        }

        DynamicRole result = null;

        for (DynamicRole role : dynamicRoles) {
            if (role.getName().equals(name)) {
                result = role;
            }
        }

        return result;
    }

    //------------------------------------------------------------------------------------------------------------------
    public DynamicRole getDynamicRoleById(UUID id) {
        if (id == null) {
            throw new IllegalArgumentException("Id is required.");
        }

        DynamicRole result = null;

        for (DynamicRole role : dynamicRoles) {
            if (role.getId().equals(id)) {
                result = role;
            }
        }

        return result;
    }

    //------------------------------------------------------------------------------------------------------------------
    public Set<DynamicRole> getDynamicRoles() {
        return dynamicRoles;
    }

    /**
     * Create a new DynamicRole for the ResourceType. The DynamicRole is not saved to be persisted when returned.
     * @param name the name of the DynamicRole to create
     * @return a unsaved DynamicRole for the ResourceType
     * @throws SecurityException a DynamicRole with the same name already exists on the ResourceType
     */
    public DynamicRole createDynamicRole(String name)
    throws SecurityException {
        if (StringUtil.isEmpty(name)) {
            throw new IllegalArgumentException("Name is required.");
        }
        if (dynamicRoleExists(name)) {
            throw new SecurityException("Dynamic role with name '" + name + "' already exists.");
        } 
        DynamicRole dynamicRole = new DynamicRole(this, name);
        addDynamicRole(dynamicRole);
        return dynamicRole;
    }

    //------------------------------------------------------------------------------------------------------------------
    @Override
    public String toString() {
        String string = "Resource Type: " + getName() + " (schema: " + getSchema().getName() + ")";

        return string;
    }

}
