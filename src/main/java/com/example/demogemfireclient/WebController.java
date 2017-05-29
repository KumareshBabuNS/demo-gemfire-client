package com.example.demogemfireclient;

import com.gemstone.gemfire.cache.Region;
import com.gemstone.gemfire.cache.client.Pool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.gemfire.function.execution.GemfireOnServersFunctionTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Created by derrickwong on 29/5/2017.
 */
@RestController
@Slf4j
public class WebController{


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

	@Autowired
	private Pool gemfirePool;
	private GemfireOnServersFunctionTemplate gemfireOnServersFunctionTemplate = new GemfireOnServersFunctionTemplate(gemfirePool);

	@GetMapping("/func")
	public Iterable<String> func(){
		Iterable<String> result = gemfireOnServersFunctionTemplate.execute("function1");
		return result;
	}
}
