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

    public void ConvertResponse(){
        urlClassifier();
        RestTemplate restTemplate = new RestTemplate();

    }

    private String urlClassifier(){
        String newUrl = url.substring(url.indexOf("api")+4, url.lastIndexOf("/"));
        System.out.println(newUrl);
        if (url.contains("patient")){

        }
        return  "  ";
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
