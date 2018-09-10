package com.org.tests;

import static com.jayway.restassured.RestAssured.given;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import com.org.constants.Constants;
import com.org.dataprovider.JsonArrTestDataProvider;
import com.org.dataprovider.SchemaDataProvider;

public class DarkSkyAPITest {

	Response response;

	/*
	 * This is Before Test that calls the API
	 */
	@BeforeClass
	public void callAPI() {

		response = given().baseUri(Constants.BASE_URL + "/" + Constants.AUTH_KEY).log().everything()
				.contentType(ContentType.JSON).when().get("/" + Constants.COORDINATES).then()
				.contentType(ContentType.JSON).statusCode(200).extract().response();
	}

	/*
	 * This test validates top level schema
	 */
	@Test
	public void validateResponseTopLevelStructure() {
		try {
			HashMap<String, Object> res = response.body().jsonPath().get("$");
			HashMap<String, String> schema = SchemaDataProvider.getTestData();
			for (String key : schema.keySet()) {
				Assert.assertTrue(schema.get(key).equalsIgnoreCase(getDataType(res.get(key))));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/*
	 * This test validates Array Size of daily, hourly & minutely objects
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void validateJSONArrayCount() {
		try {
			HashMap<String, String> testData = JsonArrTestDataProvider.getTestData();
			for (String key : testData.keySet()) {
				Assert.assertTrue(((ArrayList<Object>) response.body().jsonPath().get(key + ".data")).size() == Integer
						.parseInt(testData.get(key)));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * This test validates coordinates in response is same as the same in request
	 */
	@Test
	public void validateCoordinatesInResponse() {
		try {
		Assert.assertTrue(
				(response.body().jsonPath().get("latitude") + "," + response.body().jsonPath().get("longitude"))
						.equalsIgnoreCase(Constants.COORDINATES));
		}catch(Exception e) {
			e.printStackTrace();
		}
	}

	public String getDataType(Object obj) {
		if (obj instanceof Integer)
			return "int";
		if (obj instanceof Float)
			return "float";
		if (obj instanceof String)
			return "string";
		if (obj instanceof Boolean)
			return "boolean";
		return "object";
	}
}
