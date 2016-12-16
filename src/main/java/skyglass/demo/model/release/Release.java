package skyglass.demo.model.release;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;

import skyglass.demo.model.AbstractNameEntity;

@Entity
@Table(name = "release")
public class Release extends AbstractNameEntity<Long> {

	private static final long serialVersionUID = 6738346955865344153L;

	@Column(name = "publish_date", nullable = false)
	private Date publishDate;

	@ManyToOne(optional = false)
    @JoinColumn(name = "publisher_id")
	private Publisher publisher;

    @Column(name = "uuid", nullable = false)
    private String uuid; //external id

    @ManyToOne(optional = false)
    @JoinColumn(name = "category_id")
    private Category category;

    @PrePersist
    void onPersist() {
        this.publishDate = new Date();
    }

    public Date getPublishDate() {
        return publishDate;
    }

    public Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
