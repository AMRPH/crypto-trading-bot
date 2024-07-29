package shdlv.trading.bot;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Date;
import java.util.TimeZone;

@SpringBootApplication
public class TradingBotApplication {

	public static void main(String[] args) {
		SpringApplication.run(TradingBotApplication.class, args);
	}

	@PostConstruct
	public void init(){
		TimeZone.setDefault(TimeZone.getTimeZone("Europe/Moscow"));
		System.out.println("Current date: " + new Date());
	}

}
