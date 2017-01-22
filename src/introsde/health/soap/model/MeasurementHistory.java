package introsde.health.soap.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import introsde.health.soap.dao.EHealthDao;
import introsde.health.soap.model.Measurement;
import introsde.health.soap.model.Person;


/**
 * The persistent JAVA class for the "measurement_history" database table.
 * 
 * @author alan
 */

@Entity									// this class is an entity to persist in DB
@Table(name="measurement_history")		// the table used to persist the entity in the DB
@XmlRootElement(name="measure")			// make it the root element

// Statically defined queries with predefined unchangeable query strings
@NamedQueries({
	@NamedQuery(name="MeasurementHistory.findMeasureHistory",
			query="SELECT m FROM MeasurementHistory m WHERE m.person = :person AND m.measureName = :measureName"),
	@NamedQuery(name="MeasurementHistory.findMeasureHistoryById",
			query="SELECT m FROM MeasurementHistory m WHERE m.person = :person AND m.id = :mid AND m.measureName = :measureName")
})

public class MeasurementHistory implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id															// this attribute identifies the entity
	@TableGenerator(name="MEASUREMENTH_ID_GENERATOR", table="MEASUREMENTH_SEQUENCES", pkColumnName="MEASUREMENTH_SEQ_NAME",
    valueColumnName="MEASUREMENTH_SEQ_NUMBER", pkColumnValue = "MEASUREMENTH_SEQUENCE", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.TABLE, generator="MEASUREMENTH_ID_GENERATOR")
	@Column(name="id")											// map the following attribute to a column
	private int id;

	@Column(name="measure_name")
	private String measureName;

	@Column(name="measure_value")
	private String measureValue;
	
	@Column(name="measure_value_type")
	private String measureValueType;

	@Column(name="time")
	private String time;
	
	@ManyToOne	// Person contains one or more MeasurementHistory
	@JoinColumn(name="person_id", referencedColumnName="id")
	private Person person;

	/**
	 * The empty constructor of the class.
	 */
	public MeasurementHistory() {
		// Empty constructor
	}
	
	
	/********************************************************************************
	 * LIST OF ALL THE GETTERS AND SETTERS OF ALL THE PRIVATE ATTRIBUTES			*
	 ********************************************************************************/
	
	/**
	 * A method that returns the measurement history identifier.
	 * @return id: the measurement history identifier
	 */
	@XmlElement(name="mid")
	public int getId() {
		return this.id;
	}

	/**
	 * A method that sets the measurement history identifier.
	 * @param id: the measurement history identifier
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * A method that returns the measure name.
	 * @return measureType: the measure name
	 */
	// @XmlTransient						// it will not be saved in database
	@XmlElement(name="measure")
	public String getMeasureName() {
		return this.measureName;
	}

	/**
	 * A method that sets the measure name.
	 * @param measureType: the measure name
	 */
	public void setMeasureName(String measureName) {
		this.measureName = measureName;
	}

	/**
	 * A method that returns the measure value.
	 * @return measureValue: the measure value
	 */
	@XmlElement(name="value")			// customize the XML element name
	public String getMeasureValue() {
		return this.measureValue;
	}

	/**
	 * A method that sets the measure value.
	 * @param measureValue: the measureValue
	 */
	public void setMeasureValue(String measureValue) {
		this.measureValue = measureValue;
	}
	
	/**
	 * A method that returns the measure value type.
	 * @return measureValueType: the measure value type
	 */
	@XmlElement(name="value_type")		// customize the XML element name
	public String getMeasureValueType() {
		return this.measureValueType;
	}

	/**
	 * A method that sets the measure value type.
	 * @param measureValueType: the measure value type
	 */
	public void setMeasureValueType(String measureValueType) {
		this.measureValueType = measureValueType;
	}

	/**
	 * A method that returns the time.
	 * @return time: the time
	 */
	@XmlElement(name="created")			// customize the XML element name
	public String getTime() {
		return this.time;
	}

	/**
	 * A method that sets the time.
	 * @param time: the time
	 */
	public void setTime(String time) {
		this.time = time;
	}
	
	/**
	 * A method that returns the person.
	 * @return person: the person
	 */
	@XmlTransient						// it will not be saved in database
	public Person getPerson() {
		return this.person;
	}
	
	/**
	 * A method that sets the person.
	 * @param person: the person
	 */
	public void setPerson(Person person) {
		this.person = person;
	}
	
	
	/********************************************************************************
	 * LIST OF ALL THE DATABASE OPERATIONS FOR THIS CLASS							*
	 ********************************************************************************/
	
	/**
	 * A method that allows to retrieve a history of a particular measure for a particular person.
	 * @param p: the person
	 * @param mName: the measure
	 * @return mhList: the history of a measure
	 */
	public static List<MeasurementHistory> getHistoryOfAMeasure(Person p, String mName) {
		EntityManager em = EHealthDao.instance.createEntityManager();
		List<MeasurementHistory> mhList;
		
		mhList = em.createNamedQuery("MeasurementHistory.findMeasureHistory", MeasurementHistory.class)
				.setParameter("person", p).setParameter("measureName", mName).getResultList();
		
		EHealthDao.instance.closeConnections(em);
		
		return mhList;
	}
	
	/**
	 * A method that allows to retrieve a measure given a measure type, a person and a measure identifier.
	 * @param p: the person
	 * @param mId: the identifier of the measure
	 * @param mName: the measure
	 * @return mHistory: the measurement history
	 */
	public static MeasurementHistory getHistoryOfAMeasureById(Person p, int mId, String mName) {
		EntityManager em = EHealthDao.instance.createEntityManager();
		MeasurementHistory mHistory = new MeasurementHistory();
		
		try {
			mHistory = em.createNamedQuery("MeasurementHistory.findMeasureHistoryById", MeasurementHistory.class)
					.setParameter("person", p).setParameter("measureName", mName)
					.setParameter("mid", mId).getSingleResult();
		} catch (Exception e) {
			return null;
		}
		
		if (mHistory != null) {
			em.refresh(mHistory);
		}
		
		EHealthDao.instance.closeConnections(em);
		
		return mHistory;
	}
	
	/**
	 * A method that allows to add the measurement to the history.
	 * @param m: the measurement
	 * @return mHistory: the measurement history
	 */
	public static MeasurementHistory addMeasurementToHistory(Measurement m) {
		MeasurementHistory mHistory = createNewHealthProfileHistory(m);
		
		EntityManager em = EHealthDao.instance.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		mHistory = em.merge(mHistory);
		tx.commit();
		EHealthDao.instance.closeConnections(em);
		
		return mHistory;
	}
	
	/**
	 * A method that allows to update the measurement of the history.
	 * @param mh: the measurement history
	 * @return mHistory: the measurement history
	 */
	public static MeasurementHistory updateMeasurementHistory(MeasurementHistory mh) {
		EntityManager em = EHealthDao.instance.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		mh = em.merge(mh);
		tx.commit();
		EHealthDao.instance.closeConnections(em);
		
		return mh;
	}
	
	/**
	 * An helper method of the one above that allows to create a new measurement history
	 * @param p: the person
	 * @param m: the measurement
	 * @return mHistory: the measurement history
	 */
	private static MeasurementHistory createNewHealthProfileHistory(Measurement m) {
		MeasurementHistory mHistory = new MeasurementHistory();
		
		mHistory.setPerson(m.getPerson());
    	mHistory.setTime(m.getTime());
		mHistory.setMeasureName(m.getMeasureName());
		mHistory.setMeasureValue(m.getMeasureValue());
		
		return mHistory;
	}
}