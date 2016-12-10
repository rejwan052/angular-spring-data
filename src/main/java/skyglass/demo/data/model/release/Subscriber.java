package skyglass.demo.data.model.release;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.fasterxml.jackson.annotation.JsonIgnore;

import skyglass.demo.data.model.security.User;

@Entity(name = "subscriber")
public class Subscriber {

	@GenericGenerator(name = "generator", strategy = "foreign",
	parameters = @Parameter(name = "property", value = "user"))
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name = "id", unique = true, nullable = false)
    private Long id;

	@OneToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn
    @JsonIgnore
    private User user;

    @Column(name = "name", nullable = false)
    private String name;
    
	@OneToMany(mappedBy = "subscriber", fetch = FetchType.LAZY)
	@Cascade(CascadeType.ALL)
	private List<Subscription> subscriptions = new ArrayList<Subscription>();
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
	public List<Subscription> getSubscriptions() {
		return subscriptions;
	}

	public void setSubscriptions(List<Subscription> subscriptions) {
		this.subscriptions = subscriptions;
	}    
}
