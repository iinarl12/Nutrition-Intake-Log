package FHIR;

import Fachlogik.Benutzer;
import Fachlogik.Einheit;
import Fachlogik.Geschlecht;
import Fachlogik.Tagsuebersicht;
import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.api.MethodOutcome;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import javafx.util.converter.LocalTimeStringConverter;
import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r5.context.SystemOutLoggingService;
import org.hl7.fhir.r5.model.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Tagebuch {

    NutritionIntake nutritionIntake = new NutritionIntake();

    FhirContext ctx = FhirContext.forR5();
    String serverBase = "https://hapi.fhir.org/baseR5";

    IGenericClient client = ctx.newRestfulGenericClient(serverBase);

    public void saveTagebuch(Benutzer benutzer, Tagsuebersicht tagsuebersicht){


        try{
            nutritionIntake.setSubject(new Reference("https://hapi.fhir.org/baseR5/Patient/"+benutzer.getFhirId()));
            NutritionIntake.NutritionIntakeConsumedItemComponent  consumedItem= new NutritionIntake.NutritionIntakeConsumedItemComponent();
            CodeableReference codeableReference= new CodeableReference();
            if(tagsuebersicht.getNutritionFHIRId()!=null){
                codeableReference.setReference(new Reference("https://hapi.fhir.org/baseR5/NutritionProduct/"+tagsuebersicht.getNutritionFHIRId()));

            }
            else{
                codeableReference.setConcept(new CodeableConcept().setText(tagsuebersicht.getNahrungsmittel()));
            }
            consumedItem.setNutritionProduct(codeableReference);
            Quantity menge= new Quantity();
            menge.setUnit(tagsuebersicht.getEinheit().name());
            menge.setValue(tagsuebersicht.getMenge());
            consumedItem.setAmount(menge);
            BaseDateTimeType dateTime= new DateTimeType();
            dateTime.setValue(Date.from(tagsuebersicht.getDatum().atStartOfDay(ZoneId.systemDefault()).toInstant()));
            dateTime.setHour(tagsuebersicht.getZeit().getHour());
            dateTime.setMinute(tagsuebersicht.getZeit().getMinute());
            nutritionIntake.addConsumedItem(consumedItem);
            nutritionIntake.setOccurrence(dateTime);
            nutritionIntake.addNote(new Annotation().setText(tagsuebersicht.getBeschwerde()));
            System.out.println("tes");

            MethodOutcome outcome= client.create().resource(nutritionIntake).prettyPrint().encodedJson().execute();
            System.out.println("tes nach method");
            IIdType id = outcome.getId();
            System.out.println("Got ID: " + id.getValue());
        }
        catch (Exception e){

        }

    }

    public List<Tagsuebersicht> getTagebuch(Benutzer benutzer, LocalDate datum){
        BaseDateTimeType dateTime= new DateTimeType();
        dateTime.setValue(Date.from(datum.atStartOfDay(ZoneId.systemDefault()).toInstant()));
        Bundle response = client.search().forResource(NutritionIntake.class).where(NutritionIntake.SUBJECT.hasId(benutzer.getFhirId())).returnBundle(Bundle.class).execute();

        List<NutritionIntake> nutritionIntakes= new ArrayList<>();
        List<Tagsuebersicht> tagsuebersichtList= new ArrayList<>();

        List<Bundle.BundleEntryComponent> entries=response.getEntry();
        for(Bundle.BundleEntryComponent entry:entries ){
            NutritionIntake nutritionIntake=client.read().resource(NutritionIntake.class).withId(entry.getResource().getId()).execute();
            nutritionIntakes.add(nutritionIntake);
        }
        for (NutritionIntake  nutritionIntake:nutritionIntakes){
            if(nutritionIntake.getOccurrence().dateTimeValue().getValueAsCalendar().toZonedDateTime().toLocalDate().equals(datum)){
                System.out.println("nintake "+ nutritionIntake.getId());
                Tagsuebersicht tagsuebersicht= new Tagsuebersicht();
                tagsuebersicht.setDatum(datum);

                DateTimeFormatter format= DateTimeFormatter.ofPattern("H:m");

                LocalTime zeit=LocalTime.parse(nutritionIntake.getOccurrence().dateTimeValue().getHour() +":"+ nutritionIntake.getOccurrence().dateTimeValue().getMinute().toString(), format);

                tagsuebersicht.setZeit(zeit);

                tagsuebersicht.setBenutzer(benutzer);

                tagsuebersicht.setBeschwerde(nutritionIntake.getNoteFirstRep().getText());

                tagsuebersicht.setMenge(nutritionIntake.getConsumedItemFirstRep().getAmount().getValue().intValue());

                if(nutritionIntake.getConsumedItemFirstRep().getAmount().getUnit().equals("gramm")){
                    tagsuebersicht.setEinheit(Einheit.gramm);
                }
                else if(nutritionIntake.getConsumedItemFirstRep().getAmount().getUnit().equals("milliliter")){
                    tagsuebersicht.setEinheit(Einheit.milliliter);
                }
                else{
                    tagsuebersicht.setEinheit(Einheit.stueck);
                }

                if(nutritionIntake.getConsumedItemFirstRep().getNutritionProduct().getReference().getReference()!=null){

                    String[] fhirIdAll= nutritionIntake.getConsumedItemFirstRep().getNutritionProduct().getReference().getReference().split("/");
                    String npId= fhirIdAll[1];
                    tagsuebersicht.setNutritionFHIRId(npId);
                    System.out.println(npId+" np id");
                    NutritionProduct nutritionProduct= client.read().resource(NutritionProduct.class).withId(tagsuebersicht.getNutritionFHIRId()).execute();
                    tagsuebersicht.setNahrungsmittel(nutritionProduct.getCode().getText());

                }
                else{
                    tagsuebersicht.setNahrungsmittel(nutritionIntake.getConsumedItemFirstRep().getNutritionProduct().getConcept().getText());
                }

                tagsuebersicht.setFhirID(nutritionIntake.getIdPart());

                tagsuebersichtList.add(tagsuebersicht);

            }

        }
        return tagsuebersichtList;

    }

    public List<NutritionIntake> getAllNutritionIntake(Benutzer benutzer){
        Bundle response = client.search().forResource(NutritionIntake.class).where(NutritionIntake.SUBJECT.hasId(benutzer.getFhirId())).returnBundle(Bundle.class).execute();

        List<NutritionIntake> nutritionIntakes= new ArrayList<>();

        List<Bundle.BundleEntryComponent> entries=response.getEntry();
        for(Bundle.BundleEntryComponent entry:entries ){
            NutritionIntake nutritionIntake=client.read().resource(NutritionIntake.class).withId(entry.getResource().getId()).execute();
            nutritionIntakes.add(nutritionIntake);

        }
        return nutritionIntakes;
    }

    public boolean delete(Tagsuebersicht tagsuebersicht){
            try{
            String id= "NutritionIntake/"+tagsuebersicht.getFhirID();
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
        }catch (Exception exception){
        return false;
     }
        return false;

    }


}
