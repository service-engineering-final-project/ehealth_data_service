package introsde.ehealthdata.soap.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import introsde.ehealthdata.soap.dao.EHealthDao;
import introsde.ehealthdata.soap.model.Measurement;
import introsde.ehealthdata.soap.model.MeasurementHistory;


/**
 * The persistent JAVA class for the "person" database table.
 * 
 * @author alan
 */

@Entity					// this class is an entity to persist in DB
@Table(name="person")	// the table used to persist the entity in the DB
@XmlRootElement			// make it the root element

// The content order in the generated schema type
@XmlType(propOrder={"id","firstname","lastname","birthdate","healthProfile"})

// A statically defined query with a predefined unchangeable query string
@NamedQuery(name="Person.findAll", query="SELECT p FROM Person p")

public class Person implements Serializable {
	private static final long serialVersionUID = 1L;

	/********************************************************************************
	 * DEFINITION OF ALL THE PRIVATE ATTRIBUTES OF THE CLASS AND THEIR MAPPING		*
	 ********************************************************************************/
	
	@Id											// this attribute identifies the entity
	@TableGenerator(name="PERSON_ID_GENERATOR", table="PERSON_SEQUENCES", pkColumnName="PERSON_SEQ_NAME",
    valueColumnName="PERSON_SEQ_NUMBER", pkColumnValue = "PERSON_SEQUENCE", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.TABLE, generator="PERSON_ID_GENERATOR")
	
	@Column(name="id")							// map the following attribute to a column
	private int id;
	
	@Column(name="firstname")
	private String firstname;

	@Column(name="lastname")
	private String lastname;
	
	@Column(name="birthdate")
	private String birthdate;
	
	// Person contains one or more Measurement
	// (mappedBy = name of the attribute in Measurement that maps this relation)
	@OneToMany(mappedBy="person", cascade=CascadeType.ALL, fetch=FetchType.LAZY)
	private List<Measurement> measurement;
	
	// Person contains one or more MeasurementHistory
	// (mappedBy = name of the attribute in MeasurementHistory that maps this relation)
	@OneToMany(mappedBy="person", cascade=CascadeType.ALL, fetch=FetchType.LAZY)
    private List<MeasurementHistory> mHistory;
	
	/**
	 * The empty constructor of the class.
	 */
	public Person() {
		// Empty constructor
	}

	
	/********************************************************************************
	 * LIST OF ALL THE GETTERS AND SETTERS OF ALL THE PRIVATE ATTRIBUTES			*
	 ********************************************************************************/
	
	/**
	 * A method that returns the person identifier.
	 * @return id: the person identifier
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * A method that sets the person identifier.
	 * @param id: the person identifier
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	/**
	 * A method that returns the firstname of the person.
	 * @return firstname: the firstname of the person
	 */
	public String getFirstname() {
		return this.firstname;
	}

	/**
	 * A method that sets the firstname of the person.
	 * @param firstname: the firstname of the person
	 */
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	/**
	 * A method that returns the lastname of the person.
	 * @return lastname: the lastname of the person
	 */
	public String getLastname() {
		return this.lastname;
	}

	/**
	 * A method that sets the lastname of the person.
	 * @param lastname: the lastname of the person
	 */
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	
	/**
	 * A method that returns the birthdate of the person.
	 * @return birthdate: the birthdate of the person
	 */
	public String getBirthdate() {
		return this.birthdate;
	}

	/**
	 * A method that sets the birthdate of the person.
	 * @param birthdate: the birthdate of the person
	 */
	public void setBirthdate(String birthdate) {
		this.birthdate = birthdate;
	}
	
	/**
	 * A method that returns the list of measurements of the person.
	 * @return measurement: the list of measurements of the person
	 */
	@XmlElementWrapper(name="health_profile")	// the node in which the List<Measurement> elements will be inserted
	@XmlElement(name="measure_type")			// customize the XML element name
	public List<Measurement> getHealthProfile() {
		return this.measurement;
	}
	
	/**
	 * A method that sets the list of measurements of the person.
	 * @param measurement: the list of measurements of the person
	 */
	public void setHealthProfile(List<Measurement> measurement) {
		this.measurement = measurement;
	}
	
	/**
	 * A method that returns the list of measurement (history) of the person.
	 * @return mHistory: the list of measurement (history) of the person
	 */
	@XmlTransient		// it will not be saved in database
    public List<MeasurementHistory> getMeasurementHistory() {
        return this.mHistory;
    }
	
