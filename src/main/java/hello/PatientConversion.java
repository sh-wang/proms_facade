package hello;

import com.google.gson.JsonObject;
import org.hl7.fhir.dstu3.model.Enumerations;
import org.hl7.fhir.dstu3.model.Patient;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class PatientConversion {

    public PatientConversion(){ }

    public String conversionSingle(String rawData){
        JSONObject jsonObject = new JSONObject(rawData);

        Patient patient = patientConversion(jsonObject);

        return patient.toString();
    }

    public String conversionArray(String rawData) {
        List<Patient> patientArray = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(rawData);
        JSONObject jsonObject;

        for(int i = 0; i < jsonArray.length(); i++){
            jsonObject = jsonArray.getJSONObject(i);
            Patient patient = patientConversion(jsonObject);
            patientArray.add(patient);
        }

        return patientArray.toString();
    }

    private Patient patientConversion(JSONObject jsonObject){
        Patient patient = new Patient();
        patient.setId(jsonObject.get("id").toString().replaceAll(".0+?$", ""));

        // add name
        patient.addName().setFamily(jsonObject.get("familyName").toString()).addGiven(jsonObject.get("givenName").toString());

        // add dob
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//        patient.setBirthDate(format.parse(jsonObject.get("birthDate").toString()));

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

        return  patient;
    }
}
