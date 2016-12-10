package skyglass.demo.data.model.release;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity(name = "subscription")
public class Subscription {

	@Id
	@GeneratedValue
	private Long id;

	@ManyToOne(fetch=FetchType.LAZY, cascade=CascadeType.ALL, optional = false)
	@JoinColumn(name = "subscriber_id")
	private Subscriber subscriber;

	@Temporal(TemporalType.DATE)
	@Column(name = "subscription_date", nullable = false, length = 10)
	private Date subscriptionDate;

	@ManyToOne(fetch=FetchType.LAZY, cascade=CascadeType.ALL, optional = false)
	@JoinColumn(name = "category_id")
	private Category category;

	@PrePersist
	private void onPersist() {
		subscriptionDate = new Date();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Subscriber subscriber() {
		return subscriber;
	}

	public void setSubscriber(Subscriber subscriber) {
		this.subscriber = subscriber;
	}

	public Date getSubscriptionDate() {
		return subscriptionDate;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}
}
