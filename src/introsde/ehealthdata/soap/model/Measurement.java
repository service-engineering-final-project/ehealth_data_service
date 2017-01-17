package introsde.ehealthdata.soap.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import introsde.ehealthdata.soap.dao.EHealthDao;
import introsde.ehealthdata.soap.model.Person;


/**
 * The persistent JAVA class for the "measurement" database table.
 * 
 * @author alan
 */

@Entity							// this class is an entity to persist in DB
@Table(name="measurement")		// the table used to persist the entity in the DB
@XmlRootElement(name="measure")	// make it the root element

// A statically defined query with a predefined unchangeable query string
@NamedQuery(name="Measurement.findMeasure",
	query="SELECT m FROM Measurement m WHERE m.measureName = :measureName AND m.person = :person")

public class Measurement implements Serializable {
	private static final long serialVersionUID = 1L;

	/********************************************************************************
	 * DEFINITION OF ALL THE PRIVATE ATTRIBUTES OF THE CLASS AND THEIR MAPPING		*
	 ********************************************************************************/
	
	@Id													// this attribute identifies the entity
	@TableGenerator(name="MEASUREMENT_ID_GENERATOR", table="MEASUREMENT_SEQUENCES", pkColumnName="MEASUREMENT_SEQ_NAME",
    valueColumnName="MEASUREMENT_SEQ_NUMBER", pkColumnValue = "MEASUREMENT_SEQUENCE", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.TABLE, generator="MEASUREMENT_ID_GENERATOR")
	
	@Column(name="id")									// map the following attribute to a column
	private int id;

	@Column(name="measure_name")
	private String measureName;

	@Column(name="measure_value")
	private String measureValue;
	
	@Column(name="measure_value_type")
	private String measureValueType;

	@Column(name="time")
	private String time;

	@ManyToOne	// Person contains one or more Measurement
	@JoinColumn(name="person_id", referencedColumnName="id")
	private Person person;
	
	/**
	 * The empty constructor of the class.
	 */
	public Measurement() {
		// Empty constructor
	}
	
	
	/********************************************************************************
	 * LIST OF ALL THE GETTERS AND SETTERS OF ALL THE PRIVATE ATTRIBUTES			*
	 ********************************************************************************/

	/**
	 * A method that returns the health profile identifier.
	 * @return id: the health profile identifier
	 */
	// @XmlTransient					// it will not be saved in database
	@XmlElement(name="mid")				// customize the XML element name
	public int getId() {
		return this.id;
	}

	/**
	 * A method that sets the health profile identifier.
	 * @param id: the health profile identifier
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * A method that returns the measure name.
	 * @return measureName: the measure name
	 */
	@XmlElement(name="measure")			// customize the XML element name
	public String getMeasureName() {
		return this.measureName;
	}

	/**
	 * A method that sets the measure name.
	 * @param measureName: the measure name
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
	 * @param measureValue: the measure value
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
	@XmlElement(name="created")		// customize the XML element name
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
		return person;
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
	 * A method that allows to retrieve a measure given a measure name and a person.
	 * @param p: the person
	 * @param mName: the measure
	 * @return m: the measure
	 */
	public static Measurement getMeasure(Person p, String mName) {
		EntityManager em = EHealthDao.instance.createEntityManager();
		Measurement m = new Measurement();
		
		try {
			m = em.createNamedQuery("Measurement.findMeasure", Measurement.class)
					.setParameter("person", p).setParameter("measureName", mName).getSingleResult();
		} catch (Exception e) {
			return null;
		}
		
		EHealthDao.instance.closeConnections(em);
		
		return m;
	}
	
	/**
	 * A method that updates a particular measurement (health profile with a new one, checking
	 * if it exists or not in order to update it or simply create a new one.
	 * @param mId: the current measurement (health profile) ID
	 * @param measure: the new measure
	 * @return newM: the updated measurement (health profile) with the new measure
	 */
	public static Measurement updateMeasurement(Integer mId, Measurement measure) {
		Date date = new Date();						// get the current time
		String date_string = date.toString();		// store its string representation
		Measurement newM = new Measurement();		// the new measurement (health profile)
		
		EntityManager em = EHealthDao.instance.createEntityManager();
		EntityTransaction tx = em.getTransaction();

		if (mId != null) {		// if the health profile exists, update it
			Measurement m = em.find(Measurement.class, (int)mId);
			
			tx.begin();
			m.setMeasureValue(measure.getMeasureValue());
			m.setTime(date_string);
			tx.commit();
			
			newM = m;
		} else {				// otherwise, create a new one
			measure.setTime(date_string);
			
			tx.begin();
			newM = em.merge(measure);
			tx.commit();
		}
		
		EHealthDao.instance.closeConnections(em);
		
		return newM;
	}	
}