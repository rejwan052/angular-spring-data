package skyglass.data.model.security;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.annotations.AccessType;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

/**
 * This class provides primitive implementations of saving, updating, deleting and other actions used to manage a
 * persistent entities state with the database.
 *
 * I is the type of the id for this entity, usually Long or Integer.
 * T is the type of the class which is used for this AbstractEntity.
 */
@MappedSuperclass
@AccessType(value = "field")
abstract public class AbstractEntity<I extends Serializable> implements Serializable {

    private static final long serialVersionUID = 2806840540448132368L;

    //******************************************************************************************************************
    // CLASS
    //******************************************************************************************************************

    //------------------------------------------------------------------------------------------------------------------
    /**
     * Retrieve all instances of a given class.
     *
     * Not all entities may wish to expose this method for performance reasons.
     *
     * @param clazz The type of object to fetch from the database.
     * @return A List of objects of type clazz containing all instances in the database.
     */
    static protected List<AbstractEntity<Serializable>> getAll(SessionFactory sessionFactory, Class<? extends AbstractEntity<Serializable>> clazz) {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(clazz);
        @SuppressWarnings("unchecked")
        List<AbstractEntity<Serializable>> result = criteria.list();

        return result;
    }

    //------------------------------------------------------------------------------------------------------------------
    /**
     * Similar to {@link #getAll(Class)}, but allows sorting the result on a given property.
     *
     * @param clazz The type of object to fetch from the database.
     * @param propertyName property name to rely sorting on
     * @param asc ascending?
     * @return a sorted list of objects of type clazz containing all instances in the database.
     */
    @SuppressWarnings("unchecked")
    static protected <T extends AbstractEntity> List<T> getAll(SessionFactory sessionFactory, Class<T> clazz, String propertyName, boolean asc) {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(clazz);
        Order order;
        if (asc) {
            order = Order.asc(propertyName);
        }
        else {
            order = Order.desc(propertyName);
        }
        criteria.addOrder(order);

        return criteria.list();
    }

    //------------------------------------------------------------------------------------------------------------------
    /**
     * A convenience method to retrieve an entity by id.
     *
     * @param clazz The class of the desired entity.
     * @param id The unique id of the entity.
     * @return An instance of the desired entity with the given unique id.
     * @throws IllegalArgumentException when id is null
     */
    static public <T extends AbstractEntity<Serializable>> T getById(SessionFactory sessionFactory, Class<T> clazz, Serializable id) {
        if (id == null) {
            throw new IllegalArgumentException("The parameter id must be non-null.");
        }
        return AbstractEntity.getByUniqueProperty(sessionFactory, clazz, "id", id);
    }

    //------------------------------------------------------------------------------------------------------------------
    /**
     * A convenience method to retrieve an entity by name.
     *
     * @param clazz The class of the desired entity.
     * @param name The name that the entity should have.
     * @return An instance of the desired entity with the given name or null if no such entity exists.
     */
    static protected <T extends AbstractEntity<Serializable>> T getByName(SessionFactory sessionFactory, Class<T> clazz, Serializable name) {
        return AbstractEntity.getByUniqueProperty(sessionFactory, clazz, "name", name);
    }

    //------------------------------------------------------------------------------------------------------------------
    /**
     * This method can be used to fetch a unique instance of an entity given the class of the entity, the name of the
     * unique property and the value which it should be equal to.  The property must uniquely define an entity in order
     * for this method to behave in a predictable manner.
     *
     * @param clazz The class instance of the persistent entity we wish to fetch.
     * @param propertyName The name of the property on the object which is unique.
     * @param value The value of that property which should be used to query the database.
     * @return A BasicEntity of the same type specified by clazz.
     */
    @SuppressWarnings("unchecked")
    static protected <T extends AbstractEntity> T getByUniqueProperty(SessionFactory sessionFactory, Class<T> clazz, String propertyName, Object value) {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(clazz);
        criteria.add(Restrictions.eq(propertyName, value));
        return (T) criteria.uniqueResult();
    }

    //******************************************************************************************************************
    // INSTANCE
    //******************************************************************************************************************

    @Basic
    @NotNull
    @Version
    @Column(name = "version")
    private Long version;

    //------------------------------------------------------------------------------------------------------------------
    protected AbstractEntity() {
    }

    //------------------------------------------------------------------------------------------------------------------
    abstract public I getId();

    //------------------------------------------------------------------------------------------------------------------
    public Long getVersion() {
        return this.version;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        boolean result = false;

        if (obj instanceof AbstractEntity) {
            AbstractEntity<?> other = (AbstractEntity<?>) obj;
            result = other.getId().equals(this.getId());
        }

        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        int hash;

        if (getId() != null) {
            hash = 1;
            hash = hash * 31 + getClass().hashCode();
            hash = hash * 31 + getId().hashCode();
        }
        else {
            hash = super.hashCode();
        }

        return hash;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return getClass().getSimpleName() + "#" + getId();
    }
}
