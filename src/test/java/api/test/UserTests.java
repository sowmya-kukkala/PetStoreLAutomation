package api.test;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.github.javafaker.Faker;

import api.endpoints.UserEndPoints;
import api.payload.User;
import io.restassured.response.Response;

public class UserTests {
	
	Faker faker;
	User userPayload;
	
	public Logger logger;
	
	@BeforeClass
	public void setupData() 
	{
		faker = new Faker();
		userPayload = new User();
		
		userPayload.setId(faker.idNumber().hashCode());
		userPayload.setUsername(faker.name().username());
		userPayload.setFirstName(faker.name().firstName());
		userPayload.setLastName(faker.name().lastName());
		userPayload.setEmail(faker.internet().safeEmailAddress());
		userPayload.setPassword(faker.internet().password(5,10));
		userPayload.setPhone(faker.phoneNumber().cellPhone());
		
		// logs
		logger = LogManager.getLogger(this.getClass());
		
		logger.debug("Debugging...................");
	}
	
	@Test(priority=1)
	public void testPostUser() 
	{
		logger.info("*************Creating User******************");
		Response response = UserEndPoints.createUser(userPayload);
		response.then().log().all();
		
		Assert.assertEquals(response.getStatusCode(), 200);	
		logger.info("*************User Created******************");
	}
	
	@Test(priority=2)
	public void testGetUserByName() 
	{
		logger.info("*************Reading User Info******************");
		Response response = UserEndPoints.readUser(this.userPayload.getUsername());
		response.then().log().all();
		
		Assert.assertEquals(response.getStatusCode(), 200);	// TestNG Assertion
		logger.info("************* User Info Displayed******************");
	}
	
	@Test(priority=3)
	public void testUpdateUserByName() 
	{
		logger.info("*************Updating User Info******************");
		// Update data using Payload
		userPayload.setFirstName(faker.name().firstName());
		userPayload.setLastName(faker.name().lastName());
		userPayload.setEmail(faker.internet().safeEmailAddress());
		
		Response response = UserEndPoints.updateUser(this.userPayload.getUsername(), userPayload);
		response.then().log().body().statusCode(200); // Chai Assertion Library		
		
		// checking the data after the updation
		Response responseAfterupdate = UserEndPoints.readUser(this.userPayload.getUsername());
		Assert.assertEquals(responseAfterupdate.getStatusCode(), 200);
		
		logger.info("*************User Updated******************");
	}
	
	@Test(priority=4)
	public void testDeleteUserByName() 
	{
		logger.info("*************Deleting User Info******************");
		Response response = UserEndPoints.deleteUser(this.userPayload.getUsername());
		response.then().log().all();
		
		Assert.assertEquals(response.getStatusCode(), 200);
		
		logger.info("*************User Deleted******************");
	}

}
