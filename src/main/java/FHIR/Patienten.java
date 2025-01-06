package FHIR;

import Fachlogik.Benutzer;
import Fachlogik.Geschlecht;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.annotation.Validate;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.util.BundleUtil;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r5.model.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Patienten {

    Patient patient= new Patient();
    String fhirId;

    FhirContext ctx = FhirContext.forR5();
    String serverBase = "https://hapi.fhir.org/baseR5";

    IGenericClient client = ctx.newRestfulGenericClient(serverBase);

    public boolean savePatient(Benutzer benutzer){


        try{
            HumanName name= new HumanName();
            name.setFamily(benutzer.getNachname()).addGiven(benutzer.getVorname());
            List<HumanName> nameList= new ArrayList<>();
            nameList.add(name);
            patient.setName(nameList);
            patient.setBirthDate(Date.from(benutzer.getGeburtsdatum().atStartOfDay(ZoneId.systemDefault()).toInstant()));

            patient.setMeta(new Meta().addProfile("http://example.org/StructureDefinition/TelematikPatientGruppe1"));

            if(benutzer.getGeschlecht().equals(Geschlecht.maennlich)){
                patient.setGender(Enumerations.AdministrativeGender.MALE);
            }
            else if(benutzer.getGeschlecht().equals(Geschlecht.weiblich)){
                patient.setGender(Enumerations.AdministrativeGender.FEMALE);
            }
            else{
                patient.setGender(Enumerations.AdministrativeGender.OTHER);
            }

            if(benutzer.getFhirId()!=null){
                String fhir="Patient/"+benutzer.getFhirId();
                patient.setId(fhir);
                MethodOutcome outcome= client.update().resource(patient).execute();
                IdType id = (IdType) outcome.getId();
                System.out.println("Got ID: " + id.getValue());
                fhirId=benutzer.getFhirId();

                List<IBaseResource> observationList= new ArrayList<>();
                Bundle bundle= client.search().forResource(Observation.class).withProfile("http://example.org/StructureDefinition/TelematikPatientBodyWeight"+fhirId).returnBundle(Bundle.class).execute();

                observationList.addAll(BundleUtil.toListOfResources(ctx,bundle));

                Observation observation= (Observation) observationList.get(0);

                observation.setEffective(new DateTimeType(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant())));

                observation.setValue(new Quantity(benutzer.getGewicht()).setUnit("kg"));

                MethodOutcome outcomeObs = client.update()
                        .resource(observation)
                        .execute();

                IdType idObs = (IdType) outcomeObs.getId();
                System.out.println("Got IDOBSaved: " + idObs.getValue());
            }
            else{
                MethodOutcome outcome = client.create()
                        .resource(patient)
                        .prettyPrint()
                        .encodedJson()
                        .execute();

                IIdType id = outcome.getId();
                Patient result=(Patient)outcome.getResource();
                fhirId= result.getIdPart();
                benutzer.setFhirId(fhirId);

                Observation gewicht= new Observation();
                gewicht.setMeta(new Meta().addProfile("http://example.org/StructureDefinition/TelematikPatientBodyWeight"+fhirId));
                gewicht.setStatus(Enumerations.ObservationStatus.FINAL);
                System.out.println(fhirId+"      obs");

                CodeableConcept vital= new CodeableConcept();
                vital.addCoding("http://terminology.hl7.org/CodeSystem/observation-category","vital-signs","Vital Signs");
                gewicht.addCategory(vital);

                CodeableConcept bodyWeight= new CodeableConcept();
                bodyWeight.addCoding("http://loinc.org","29463-7","Body Weight");
                gewicht.setCode(bodyWeight);

                gewicht.setSubject(new Reference("https://hapi.fhir.org/baseR5/Patient/"+fhirId));


                gewicht.setValue(new Quantity(benutzer.getGewicht()).setUnit("kg"));

                gewicht.setEffective(new DateTimeType(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant())));

                MethodOutcome outcomeObs = client.create()
                        .resource(gewicht)
                        .prettyPrint()
                        .encodedJson()
                        .execute();

                IIdType idObs = outcomeObs.getId();
                Observation resultObs=(Observation) outcomeObs.getResource();
                String fhirIdObs= resultObs.getIdPart();
                System.out.println(fhirIdObs+"   obsId");
            }




            return true;
        }
        catch (Exception exception){
            System.out.println(exception);
            return false;
        }

    }

    public List<Benutzer> getPatient(){
        List<IBaseResource> patients= new ArrayList<>();
        Bundle bundle= client.search().forResource(Patient.class).withProfile("http://example.org/StructureDefinition/TelematikPatientGruppe1").returnBundle(Bundle.class).execute();
        System.out.println("fertig");


        patients.addAll(BundleUtil.toListOfResources(ctx,bundle));

        if(bundle.getLink().size()>1){
            while(bundle.getLink().get(1).getRelation().name().equals("NEXT")){
                bundle=client.loadPage().next(bundle).execute();
                patients.addAll(BundleUtil.toListOfResources(ctx,bundle));
            }
        }




        List<Patient> patientList = new ArrayList<>();

        for(IBaseResource patient:patients){
            Patient patient1= (Patient) patient;
            patientList.add(patient1);

        }

        List<Benutzer> benutzerList= new ArrayList<>();


        for(Patient patient:patientList){
            Benutzer benutzerNew= new Benutzer();
            String fhirId= patient.getIdPart();
            benutzerNew.setFhirId(fhirId);

            if(patient.getGender().equals(Enumerations.AdministrativeGender.FEMALE)){
                benutzerNew.setGeschlecht(Geschlecht.weiblich);
            }
            else if(patient.getGender().equals(Enumerations.AdministrativeGender.OTHER)){
                benutzerNew.setGeschlecht(Geschlecht.divers);
            }
            else{
                benutzerNew.setGeschlecht(Geschlecht.maennlich);
            }

            benutzerNew.setVorname(patient.getNameFirstRep().getGiven().get(0).getValue());
            benutzerNew.setNachname(patient.getNameFirstRep().getFamily());

            List<IBaseResource> observationList= new ArrayList<>();
            Bundle bundleObs= client.search().forResource(Observation.class).withProfile("http://example.org/StructureDefinition/TelematikPatientBodyWeight"+benutzerNew.getFhirId()).returnBundle(Bundle.class).execute();

            observationList.addAll(BundleUtil.toListOfResources(ctx,bundleObs));
            //System.out.println(fhirId+ " idddddd "+observationList.size()+" obs size  " +"http://example.org/StructureDefinition/TelematikPatientBodyWeight"+benutzerNew.getFhirId() );

            Observation observation= (Observation) observationList.get(0);
            benutzerNew.setGewicht(observation.getValueQuantity().getValue().doubleValue());

            benutzerNew.setGeburtsdatum(patient.getBirthDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
            benutzerList.add(benutzerNew);



        }


        return benutzerList;

    }



    public boolean deletePatient(Benutzer benutzer){

        List<Boolean> observationDeleted= new ArrayList<>();
        List<Boolean> nutritionIntakeDeleted= new ArrayList<>();

        try{

            List<IBaseResource> observationList= new ArrayList<>();
            Bundle bundleObs= client.search().forResource(Observation.class).withProfile("http://example.org/StructureDefinition/TelematikPatientBodyWeight"+benutzer.getFhirId()).returnBundle(Bundle.class).execute();

            observationList.addAll(BundleUtil.toListOfResources(ctx,bundleObs));
            while(bundleObs.getLink().get(1).getRelation().name().equals("NEXT")){
                bundleObs=client.loadPage().next(bundleObs).execute();
                observationList.addAll(BundleUtil.toListOfResources(ctx,bundleObs));
            }

            for(IBaseResource observation:observationList){
                int i=0;
                Observation observation1= (Observation) observation;

                String obsId= "Observation/"+observation1.getIdPart();

                MethodOutcome responseObs = client
                        .delete()
                        .resourceById(new IdType(obsId))
                        .execute();
                OperationOutcome outcome = (OperationOutcome) responseObs.getOperationOutcome();
                if (outcome != null) {
                    System.out.println(outcome.getIssueFirstRep().getDetails().getCodingFirstRep().getCode());
                    if(outcome.getIssueFirstRep().getDetails().getCodingFirstRep().getCode().contains("SUCCESSFUL_DELETE")){
                        observationDeleted.add(true);
                    }
                }

            }

            Tagebuch tagebuch= new Tagebuch();

            for(NutritionIntake nutritionIntake:tagebuch.getAllNutritionIntake(benutzer)){
                String niId= "NutritionIntake/"+nutritionIntake.getIdPart();

                MethodOutcome responseNI= client
                        .delete()
                        .resourceById(new IdType(niId))
                        .execute();
                OperationOutcome outcome = (OperationOutcome) responseNI.getOperationOutcome();
                if (outcome != null) {
                    System.out.println(outcome.getIssueFirstRep().getDetails().getCodingFirstRep().getCode());
                    if(outcome.getIssueFirstRep().getDetails().getCodingFirstRep().getCode().contains("SUCCESSFUL_DELETE")){
                        nutritionIntakeDeleted.add(true);
                    }
                }

            }

            if(!observationDeleted.contains(false)&&!nutritionIntakeDeleted.contains(false)){
                String id= "Patient/"+benutzer.getFhirId();
                MethodOutcome response = client
                        .delete()
                        .resourceById(new IdType(id))
                        .execute();
                OperationOutcome outcome = (OperationOutcome) response.getOperationOutcome();
                if (outcome != null) {
                    System.out.println(outcome.getIssueFirstRep().getDetails().getCodingFirstRep().getCode());
                    if(outcome.getIssueFirstRep().getDetails().getCodingFirstRep().getCode().contains("SUCCESSFUL_DELETE")){
                        return true;
                    }
                }
            }

        }
        catch (Exception exception){
            exception.printStackTrace();
            return false;
        }
        return false;

    }


}
