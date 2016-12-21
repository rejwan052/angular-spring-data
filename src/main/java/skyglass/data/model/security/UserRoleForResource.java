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
@Table(name = "sec_user_role_on_resource", uniqueConstraints = {@UniqueConstraint(columnNames = {
        "sec_user_id", "sec_role_id", "sec_resource_id"})})
public class UserRoleForResource extends AbstractEntity<UUID> {

    //******************************************************************************************************************
    // CLASS
    //******************************************************************************************************************
    @Transient
    static private final long serialVersionUID = 2170465844622380405L;

    //******************************************************************************************************************
    // INSTANCE
    //******************************************************************************************************************
    @Id
    @Column(name = "id")
    @Type(type = "uuid-char")
    private UUID id = UUID.randomUUID();

    @ManyToOne
    @JoinColumn(name = "sec_user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "sec_role_id")
    private Role role;

    @ManyToOne
    @JoinColumn(name = "sec_resource_id")
    private SecurityResource resource;

    //------------------------------------------------------------------------------------------------------------------
    @SuppressWarnings("unused")
    private UserRoleForResource() {

    }

    //------------------------------------------------------------------------------------------------------------------
    public UserRoleForResource(User user, Role role, SecurityResource resource) {
        this.user = user;
        this.role = role;
        this.resource = resource;
    }

    //------------------------------------------------------------------------------------------------------------------
    public UUID getId() {
        return id;
    }

    //------------------------------------------------------------------------------------------------------------------
    public User getUser() {
        return user;
    }

    //------------------------------------------------------------------------------------------------------------------
    @SuppressWarnings("unused")
    private void setUser(User user) {
        this.user = user;
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
