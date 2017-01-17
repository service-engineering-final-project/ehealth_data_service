package introsde.ehealthdata.soap.endpoint;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URISyntaxException;

import javax.xml.ws.Endpoint;

import introsde.ehealthdata.soap.ws.PeopleImplementation;


/**
 * The endpoint publisher.
 * 
 * @author alan
 */

public class PeoplePublisher {
	public static void main(String[] args) throws IllegalArgumentException, IOException, URISyntaxException {
		String PROTOCOL = "http://";
		String HOSTNAME = InetAddress.getLocalHost().getHostAddress();
		String PORT = "6902";
		String BASE_URL = "/ws/people";
		
		if (HOSTNAME.equals("127.0.0.1")) {
			HOSTNAME = "localhost";
		}
		
		if (String.valueOf(System.getenv("PORT")) != "null") {
			PORT = String.valueOf(System.getenv("PORT"));
		}
		
		String endpointUrl = PROTOCOL + HOSTNAME + ":" + PORT + BASE_URL;
		
		System.out.println("Starting People Service...");
		System.out.println("--> Service published. Check out at the following endpoint url:\n" 
				+ endpointUrl + "?wsdl");
		
		Endpoint.publish(endpointUrl, new PeopleImplementation());
	}
}