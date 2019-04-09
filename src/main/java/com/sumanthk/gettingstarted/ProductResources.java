package com.sumanthk.gettingstarted;

import java.util.ArrayList;
import java.util.List;

import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;

public class ProductResources {
	
public void defaultProcessorForAllAPI(RoutingContext routingContext){
		
		if (!routingContext.request().getHeader("authtoken").equalsIgnoreCase("abc")){
			routingContext.response()
			.setStatusCode(401)
			.putHeader("Content-type", "application/json")
			.end(Json.encodePrettily(new JsonObject().put("message", "invalid token")));
		}else{
			//Allowing CORS - Cross Domain API calls
			routingContext.response().putHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
			routingContext.response().putHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "GET,POST,PUT,DELETE");
			
			// will call next matching route
			routingContext.next();
		}
		
	}
	
	public void getAllProducts(RoutingContext routingContext){
		
		// we can add multiple jsons like aray of jsons.
		
		JsonObject responseJson = new JsonObject();
		
		Product firstItem = new Product("231", "hello");
		Product secondItem = new Product("232", "hi");
		
		List<Product> pList = new ArrayList<Product>();
		pList.add(firstItem);
		pList.add(secondItem);
		
		responseJson.put("products", pList);
		
		/*responseJson.put("itemno", "i123");
		responseJson.put("desc", "123 item");*/
		
		 routingContext.response()
		.setStatusCode(200)
		.putHeader("Context-type", "aplication/json")
		.end(Json.encodePrettily(responseJson));
		
		//if we want to return bad status
		/*routingContext.response()
		.setStatusCode(400)
		.putHeader("Context-type", "aplication/json")
		.end(Json.encodePrettily(new JsonObject().put("error", "some error")));*/
		
	}

}
