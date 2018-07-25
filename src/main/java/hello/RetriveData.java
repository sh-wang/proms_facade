package hello;

import org.json.JSONArray;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class RetriveData {
    private String url;

    public RetriveData(String url){
        this.url=url;
    }

    public void printResponse(){
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        JSONArray array = new JSONArray(response.getBody());
//            array.getJSONObject(1).get()
        System.out.println(array.toString());
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
