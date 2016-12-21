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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
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
@Table(name = "sec_group", uniqueConstraints = {@UniqueConstraint(columnNames = "name")})
public class Group extends AbstractEntity<UUID> implements SimpleObject<UUID> {

    //******************************************************************************************************************
    // CLASS
    //******************************************************************************************************************
    @Transient
    static private final long serialVersionUID = 7142641079889975718L;

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

    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region="securityServerCache")
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "sec_user_to_group",
        joinColumns = @JoinColumn(name = "sec_group_id"), inverseJoinColumns = @JoinColumn(name = "sec_user_id"))
    private Set<User> users = new HashSet<User>();

    private Group() {
        super();
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
    public Set<User> getUsers() {
        return users;
    }

    //------------------------------------------------------------------------------------------------------------------
    public boolean containsUser(User user) {
        return users.contains(user);
    }

    //------------------------------------------------------------------------------------------------------------------
    @SuppressWarnings("unused")
    private void setUsers(Set<User> users) {
        this.users = users;
    }

    //------------------------------------------------------------------------------------------------------------------
    public void addUser(User user) {
        if (!users.contains(user)) {
            users.add(user);
        }
    }

    //------------------------------------------------------------------------------------------------------------------
    public void removeUser(User user) {
        users.remove(user);
    }
    
    //------------------------------------------------------------------------------------------------------------------
    public void removeAllUsers() {
        users.clear();
    }    

    //------------------------------------------------------------------------------------------------------------------
    @Override
    public String toString() {
        String string = "Group: " + getName();

        return string;
    }
}
