package introsde.ehealthdata.soap.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import introsde.ehealthdata.soap.dao.EHealthDao;


/**
 * The persistent JAVA class for the "measure" database table.
 * 
 * @author alan
 */

@Entity								// this class is an entity to persist in DB
@Table(name="measure")				// the table used to persist the entity in the DB
@XmlRootElement(name="measure")		// make it the root element

//A statically defined query with a predefined unchangeable query string
@NamedQuery(name="Measure.findAll", query="SELECT m FROM Measure m")

public class Measure implements Serializable {
	private static final long serialVersionUID = 1L;

	/********************************************************************************
	 * DEFINITION OF ALL THE PRIVATE ATTRIBUTES OF THE CLASS AND THEIR MAPPING		*
	 ********************************************************************************/
	
	@Id													// this attribute identifies the entity
	@TableGenerator(name="MEASURE_ID_GENERATOR", table="MEASURE_SEQUENCES", pkColumnName="MEASURE_SEQ_NAME",
    valueColumnName="MEASURE_SEQ_NUMBER", pkColumnValue = "MEASURE_SEQUENCE", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.TABLE, generator="MEASURE_ID_GENERATOR")
	@Column(name="id")									// map the following attribute to a column
	private int id;

	@Column(name="measure_name")
	private String measureName;

	/**
	 * The empty constructor of the class.
	 */
	public Measure() {
		// Empty constructor
	}
	
	
	/********************************************************************************
	 * LIST OF ALL THE GETTERS AND SETTERS OF ALL THE PRIVATE ATTRIBUTES			*
	 ********************************************************************************/

	/**
	 * A method that returns the measure identifier.
	 * @return id: the measure identifier
	 */
	public int getId() {
		return this.id;
	}

	/**
	 * A method that sets the measure identifier.
	 * @return id: the measure identifier
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * A method that returns the measure name.
	 * @return id: the measure name
	 */
	@XmlElement(name="name")					// customize the XML element name
	public String getMeasureName() {
		return this.measureName;
	}

	/**
	 * A method that sets the measure name.
	 * @return id: the measure name
	 */
	public void setMeasureName(String measureName) {
		this.measureName = measureName;
	}
	
	
	/********************************************************************************
	 * LIST OF ALL THE DATABASE OPERATIONS FOR THIS CLASS							*
	 ********************************************************************************/
	
	/**
	 * A method that queries the database to find all the measures in it.
	 * @return list: a list of all the measures
	 */
	public static List<Measure> getAllMeasures() {
		EntityManager em = EHealthDao.instance.createEntityManager();
		List<Measure> list = em.createNamedQuery("Measure.findAll", Measure.class).getResultList();
		EHealthDao.instance.closeConnections(em);
		
		return list;
	}
	
	/**
	 * A method that saves a particular person.
	 * @param p: a person to save
	 * @return p: the saved person
	 */
	public static Measure saveMeasure(Measure m) {
		System.out.println(m.measureName);
		System.out.println(m.id);
		
		EntityManager em = EHealthDao.instance.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		em.persist(m);
		tx.commit();
		EHealthDao.instance.closeConnections(em);
		
		return m;
	}
}