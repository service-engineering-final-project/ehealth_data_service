# Health Internal Service (SOAP)

The **health internal service** is the service that interacts with the internal database providing data to the **storage data service** through a SOAP interface. Indeed, it handles all the persistence tasks.

| resource | link |
|----------|------|
| API documentation | http://docs.healthinternalservice.apiary.io/ |
| Heroku base URL | https://health-internal-service-ar.herokuapp.com/ws/ |
| WSDL file | https://health-internal-service-ar.herokuapp.com/ws/people?wsdl |

### How to run it
Since the server is already deployed on Heroku, it is only needed to make SOAP requests checking the WSDL file. However, you can also deploy again the server on Heroku via git.

**Optional**: If you want to try the server locally, you can follow the steps below:
* **Clone** the repo: `git clone https://github.com/service-engineering-final-project/health_internal_service.git`;
* **Navigate** into the project folder: `cd health_internal_service`;
* **Install** the packages needed: `ant install`;
* **Run** the server using ant: `ant execute.server`.