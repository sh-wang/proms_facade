package com.promsFacade.conversion;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import org.hl7.fhir.dstu3.model.Enumerations;
import org.json.JSONArray;
import org.json.JSONObject;
import org.hl7.fhir.dstu3.model.Questionnaire;


public class QuestionnaireConversion {
    public  QuestionnaireConversion(){}

    private FhirContext ctx = FhirContext.forDstu3();
    private IParser p =ctx.newJsonParser().setPrettyPrint(true);

    private String defaultPath = "http://localhost:8080/api/";


    public String conversionSingle(String rawData){
        JSONObject jsonObject = new JSONObject(rawData);

        Questionnaire questionnaire = questionnaireConversion(jsonObject);
        String encode = p.encodeResourceToString(questionnaire);

        return encode;
    }
    public String conversionArray(String rawData) {
        JSONArray jsonArray = new JSONArray(rawData);
        JSONArray FHIRarray = new JSONArray();

        for(int i = 0; i < jsonArray.length(); i++){
            FHIRarray.put(new JSONObject(p.encodeResourceToString
                    (questionnaireConversion(jsonArray.getJSONObject(i)))));
        }

        return FHIRarray.toString();
    }

    public Questionnaire questionnaireConversion(JSONObject jsonObject){
        Questionnaire questionnaire = new Questionnaire();

        //add url
        questionnaire.setUrl(defaultPath + "questionnaires/"+jsonObject.get("id"));

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
