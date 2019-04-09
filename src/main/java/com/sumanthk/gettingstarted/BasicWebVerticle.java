package com.sumanthk.gettingstarted;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import io.vertx.config.ConfigRetriever;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import io.vertx.ext.web.handler.StaticHandler;

/**
 * Hello world!
 *
 */
public class BasicWebVerticle extends AbstractVerticle
{
	
	private static final Logger LOGGER = LoggerFactory.getLogger(BasicWebVerticle.class); 
	
    public static void main( String[] args )
    {
    	// we can also setup deployment configurations in this way
    	/*DeploymentOptions options = new DeploymentOptions();
    	options.setConfig(new JsonObject().put("http.port", 8080));*/
    	
        Vertx vertx = Vertx.vertx();
        
        ConfigRetriever r = ConfigRetriever.create(vertx);
        
        r.getConfig(config -> {
        	if (config.succeeded()){
        		
        		JsonObject obj = config.result();
        		System.out.println(obj.encodePrettily());
        		DeploymentOptions options = new DeploymentOptions().setConfig(obj);
        		
        		vertx.deployVerticle(new BasicWebVerticle(), options);
        		
        	}
        	
        	
        	
        });
       
    }

	@Override
	public void start() throws Exception {
		LOGGER.info("verticle App started");
		
		// default handler without routes 
		// vertx.createHttpServer()
		// .requestHandler(routingContext ->
		// routingContext.response().end("<h1>welcome to vertx intro</h1>"))
		// .listen(8080);
		
		Router router = Router.router(vertx);
		
		Router subRouter = Router.router(vertx);
		
		ProductResources pr = new ProductResources();
		
		
		
		//ALL API goes through this
		subRouter.route("/*").handler(pr::defaultProcessorForAllAPI);
		
		//we need to have a body handler for put and post to accept body and convert them to json
		subRouter.route("/v1/products*").handler(BodyHandler.create());
		subRouter.get("/v1/products").handler(pr::getAllProducts);
		
		//adding subrouter to router
		router.mountSubRouter("/api/", subRouter);
		
		
		router.get("/yo.html").handler(routingContext -> {
			
			ClassLoader classLoader = getClass().getClassLoader();
			File file = new File(classLoader.getResource("webroot/yo.html").getFile());
			
			String result = "";
			
			try{
				StringBuilder res = new StringBuilder("");
				Scanner scanner = new Scanner(file);
				
				while (scanner.hasNextLine()){
					String line = scanner.nextLine();
					res.append(line).append("\n");
				}
				
				scanner.close();
				
				result = res.toString();
				
				result = replaceAlltokens(result, "{name}", "Sumanth");
			}catch(Exception e){
				e.printStackTrace();
			}
			
			routingContext.response().putHeader("content-type", "texthtml").end(result);
			
		});
		
		//Default If no routes are matched.
		router.route().handler(StaticHandler.create().setCachingEnabled(false));
		
		vertx.createHttpServer().requestHandler(router::accept).listen(config().getInteger("http.port"), asyncResult -> {
			
			if(asyncResult.succeeded()){
				
				LOGGER.info("http server running on port: "+ config().getInteger("http.port"));
				
			}else{
				
				LOGGER.error("could not stop http server: "+ asyncResult.cause());
				
			}
			
		});
		
	}
	
	/*private void defaultProcessorForAllAPI(RoutingContext routingContext){
		
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
	
	private void getAllProducts(RoutingContext routingContext){
		
		// we can add multiple jsons like aray of jsons.
		
		JsonObject responseJson = new JsonObject();
		
		Product firstItem = new Product("231", "hello");
		Product secondItem = new Product("232", "hi");
		
		List<Product> pList = new ArrayList<Product>();
		pList.add(firstItem);
		pList.add(secondItem);
		
		responseJson.put("products", pList);
		
		responseJson.put("itemno", "i123");
		responseJson.put("desc", "123 item");
		
		 routingContext.response()
		.setStatusCode(200)
		.putHeader("Context-type", "aplication/json")
		.end(Json.encodePrettily(responseJson));
		
		//if we want to return bad status
		routingContext.response()
		.setStatusCode(400)
		.putHeader("Context-type", "aplication/json")
		.end(Json.encodePrettily(new JsonObject().put("error", "some error")));
		
	}*/

	private String replaceAlltokens(String result, String token, String newValue) {
		// TODO Auto-generated method stub
		String output = result;
		
		while (output.indexOf(token)!= -1){
			output = output.replace(token, newValue);
		}
		
		return output;
	}

	@Override
	public void stop() throws Exception {
		// TODO Auto-generated method stub
		LOGGER.info("verticle App stopped");
	}
    
    
    
}
