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


import java.util.Scanner;

@SpringBootApplication
public class Application {

    private static final Logger log = LoggerFactory.getLogger(Application.class);

    public static void main(String args[]) {
        SpringApplication.run(Application.class);

        RetriveData temp = new RetriveData("");
        while(true){
            Scanner scanner = new Scanner(System.in);
            System.out.println("Enter URL: ");
            String inputUrl = scanner.next();
            if (inputUrl.equalsIgnoreCase("quit")){
                break;
            }else{
                temp.setUrl(inputUrl);
                temp.ConvertResponse();
            }
        }
//		RetriveData temp = new RetriveData("http://localhost:8080/api/patients/1");
//		temp.ConvertResponse();
//
// 		RetriveData temp = new RetriveData("http://localhost:8080/api/procedures/2");
//		temp.ConvertResponse("procedure");

//		RetriveData temp = new RetriveData("http://localhost:8080/api/questionnaires/2");
//		temp.ConvertResponse("Questionnaire");

//        RetriveData temp = new RetriveData("http://localhost:8080/api/Questionnaire-response/1");
//        temp.ConvertResponse("QuestionnaireResponse");
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