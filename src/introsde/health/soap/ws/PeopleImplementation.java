package introsde.health.soap.ws;

import java.util.List;

import javax.jws.WebService;

import introsde.health.soap.dao.EHealthDao;
import introsde.health.soap.model.Goal;
import introsde.health.soap.model.Measure;
import introsde.health.soap.model.Measurement;
import introsde.health.soap.model.MeasurementHistory;
import introsde.health.soap.model.Person;

/**
 * The service implementation.
 * 
 * @author alan
 */

@WebService(endpointInterface="introsde.health.soap.ws.People", serviceName="People")
public class PeopleImplementation implements People {

	/***
	 * A method that lists all the people in the database.
	 * @return all the people in the database
	 */
	@Override
	public List<Person> readPersonList() {
		System.out.println("Executing readPersonList()...");
		List<Person> people = Person.getAllPeople();
		
		System.out.println("\tReturning the whole list of people...");
		return people;
	}
	
	/***
	 * A method that gives all the information of a person identified by {id}.
	 * @param id: the identifier
	 * @return the person identified by {id}
	 */
	@Override
	public Person readPerson(Long id) {
		System.out.println("Executing readPerson()...");
		Person person = Person.getPersonById(id.intValue());
		
		System.out.println("\tReturning the person with ID " + id.toString() + "...");
		return person;
	}
	
	/***
	 * A method that updates the information of a person identified by {id}
	 * (i.e. only the person's information, not the measures of the health profile).
	 * @param p: the person to update
	 * @return the person updated
	 */
	@Override
	public Person updatePerson(Person p) {
		System.out.println("Executing updatePerson()...");
		p.setHealthProfile(null);	// prevent updates on the person's health profile
		p.updatePerson(p);			// update data (without updating the health profile)
		
		System.out.println("\tReturning the person updated (ID: " + p.getId() + ")...");
		return p;
	}
	
	/***
	 * A method that creates a new person and returns it with its assigned id
	 * (i.e. if a health profile is included, also its measurements will be created).
	 * @param p: the person to create
	 * @return the person created
	 */
	@Override
	public Person createPerson(Person p) {
		System.out.println("Executing createPerson()...");
		Person person = Person.savePerson(p);	// create person and its health profile
		
		System.out.println("\tReturning the person created (ID: " + person.getId() + ")...");
		return person;
	}
	
	/***
	 * A method that deletes the person identified by {id} from the system.
	 * @param id: the identifier of the person to delete
	 */
	@Override
	public void deletePerson(Long id) {
		System.out.println("Executing deletePerson()...");
		Person person = Person.getPersonById(id.intValue());
		
		if (person != null) {					// check if the person exists
			Person.deletePerson(person);		// if yes, delete it
			System.out.println("\tDeleting the person with ID " + person.getId() + "...");
		} else {								// o.w., print an error message
			System.out.println("\tERROR! The person with ID " + id + " doesn't exist!");
		}
	}
	
	/***
	 * A method that returns the list of values (the history) of {measureType}
	 * (e.g. weight) for a person identified by {id}.
	 * @param id: the identifier of the person
	 * @param measureType: the measure of interest
	 * @return the list of all the measurements of a particular measure relative to a person
	 */
	@Override
	public List<MeasurementHistory> readPersonHistory(Long id, String measureType) {
		System.out.println("Executing readPersonHistory()...");
		Person person = Person.getPersonById(id.intValue());
		List<MeasurementHistory> history = MeasurementHistory.getHistoryOfAMeasure(person, measureType);
		
		System.out.println("\tReturning the \"" + measureType + "\" history of the person with ID " + id + "...");
		return history;
	}
	
	/***
	 * A method that returns the list of all the measures supported by the service.
	 * @return the list of all the measures supported by the service
	 */
	@Override
	public List<Measure> readMeasureTypes() {
		System.out.println("Executing readMeasureTypes()...");
		List<Measure> measures = Measure.getAllMeasures();
		
		System.out.println("\tReturning the whole list of measures...");
		return measures;
	}
	
