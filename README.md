### Introduction to Service Design and Engineering: 3rd assignment (Alan Ramponi, 179850)

This is the **server repository** (client repository: https://github.com/alanramponi/introsde-2016-assignment-3-client).

The project consists in developing a SOAP web service. An ant `build.xml` file allows to automate the compilation, cleaning and execution; in addition to that, dependencies are managed with ivy. The whole code is well documented and commented.

The project has been developed individually and the server WSDL can be found on heroku at the following url:
https://introsde-assignment3-ar.herokuapp.com/ws/people?wsdl

**Extra tasks**: as in the assignment-2, the health profile is built dinamically (according to measure types).

### Project architecture
The project is structured in several packages in order to organize all the classes in a reasonable way. Inside the main common package (`introsde.assignment.soap`) it is possible to find:
* `dao`: a package that contains the DAO:
   * **EHealthDao.java**: a class (singleton JAVA instance) that connects the model to the DB, specifically used to create an Entity Manager whenever we need to execute an operation in the SQLite database.
* `endpoint`: a package that contains the endpoint:
   * **PeoplePublisher.java**: the web service endpoint publisher.
* `model`: a package that contains the model objects of the application (annotated):
   * **Person.java**: the class of the "person" table;
   * **Measure.java**: the class of the "measure" table;
   * **Measurement.java**: the class of the "measurement" table;
   * **MeasurementHistory.java**: the class of the "measurement_history" table.
* `ws`: a package that contains the SOAP layer for the clients:
   * **People.java**: the service endpoint interface;
   * **PeopleImplementation.java**: the service endpoint implementation.

### Tasks
The `build.xml` file contains some targets in order to execute various operations. In particular, using ant (`ant execute.server`) it is possible to register the SOAP endpoint (that is executed by Heroku thanks to the Procfile task configuration file). The server accomplishes all the requests of the assignment (that can be found here: https://sites.google.com/a/unitn.it/introsde_2016-17/lab-sessions/assignments/assignment-3).
Among the other things, it is possible to clean the project using the **clean** target.
As in the assignment-2, an additional task was performed, i.e. the extra request related to the dynamical health profile.

### How to run it
Since the server is already (correctly) deployed on Heroku, it is only needed to **run the client as described on the client repository** (https://github.com/alanramponi/introsde-2016-assignment-3-client/blob/master/README.md). However, you can also deploy again the server on Heroku via git.

**Optional**: If you want to try the server locally, you can follow the steps below:
* **Clone** the repo: `git clone https://github.com/alanramponi/introsde-2016-assignment-3-server.git`;
* **Navigate** into the project folder: `cd introsde-2016-assignment-3-server`;
* **Run** the server using ant: `ant execute.server`.
  * Note: obviously, in this case you need to run the client after changing the URL on the client side (PeopleClient.java: decomment line 24 and comment line 23) from remote to local.
