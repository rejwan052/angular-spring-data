package skyglass.data.metadata;

import java.io.Serializable;

import org.hibernate.SessionFactory;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.type.CollectionType;
import org.hibernate.type.EntityType;
import org.hibernate.type.Type;


public class JpaEntityMetadata implements Metadata {

	private SessionFactory sessionFactory;
	private ClassMetadata metadata;
	private Class<?> collectionType;
	
	public JpaEntityMetadata(SessionFactory sessionFactory, ClassMetadata classMetaData, Class<?> collectionType) {
		this.sessionFactory = sessionFactory;
		this.metadata = classMetaData;
		this.collectionType = collectionType;
	}
	
	public String getIdProperty() {
		return metadata.getIdentifierPropertyName();
	}

	public Metadata getIdType() {
		return new JpaNonEntityMetadata(sessionFactory, metadata.getIdentifierType(), null);
	}

	public Serializable getIdValue(Object object) {
		if (object instanceof HibernateProxy) {
			return ((HibernateProxy) object).getHibernateLazyInitializer().getIdentifier();
		} else {
			return metadata.getIdentifier(object, (SessionImplementor)sessionFactory.getCurrentSession());
		}
	}

	public Class<?> getJavaClass() {
		return metadata.getMappedClass();
	}

	public String[] getProperties() {
		String[] pn = metadata.getPropertyNames();
		String[] result = new String[pn.length + 1];
		result[0] = metadata.getIdentifierPropertyName();
		for (int i = 0; i < pn.length; i++) {
			result[i+1] = pn[i];
		}
		return result;
	}

	public Metadata getPropertyType(String property) {
		Type pType = metadata.getPropertyType(property);
		Class<?> pCollectionType = null;
		if (pType.isCollectionType()) {
			pType = ((CollectionType)pType).getElementType((SessionFactoryImplementor) sessionFactory);
			pCollectionType = pType.getReturnedClass();
		}
		
		if (pType.isEntityType()) {
			return new JpaEntityMetadata(sessionFactory, sessionFactory.getClassMetadata(((EntityType)pType).getName()), pCollectionType);
		} else {
			return new JpaNonEntityMetadata(sessionFactory, pType, pCollectionType);
		}
	}

	@SuppressWarnings("deprecation")
	public Object getPropertyValue(Object object, String property) {
		if (getIdProperty().equals(property))
			return metadata.getIdentifier(object);
		else
			return metadata.getPropertyValue(object, property);
	}

	public boolean isCollection() {
		return collectionType != null;
	}

	public Class<?> getCollectionClass() {
		return collectionType;
	}

	public boolean isEmeddable() {
		return false;
	}

	public boolean isEntity() {
		return true;
	}

	public boolean isNumeric() {
		return false;
	}

	public boolean isString() {
		return false;
	}

}