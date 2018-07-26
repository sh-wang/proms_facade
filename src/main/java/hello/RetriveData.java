package hello;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.URL;

public class RetriveData {
    private String url;

    private PatientConversion patientConversion = new PatientConversion();

    public RetriveData(String url){
        this.url=url;
    }

    public void ConvertResponse(){
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
//        JSONObject object = new JSONObject(response.getBody());

//        System.out.println(response.getBody());
        String answer = patientConversion.conversionSingle(response.getBody());

        System.out.println(answer);
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
