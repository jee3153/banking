package com.example.banking;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BankingApplication {

	private static final Logger log = LoggerFactory.getLogger(BankingApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(BankingApplication.class, args);
	}

//	@Bean
//	public CommandLineRunner demo(AccountRepository repository) {
//		return (args) -> {
//			// save a few accounts
//			repository.save(new Account(null, BigDecimal.valueOf(199)));
//			repository.save(new Account(null, BigDecimal.valueOf(100)));
//
//			// fetch all accounts
//			log.info("Accounts found with findAll():");
//			log.info("-------------------------------");
//			for (Account account : repository.findAll()) {
//				log.info(account.toString());
//			}
//			log.info("");
//
//			// fetch an individual account by ID
//			Account account = repository.findById(1L).get();
//			log.info("Account found with findById(1L):");
//			log.info("--------------------------------");
//			log.info(account.toString());
//			log.info("");
//		};
//
//	}
}
