package hello;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import com.google.gson.JsonObject;
import org.hl7.fhir.dstu3.model.ContactPoint;
import org.hl7.fhir.dstu3.model.Enumerations;
import org.hl7.fhir.dstu3.model.Patient;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class PatientConversion {

    public PatientConversion(){ }

    private FhirContext ctx = FhirContext.forDstu3();
    private IParser p =ctx.newJsonParser().setPrettyPrint(true);

    private String defaultPath = "http://localhost:8080/api/";

    public String conversionSingle(String rawData){
        JSONObject jsonObject = new JSONObject(rawData);

        Patient patient = patientConversion(jsonObject);
        String encode = p.encodeResourceToString(patient);

        return encode;
    }

    public String conversionArray(String rawData) {
        JSONArray jsonArray = new JSONArray(rawData);
        JSONArray FHIRarray = new JSONArray();

        for(int i = 0; i < jsonArray.length(); i++){
            FHIRarray.put(new JSONObject(p.encodeResourceToString
                    (patientConversion(jsonArray.getJSONObject(i)))));
        }

        return FHIRarray.toString();
    }

    public Patient patientConversion(JSONObject jsonObject){
        Patient patient = new Patient();
        patient.setId(jsonObject.get("id").toString().replaceAll(".0+?$", ""));

        // add name
        patient.addName().setFamily(jsonObject.get("familyName").toString()).addGiven(jsonObject.get("givenName").toString());

        // add dob
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            patient.setBirthDate(format.parse(jsonObject.get("birthDate").toString()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //add gender
        if (jsonObject.get("gender").equals("MALE")) {
            patient.setGender(Enumerations.AdministrativeGender.MALE);
        } else if (jsonObject.get("gender").equals("FEMALE")) {
            patient.setGender(Enumerations.AdministrativeGender.FEMALE);
        } else if (jsonObject.get("gender").equals("OTHER")) {
            patient.setGender(Enumerations.AdministrativeGender.OTHER);
        } else if (jsonObject.get("gender").equals("UNKNOWN")) {
            patient.setGender(Enumerations.AdministrativeGender.UNKNOWN);
        }

        // add NHS number
        if (jsonObject.get("nhsNumber") == null) {
            patient.addIdentifier().setSystem("nhsNumber").setValue("0000000000");
        } else {
            patient.addIdentifier().setSystem("nhsNumber").setValue(jsonObject.get("nhsNumber").toString());
        }

        // add Email
        if (jsonObject.get("email") == null) {
            patient.addTelecom().setSystem(ContactPoint.ContactPointSystem.EMAIL).setValue("null");
        }else{
            patient.addTelecom().setSystem(ContactPoint.ContactPointSystem.EMAIL).setValue(jsonObject.get("email").toString());
        }

        // add address
        RestTemplate restTemplate = new RestTemplate();
        String addressUrl = defaultPath + "addresses/" + jsonObject.get("id").toString();
        ResponseEntity<String> response;
        try {
            response = restTemplate.getForEntity(addressUrl, String.class);
        } catch (Exception e) {
            return  patient;
        }

        JSONObject addressJson = new JSONObject(response.getBody());

        if (addressJson!=null) {
            org.hl7.fhir.dstu3.model.Address addressFHIR = new org.hl7.fhir.dstu3.model.Address();
            addressFHIR.setPostalCode(addressJson.get("postalCode").toString());
            addressFHIR.setCity(addressJson.get("city").toString());
            addressFHIR.setCountry(addressJson.get("country").toString());

            for (int i = 0; i < addressJson.getJSONArray("lines").length(); i++){
                addressFHIR.addLine(addressJson.getJSONArray("lines").get(i).toString());
            }

            patient.addAddress(addressFHIR);
        }

        return  patient;
    }
}
