package hello;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URL;

public class RetriveData {
    private String url;

    private PatientConversion patientConversion = new PatientConversion();
    private ProcedureConversion procedureConversion = new ProcedureConversion();
    private QuestionnaireConversion questionnaireConversion = new QuestionnaireConversion();
    private QuestionnaireResponseConversion questionnaireResponseConversion = new QuestionnaireResponseConversion();

    public RetriveData(String url){
        this.url=url;
    }

    public void ConvertResponse(String type){
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
//        JSONObject object = new JSONObject(response.getBody());

//        System.out.println(response.getBody());
        String answer;
        switch(type){
            case "Patient":
                answer = patientConversion.conversionSingle(response.getBody());
                break;
            case "Procedure":
                answer = procedureConversion.conversionSingle(response.getBody());
                break;
            case "Questionnaire":
                answer = questionnaireConversion.conversionSingle(response.getBody());
                break;
            case "QuestionnaireResponse":
                answer = questionnaireResponseConversion.conversionSingle(response.getBody());
                break;
            default:

                break;
        }

        System.out.println(response.getBody());
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