	/***
	 * A method that returns the value of {measureType} (e.g. weight)
	 * identified by {mid} for a person identified by {id}.
	 * @param id: the identifier of the person
	 * @param measureType: the measure of interest
	 * @param mid: the measure identifier
	 * @return the value of the measure with a particular identifier relative to a person
	 */
	@Override
	public MeasurementHistory readPersonMeasure(Long id, String measureType, Long mid) {
		System.out.println("Executing readPersonMeasure()...");
		Person person = Person.getPersonById(id.intValue());
		MeasurementHistory measurement = MeasurementHistory.getHistoryOfAMeasureById(
				person, mid.intValue(), measureType);
		
		System.out.println("\tReturning the \"" + measureType + "\" with mID " + mid + 
				" of the person with ID " + id + "...");
		return measurement;
	}
	
	/***
	 * A method that saves a new measure object {m} (e.g. weight)
	 * for a person identified by {id} and archives the old value in the history.
	 * @param id: the identifier of the person
	 * @param m: the measurement of interest
	 * @return the saved measurement
	 */
	@Override
	public Measurement savePersonMeasure(Long id, Measurement m) {
		System.out.println("Executing savePersonMeasure()...");
		Person person = Person.getPersonById(id.intValue());
		m.setPerson(person);
		Measurement mCurr = Measurement.getMeasure(person, m.getMeasureName());
		int mIdCurr = (mCurr!=null) ? mCurr.getId() : -1;	// get the current measure ID

		mCurr = Measurement.updateMeasurement(mIdCurr, m);	// update/create the measurement
		MeasurementHistory.addMeasurementToHistory(mCurr);	// add it to the history
		
		System.out.println("\tReturning the saved measurement with mID " + mIdCurr + 
				" of the person with ID " + id + "...");
		return mCurr;
	}
	
	/***
	 * A method that updates the measure (e.g. weight) identified
	 * with {m.mid} for a person identified by {id}.
	 * @param id: the identifier of the person
	 * @param m: the measurement history of interest
	 * @return the saved measurement
	 */
	@Override
	public Long updatePersonMeasure(Long id, MeasurementHistory m) {
		System.out.println("Executing updatePersonMeasure()...");
		Person person = Person.getPersonById(id.intValue());
		MeasurementHistory mHistory = MeasurementHistory.getHistoryOfAMeasureById(
				person, m.getId(), m.getMeasureName());
		
		mHistory.setMeasureValue(m.getMeasureValue());
		MeasurementHistory.updateMeasurementHistory(mHistory);
		
		return Long.valueOf(mHistory.getId());
	}
	
	/***
	 * A method that lists all the goals in the database.
	 * @return all the goals in the database
	 */
	@Override
	public List<Goal> readGoalList() {
		System.out.println("Executing readGoalList()...");
		List<Goal> goal = Goal.getAllGoals();
		
		System.out.println("\tReturning the whole list of goals...");
		return goal;
	}
	
	/***
	 * A method that gives all the information of a goal identified by {id}.
	 * @param id: the identifier
	 * @return the goal identified by {id}
	 */
	@Override
	public Goal readGoal(Long id) {
		System.out.println("Executing readGoal()...");
		Goal goal = Goal.getGoalById(id.intValue());
		
		System.out.println("\tReturning the goal with ID " + id.toString() + "...");
		return goal;
	}
	
	/***
	 * A method that updates the information of a goal for a particular person with a certain {id}.
	 * @param id: the identifier of the person
	 * @param g: the goal to update
	 * @return the goal updated
	 */
	@Override
	public Goal updateGoal(Long id, Goal g) {
		System.out.println("Executing updateGoal()...");
		g.setPerson(Person.getPersonById(id.intValue()));
		g.updateGoal(g);
		
		System.out.println("\tReturning the goal updated (ID: " + g.getId() + ")...");
		return g;
	}
	
	/***
	 * A method that creates a new goal for a particular person with a certain {id}.
	 * @param id: the identifier of the person
	 * @param g: the goal to create
	 * @return the goal created
	 */
	@Override
	public Goal createGoal(Long id, Goal g) {
		System.out.println("Executing createGoal()...");
		g.setPerson(Person.getPersonById(id.intValue()));
		Goal goal = Goal.saveGoal(g);
		
		System.out.println("\tReturning the goal created (ID: " + goal.getId() + ")...");
		return goal;
	}
	
