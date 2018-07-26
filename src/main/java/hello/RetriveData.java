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

    public void ConvertResponse() {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response;
        try {
            response = restTemplate.getForEntity(url, String.class);
        } catch (Exception e) {
            System.out.println("url error, please type the correct url");
            return;
        }

        String answer;
        String newUrl = url.substring(url.indexOf("api") + 4, url.lastIndexOf("/"));
        Boolean isArray;
        try {
            Long.valueOf(url.substring(url.lastIndexOf("/")+1));
            isArray = false;
        } catch (Exception e) {
            isArray = true;
        }
        if (isArray) {
            switch (newUrl) {
                case "patients":
                    answer = patientConversion.conversionArray(response.getBody());
                    break;
                case "procedures":
                    answer = procedureConversion.conversionArray(response.getBody());
                    break;
                case "questionnaires":
                    answer = questionnaireConversion.conversionArray(response.getBody());
                    break;
                case "followup-actions":
                    answer = questionnaireResponseConversion.conversionArray(response.getBody());
                    break;
                case "Questionnaire-response":
                    answer = questionnaireResponseConversion.conversionSingle(response.getBody());
                    break;
                default:
                    answer = "[]";
                    break;
            }
        } else {
            switch (newUrl) {
                case "patients":
                    answer = patientConversion.conversionSingle(response.getBody());
                    break;
                case "procedures":
                    answer = procedureConversion.conversionSingle(response.getBody());
                    break;
                case "questionnaires":
                    answer = questionnaireConversion.conversionSingle(response.getBody());
                    break;
                case "followup-actions":
                    answer = questionnaireResponseConversion.conversionSingle(response.getBody());
                    break;
                case "Questionnaire-response":
                    answer = questionnaireResponseConversion.conversionSingle(response.getBody());
                    break;
                default:
                    answer = "[]";
                    break;
            }

            System.out.println(answer);
        }
    }

//        JSONObject object = new JSONObject(response.getBody());

//        System.out.println(response.getBody());
//        String answer;
//        switch(type){
//            case "Patient":
//                answer = patientConversion.conversionSingle(response.getBody());
//                break;
//            case "Procedure":
//                answer = procedureConversion.conversionSingle(response.getBody());
//                break;
//            case "Questionnaire":
//                answer = questionnaireConversion.conversionSingle(response.getBody());
//                break;
//            case "QuestionnaireResponse":
//                answer = questionnaireResponseConversion.conversionSingle(response.getBody());
//                break;
//            default:
//
//                break;
//        }

//        System.out.println(response.getBody());


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
