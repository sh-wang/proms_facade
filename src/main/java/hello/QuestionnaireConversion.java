package hello;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import org.hl7.fhir.dstu3.model.Enumerations;
import org.json.JSONArray;
import org.json.JSONObject;
import org.hl7.fhir.dstu3.model.Questionnaire;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class QuestionnaireConversion {
    public  QuestionnaireConversion(){}

    private FhirContext ctx = FhirContext.forDstu3();
    private IParser p =ctx.newJsonParser().setPrettyPrint(true);


    public String conversionSingle(String rawData){
        JSONObject jsonObject = new JSONObject(rawData);

        Questionnaire questionnaire = questionnaireConversion(jsonObject);
        String encode = p.encodeResourceToString(questionnaire);

        return encode;
    }
    public String conversionArray(String rawData) {
        List<Questionnaire> questionnaireArray = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(rawData);
        JSONObject jsonObject;

        for(int i = 0; i < jsonArray.length(); i++){
            jsonObject = jsonArray.getJSONObject(i);
            Questionnaire questionnaire = questionnaireConversion(jsonObject);
            questionnaireArray.add(questionnaire);
        }

        return questionnaireArray.toString();
    }

    public Questionnaire questionnaireConversion(JSONObject jsonObject){
        Questionnaire questionnaire = new Questionnaire();

        //add url
        questionnaire.setUrl("localhost:8080/api/fhir/questionnaires/"+jsonObject.get("id"));

        //add id
        questionnaire.setId(jsonObject.get("id").toString());

        //add Status
        questionnaire.setStatus(Enumerations.PublicationStatus.ACTIVE);

        //add name
        questionnaire.setName(jsonObject.get("name").toString());

        //add Copyright
        questionnaire.setCopyright(jsonObject.get("copyright").toString());
        return  questionnaire;
    }
}
