package com.promsFacade.conversion;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import com.promsFacade.conversion.PatientConversion;
import com.promsFacade.conversion.ProcedureConversion;
import com.promsFacade.conversion.QuestionnaireConversion;
import com.promsFacade.util.ActionStatus;
import org.hl7.fhir.dstu3.model.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.util.Date;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class QuestionnaireResponseConversion {
    private PatientConversion patientConversion = new PatientConversion();
    private ProcedureConversion procedureConversion = new ProcedureConversion();
    private QuestionnaireConversion questionnaireConversion= new QuestionnaireConversion();
    public QuestionnaireResponseConversion(){}

    private FhirContext ctx = FhirContext.forDstu3();
    private IParser p =ctx.newJsonParser().setPrettyPrint(true);

    private String defaultPath = "http://localhost:8080/api/";

    public String conversionSingle(String rawData){
        JSONObject jsonObject = new JSONObject(rawData);

        QuestionnaireResponse questionnaireResponse = questionnaireResponseConversion(jsonObject);
        String encode = p.encodeResourceToString(questionnaireResponse);

        return encode;
    }

    public String conversionArray(String rawData) {
        JSONArray jsonArray = new JSONArray(rawData);
        JSONArray FHIRarray = new JSONArray();

        for(int i = 0; i < jsonArray.length(); i++){
            FHIRarray.put(new JSONObject(p.encodeResourceToString
                    (questionnaireResponseConversion(jsonArray.getJSONObject(i)))));
        }

        return FHIRarray.toString();
    }

    private QuestionnaireResponse questionnaireResponseConversion(JSONObject jsonObject){
        QuestionnaireResponse questionnaireResponse = new QuestionnaireResponse();

        //add id
        questionnaireResponse.setId(jsonObject.get("id").toString());

        //add status
        if (jsonObject.get("status").equals(ActionStatus.STARTED)){
            questionnaireResponse.setStatus(QuestionnaireResponse.QuestionnaireResponseStatus.INPROGRESS);
        }
        if (jsonObject.get("status").equals(ActionStatus.UNINITIALISED)){
            questionnaireResponse.setStatus(QuestionnaireResponse.QuestionnaireResponseStatus.NULL);
        }
        if (jsonObject.get("status").equals(ActionStatus.COMPLETED)){
            questionnaireResponse.setStatus(QuestionnaireResponse.QuestionnaireResponseStatus.COMPLETED);
        }
        if (jsonObject.get("status").equals(ActionStatus.UNKNOWN)){
            questionnaireResponse.setStatus(QuestionnaireResponse.QuestionnaireResponseStatus.NULL);
        }
        if (jsonObject.get("status").equals(ActionStatus.PENDING)){
            questionnaireResponse.setStatus(QuestionnaireResponse.QuestionnaireResponseStatus.INPROGRESS);
        }

        //add patient
        JSONObject jsonPatient = new JSONObject(jsonObject.get("patient").toString());
        Patient patient = patientConversion.patientConversion(jsonPatient,"Questionnaire response");

        org.hl7.fhir.dstu3.model.Reference refePa = new org.hl7.fhir.dstu3.model.Reference(patient);
        questionnaireResponse.setSubject(refePa);

        //add procedure
        JSONObject jsonCareEvent = new JSONObject(jsonObject.get("careEvent").toString());
        JSONObject jsonFollowupPlan = new JSONObject(jsonCareEvent.get("followupPlan").toString());
        JSONObject jsonProcedureBooking = new JSONObject(jsonFollowupPlan.get("procedureBooking").toString());
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(defaultPath + "procedures/"
                +jsonProcedureBooking.get("id"), String.class);
        JSONObject jsonProcedure = new JSONObject(response.getBody());
        Procedure procedure = procedureConversion.procedureConversion(jsonProcedure);
        org.hl7.fhir.dstu3.model.Reference refePr = new org.hl7.fhir.dstu3.model.Reference(procedure);
        questionnaireResponse.addParent(refePr);

        //add completed date
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        if(jsonObject.get("completedDate").toString().equals("null")){
            try {
                date = format.parse("2020-01-01");
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }else{
            try {
                date = format.parse(jsonObject.get("completedDate").toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        questionnaireResponse.setAuthored(date);



        // add patient's questionnaire need to accomplish, in the format of fhir standard, json format.
        JSONObject jsonQuestionnaire = new JSONObject(jsonObject.get("questionnaire").toString());

        Questionnaire questionnaire = questionnaireConversion.questionnaireConversion(jsonQuestionnaire);

        org.hl7.fhir.dstu3.model.Reference refeQu = new org.hl7.fhir.dstu3.model.Reference(questionnaire);
        questionnaireResponse.setQuestionnaire(refeQu);

        // display each question and its corresponding answer for the questionnaire.
        if(!jsonObject.get("responseItems").toString().equals("null")){
            JSONArray jsonResponseItems = new JSONArray(jsonObject.get("responseItems").toString());
           for(int i=0; i<jsonResponseItems.length(); i++){
               org.hl7.fhir.dstu3.model.IntegerType j = new org.hl7.fhir.dstu3.model.IntegerType();
               j.setValue((Integer) jsonResponseItems.getJSONObject(i).get("value"));
               questionnaireResponse.addItem().setLinkId(jsonResponseItems.getJSONObject(i).get("id").
                       toString()).setText(jsonResponseItems.getJSONObject(i).get("localId").
                       toString()).addAnswer().setValue(j);
           }
        }

            // outcome comment
            org.hl7.fhir.dstu3.model.StringType s = new org.hl7.fhir.dstu3.model.StringType();
            s.setValue(jsonObject.get("outcomeComment").toString());
            questionnaireResponse.addItem().addAnswer().setValue(s);


            String author = jsonObject.get("createdBy").toString();
            org.hl7.fhir.dstu3.model.Reference authorRef = new org.hl7.fhir.dstu3.model.Reference(author);
            questionnaireResponse.setAuthor(authorRef);

        return  questionnaireResponse;
    }
}
