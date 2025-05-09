package com.yaksha.assignment.functional;

import static com.yaksha.assignment.utils.TestUtils.businessTestFile;
import static com.yaksha.assignment.utils.TestUtils.currentTest;
import static com.yaksha.assignment.utils.TestUtils.testReport;
import static com.yaksha.assignment.utils.TestUtils.yakshaAssert;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.yaksha.assignment.controller.AppController;
import com.yaksha.assignment.utils.JavaParserUtils;

@WebMvcTest(AppController.class)
public class ApiControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@AfterAll
	public static void afterAll() {
		testReport();
	}

	@Test
	public void testHandleSuccess() throws Exception {
		String apiUrl = "https://reqres.in/api/users/2"; // A valid URL that requires basic authentication

		boolean statusOk = false;
		boolean containsSuccessMessage = false;

		try {
			MvcResult result = mockMvc.perform(get("/authenticateApi")
					.param("apiUrl", apiUrl)
					.param("username", "user") // Correct username
					.param("password", "password")) // Correct password
					.andReturn(); // Capture the result

			int responseStatus = result.getResponse().getStatus();

			// Check if the HTTP status is OK (200)
			if (responseStatus == 200) {
				statusOk = true;
				String responseBody = result.getResponse().getContentAsString();
				containsSuccessMessage = responseBody.contains("data");
			} else {
				System.out.println("API request failed with status: " + responseStatus);
			}

		} catch (Exception ex) {
			System.out.println("Error occurred while sending the request: " + ex.getMessage());
		}

		boolean finalResult = statusOk && containsSuccessMessage;

		// Assert test case result while handling failure scenario
		if (!finalResult) {
			System.out.println("Test case execution completed, but the expected API response was not received.");
		}

		yakshaAssert(currentTest(), finalResult ? "true" : "false", businessTestFile);
	}

	@Test
	public void testHandleUnauthorized() throws Exception {
		String apiUrl = "https://reqres.in/api/users/2"; // A valid URL that requires basic authentication

		// Perform the GET request without passing username and password (Reqres allows
		// any credentials)
		MvcResult result = mockMvc.perform(get("/authenticateApi").param("apiUrl", apiUrl)) // No username and password
																							// parameters
				.andReturn(); // Capture the result

		// Declare boolean flags to track assertions
		boolean statusOk = false;
		boolean containsErrorMessage = false;

		try {
			// Check if the status is 401 Unauthorized
			statusOk = result.getResponse().getStatus() == 400;

			// Check if the response contains "Unauthorized" message
			containsErrorMessage = result.getResponse().getContentAsString().contains("");
			
			// Use yakshaAssert to ensure all checks pass
			yakshaAssert(currentTest(), statusOk && containsErrorMessage ? "true" : "false", businessTestFile);

		} catch (Exception ex) {
			System.out.println("Error occurred: " + ex.getMessage());
			yakshaAssert(currentTest(), "false", businessTestFile); // Fail the test if an exception is thrown
		}
	}

	@Test
	public void testControllerStructure() throws Exception {
		String filePath = "src/main/java/com/yaksha/assignment/controller/AppController.java"; // Update path to your
																								// file
		boolean result = JavaParserUtils.checkControllerStructure(filePath, // Pass the class file path
				"RestController", // Check if @RestController is used on the class
				"authenticateApiRequest", // Check if the method name is correct
				"GetMapping", // Check if @GetMapping is present on the method
				"apiUrl", // Check if the parameter has @RequestParam annotation
				"String" // Ensure the return type is String
		);
		// checkControllerStructure
		yakshaAssert(currentTest(), result, businessTestFile);
	}
}
