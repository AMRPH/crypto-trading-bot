package shdlv.trading.bot;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Date;
import java.util.TimeZone;

@SpringBootApplication
public class SimulatedTradingApplication {

	public static void main(String[] args) {
		SpringApplication.run(SimulatedTradingApplication.class, args);
	}

	@PostConstruct
	public void init(){
		TimeZone.setDefault(TimeZone.getTimeZone("Europe/Moscow"));
		System.out.println("Current date: " + new Date());
	}

}