	/***
	 * A method that deletes the goal identified by {id} from the system.
	 * @param id: the identifier of the goal to delete
	 */
	@Override
	public void deleteGoal(Long id) {
		System.out.println("Executing deleteGoal()...");
		Goal goal = Goal.getGoalById(id.intValue());
		
		if (goal != null) {						// check if the goal exists
			Goal.deleteGoal(goal);				// if yes, delete it
			System.out.println("\tDeleting the goal with ID " + goal.getId() + "...");
		} else {								// o.w., print an error message
			System.out.println("\tERROR! The goal with ID " + id + " doesn't exist!");
		}
	}
	
	/***
	 * A method that lists all the goals for a particular person with {id} in the database.
	 * @param id: the identifier of the person
	 * @return all the goals for a particular person in the database
	 */
	@Override
	public List<Goal> readPersonGoalList(Long id) {
		System.out.println("Executing readPersonGoalList()...");
		Person person = Person.getPersonById(id.intValue());
		List<Goal> goals = Goal.getAllPersonGoals(person);
		
		System.out.println("\tReturning the whole list of goals of the person with ID " + id + "...");
		return goals;
	}
	
	/***
	 * A method that gives all the information of a goal identified by {gId}
	 * for a particular person with {id}.
	 * @param id: the identifier of the person
	 * @param gId: the identifier of the goal
	 * @return the goal identified by {gId} for a particular person
	 */
	@Override
	public Goal readPersonGoalById(Long id, Long gId) {
		System.out.println("Executing readPersonGoalById()...");
		Person person = Person.getPersonById(id.intValue());
		Goal goal = Goal.getPersonGoalById(person, gId.intValue());
		
		System.out.println("\tReturning the goal with gID " + gId + 
				" of the person with ID " + id + "...");
		return goal;
	}
	
	/***
	 * A method that gives all the information of a goal identified by {title} 
	 * for a particular person with {id}.
	 * @param id: the identifier of the person
	 * @param title: the title of the goal
	 * @return the goal identified by {title} for a particular person
	 */
	@Override
	public Goal readPersonGoalByName(Long id, String title) {
		System.out.println("Executing readPersonGoalByName()...");
		Person person = Person.getPersonById(id.intValue());
		Goal goal = Goal.getPersonGoalByName(person, title);
		
		System.out.println("\tReturning the \"" + title + "\" goal " +
				"of the person with ID " + id + "...");
		return goal;
	}
	
	/***
	 * A method that gives all the information of goals identified by {status} 
	 * for a particular person with {id}.
	 * @param id: the identifier of the person
	 * @param status: the status of the goal
	 * @return a list of goals identified by {status} for a particular person
	 */
	@Override
	public List<Goal> readPersonGoalByStatus(Long id, String status) {
		System.out.println("Executing readPersonGoalByStatus()...");
		Person person = Person.getPersonById(id.intValue());
		List<Goal> goals = Goal.getPersonGoalByStatus(person, status);
		
		System.out.println("\tReturning the \"" + status + "\" goals " +
				"of the person with ID " + id + "...");
		return goals;
	}
	
	/***
	 * A method that gives all the information of the goal identified by {title} 
	 * and {status} for a particular person with {id}.
	 * @param id: the identifier of the person
	 * @param title: the title of the goal
	 * @param status: the status of the goal
	 * @return the goal identified by {status} for a particular person
	 */
	@Override
	public Goal readPersonGoalByNameAndStatus(Long id, String title, String status) {
		System.out.println("Executing readPersonGoalByNameAndStatus()...");
		Person person = Person.getPersonById(id.intValue());
		Goal goal = Goal.getPersonGoalByNameAndStatus(person, title, status);
		
		System.out.println("\tReturning the \"" + title + "\" goal (achieved: " + status + ") " +
				"of the person with ID " + id + "...");
		return goal;
	}
	
	/***
	 * An accessory method used to reset the database and that populates it with sample data.
	 */
	@Override
	public void initializeDatabase() {
		EHealthDao.instance.initializeDB();	// call the initialization method on the EHealthDao
	}
}