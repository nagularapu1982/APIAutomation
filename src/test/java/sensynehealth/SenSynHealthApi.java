package sensynehealth;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matcher.*;
import static org.testng.Assert.assertEquals;
import java.io.IOException;
import java.util.List;
import org.codehaus.groovy.runtime.dgmimpl.arrays.IntegerArrayGetAtMetaMethod;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.google.gson.JsonObject;

public class SenSynHealthApi {
	String expectedProduct[] = { "Lavender heart", "Personalised cufflinks", 
	"Kids T-shirt" }; 
	@Test
	public void test_ListOfProducts() throws IOException
	{
		List<String> productlist=  given().
				when().
				get("http://localhost:5000/v1/products").
				jsonPath().getList("name");
		//for(int i=0;i<productlist.size();i++)
		for(int i=0;i<3;i++)
		{
			assertEquals( expectedProduct[i],productlist.get(i));		
		}
	}

	@Test
	public void test_getProduct()
	{
		String product  = given().pathParam("id",2).
				when().
				get("http://localhost:5000/v1/product/{id}").jsonPath().getJsonObject("name");
		Assert.assertEquals(product, expectedProduct[1]);
	}
	
	@Test
	public void test_addProduct()
	{
		JsonObject obj = new JsonObject();
		obj.addProperty("name", "test Product");
		obj.addProperty("price", 10);
		int satuscode  = given().contentType("application/json").body(obj).
				when().
				post("http://localhost:5000/v1/product").statusCode();
		assertEquals(satuscode, 200);
	}

	@Test
	public void test_updateProduct_withPrice()
	{
		double price = 11.0;
		JsonObject obj = new JsonObject();
		obj.addProperty("price", price);
		int statusCode  = given().contentType("application/json").pathParam("id",2).body(obj).
				when().
				put("http://localhost:5000/v1/product/{id}").getStatusCode();
		assertEquals(statusCode, 200);
		/******* verifying the updated price code *******/
		String updtedprice  = given().pathParam("id",2).
				when().
				get("http://localhost:5000/v1/product/{id}").jsonPath().getJsonObject("price");
		
		Assert.assertEquals(price, Double.parseDouble(updtedprice));
		
	}

	@Test
	public void test_deleteProduct()
	{
		int satuscode =  given().pathParam("id",4).
				when().
				get("http://localhost:5000/v1/product/{id}").statusCode();
		assertEquals(satuscode, 200);
	}

}
