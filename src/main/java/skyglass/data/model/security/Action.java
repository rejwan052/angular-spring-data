package skyglass.data.model.security;

import java.util.UUID;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;
import org.hibernate.validator.constraints.Length;

import skyglass.data.common.model.NameableObject;

@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="securityServerCache")
@Entity
@Table(name = "sec_action", uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "sec_schema_id"})})
public class Action extends AbstractEntity<UUID> implements NameableObject {

    //******************************************************************************************************************
    // CLASS
    //******************************************************************************************************************
    @Transient
    static private final long serialVersionUID = 1333537317909093308L;

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
    @Column(name = "cascading")
    @Type(type="yes_no")
    private boolean cascading = false;

    @ManyToOne
    @JoinColumn(name = "sec_schema_id")
    private Schema schema;

    //------------------------------------------------------------------------------------------------------------------
    @SuppressWarnings("unused")
    private Action() {

    }

    //------------------------------------------------------------------------------------------------------------------
    public Action(Schema schema, String name) {
        this.schema = schema;
        this.name = name;
    }

    //------------------------------------------------------------------------------------------------------------------
    @Override
    public UUID getId() {
        return id;
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
    public String getName() {
        return name;
    }

    //------------------------------------------------------------------------------------------------------------------
    @SuppressWarnings("unused")
    private void setName(String name) {
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
    public boolean isCascading() {
        return cascading;
    }

    //------------------------------------------------------------------------------------------------------------------
    public void setCascading(boolean cascading) {
        this.cascading = cascading;
    }

    //------------------------------------------------------------------------------------------------------------------
    @Override
    public String toString() {
        String string = "Action: " + getName() + " (type: " + getSchema().getName() + ")";

        return string;
    }
}
