package introsde.health.soap.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import introsde.health.soap.dao.EHealthDao;
import introsde.health.soap.model.Person;


/**
 * The persistent JAVA class for the "goal" database table.
 * 
 * @author alan
 */

@Entity							// this class is an entity to persist in DB
@Table(name="goal")				// the table used to persist the entity in the DB
@XmlRootElement(name="goal")	// make it the root element

//The content order in the generated schema type
@XmlType(propOrder={"id","title","initValue","finalValue","time","deadline","achieved"})

//Statically defined queries with predefined unchangeable query strings
@NamedQueries({
	@NamedQuery(name="Goal.findAll", query="SELECT g FROM Goal g"),
	@NamedQuery(name="Goal.findAllForPerson", 
		query="SELECT g FROM Goal g WHERE g.person = :person"),
	@NamedQuery(name="Goal.findPersonGoalByName",
		query="SELECT g FROM Goal g WHERE g.title = :title AND g.person = :person"),
	@NamedQuery(name="Goal.findPersonGoalById",
		query="SELECT g FROM Goal g WHERE g.id = :id AND g.person = :person")
})

public class Goal implements Serializable {
	private static final long serialVersionUID = 1L;

	/********************************************************************************
	 * DEFINITION OF ALL THE PRIVATE ATTRIBUTES OF THE CLASS AND THEIR MAPPING		*
	 ********************************************************************************/
	
	@Id													// this attribute identifies the entity
	@TableGenerator(name="GOAL_ID_GENERATOR", table="GOAL_SEQUENCES", pkColumnName="GOAL_SEQ_NAME",
    valueColumnName="GOAL_SEQ_NUMBER", pkColumnValue = "GOAL_SEQUENCE", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.TABLE, generator="GOAL_ID_GENERATOR")
	
	@Column(name="id")									// map the following attribute to a column
	private int id;

	@Column(name="title")
	private String title;
	
	@Column(name="init_value")
	private String initValue;
	
	@Column(name="final_value")
	private String finalValue;

	@Column(name="deadline")
	private String deadline;
	
	@Column(name="time")
	private String time;
	
	@Column(name="achieved")
	private String achieved;

	@ManyToOne	// Person contains one or more Goal
	@JoinColumn(name="person_id", referencedColumnName="id")
	private Person person;
	
