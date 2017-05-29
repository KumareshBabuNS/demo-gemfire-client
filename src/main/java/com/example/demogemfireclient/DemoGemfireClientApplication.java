package com.example.demogemfireclient;

import com.gemstone.gemfire.cache.EntryEvent;
import com.gemstone.gemfire.cache.GemFireCache;
import com.gemstone.gemfire.cache.Region;
import com.gemstone.gemfire.cache.client.ClientRegionShortcut;
import com.gemstone.gemfire.cache.client.Pool;
import com.gemstone.gemfire.cache.execute.FunctionException;
import com.gemstone.gemfire.cache.execute.ResultCollector;
import com.gemstone.gemfire.cache.util.CacheListenerAdapter;
import com.gemstone.gemfire.distributed.DistributedMember;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.gemfire.client.ClientCacheFactoryBean;
import org.springframework.data.gemfire.client.ClientRegionFactoryBean;
import org.springframework.data.gemfire.client.PoolFactoryBean;
import org.springframework.data.gemfire.function.annotation.FunctionId;
import org.springframework.data.gemfire.function.annotation.OnServers;
import org.springframework.data.gemfire.function.config.EnableGemfireFunctionExecutions;
import org.springframework.data.gemfire.support.ConnectionEndpoint;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class DemoGemfireClientApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoGemfireClientApplication.class, args);
	}
}

@RestController
@Slf4j
class WebController{


	@Autowired FunctionExecution functionExecution;

	@Resource(name = "Factorials")
	private Region<Long, Long> factorials;

	@GetMapping("/")
	public String hi(){
		return "Hello";
	}

	@GetMapping("/all")
	public Iterable<Long> getAll(){
		return factorials.keySet();
	}

	@GetMapping("/find/{key}")
	public Long findOne(@PathVariable("key") Long key){
		return factorials.get(key);
	}

	@GetMapping("/put/{key}/{value}")
	public void add(@PathVariable("key") Long key, @PathVariable("value") Long value){
		log.info(key + " " + value);
		factorials.put(key, value);
	}

	@GetMapping("/func")
	public String func(){

		return functionExecution.doIt();
	}
}

@Slf4j
class FactorialCacheListener extends CacheListenerAdapter<Long, Long>{
	@Override
	public void afterCreate(EntryEvent e) {
		log.info("Created " + e.getNewValue());
	}

	@Override
	public void afterUpdate(EntryEvent e) {
		log.info("Updated " + e.getOldValue());
	}

}

@EnableGemfireFunctionExecutions(basePackages = "com.example.demogemfireclient")
@Configuration
class GemfireConfiguration {


	@Bean
	Properties gemfireProperties() {
		Properties gemfireProperties = new Properties();
		gemfireProperties.setProperty("log-level", "config");
		return gemfireProperties;
	}

	@Bean
	PoolFactoryBean gemfirePool(@Value("${locator.host:localhost}") String host, @Value("${locator.port:10334}") int port) {

		PoolFactoryBean gemfirePool = new PoolFactoryBean();

		gemfirePool.setKeepAlive(false);
		gemfirePool.setSubscriptionEnabled(true);
		gemfirePool.setThreadLocalConnections(false);
		gemfirePool.addLocators(new ConnectionEndpoint(host, port));


		return gemfirePool;
	}

	@Bean
	ClientCacheFactoryBean gemfireCache() {
		ClientCacheFactoryBean gemfireCache = new ClientCacheFactoryBean();
		gemfireCache.setClose(true);
		gemfireCache.setProperties(gemfireProperties());
		return gemfireCache;
	}

	@Bean(name = "Factorials")
	ClientRegionFactoryBean<Long, Long> factorialsRegion(GemFireCache gemfireCache, Pool gemfirePool){

		ClientRegionFactoryBean<Long, Long> factorialsRegion = new ClientRegionFactoryBean<>();
		factorialsRegion.setCache(gemfireCache);
		factorialsRegion.setName("Factorials");
		factorialsRegion.setPool(gemfirePool);
		factorialsRegion.setShortcut(ClientRegionShortcut.PROXY);

		FactorialCacheListener factorialCacheListener = new FactorialCacheListener();
		factorialsRegion.setCacheListeners((FactorialCacheListener[])Arrays.asList(factorialCacheListener).toArray());


		return factorialsRegion;

	}

}

//@OnRegion(region="Factorials")
@OnServers(resultCollector = "myResultCollector")
interface FunctionExecution{

	@FunctionId("function1")
	public String doIt();
}

@Slf4j
@Component
class MyResultCollector implements ResultCollector<String, List> {
	private List<String> result = null;

//	private String result = "";

	@Override
	public List getResult() throws FunctionException {
		log.info("getResult " + result.size());
		return result;
	}

	@Override
	public List getResult(long l, TimeUnit timeUnit) throws FunctionException, InterruptedException {
		log.info("getResult " + result.size());
		return result;
	}

	@Override
	public void addResult(DistributedMember distributedMember, String s) {
		log.info("addResult " + s);

		if(result == null) result = new ArrayList<>();

		if(result.size()>0) result.set(0,result.get(0).concat(s));
		else result.add(s);
	}

	@Override
	public void endResults() {
		log.info("endResults");
	}

	@Override
	public void clearResults() {
		log.info("clearResults");
		result = new ArrayList<>();
	}
}