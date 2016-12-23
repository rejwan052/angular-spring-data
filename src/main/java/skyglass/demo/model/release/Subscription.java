package skyglass.demo.model.release;

import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import skyglass.data.model.AbstractEntity;

@Entity(name = "subscription")
@Table(name = "subscription")
public class Subscription extends AbstractEntity<Long> {

	private static final long serialVersionUID = -3171041438789593995L;

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
