package com.example.demogemfireclient;

import com.gemstone.gemfire.cache.Region;
import com.gemstone.gemfire.cache.client.Pool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.gemfire.function.execution.GemfireOnServersFunctionTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.UUID;

/**
 * Created by derrickwong on 29/5/2017.
 */
@RestController
@Slf4j
public class WebController{

	@Resource(name = "ClientHealth")
	private Region<String, ClientHealthInfo> clientHealth;


	@Autowired
	private Pool gemfirePool;

	@GetMapping("/func")
	public Iterable<String> func(){
		GemfireOnServersFunctionTemplate gemfireOnServersFunctionTemplate = new GemfireOnServersFunctionTemplate(gemfirePool);
		Iterable<String> result = gemfireOnServersFunctionTemplate.execute("function1");
		return result;
	}

	@GetMapping("/randomEvent")
	public void healthEvent(){

		ClientHealthInfo clientHealthInfo = new ClientHealthInfo(UUID.randomUUID().toString(), 1000L, 1000L, 100, 100L, 1000L, 100L, 100L, 10L, 10L);
		log.info("put " + clientHealthInfo.getAccountId());
		clientHealth.put(clientHealthInfo.getAccountId(), clientHealthInfo);

	}

	@GetMapping("/clientHealthInfo/{id}")
	public ClientHealthInfo getOne(@PathVariable("id") String id){

		return clientHealth.get(id);

	}

	@PostMapping("/clientHealthInfo")
	public void updateClientHealthInfo(@RequestBody ClientHealthInfo clientHealthInfo){

		clientHealth.put(clientHealthInfo.getAccountId(), clientHealthInfo);

	}
}
