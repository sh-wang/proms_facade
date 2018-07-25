package hello;

import org.hl7.fhir.dstu3.model.Enumerations;
import org.hl7.fhir.dstu3.model.Patient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class PatientConversion {

    public PatientConversion(){

    }

    public String conversionSingle(JSONObject jsonObject){
        Patient patient = new Patient();
        patient.setId(jsonObject.get("id").toString().replaceAll(".0+?$", ""));

        // add name
        patient.addName().setFamily(jsonObject.get("familyName").toString()).addGiven(jsonObject.get("givenName").toString());

        // add dob
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//        patient.setBirthDate(format.parse(jsonObject.get("birthDate").toString()));

        //add gender
        if (jsonObject.get("gender").equals("MALE")){
            patient.setGender(Enumerations.AdministrativeGender.MALE);
        }else if(jsonObject.get("gender").equals("FEMALE")){
            patient.setGender(Enumerations.AdministrativeGender.FEMALE);
        }else if(jsonObject.get("gender").equals("OTHER")){
            patient.setGender(Enumerations.AdministrativeGender.OTHER);
        }else if(jsonObject.get("gender").equals("UNKNOWN")){
            patient.setGender(Enumerations.AdministrativeGender.UNKNOWN);
        }

        // add NHS number
        if (jsonObject.get("nhsNumber") == null){
            patient.addIdentifier().setSystem("nhsNumber").setValue("0000000000");
        }else{
            patient.addIdentifier().setSystem("nhsNumber").setValue(jsonObject.get("nhsNumber").toString());
        }

        return patient.toString();
    }
}
