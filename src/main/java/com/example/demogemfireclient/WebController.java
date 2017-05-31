package com.example.demogemfireclient;

import com.gemstone.gemfire.cache.client.Pool;
import com.gemstone.gemfire.pdx.PdxInstance;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.gemfire.function.execution.GemfireOnServersFunctionTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Created by derrickwong on 29/5/2017.
 */
@RestController
@Slf4j
public class WebController{


	@Autowired
	private ClientHealthInfoRepository clientHealthInfoRepository;

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
		log.debug("put " + clientHealthInfo.getAccountId());
		clientHealthInfoRepository.save(clientHealthInfo);
	}

	@GetMapping("/clientHealthInfo/{id}")
	public ClientHealthInfo getOne(@PathVariable("id") String id){
		return (ClientHealthInfo) ((PdxInstance)clientHealthInfoRepository.findOne(id)).getObject();
	}

	@PostMapping("/clientHealthInfo")
	public void updateClientHealthInfo(@RequestBody ClientHealthInfo clientHealthInfo){
		clientHealthInfoRepository.save(clientHealthInfo);
	}
}
