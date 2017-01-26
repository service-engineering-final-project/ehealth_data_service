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
		emf = Persistence.createEntityManagerFactory("health-internal-service");
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
		int resDelGoal = em.createQuery("DELETE FROM Goal g").executeUpdate();
		System.out.println("DELETE queries were executed (# records for each table: " 
				+ resDelPerson + ", " + resDelMeasure + ", " + resDelMeasurement + ", " 
				+ resDelMeasurementHistory + ", " + resDelGoal + ")");
		
		// Store new data
		insertPerson(em, 1, "John", "Brown", "10-10-1943");
		insertPerson(em, 2, "Margaret", "Robinson", "15-09-1982");
		insertPerson(em, 3, "Jeremy", "Allen", "01-01-1971");
		
		insertMeasure(em, 1, "weight");
		insertMeasure(em, 2, "height");
		insertMeasure(em, 3, "steps");
		insertMeasure(em, 4, "bloodpressure");
		insertMeasure(em, 5, "sleep_hours");
		insertMeasure(em, 6, "carbohydrates");
		insertMeasure(em, 7, "proteins");
		insertMeasure(em, 8, "lipids");
		insertMeasure(em, 9, "sodium");
		
		// PERSON #1
		// More recent measurements (formerly, Day 14)
		insertMeasurement(em, 1, 1, "weight", "74", "Tue Jan 31 22:34:24 CET 2017", "Kg");
		insertMeasurement(em, 2, 1, "height", "1.72", "Tue Jan 31 22:34:24 CET 2017", "m");
		insertMeasurement(em, 3, 1, "steps", "1850", "Tue Jan 31 22:34:24 CET 2017", "steps");
		insertMeasurement(em, 4, 1, "bloodpressure", "122", "Tue Jan 31 22:34:24 CET 2017", "mmHg");
		insertMeasurement(em, 5, 1, "sleep_hours", "8", "Tue Jan 31 22:34:24 CET 2017", "h");
		insertMeasurement(em, 6, 1, "carbohydrates", "402", "Tue Jan 31 22:34:24 CET 2017", "g");
		insertMeasurement(em, 7, 1, "proteins", "122", "Tue Jan 31 22:34:24 CET 2017", "g");
		insertMeasurement(em, 8, 1, "lipids", "160", "Tue Jan 31 22:34:24 CET 2017", "g");
		insertMeasurement(em, 9, 1, "sodium", "1.9", "Tue Jan 31 22:34:24 CET 2017", "g");
		
		// Day 1
		insertMeasurementHistory(em, 1, 1, "weight", "78.5", "Wed Jan 18 22:34:24 CET 2017", "Kg");
		insertMeasurementHistory(em, 2, 1, "height", "1.72", "Wed Jan 18 22:34:24 CET 2017", "m");
		insertMeasurementHistory(em, 3, 1, "steps", "1900", "Wed Jan 18 22:34:24 CET 2017", "steps");
		insertMeasurementHistory(em, 4, 1, "bloodpressure", "141", "Wed Jan 18 22:34:24 CET 2017", "mmHg");
		insertMeasurementHistory(em, 5, 1, "sleep_hours", "8", "Wed Jan 18 22:34:24 CET 2017", "h");
		insertMeasurementHistory(em, 6, 1, "carbohydrates", "600", "Wed Jan 18 22:34:24 CET 2017", "g");
		insertMeasurementHistory(em, 7, 1, "proteins", "104", "Wed Jan 18 22:34:24 CET 2017", "g");
		insertMeasurementHistory(em, 8, 1, "lipids", "220", "Wed Jan 18 22:34:24 CET 2017", "g");
		insertMeasurementHistory(em, 9, 1, "sodium", "2.5", "Wed Jan 18 22:34:24 CET 2017", "g");
		
		// Day 2
		insertMeasurementHistory(em, 10, 1, "weight", "79", "Thu Jan 19 22:34:24 CET 2017", "Kg");
		insertMeasurementHistory(em, 11, 1, "height", "1.72", "Thu Jan 19 22:34:24 CET 2017", "m");
		insertMeasurementHistory(em, 12, 1, "steps", "3950", "Thu Jan 19 22:34:24 CET 2017", "steps");
		insertMeasurementHistory(em, 13, 1, "bloodpressure", "138", "Thu Jan 19 22:34:24 CET 2017", "mmHg");
		insertMeasurementHistory(em, 14, 1, "sleep_hours", "8", "Thu Jan 19 22:34:24 CET 2017", "h");
		insertMeasurementHistory(em, 15, 1, "carbohydrates", "360", "Thu Jan 19 22:34:24 CET 2017", "g");
		insertMeasurementHistory(em, 16, 1, "proteins", "50", "Thu Jan 19 22:34:24 CET 2017", "g");
		insertMeasurementHistory(em, 17, 1, "lipids", "159", "Thu Jan 19 22:34:24 CET 2017", "g");
		insertMeasurementHistory(em, 18, 1, "sodium", "2.6", "Thu Jan 19 22:34:24 CET 2017", "g");
		
		// Day 3
		insertMeasurementHistory(em, 19, 1, "weight", "78.5", "Fri Jan 20 22:34:24 CET 2017", "Kg");
		insertMeasurementHistory(em, 20, 1, "height", "1.72", "Fri Jan 20 22:34:24 CET 2017", "m");
		insertMeasurementHistory(em, 21, 1, "steps", "1500", "Fri Jan 20 22:34:24 CET 2017", "steps");
		insertMeasurementHistory(em, 22, 1, "bloodpressure", "139", "Fri Jan 20 22:34:24 CET 2017", "mmHg");
		insertMeasurementHistory(em, 23, 1, "sleep_hours", "7", "Fri Jan 20 22:34:24 CET 2017", "h");
		insertMeasurementHistory(em, 24, 1, "carbohydrates", "399", "Fri Jan 20 22:34:24 CET 2017", "g");
		insertMeasurementHistory(em, 25, 1, "proteins", "55", "Fri Jan 20 22:34:24 CET 2017", "g");
		insertMeasurementHistory(em, 26, 1, "lipids", "150", "Fri Jan 20 22:34:24 CET 2017", "g");
		insertMeasurementHistory(em, 27, 1, "sodium", "2.7", "Fri Jan 20 22:34:24 CET 2017", "g");
		
		// Day 4
		insertMeasurementHistory(em, 28, 1, "weight", "78", "Sat Jan 21 22:34:24 CET 2017", "Kg");
		insertMeasurementHistory(em, 29, 1, "height", "1.72", "Sat Jan 21 22:34:24 CET 2017", "m");
		insertMeasurementHistory(em, 30, 1, "steps", "2600", "Sat Jan 21 22:34:24 CET 2017", "steps");
		insertMeasurementHistory(em, 31, 1, "bloodpressure", "136", "Sat Jan 21 22:34:24 CET 2017", "mmHg");
		insertMeasurementHistory(em, 32, 1, "sleep_hours", "6", "Sat Jan 21 22:34:24 CET 2017", "h");
		insertMeasurementHistory(em, 33, 1, "carbohydrates", "408", "Sat Jan 21 22:34:24 CET 2017", "g");
		insertMeasurementHistory(em, 34, 1, "proteins", "79", "Sat Jan 21 22:34:24 CET 2017", "g");
		insertMeasurementHistory(em, 35, 1, "lipids", "140", "Sat Jan 21 22:34:24 CET 2017", "g");
		insertMeasurementHistory(em, 36, 1, "sodium", "2.4", "Sat Jan 21 22:34:24 CET 2017", "g");
		
		// Day 5
		insertMeasurementHistory(em, 37, 1, "weight", "77.5", "Sun Jan 22 22:34:24 CET 2017", "Kg");
		insertMeasurementHistory(em, 38, 1, "height", "1.72", "Sun Jan 22 22:34:24 CET 2017", "m");
		insertMeasurementHistory(em, 39, 1, "steps", "3000", "Sun Jan 22 22:34:24 CET 2017", "steps");
		insertMeasurementHistory(em, 40, 1, "bloodpressure", "132", "Sun Jan 22 22:34:24 CET 2017", "mmHg");
		insertMeasurementHistory(em, 41, 1, "sleep_hours", "8", "Sun Jan 22 22:34:24 CET 2017", "h");
		insertMeasurementHistory(em, 42, 1, "carbohydrates", "406", "Sun Jan 22 22:34:24 CET 2017", "g");
		insertMeasurementHistory(em, 43, 1, "proteins", "79", "Sun Jan 22 22:34:24 CET 2017", "g");
		insertMeasurementHistory(em, 44, 1, "lipids", "166", "Sun Jan 22 22:34:24 CET 2017", "g");
		insertMeasurementHistory(em, 45, 1, "sodium", "3.4", "Sun Jan 22 22:34:24 CET 2017", "g");
		
		// Day 6
		insertMeasurementHistory(em, 46, 1, "weight", "78", "Mon Jan 23 22:34:24 CET 2017", "Kg");
		insertMeasurementHistory(em, 47, 1, "height", "1.72", "Mon Jan 23 22:34:24 CET 2017", "m");
		insertMeasurementHistory(em, 48, 1, "steps", "1750", "Mon Jan 23 22:34:24 CET 2017", "steps");
		insertMeasurementHistory(em, 49, 1, "bloodpressure", "131", "Mon Jan 23 22:34:24 CET 2017", "mmHg");
		insertMeasurementHistory(em, 50, 1, "sleep_hours", "5", "Mon Jan 23 22:34:24 CET 2017", "h");
		insertMeasurementHistory(em, 51, 1, "carbohydrates", "503", "Mon Jan 23 22:34:24 CET 2017", "g");
		insertMeasurementHistory(em, 52, 1, "proteins", "65", "Mon Jan 23 22:34:24 CET 2017", "g");
		insertMeasurementHistory(em, 53, 1, "lipids", "94", "Mon Jan 23 22:34:24 CET 2017", "g");
		insertMeasurementHistory(em, 54, 1, "sodium", "4.1", "Mon Jan 23 22:34:24 CET 2017", "g");
		
		// Day 7
		insertMeasurementHistory(em, 55, 1, "weight", "76.5", "Tue Jan 24 22:34:24 CET 2017", "Kg");
		insertMeasurementHistory(em, 56, 1, "height", "1.72", "Tue Jan 24 22:34:24 CET 2017", "m");
		insertMeasurementHistory(em, 57, 1, "steps", "2800", "Tue Jan 24 22:34:24 CET 2017", "steps");
		insertMeasurementHistory(em, 58, 1, "bloodpressure", "133", "Tue Jan 24 22:34:24 CET 2017", "mmHg");
		insertMeasurementHistory(em, 59, 1, "sleep_hours", "11", "Tue Jan 24 22:34:24 CET 2017", "h");
		insertMeasurementHistory(em, 60, 1, "carbohydrates", "501", "Tue Jan 24 22:34:24 CET 2017", "g");
		insertMeasurementHistory(em, 61, 1, "proteins", "100", "Tue Jan 24 22:34:24 CET 2017", "g");
		insertMeasurementHistory(em, 62, 1, "lipids", "179", "Tue Jan 24 22:34:24 CET 2017", "g");
		insertMeasurementHistory(em, 63, 1, "sodium", "1.9", "Tue Jan 24 22:34:24 CET 2017", "g");
		
		// Day 8
		insertMeasurementHistory(em, 64, 1, "weight", "76.5", "Wed Jan 25 22:34:24 CET 2017", "Kg");
		insertMeasurementHistory(em, 65, 1, "height", "1.72", "Wed Jan 25 22:34:24 CET 2017", "m");
		insertMeasurementHistory(em, 66, 1, "steps", "1000", "Wed Jan 25 22:34:24 CET 2017", "steps");
		insertMeasurementHistory(em, 67, 1, "bloodpressure", "130", "Wed Jan 25 22:34:24 CET 2017", "mmHg");
		insertMeasurementHistory(em, 68, 1, "sleep_hours", "10", "Wed Jan 25 22:34:24 CET 2017", "h");
		insertMeasurementHistory(em, 69, 1, "carbohydrates", "350", "Wed Jan 25 22:34:24 CET 2017", "g");
		insertMeasurementHistory(em, 70, 1, "proteins", "105", "Wed Jan 25 22:34:24 CET 2017", "g");
		insertMeasurementHistory(em, 71, 1, "lipids", "78", "Wed Jan 25 22:34:24 CET 2017", "g");
		insertMeasurementHistory(em, 72, 1, "sodium", "1.2", "Wed Jan 25 22:34:24 CET 2017", "g");
		
		// Day 9
		insertMeasurementHistory(em, 73, 1, "weight", "76", "Thu Jan 26 22:34:24 CET 2017", "Kg");
		insertMeasurementHistory(em, 74, 1, "height", "1.72", "Thu Jan 26 22:34:24 CET 2017", "m");
		insertMeasurementHistory(em, 75, 1, "steps", "800", "Thu Jan 26 22:34:24 CET 2017", "steps");
		insertMeasurementHistory(em, 76, 1, "bloodpressure", "127", "Thu Jan 26 22:34:24 CET 2017", "mmHg");
		insertMeasurementHistory(em, 77, 1, "sleep_hours", "8", "Thu Jan 26 22:34:24 CET 2017", "h");
		insertMeasurementHistory(em, 78, 1, "carbohydrates", "392", "Thu Jan 26 22:34:24 CET 2017", "g");
		insertMeasurementHistory(em, 79, 1, "proteins", "101", "Thu Jan 26 22:34:24 CET 2017", "g");
		insertMeasurementHistory(em, 80, 1, "lipids", "79", "Thu Jan 26 22:34:24 CET 2017", "g");
		insertMeasurementHistory(em, 81, 1, "sodium", "2.1", "Thu Jan 26 22:34:24 CET 2017", "g");
		
		// Day 10
		insertMeasurementHistory(em, 82, 1, "weight", "74.5", "Fri Jan 27 22:34:24 CET 2017", "Kg");
		insertMeasurementHistory(em, 83, 1, "height", "1.72", "Fri Jan 27 22:34:24 CET 2017", "m");
		insertMeasurementHistory(em, 84, 1, "steps", "600", "Fri Jan 27 22:34:24 CET 2017", "steps");
		insertMeasurementHistory(em, 85, 1, "bloodpressure", "126", "Fri Jan 27 22:34:24 CET 2017", "mmHg");
		insertMeasurementHistory(em, 86, 1, "sleep_hours", "8", "Fri Jan 27 22:34:24 CET 2017", "h");
		insertMeasurementHistory(em, 87, 1, "carbohydrates", "450", "Fri Jan 27 22:34:24 CET 2017", "g");
		insertMeasurementHistory(em, 88, 1, "proteins", "99", "Fri Jan 27 22:34:24 CET 2017", "g");
		insertMeasurementHistory(em, 89, 1, "lipids", "209", "Fri Jan 27 22:34:24 CET 2017", "g");
		insertMeasurementHistory(em, 90, 1, "sodium", "2.4", "Fri Jan 27 22:34:24 CET 2017", "g");
		
		// Day 11
		insertMeasurementHistory(em, 91, 1, "weight", "75", "Sat Jan 28 22:34:24 CET 2017", "Kg");
		insertMeasurementHistory(em, 92, 1, "height", "1.72", "Sat Jan 28 22:34:24 CET 2017", "m");
		insertMeasurementHistory(em, 93, 1, "steps", "300", "Sat Jan 28 22:34:24 CET 2017", "steps");
		insertMeasurementHistory(em, 94, 1, "bloodpressure", "125", "Sat Jan 28 22:34:24 CET 2017", "mmHg");
		insertMeasurementHistory(em, 95, 1, "sleep_hours", "7", "Sat Jan 28 22:34:24 CET 2017", "h");
		insertMeasurementHistory(em, 96, 1, "carbohydrates", "470", "Sat Jan 28 22:34:24 CET 2017", "g");
		insertMeasurementHistory(em, 97, 1, "proteins", "87", "Sat Jan 28 22:34:24 CET 2017", "g");
		insertMeasurementHistory(em, 98, 1, "lipids", "129", "Sat Jan 28 22:34:24 CET 2017", "g");
		insertMeasurementHistory(em, 99, 1, "sodium", "2.7", "Sat Jan 28 22:34:24 CET 2017", "g");
		
		// Day 12
		insertMeasurementHistory(em, 100, 1, "weight", "74.5", "Sat Jan 29 22:34:24 CET 2017", "Kg");
		insertMeasurementHistory(em, 101, 1, "height", "1.72", "Sun Jan 29 22:34:24 CET 2017", "m");
		insertMeasurementHistory(em, 102, 1, "steps", "1000", "Sun Jan 29 22:34:24 CET 2017", "steps");
		insertMeasurementHistory(em, 103, 1, "bloodpressure", "122", "Sun Jan 29 22:34:24 CET 2017", "mmHg");
		insertMeasurementHistory(em, 104, 1, "sleep_hours", "8", "Sun Jan 29 22:34:24 CET 2017", "h");
		insertMeasurementHistory(em, 105, 1, "carbohydrates", "410", "Sun Jan 29 22:34:24 CET 2017", "g");
		insertMeasurementHistory(em, 106, 1, "proteins", "110", "Sun Jan 29 22:34:24 CET 2017", "g");
		insertMeasurementHistory(em, 107, 1, "lipids", "169", "Sun Jan 29 22:34:24 CET 2017", "g");
		insertMeasurementHistory(em, 108, 1, "sodium", "1.9", "Sun Jan 29 22:34:24 CET 2017", "g");
		
		// Day 13
		insertMeasurementHistory(em, 109, 1, "weight", "74.5", "Mon Jan 30 22:34:24 CET 2017", "Kg");
		insertMeasurementHistory(em, 110, 1, "height", "1.72", "Mon Jan 30 22:34:24 CET 2017", "m");
		insertMeasurementHistory(em, 111, 1, "steps", "1800", "Mon Jan 30 22:34:24 CET 2017", "steps");
		insertMeasurementHistory(em, 112, 1, "bloodpressure", "123", "Mon Jan 30 22:34:24 CET 2017", "mmHg");
		insertMeasurementHistory(em, 113, 1, "sleep_hours", "9", "Mon Jan 30 22:34:24 CET 2017", "h");
		insertMeasurementHistory(em, 114, 1, "carbohydrates", "400", "Mon Jan 30 22:34:24 CET 2017", "g");
		insertMeasurementHistory(em, 115, 1, "proteins", "121", "Mon Jan 30 22:34:24 CET 2017", "g");
		insertMeasurementHistory(em, 116, 1, "lipids", "179", "Mon Jan 30 22:34:24 CET 2017", "g");
		insertMeasurementHistory(em, 117, 1, "sodium", "1.8", "Mon Jan 30 22:34:24 CET 2017", "g");
		
		// PERSON #2
		insertMeasurement(em, 10, 2, "weight", "82", "Tue Jan 31 22:34:24 CET 2017", "Kg");
		insertMeasurement(em, 11, 2, "height", "1.69", "Tue Jan 31 22:34:24 CET 2017", "m");
		insertMeasurement(em, 12, 2, "steps", "1150", "Tue Jan 31 22:34:24 CET 2017", "steps");
		insertMeasurement(em, 13, 2, "bloodpressure", "152", "Tue Jan 31 22:34:24 CET 2017", "mmHg");
		insertMeasurement(em, 14, 2, "sleep_hours", "10", "Tue Jan 31 22:34:24 CET 2017", "h");
		insertMeasurement(em, 15, 2, "carbohydrates", "502", "Tue Jan 31 22:34:24 CET 2017", "g");
		insertMeasurement(em, 16, 2, "proteins", "112", "Tue Jan 31 22:34:24 CET 2017", "g");
		insertMeasurement(em, 17, 2, "lipids", "190", "Tue Jan 31 22:34:24 CET 2017", "g");
		insertMeasurement(em, 18, 2, "sodium", "3.2", "Tue Jan 31 22:34:24 CET 2017", "g");
		insertMeasurementHistory(em, 118, 2, "weight", "81.5", "Mon Jan 30 22:34:24 CET 2017", "Kg");
		insertMeasurementHistory(em, 119, 2, "height", "1.69", "Mon Jan 30 22:34:24 CET 2017", "m");
		insertMeasurementHistory(em, 120, 2, "steps", "1850", "Mon Jan 30 22:34:24 CET 2017", "steps");
		insertMeasurementHistory(em, 121, 2, "bloodpressure", "151", "Mon Jan 30 22:34:24 CET 2017", "mmHg");
		insertMeasurementHistory(em, 122, 2, "sleep_hours", "9", "Mon Jan 30 22:34:24 CET 2017", "h");
		insertMeasurementHistory(em, 123, 2, "carbohydrates", "510", "Mon Jan 30 22:34:24 CET 2017", "g");
		insertMeasurementHistory(em, 124, 2, "proteins", "133", "Mon Jan 30 22:34:24 CET 2017", "g");
		insertMeasurementHistory(em, 125, 2, "lipids", "160", "Mon Jan 30 22:34:24 CET 2017", "g");
		insertMeasurementHistory(em, 126, 2, "sodium", "3.1", "Mon Jan 30 22:34:24 CET 2017", "g");
		
		// PERSON #3
		insertMeasurement(em, 19, 3, "weight", "82", "Tue Jan 31 22:34:24 CET 2017", "Kg");
		insertMeasurement(em, 20, 3, "height", "1.71", "Tue Jan 31 22:34:24 CET 2017", "m");
		insertMeasurement(em, 21, 3, "steps", "1950", "Tue Jan 31 22:34:24 CET 2017", "steps");
		insertMeasurement(em, 22, 3, "bloodpressure", "150", "Tue Jan 31 22:34:24 CET 2017", "mmHg");
		insertMeasurement(em, 23, 3, "sleep_hours", "8", "Tue Jan 31 22:34:24 CET 2017", "h");
		insertMeasurement(em, 24, 3, "carbohydrates", "450", "Tue Jan 31 22:34:24 CET 2017", "g");
		insertMeasurement(em, 25, 3, "proteins", "122", "Tue Jan 31 22:34:24 CET 2017", "g");
		insertMeasurement(em, 26, 3, "lipids", "155", "Tue Jan 31 22:34:24 CET 2017", "g");
		insertMeasurement(em, 27, 3, "sodium", "2.8", "Tue Jan 31 22:34:24 CET 2017", "g");
		insertMeasurementHistory(em, 127, 3, "weight", "81.5", "Mon Jan 30 22:34:24 CET 2017", "Kg");
		insertMeasurementHistory(em, 128, 3, "height", "1.71", "Mon Jan 30 22:34:24 CET 2017", "m");
		insertMeasurementHistory(em, 129, 3, "steps", "1850", "Mon Jan 30 22:34:24 CET 2017", "steps");
		insertMeasurementHistory(em, 130, 3, "bloodpressure", "151", "Mon Jan 30 22:34:24 CET 2017", "mmHg");
		insertMeasurementHistory(em, 131, 3, "sleep_hours", "9", "Mon Jan 30 22:34:24 CET 2017", "h");
		insertMeasurementHistory(em, 132, 3, "carbohydrates", "510", "Mon Jan 30 22:34:24 CET 2017", "g");
		insertMeasurementHistory(em, 133, 3, "proteins", "133", "Mon Jan 30 22:34:24 CET 2017", "g");
		insertMeasurementHistory(em, 134, 3, "lipids", "160", "Mon Jan 30 22:34:24 CET 2017", "g");
		insertMeasurementHistory(em, 135, 3, "sodium", "3.1", "Mon Jan 30 22:34:24 CET 2017", "g");
		
		insertGoal(em, 1, 1, "weight", "81", "73.5", "Wed Jan 18 22:34:24 CET 2017", "Sun Dec 31 22:34:24 CET 2017", "NO");
		insertGoal(em, 2, 1, "steps", "0", "9000", "Wed Jan 18 22:34:24 CET 2017", "Sun Dec 31 22:34:24 CET 2017", "NO");
		insertGoal(em, 3, 1, "bloodpressure", "141", "119", "Thu Jan 26 22:34:24 CET 2017", "Thu Feb 02 22:34:24 CET 2017", "NO");
		insertGoal(em, 4, 1, "sleep_hours", "0", "9", "Tue Jan 31 22:34:24 CET 2017", "Wed Feb 01 22:34:24 CET 2017", "NO");
		insertGoal(em, 5, 1, "carbohydrates", "0", "406", "Tue Jan 31 22:34:24 CET 2017", "Wed Feb 01 22:34:24 CET 2017", "NO");
		insertGoal(em, 6, 1, "proteins", "0", "169", "Tue Jan 31 22:34:24 CET 2017", "Wed Feb 01 22:34:24 CET 2017", "NO");
		insertGoal(em, 7, 1, "lipids", "0", "102", "Tue Jan 31 22:34:24 CET 2017", "Wed Feb 01 22:34:24 CET 2017", "NO");
		insertGoal(em, 8, 1, "sodium", "0", "2.0", "Tue Jan 31 22:34:24 CET 2017", "Wed Feb 01 22:34:24 CET 2017", "NO");
		
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
	
	/**
	 * A function that allows to insert data into the database (table: Goal).
	 * @param em: the entity manager
	 * @param id: the goal's identifier
	 * @param personId: the person's identifier
	 * @param title: the goal's title
	 * @param initValue: the initial value
	 * @param finalValue: the final value (the goal value)
	 * @param deadline: the final date (the goal date)
	 * @param time: the time of creation
	 * @param achieved: the status (YES/NO)
	 */
	public void insertGoal(EntityManager em, int id, int personId, String title, String initValue, String finalValue, String deadline, String time, String achieved) {
		Query query = em.createNativeQuery("INSERT INTO Goal (id, person_id, title, init_value, final_value, deadline, time, achieved) " +
				"VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
		query.setParameter(1, id);
		query.setParameter(2, personId);
		query.setParameter(3, title);
		query.setParameter(4, initValue);
		query.setParameter(5, finalValue);
		query.setParameter(6, deadline);
		query.setParameter(7, time);
		query.setParameter(8, achieved);
		query.executeUpdate();
	}
}