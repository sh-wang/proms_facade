package hello;

import org.json.JSONArray;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URL;
import java.util.Scanner;

@SpringBootApplication
public class Application {

	private static final Logger log = LoggerFactory.getLogger(Application.class);

	public static void main(String args[]) {
		SpringApplication.run(Application.class);

		Scanner scanner = new Scanner(System.in);

		while(true){
			System.out.println("Enter URL: ");
			String url = scanner.next();
			if (url.equalsIgnoreCase("quit")){
				break;
			}
			else{
				RetriveData temp =new RetriveData(url);
				temp.ConvertResponse();
			}
		}
//		RetriveData temp = new RetriveData("http://localhost:8080/api/patients/1");
//		temp.ConvertResponse();
	}
	
	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder builder) {
		return builder.build();
	}

//	@Bean
//	public CommandLineRunner run(RestTemplate restTemplate) throws Exception {
//		return args -> {
////			Quote quote = restTemplate.getForObject(
////					"http://gturnquist-quoters.cfapps.io/api/random", Quote.class);
////			log.info(quote.toString());
//			String fooResourceUrl
//					= "http://localhost:8080/api/patients";
//			ResponseEntity<String> response
//					= restTemplate.getForEntity(fooResourceUrl + "/1", String.class);
//			System.out.println(response);
//		};
//	}
}