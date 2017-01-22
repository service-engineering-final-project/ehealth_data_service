package introsde.health.soap.ws;

import java.util.List;

import javax.jws.WebService;

import introsde.health.soap.dao.EHealthDao;
import introsde.health.soap.model.Measure;
import introsde.health.soap.model.Measurement;
import introsde.health.soap.model.MeasurementHistory;
import introsde.health.soap.model.Person;

/**
 * The service implementation.
 * 
 * @author alan
 */

@WebService(endpointInterface="introsde.ehealthdata.soap.ws.People", serviceName="People")
public class PeopleImplementation implements People {

	/***
	 * Method #1: A method that lists all the people in the database.
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
	 * Method #2: A method that gives all the information of a person identified by {id}.
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
	 * Method #3: A method that updates the information of a person identified by {id}
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
	 * Method #4: A method that creates a new person and returns it with its assigned id
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
	 * Method #5: A method that deletes the person identified by {id} from the system.
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
	 * Method #6: A method that returns the list of values (the history) of {measureType}
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
	 * Method #7: A method that returns the list of all the measures supported by the service.
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
	 * Method #8: A method that returns the value of {measureType} (e.g. weight)
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
	 * Method #9: A method that saves a new measure object {m} (e.g. weight)
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
	 * Method #10: A method that updates the measure (e.g. weight) identified
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
	 * An accessory method used to reset the database before the client start executing.
	 */
	@Override
	public void initializeDatabase() {
		EHealthDao.instance.initializeDB();	// call the initialization method on the EHealthDao
	}
}