	/**
	 * The empty constructor of the class.
	 */
	public Goal() {
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
	@XmlElement(name="id")				// customize the XML element name
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
	 * A method that returns the title name.
	 * @return title: the title
	 */
	@XmlElement(name="title")			// customize the XML element name
	public String getTitle() {
		return this.title;
	}

	/**
	 * A method that sets the title.
	 * @param title: the title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * A method that returns the initial value.
	 * @return initValue: the initial value
	 */
	@XmlElement(name="init_value")			// customize the XML element name
	public String getInitValue() {
		return this.initValue;
	}

	/**
	 * A method that sets the initial value.
	 * @param initValue: the initial value
	 */
	public void setInitValue(String initValue) {
		this.initValue = initValue;
	}
	
	/**
	 * A method that returns the final value.
	 * @return finalValue: the final value
	 */
	@XmlElement(name="final_value")			// customize the XML element name
	public String getFinalValue() {
		return this.finalValue;
	}

	/**
	 * A method that sets the final value.
	 * @param finalValue: the final value
	 */
	public void setFinalValue(String finalValue) {
		this.finalValue = finalValue;
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
	 * A method that returns the deadline.
	 * @return deadline: the deadline
	 */
	@XmlElement(name="deadline")	// customize the XML element name
	public String getDeadline() {
		return this.deadline;
	}

	/**
	 * A method that sets the deadline.
	 * @param deadline: the deadline
	 */
	public void setDeadline(String deadline) {
		this.deadline = deadline;
	}
	
	/**
	 * A method that returns the achieved status.
	 * @return achieved: the achieved status
	 */
	@XmlElement(name="achieved")	// customize the XML element name
	public String getAchieved() {
		return this.achieved;
	}

	/**
	 * A method that sets the achieved status.
	 * @param achieved: the achieved status
	 */
	public void setAchieved(String achieved) {
		this.achieved = achieved;
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
	 * HELPER METHODS USEFUL FOR THE COMPUTATION OF OTHER METHODS IN THIS CLASS		*
	 ********************************************************************************/

	/**
	 * A method that provides all the set operations to allow to update the goal after a PUT request.
	 * @param g: the goal to update
	 */
	private void setGoalAttributes(Goal g) {
		if (g.initValue != null) this.setInitValue(g.getInitValue());
		if (g.finalValue != null) this.setFinalValue(g.getFinalValue());
		if (g.deadline != null) this.setDeadline(g.getDeadline());
		if (g.time != null) this.setTime(g.getTime());
		if (g.achieved != null) this.setAchieved(g.getAchieved());
	}
	
	
	/********************************************************************************
	 * LIST OF ALL THE DATABASE OPERATIONS FOR THIS CLASS							*
	 ********************************************************************************/
	
	/**
	 * A method that queries the database to find all the people in it.
	 * @return list: a list of all the people
	 */
	public static List<Goal> getAllGoals() {
		EntityManager em = EHealthDao.instance.createEntityManager();
		List<Goal> list = em.createNamedQuery("Goal.findAll", Goal.class).getResultList();
		EHealthDao.instance.closeConnections(em);
		
		return list;
	}
	
	/**
	 * A method that queries the database to find a goal with a particular identifier.
	 * @param id: the univocal identifier of the goal
	 * @return g: the goal with that very identifier
	 */
	public static Goal getGoalById(int id) {
		EntityManager em = EHealthDao.instance.createEntityManager();
		Goal g = em.find(Goal.class, id);
		EHealthDao.instance.closeConnections(em);
		
		return g;
	}
	
	/**
	 * A method that saves a particular goal.
	 * @param g: a goal to save
	 * @return g: the saved goal
	 */
	public static Goal saveGoal(Goal g) {
		EntityManager em = EHealthDao.instance.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		em.persist(g);
		tx.commit();
		EHealthDao.instance.closeConnections(em);
		
		return g;
	}
	
	/**
	 * A method that updates a particular goal.
	 * @param g: a goal to update
	 * @return g: the updated goal
	 */
	public Goal updateGoal(Goal g) {
		setGoalAttributes(g);
		
		EntityManager em = EHealthDao.instance.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		
		if (em.find(Goal.class, (int)id) != null) {		// if the goal exists, update it
			tx.begin();
			g=em.merge(this);
			tx.commit();
		} else {
			g = null;
		}
		
		EHealthDao.instance.closeConnections(em);
		
		return g;
	}
	
	/**
	 * A method that removes a particular goal.
	 * @param g: a goal to remove
	 */
	public static void deleteGoal(Goal g) {
		EntityManager em = EHealthDao.instance.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		tx.begin();
		g=em.merge(g);
		em.remove(g);
		tx.commit();
		EHealthDao.instance.closeConnections(em);
	}
	
	/**
	 * A method that queries the database to find all the goal in it for a particular person.
	 * @param p: the person
	 * @return list: a list of all the goals for a particular person
	 */
	public static List<Goal> getAllPersonGoals(Person p) {
		EntityManager em = EHealthDao.instance.createEntityManager();
		List<Goal> list = em.createNamedQuery("Goal.findAllForPerson", Goal.class)
				.setParameter("person", p).getResultList();
		EHealthDao.instance.closeConnections(em);
		
		return list;
	}
	
	/**
	 * A method that allows to retrieve a goal given a goal name and a person.
	 * @param p: the person
	 * @param gName: the goal
	 * @return g: the goal
	 */
	public static Goal getPersonGoalByName(Person p, String gName) {
		EntityManager em = EHealthDao.instance.createEntityManager();
		Goal g = new Goal();
		
		try {
			g = em.createNamedQuery("Goal.findPersonGoalByName", Goal.class)
					.setParameter("person", p).setParameter("title", gName).getSingleResult();
		} catch (Exception e) {
			return null;
		}
		
		EHealthDao.instance.closeConnections(em);
		
		return g;
	}
	
	/**
	 * A method that allows to retrieve a goal given a goal identifier and a person.
	 * @param p: the person
	 * @param gId: the goal identifier
	 * @return g: the goal
	 */
	public static Goal getPersonGoalById(Person p, int gId) {
		EntityManager em = EHealthDao.instance.createEntityManager();
		Goal g = new Goal();
		
		try {
			g = em.createNamedQuery("Goal.findPersonGoalById", Goal.class)
					.setParameter("person", p).setParameter("id", gId).getSingleResult();
		} catch (Exception e) {
			return null;
		}
		
		EHealthDao.instance.closeConnections(em);
		
		return g;
	}
}