package introsde.health.soap.ws;

import java.util.List;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;

import introsde.health.soap.model.Measure;
import introsde.health.soap.model.Measurement;
import introsde.health.soap.model.MeasurementHistory;
import introsde.health.soap.model.Person;

/**
 * The service endpoint interface.
 * 
 * @author alan
 */

@WebService
@SOAPBinding(style=Style.DOCUMENT, use=Use.LITERAL)
public interface People {
	
	/***
	 * Method #1: A method that lists all the people in the database.
	 * @return all the people in the database
	 */
	@WebMethod(operationName="readPersonList")
	@WebResult(name="person")				// the root of the result
	public List<Person> readPersonList();
	
	/***
	 * Method #2: A method that gives all the information of a person identified by {id}.
	 * @param id: the identifier
	 * @return the person identified by {id}
	 */
	@WebMethod(operationName="readPerson")
	@WebResult(name="person")
	public Person readPerson(
			@WebParam(name="id") Long id
	);
	
	/***
	 * Method #3: A method that updates the information of a person identified by {id}
	 * (i.e. only the person's information, not the measures of the health profile).
	 * @param p: the person to update
	 * @return the person updated
	 */
	@WebMethod(operationName="updatePerson")
	@WebResult(name="updatedPerson")
	public Person updatePerson(
			@WebParam(name="person") Person p
	);
	
	/***
	 * Method #4: A method that creates a new person and returns it with its assigned id
	 * (i.e. if a health profile is included, also its measurements will be created).
	 * @param p: the person to create
	 * @return the person created
	 */
	@WebMethod(operationName="createPerson")
	@WebResult(name="createdPerson")
	public Person createPerson(
			@WebParam(name="person") Person p
	);
	
	/***
	 * Method #5: A method that deletes the person identified by {id} from the system.
	 * @param id: the identifier of the person to delete
	 */
	@WebMethod(operationName="deletePerson")
	@WebResult(name="person")
	public void deletePerson(
			@WebParam(name="id") Long id
	);
	
	/***
	 * Method #6: A method that returns the list of values (the history) of {measureType}
	 * (e.g. weight) for a person identified by {id}.
	 * @param id: the identifier of the person
	 * @param measureType: the measure of interest
	 * @return the list of all the measurements of a particular measure relative to a person
	 */
	@WebMethod(operationName="readPersonHistory")
	@WebResult(name="measurement")
	public List<MeasurementHistory> readPersonHistory(
			@WebParam(name="id") Long id,
			@WebParam(name="measure_type") String measureType
	);
	
	/***
	 * Method #7: A method that returns the list of all the measures supported by the service.
	 * @return the list of all the measures supported by the service
	 */
	@WebMethod(operationName="readMeasureTypes")
	@WebResult(name="measure")
	public List<Measure> readMeasureTypes();
	
	/***
	 * Method #8: A method that returns the value of {measureType} (e.g. weight)
	 * identified by {mid} for a person identified by {id}.
	 * @param id: the identifier of the person
	 * @param measureType: the measure of interest
	 * @param mid: the measure identifier
	 * @return the value of the measure with a particular identifier relative to a person
	 */
	@WebMethod(operationName="readPersonMeasure")
	@WebResult(name="measure")
	public MeasurementHistory readPersonMeasure(
			@WebParam(name="id") Long id,
			@WebParam(name="measure_type") String measureType,
			@WebParam(name="mid") Long mid
	);
	
	/***
	 * Method #9: A method that saves a new measure object {m} (e.g. weight)
	 * for a person identified by {id} and archives the old value in the history.
	 * @param id: the identifier of the person
	 * @param m: the measurement of interest
	 * @return the saved measurement
	 */
	@WebMethod(operationName="savePersonMeasure")
	@WebResult(name="measurement")
	public Measurement savePersonMeasure(
			@WebParam(name="id") Long id,
			@WebParam(name="measure_type") Measurement m
	);
	
	/***
	 * Method #10: A method that updates the measure (e.g. weight) identified
	 * with {m.mid} for a person identified by {id}.
	 * @param id: the identifier of the person
	 * @param m: the measurement history of interest
	 * @return the saved measurement
	 */
	@WebMethod(operationName="updatePersonMeasure")
	@WebResult(name="measure_id")
	public Long updatePersonMeasure(
			@WebParam(name="id") Long id,
			@WebParam(name="measure_type") MeasurementHistory m
	);
	
	/***
	 * An accessory method used to reset the database before the client start executing.
	 */
	@WebMethod
	void initializeDatabase();
}