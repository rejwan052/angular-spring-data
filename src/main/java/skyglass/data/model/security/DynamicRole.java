package skyglass.data.model.security;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.Length;

@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="securityServerCache")
@Entity
@Table(name = "sec_dynamic_role", uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "sec_resource_type_id"})})
public class DynamicRole extends AbstractEntity<UUID> implements SimpleObject<UUID> {

    //******************************************************************************************************************
    // CLASS
    //******************************************************************************************************************
    @Transient
    static private final long serialVersionUID = -1952712749838439181L;

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
    @Length(max = 1024)
    @Column(name = "description")
    private String description;

    @Basic
    @NotNull
    @Column(name = "enabled")
    @Type(type="yes_no")
    private boolean enabled = true;

    @Basic
    @NotNull
    @Length(min = 1, max = 1024)
    @Column(name = "class_name")
    private String className;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sec_resource_type_id")
    private ResourceType resourceType;

    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="securityServerCache")
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "sec_dynamic_role_to_action", joinColumns = @JoinColumn(name = "sec_dynamic_role_id"),
        inverseJoinColumns = @JoinColumn(name = "sec_action_id"))
    private Set<Action> actions = new HashSet<Action>();

    // Eager causes Hibernate to go into a infinite loop
    // http://opensource.atlassian.com/projects/hibernate/browse/HHH-2277
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="securityServerCache")
    @OneToMany(mappedBy = "dynamicRole", fetch = FetchType.LAZY, orphanRemoval = true, cascade = CascadeType.ALL)
    Set<DynamicRoleProperty> properties = new HashSet<DynamicRoleProperty>();

    @Transient
    transient private Class<DynamicRoleCheck> checkClass;

    //------------------------------------------------------------------------------------------------------------------
    protected DynamicRole() {
        super();
    }
    
    public DynamicRole(ResourceType resourceType, String name) {
    	this(null, resourceType, name, null, true, null);
    }	
    
    public DynamicRole(Schema schema, ResourceType resourceType, String name, String description, boolean enabled, String className) {
        this.name = name;
        this.description = description;
        this.enabled = enabled;
        this.className = className;
        if (resourceType == null) {
            throw new IllegalArgumentException("Resource Type is required.");
        }
        if (name == null) {
            throw new IllegalArgumentException("Name is required.");
        }
        if (resourceType.dynamicRoleExists(name)) {
            throw new IllegalArgumentException("Dynamic role '" + name + "' already defined in resource type '" +
                resourceType.getName() + "'.");
        }

        this.resourceType = resourceType;
        this.name = name;
    }    

    //------------------------------------------------------------------------------------------------------------------
    @SuppressWarnings("unchecked")
    public boolean isUserInRole(User user, SecurityService securityService) {
        if (user != null && user.isGhosted()) {
            throw new IllegalArgumentException("User is ghosted");
        }
        try {
            if (checkClass == null) {
                checkClass = (Class<DynamicRoleCheck>) Class.forName(className);
            }

            DynamicRoleCheck check = checkClass.newInstance();

            return check.isUserInRole(this, user, securityService);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
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
    public ResourceType getResourceType() {
        return resourceType;
    }

    //------------------------------------------------------------------------------------------------------------------
    void setResourceType(ResourceType resourceType) {
        this.resourceType = resourceType;
    }

    //------------------------------------------------------------------------------------------------------------------
    public Schema getSchema() {
        return getResourceType().getSchema();
    }

    //------------------------------------------------------------------------------------------------------------------
    public String getClassName() {
        return className;
    }

    //------------------------------------------------------------------------------------------------------------------
    public void setClassName(String className) {
        this.className = className;
    }

    //------------------------------------------------------------------------------------------------------------------
    public void addAction(Action action) {
        if (action == null) {
            throw new NullPointerException("Action is required.");
        }
        if (!action.getSchema().equals(getSchema())) {
            throw new IllegalArgumentException("Can't add Action: " + action.getName() +
                " because it is from a different schema.");
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
        return getActions().contains(action);
    }
    public DynamicRoleProperty[] getProperties() {
        return properties.toArray(new DynamicRoleProperty[properties.size()]);
    }

    public void setProperty(String name, String value) {
        DynamicRoleProperty property = getPropertyObject(name);
        if (property == null) {
            property = new DynamicRoleProperty(this, name, value);
            properties.add(property);
        }
        else {
            property.setValue(value);
        }
    }

    public String getProperty(String name) {
        DynamicRoleProperty property = getPropertyObject(name);
        return property != null ? property.getValue() : null;
    }

    protected DynamicRoleProperty getPropertyObject(String name) {
        DynamicRoleProperty result = null;
        for (DynamicRoleProperty property : properties) {
            if (property.getName().equals(name)) {
                result = property;
                break;
            }
        }
        return result;
    }

    //------------------------------------------------------------------------------------------------------------------
    @Override
    public String toString() {
        String string = "Dynamic role: " + getName() + " (schema: " + getSchema().getName() + ", class: "
                + getClass().getCanonicalName() + ")";

        string += "\n\tactions: ";
        if (!getActions().isEmpty()) {
            for (Action action : getActions()) {
                string += "\n\t\t" + action.getName();
            }
        }
        else {
            string += "<none>";
        }

        string += "\n";

        return string;
    }
    
    public String[] getPropertyNames() {
    	String[] result = new String[properties.size()];
    	int i = 0;
    	for (DynamicRoleProperty property: properties) {
    		result[i] = property.getName();
    		i++;
    	}
        return result;
    }  
    
    public String getPropertyValue(String name) {
    	for (DynamicRoleProperty property: properties) {
    		if (property.getName().equals(name)) {
    			return property.getValue();
    		}
    	}
    	return null;
    }    

}
