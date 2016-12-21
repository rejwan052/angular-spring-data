package skyglass.data.model.security;

import java.util.UUID;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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
@Table(name = "sec_authentication_realm", uniqueConstraints = {@UniqueConstraint(columnNames = {"name", "ghosted_date"})})
public class AuthenticationRealm extends AbstractEntity<UUID> {

    //******************************************************************************************************************
    // CLASS
    //******************************************************************************************************************
    @Transient
    static private final long serialVersionUID = 1L;

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

    @NotNull
    @Column(name = "ghosted_date")
    private long ghostedDate = 0;

    @NotNull
    @Column(name = "allowed_attempts")
    private int allowedAttempts = 0;

    @Basic
    @NotNull
    @Column(name = "sort_order")
    private int sortOrder;

    @Basic
    @NotNull
    @Length(min = 1, max = 1024)
    @Column(name = "login_module")
    private String loginModuleClassName;

    @Basic
    @Column(name = "read_only")
    @Type(type="yes_no")
    private boolean readOnly;


    //------------------------------------------------------------------------------------------------------------------
    @SuppressWarnings("unused")
    private AuthenticationRealm() {
    }

    public AuthenticationRealm(String name) {
        this.name = name;
    }
    
    public AuthenticationRealm(String name, String description, boolean enabled, int sortOrder,
            String loginModuleClassName, boolean readOnly, int allowedAttempts) {
        this.name = name;
        this.description = description;
        this.enabled = enabled;
        this.sortOrder = sortOrder;
        this.loginModuleClassName = loginModuleClassName;
        this.readOnly = readOnly;
        this.allowedAttempts = allowedAttempts;
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

    //----------------------------------------------------------------------------------------------
    public int getAllowedAttempts() {
        return allowedAttempts;
    }

    //----------------------------------------------------------------------------------------------
    public void setAllowedAttempts(int allowedAttempts) {
        this.allowedAttempts = allowedAttempts;
    }

    public boolean isGhosted() {
        return (ghostedDate != 0);
    }

    public void setGhostedDate (long ghostedDate) {
        if (this.ghostedDate != 0) {
            throw new UnsupportedOperationException("Cannot change the ghosted date once set.");
        } else if (ghostedDate == 0) {
            throw new IllegalStateException("Cannot unghost a user");
        }
        this.ghostedDate = ghostedDate;
    }

    public void setSortOrder(int sortOrder) {
        this.sortOrder = sortOrder;
    }

    public int getSortOrder() {
        return sortOrder;
    }

    public void setLoginModuleClassName(String loginModuleClassName) {
        this.loginModuleClassName = loginModuleClassName;
    }

    public String getLoginModuleClassName() {
        return loginModuleClassName;
    }

    /**
     * Determines if users can be created and their password edited.
     */
    public boolean isReadOnly() {
        return readOnly;
    }

    /**
     * Determines if users can be created and their password edited.
     */
    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }
   


}
