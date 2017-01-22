package introsde.health.soap.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;

/**
 * A class (singleton JAVA instance) that will connect our model to the database, specifically used
 * to create an Entity Manager whenever we need to execute an operation in the SQLite database.
 * 
 * @author alan
 */

public enum EHealthDao {
	instance;
	
	private EntityManagerFactory emf;
	
	private EHealthDao() {
		if (emf!=null) {	// check if the Entity Manager Factory is null
			emf.close();
		}
		
		// Configure the Entity Manager Factory from a particular persistence unit
		emf = Persistence.createEntityManagerFactory("introsde-2016-assignment-3-server");
	}

	public EntityManager createEntityManager() {
		try {
			// Return the Entity Manager that will provide the ops from/to the DB
			return emf.createEntityManager();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void closeConnections(EntityManager em) {
		em.close();
	}
	
	public EntityTransaction getTransaction(EntityManager em) {
		return em.getTransaction();
	}
	
	public EntityManagerFactory getEntityManagerFactory() {
		return emf;
	}
	
	/**
	 * An accessory function that resets the database and repopulates it with new data
	 */
	public void initializeDB() {
		EntityManager em = getEntityManagerFactory().createEntityManager();
		EntityTransaction t = em.getTransaction();
		
		t.begin();
		
		// Clean the database from previously stored records
		int resDelPerson = em.createQuery("DELETE FROM Person p").executeUpdate();
		int resDelMeasure = em.createQuery("DELETE FROM Measure m").executeUpdate();
		int resDelMeasurement = em.createQuery("DELETE FROM Measurement mm").executeUpdate();
		int resDelMeasurementHistory = em.createQuery("DELETE FROM MeasurementHistory mh").executeUpdate();
		System.out.println("DELETE queries were executed (# records for each table: " 
				+ resDelPerson + ", " + resDelMeasure + ", " + resDelMeasurement + ", " + resDelMeasurementHistory + ")");
		
		// Store new data
		insertPerson(em, 1, "John", "Brown", "10-10-1980");
		insertPerson(em, 2, "Margaret", "Robinson", "15-09-1982");
		insertPerson(em, 3, "Jeremy", "Allen", "01-01-1971");
		insertPerson(em, 4, "Eric", "Harris", "04-04-1979");
		insertPerson(em, 5, "Anna", "Garcia", "12-12-1985");
		insertMeasure(em, 1, "weight");
		insertMeasure(em, 2, "height");
		insertMeasure(em, 3, "steps");
		insertMeasure(em, 4, "bloodpressure");
		insertMeasurement(em, 1, 1, "weight", "72", "Tue Nov 22 22:34:24 CET 2016", "Int");
		insertMeasurement(em, 2, 1, "height", "1.72", "Fri Nov 18 14:21:58 CET 2016", "Float");
		insertMeasurement(em, 3, 1, "steps", "60", "Fri Nov 18 14:21:58 CET 2016", "Int");
		insertMeasurement(em, 4, 1, "bloodpressure", "100", "Fri Nov 18 14:21:58 CET 2016", "Int");
		insertMeasurement(em, 5, 2, "weight", "56", "Tue Nov 15 13:22:24 CET 2016", "Int");
		insertMeasurement(em, 6, 2, "height", "1.68", "Tue Nov 15 13:22:24 CET 2016", "Float");
		insertMeasurement(em, 7, 3, "weight", "110", "Wed Nov 16 11:20:00 CET 2016", "Int");
		insertMeasurement(em, 8, 3, "height", "1.87", "Wed Nov 16 11:20:00 CET 2016", "Float");
		insertMeasurement(em, 9, 3, "bloodpressure", "115", "Wed Nov 16 11:20:00 CET 2016", "Int");
		insertMeasurement(em, 10, 4, "weight", "84", "Sat Oct 10 11:00:21 CET 2015", "Int");
		insertMeasurement(em, 11, 4, "height", "1.81", "Sat Oct 10 11:00:21 CET 2015", "Float");
		insertMeasurement(em, 12, 5, "weight", "52", "Sun Apr 12 09:00:01 CET 2015", "Int");
		insertMeasurement(em, 13, 5, "height", "1.64", "Sun Apr 12 09:00:01 CET 2015", "Float");
		insertMeasurementHistory(em, 1, 1, "weight", "79", "Sun Nov 15 16:28:15 CET 2015", "Int");
		insertMeasurementHistory(em, 2, 1, "height", "1.72", "Sun Nov 15 16:28:15 CET 2015", "Float");
		insertMeasurementHistory(em, 3, 1, "steps", "60", "Sun Nov 15 16:28:15 CET 2015", "Int");
		insertMeasurementHistory(em, 4, 1, "bloodpressure", "100", "Sun Nov 15 16:28:15 CET 2015", "Int");
		insertMeasurementHistory(em, 5, 1, "weight", "78", "Fri Jan 22 10:18:23 CET 2016", "Int");
		insertMeasurementHistory(em, 6, 1, "height", "1.72", "Fri Jan 22 10:18:23 CET 2016", "Float");
		insertMeasurementHistory(em, 7, 1, "steps", "62", "Fri Jan 22 10:18:23 CET 2016", "Int");
		insertMeasurementHistory(em, 8, 1, "bloodpressure", "105", "Fri Jan 22 10:18:23 CET 2016", "Int");
		insertMeasurementHistory(em, 9, 1, "weight", "76", "Wed Jul 20 11:08:14 CET 2016", "Int");
		insertMeasurementHistory(em, 10, 1, "weight", "75", "Fri Nov 18 14:21:58 CET 2016", "Int");
		insertMeasurementHistory(em, 11, 1, "height", "1.72", "Fri Nov 18 14:21:58 CET 2016", "Float");
		insertMeasurementHistory(em, 12, 1, "steps", "60", "Fri Nov 18 14:21:58 CET 2016", "Int");
		insertMeasurementHistory(em, 13, 1, "bloodpressure", "100", "Fri Nov 18 14:21:58 CET 2016", "Int");
		insertMeasurementHistory(em, 14, 2, "weight", "55", "Tue Feb 02 17:10:34 CET 2016", "Int");
		insertMeasurementHistory(em, 15, 2, "height", "1.68", "Tue Feb 02 17:10:34 CET 2016", "Float");
		insertMeasurementHistory(em, 16, 2, "weight", "56", "Tue Nov 15 13:22:24 CET 2016", "Int");
		insertMeasurementHistory(em, 17, 2, "height", "1.68", "Tue Nov 15 13:22:24 CET 2016", "Float");
		insertMeasurementHistory(em, 18, 3, "weight", "136", "Sat Jul 16 08:58:12 CET 2016", "Int");
		insertMeasurementHistory(em, 19, 3, "height", "1.87", "Sat Jul 16 08:58:12 CET 2016", "Float");
		insertMeasurementHistory(em, 20, 3, "bloodpressure", "134", "Sat Jul 16 08:58:12 CET 2016", "Int");
		insertMeasurementHistory(em, 21, 3, "bloodpressure", "128", "Tue Aug 16 10:11:12 CET 2016", "Int");
		insertMeasurementHistory(em, 22, 3, "bloodpressure", "124", "Fri Sep 16 15:09:03 CET 2016", "Int");
		insertMeasurementHistory(em, 23, 3, "bloodpressure", "121", "Sun Oct 16 09:29:43 CET 2016", "Int");
		insertMeasurementHistory(em, 24, 3, "weight", "110", "Wed Nov 16 11:20:00 CET 2016", "Int");
		insertMeasurementHistory(em, 25, 3, "height", "1.87", "Wed Nov 16 11:20:00 CET 2016", "Float");
		insertMeasurementHistory(em, 26, 3, "bloodpressure", "115", "Wed Nov 16 11:20:00 CET 2016", "Int");
		insertMeasurementHistory(em, 27, 4, "weight", "87", "Mon Oct 08 11:00:21 CET 2014", "Int");
		insertMeasurementHistory(em, 28, 4, "height", "1.81", "Mon Oct 08 11:00:21 CET 2014", "Float");
		insertMeasurementHistory(em, 29, 4, "weight", "84", "Sat Oct 10 11:00:21 CET 2015", "Int");
		insertMeasurementHistory(em, 30, 4, "height", "1.81", "Sat Oct 10 11:00:21 CET 2015", "Float");
		insertMeasurementHistory(em, 31, 5, "weight", "48", "Thu Oct 23 11:56:15 CET 2014", "Int");
		insertMeasurementHistory(em, 32, 5, "height", "1.64", "Thu Oct 23 11:56:15 CET 2014", "Float");
		insertMeasurementHistory(em, 33, 5, "weight", "49", "Tue Nov 04 10:16:36 CET 2014", "Int");
		insertMeasurementHistory(em, 34, 5, "weight", "51", "Tue Feb 10 16:43:11 CET 2015", "Int");
		insertMeasurementHistory(em, 35, 5, "weight", "52", "Sun Apr 12 09:00:01 CET 2015", "Int");
		insertMeasurementHistory(em, 36, 5, "height", "1.64", "Sun Apr 12 09:00:01 CET 2015", "Float");
		System.out.println("INSERT queries were executed!\n");
		
		t.commit();
    }
	
	/**
	 * A function that allows to insert data into the database (table: Person).
	 * @param em: the entity manager
	 * @param id: the person's identifier
	 * @param firstname: the person's name
	 * @param lastname: the person's surname
	 * @param birthdate: the person's birthdate
	 */
	public void insertPerson(EntityManager em, int id, String firstname, String lastname, String birthdate) {
		Query query = em.createNativeQuery("INSERT INTO Person (id, firstname, lastname, birthdate) " +
				"VALUES (?, ?, ?, ?)");
		query.setParameter(1, id);
		query.setParameter(2, firstname);
		query.setParameter(3, lastname);
		query.setParameter(4, birthdate);
		query.executeUpdate();
	}
	
	/**
	 * A function that allows to insert data into the database (table: Measure).
	 * @param em: the entity manager
	 * @param id: the measure's identifier
	 * @param measureName: the measure's name
	 */
	public void insertMeasure(EntityManager em, int id, String measureName) {
		Query query = em.createNativeQuery("INSERT INTO Measure (id, measure_name) " +
				"VALUES (?, ?)");
		query.setParameter(1, id);
		query.setParameter(2, measureName);
		query.executeUpdate();
	}
	
	/**
	 * A function that allows to insert data into the database (table: Measurement).
	 * @param em: the entity manager
	 * @param id: the measurement's identifier
	 * @param personId: the person's identifier
	 * @param measureName: the measure's name
	 * @param measureValue: the measure's value
	 * @param time: the time of creation
	 * @param measureValueType: the measure's value type
	 */
	public void insertMeasurement(EntityManager em, int id, int personId, String measureName, String measureValue, String time, String measureValueType) {
		Query query = em.createNativeQuery("INSERT INTO Measurement (id, person_id, measure_name, measure_value, time, measure_value_type) " +
				"VALUES (?, ?, ?, ?, ?, ?)");
		query.setParameter(1, id);
		query.setParameter(2, personId);
		query.setParameter(3, measureName);
		query.setParameter(4, measureValue);
		query.setParameter(5, time);
		query.setParameter(6, measureValueType);
		query.executeUpdate();
	}
	
	/**
	 * A function that allows to insert data into the database (table: MeasurementHistory).
	 * @param em: the entity manager
	 * @param id: the measurement's identifier
	 * @param personId: the person's identifier
	 * @param measureName: the measure's name
	 * @param measureValue: the measure's value
	 * @param time: the time of creation
	 * @param measureValueType: the measure's value type
	 */
	public void insertMeasurementHistory(EntityManager em, int id, int personId, String measureName, String measureValue, String time, String measureValueType) {
		Query query = em.createNativeQuery("INSERT INTO Measurement_History (id, person_id, measure_name, measure_value, time, measure_value_type) " +
				"VALUES (?, ?, ?, ?, ?, ?)");
		query.setParameter(1, id);
		query.setParameter(2, personId);
		query.setParameter(3, measureName);
		query.setParameter(4, measureValue);
		query.setParameter(5, time);
		query.setParameter(6, measureValueType);
		query.executeUpdate();
	}
}