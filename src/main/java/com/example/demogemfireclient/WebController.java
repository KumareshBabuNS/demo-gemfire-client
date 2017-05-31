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
	private ClientHealthInfoRepository clientHealthInfoRepository2;


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

	@GetMapping("/randomEvent2")
	public void healthEvent2(){
		ClientHealthInfo clientHealthInfo = new ClientHealthInfo(UUID.randomUUID().toString(), 1000L, 1000L, 100, 100L, 1000L, 100L, 100L, 10L, 10L);
		log.debug("put " + clientHealthInfo.getAccountId());
		clientHealthInfoRepository2.save(clientHealthInfo);
	}

	@GetMapping("/clientHealthInfo/{id}")
	public ClientHealthInfo getOne(@PathVariable("id") String id){
		PdxInstance clientHealthInfo = (PdxInstance)clientHealthInfoRepository.findOne(id);
		if(clientHealthInfo==null) return null;
		return (ClientHealthInfo)clientHealthInfo.getObject();
	}

	@GetMapping("/clientHealthInfo2/{id}")
	public ClientHealthInfo getOne2(@PathVariable("id") String id){
		PdxInstance clientHealthInfo = (PdxInstance)clientHealthInfoRepository2.findOne(id);
		if(clientHealthInfo==null) return null;
		return (ClientHealthInfo)clientHealthInfo.getObject();
	}

	@PostMapping("/clientHealthInfo")
	public void updateClientHealthInfo(@RequestBody ClientHealthInfo clientHealthInfo){
		clientHealthInfoRepository.save(clientHealthInfo);
	}
}
