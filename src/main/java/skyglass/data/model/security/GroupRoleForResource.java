package skyglass.data.model.security;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

@Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="securityServerCache")
@Entity
@Table(name = "sec_group_role_on_resource", uniqueConstraints = {@UniqueConstraint(columnNames = {
        "sec_group_id", "sec_role_id", "sec_resource_id"})})
public class GroupRoleForResource extends AbstractEntity<UUID> {

    //******************************************************************************************************************
    // CLASS
    //******************************************************************************************************************
    @Transient
    static private final long serialVersionUID = -1634608162196657699L;

    //******************************************************************************************************************
    // INSTANCE
    //******************************************************************************************************************
    @Id
    @Column(name = "id")
    @Type(type = "uuid-char")
    private UUID id = UUID.randomUUID();

    @ManyToOne
    @JoinColumn(name = "sec_group_id")
    private Group group;

    @ManyToOne
    @JoinColumn(name = "sec_role_id")
    private Role role;

    @ManyToOne
    @JoinColumn(name = "sec_resource_id")
    private SecurityResource resource;

    //------------------------------------------------------------------------------------------------------------------
    @SuppressWarnings("unused")
    private GroupRoleForResource() {

    }

    //------------------------------------------------------------------------------------------------------------------
    public GroupRoleForResource(Group group, Role role, SecurityResource resource) {
        this.group = group;
        this.role = role;
        this.resource = resource;
    }

    //------------------------------------------------------------------------------------------------------------------
    public UUID getId() {
        return id;
    }

    //------------------------------------------------------------------------------------------------------------------
    public Group getGroup() {
        return group;
    }

    //------------------------------------------------------------------------------------------------------------------
    @SuppressWarnings("unused")
    private void setUser(Group group) {
        this.group = group;
    }

    //------------------------------------------------------------------------------------------------------------------
    public Role getRole() {
        return role;
    }

    //------------------------------------------------------------------------------------------------------------------
    @SuppressWarnings("unused")
    private void setRole(Role role) {
        this.role = role;
    }

    //------------------------------------------------------------------------------------------------------------------
    public SecurityResource getResource() {
        return resource;
    }

    //------------------------------------------------------------------------------------------------------------------
    @SuppressWarnings("unused")
    private void setResource(SecurityResource resource) {
        this.resource = resource;
    }

}
