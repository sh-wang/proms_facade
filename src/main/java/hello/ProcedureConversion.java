package hello;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import org.hl7.fhir.dstu3.model.CodeableConcept;
import org.hl7.fhir.dstu3.model.Enumerations;
import org.json.JSONArray;
import org.json.JSONObject;
import org.hl7.fhir.dstu3.model.Procedure;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class ProcedureConversion {
    public ProcedureConversion(){ }

    private FhirContext ctx = FhirContext.forDstu3();
    private IParser p =ctx.newJsonParser().setPrettyPrint(true);

    public String conversionSingle(String rawData){
        JSONObject jsonObject = new JSONObject(rawData);

        Procedure procedure = procedureConversion(jsonObject);
        String encode = p.encodeResourceToString(procedure);

        return encode;
    }

    public String conversionArray(String rawData) {
        List<Procedure> proceduresArray = new ArrayList<>();
        JSONArray jsonArray = new JSONArray(rawData);
        JSONObject jsonObject;

        for(int i = 0; i < jsonArray.length(); i++){
            jsonObject = jsonArray.getJSONObject(i);
            Procedure procedure = procedureConversion(jsonObject);
            proceduresArray.add(procedure);
        }

        return proceduresArray.toString();
    }

    public Procedure procedureConversion(JSONObject jsonObject){
        Procedure procedure = new Procedure();
        //add id
        procedure.setId(jsonObject.get("id").toString());

        //currently no status data
        procedure.setStatus(org.hl7.fhir.dstu3.model.Procedure.ProcedureStatus.UNKNOWN);

        CodeableConcept codeableConcept = new CodeableConcept();

        //add localcode as code and add name
        codeableConcept.addCoding().setCode(jsonObject.get("localCode").toString()).
                setDisplay(jsonObject.get("name").toString().substring(1, jsonObject.get("name").toString().length()));
        procedure.setCode(codeableConcept);

        return  procedure;
    }
}