	/**
	 * A method that gets the list of measurement (history) of the person.
	 * @param mHistory: the list of measurement (history) of the person
	 */
	public void setMeasurementHistory(List<MeasurementHistory> mHistory) {
        this.mHistory = mHistory;
    }

	
	/********************************************************************************
	 * HELPER METHODS USEFUL FOR THE COMPUTATION OF OTHER METHODS IN THIS CLASS		*
	 ********************************************************************************/

	/**
	 * A method that provides all the set operations to allow to update the person after a PUT request.
	 * @param p: the person to update
	 */
	private void setPersonAttributes(Person p) {
		if (p.firstname != null) this.setFirstname(p.getFirstname());
		if (p.lastname != null) this.setLastname(p.getLastname());
		if (p.birthdate != null) this.setBirthdate(p.getBirthdate());
	}
	
	/**
	 * A method that allows to update the measurement (health profile) before updating the measurement history.
	 * @param p: the person
	 * @return p: the person
	 */
	private static Person updateMeasurement(Person p) {
		Date date = new Date();						// get the current time
		String date_string = date.toString();		// store its string representation
		
		for (Measurement m : p.measurement) {		// iterate through each measure
			m.setPerson(p);							// set the person ID
			m.setTime(date_string);					// set the current time
		}
		
		return p;
	}
	
	/**
	 * A method that allows to add the measurement (health profile) to the history.
	 * @param p: the person
	 * @return p: the person
	 */
	private static Person addMeasurementToHistory(Person p) {
		List<MeasurementHistory> mHistoryList = new ArrayList<MeasurementHistory>();
		
		for (Measurement m : p.measurement) {
			MeasurementHistory mHistory = createNewMeasurementHistory(p, m);
			mHistoryList.add(mHistory);	// add the newly created health profile history to the list
		}
		
		p.setMeasurementHistory(mHistoryList);
		
		return p;
	}
	
	/**
	 * An helper method of the one above that allows to create a new measurement (health profile) history
	 * @param p: the person
	 * @param m: the measurement
	 * @return mHistory: the measurement history
	 */
	private static MeasurementHistory createNewMeasurementHistory(Person p, Measurement m) {
		MeasurementHistory mHistory = new MeasurementHistory();
		
		mHistory.setPerson(p);
		mHistory.setTime(m.getTime());
		mHistory.setMeasureName(m.getMeasureName());
		mHistory.setMeasureValue(m.getMeasureValue());
		
		return mHistory;
	}
	
	
	/********************************************************************************
	 * LIST OF ALL THE DATABASE OPERATIONS FOR THIS CLASS							*
	 ********************************************************************************/
	
	/**
	 * A method that queries the database to find all the people in it.
	 * @return list: a list of all the people
	 */
	public static List<Person> getAllPeople() {
		EntityManager em = EHealthDao.instance.createEntityManager();
		List<Person> list = em.createNamedQuery("Person.findAll", Person.class).getResultList();
		EHealthDao.instance.closeConnections(em);
		
		return list;
	}
	
	/**
	 * A method that queries the database to find a person with a particular identifier.
	 * @param id: the univocal identifier of the person
	 * @return p: the person with that very identifier
	 */
	public static Person getPersonById(int id) {
		EntityManager em = EHealthDao.instance.createEntityManager();
		Person p = em.find(Person.class, id);
		EHealthDao.instance.closeConnections(em);
		
		return p;
	}
	
	/**
	 * A method that saves a particular person.
	 * @param p: a person to save
	 * @return p: the saved person
	 */
	public static Person savePerson(Person p) {
		if (p.measurement != null) {				// check if the measurement exists
			Person.updateMeasurement(p);			// if so, update it
			Person.addMeasurementToHistory(p);		// and update also the history
		}
		
		EntityManager em = EHealthDao.instance.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		em.persist(p);
		tx.commit();
		EHealthDao.instance.closeConnections(em);
		
		return p;
	}
	
	/**
	 * A method that updates a particular person.
	 * @param p: a person to update
	 * @return p: the updated person
	 */
	public Person updatePerson(Person p) {	
		setPersonAttributes(p);
		
		EntityManager em = EHealthDao.instance.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		p=em.merge(this);
		tx.commit();
		EHealthDao.instance.closeConnections(em);
		
		return p;
	}
	
	/**
	 * A method that removes a particular person.
	 * @param p: a person to remove
	 */
	public static void deletePerson(Person p) {
		EntityManager em = EHealthDao.instance.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		p=em.merge(p);
		em.remove(p);
		tx.commit();
		EHealthDao.instance.closeConnections(em);
	}
}