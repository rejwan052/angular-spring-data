package skyglass.demo.model.release;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import skyglass.data.model.INameEntity;

@Entity(name = "subscriber")
@Table(name = "subscriber")
public class Subscriber implements INameEntity<Long> {

	@GenericGenerator(name = "generator", strategy = "foreign",
	parameters = @Parameter(name = "property", value = "releaseUser"))
	@Id
	@GeneratedValue(generator = "generator")
	@Column(name = "id", unique = true, nullable = false)
    private Long id;

	@OneToOne(fetch = FetchType.LAZY)
	@PrimaryKeyJoinColumn
    private ReleaseUser releaseUser;

    @Column(name = "name", nullable = false)
    private String name;
    
    public Subscriber() {
    	
    }
    
    public Subscriber(ReleaseUser releaseUser, String name) {
    	this.releaseUser = releaseUser;
    	this.name = name;
    }

    @Override
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ReleaseUser getReleaseUser() {
        return releaseUser;
    }

    public void setReleaseUser(ReleaseUser releaseUser) {
        this.releaseUser = releaseUser;
    }

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
