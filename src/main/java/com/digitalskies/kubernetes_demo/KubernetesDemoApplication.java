package com.digitalskies.kubernetes_demo;

import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.slf4j.Logger;

@SpringBootApplication
public class KubernetesDemoApplication {


	public static void main(String[] args) {
		SpringApplication.run(KubernetesDemoApplication.class, args);

        Logger logger= LoggerFactory.getLogger(KubernetesDemoApplication.class);
        logger.info("Application started successfully");
	}

}
