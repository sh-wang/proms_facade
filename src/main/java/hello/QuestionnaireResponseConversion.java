package hello;

import org.hl7.fhir.dstu3.model.Enumerations;
import org.json.JSONArray;
import org.json.JSONObject;
import org.hl7.fhir.dstu3.model.QuestionnaireResponse;
import org.hl7.fhir.dstu3.model.Patient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class QuestionnaireResponseConversion {
    public QuestionnaireResponseConversion(){}

    public enum ActionStatus {
        UNINITIALISED, STARTED, PENDING, COMPLETED,  UNKNOWN
    }

    public String conversionSingle(String rawData){
        JSONObject jsonObject = new JSONObject(rawData);

        QuestionnaireResponse questionnaireResponse = questionnaireResponseConversion(jsonObject);

        return questionnaireResponse.toString();
    }

    public String conversionArray(String rawData) {
        List<QuestionnaireResponse> questionnaireResponseArray = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(rawData);
        JSONObject jsonObject;

        for(int i = 0; i < jsonArray.length(); i++){
            jsonObject = jsonArray.getJSONObject(i);
            QuestionnaireResponse questionnaireResponse = questionnaireResponseConversion(jsonObject);
            questionnaireResponseArray.add(questionnaireResponse);
        }

        return questionnaireResponseArray.toString();
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

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        Patient patient = patientFhirResource.getPatientResource(
                followupAction.getPatient().getId());
        org.hl7.fhir.dstu3.model.Reference refePa = new org.hl7.fhir.dstu3.model.Reference(patientFHIR);
        questionnaireResponse.setSubject(refePa);
//
//        Procedure procedureFHIR = procedureFhirResource.getProcedureResource(followupAction.getCareEvent()
//                .getFollowupPlan().getProcedureBooking().getId());
//        org.hl7.fhir.dstu3.model.Reference refePr = new org.hl7.fhir.dstu3.model.Reference(procedureFHIR);
//        questionnaireResponse.addParent(refePr);
//
//        //add completed date
//        try{
//            questionnaireResponse.setAuthored(Date.from(followupAction.getCompletedDate().
//                    atStartOfDay(ZoneId.systemDefault()).toInstant()));
//        }catch (Exception e){ }
//
//        // add patient's questionnaire need to accomplish, in the format of fhir standard, json format.
//        org.hl7.fhir.dstu3.model.Questionnaire questionnaireFHIR = questionnaireFhirResource
//                .getQuestionnaireResource(followupAction.getQuestionnaire().getId());
//        org.hl7.fhir.dstu3.model.Reference refeQu = new org.hl7.fhir.dstu3.model.Reference(questionnaireFHIR);
//        questionnaireResponse.setQuestionnaire(refeQu);
//
//        // display each question and its corresponding answer for the questionnaire.
//        if(!followupAction.getResponseItems().isEmpty()){
//            Set<ResponseItem> responseItems = followupAction.getResponseItems();
//
//            Iterator it1 = responseItems.iterator();
//            while(it1.hasNext()){
//                ResponseItem responseItem = (ResponseItem) it1.next();
//                org.hl7.fhir.dstu3.model.IntegerType i = new org.hl7.fhir.dstu3.model.IntegerType();
////                org.hl7.fhir.dstu3.model.StringType s = new org.hl7.fhir.dstu3.model.StringType();
////                s.setValue(followupAction.getOutcomeComment());
//                i.setValue(responseItem.getValue());
//                questionnaireResponse.addItem().setLinkId(responseItem.getId().
//                        toString()).setText(responseItem.getLocalId()).addAnswer().setValue(i);
//            }
//            // outcome comment
//            org.hl7.fhir.dstu3.model.StringType s = new org.hl7.fhir.dstu3.model.StringType();
//            s.setValue(followupAction.getOutcomeComment());
//            questionnaireResponse.addItem().addAnswer().setValue(s);
//        }
//
//        String author = followupAction.getCreatedBy();
//        Reference authorRef = new Reference(author);
//        questionnaireResponse.setAuthor(authorRef);

        return  questionnaireResponse;
    }
}
