package com.example.demogemfireclient;

import com.gemstone.gemfire.cache.GemFireCache;
import com.gemstone.gemfire.cache.client.ClientRegionShortcut;
import com.gemstone.gemfire.cache.client.Pool;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.gemfire.client.ClientCacheFactoryBean;
import org.springframework.data.gemfire.client.ClientRegionFactoryBean;
import org.springframework.data.gemfire.client.PoolFactoryBean;
import org.springframework.data.gemfire.function.config.EnableGemfireFunctionExecutions;
import org.springframework.data.gemfire.support.ConnectionEndpoint;
import org.springframework.data.gemfire.util.ArrayUtils;

import java.util.Properties;

/**
 * Created by derrickwong on 29/5/2017.
 */
@EnableGemfireFunctionExecutions(basePackages = "com.example.demogemfireclient")
@Configuration
public class GemfireConfiguration {


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

		factorialsRegion.setCacheListeners(ArrayUtils.asArray(new FactorialCacheListener()));

		return factorialsRegion;

	}

}
