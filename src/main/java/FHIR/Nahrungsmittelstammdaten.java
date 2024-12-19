package FHIR;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.gclient.TokenClientParam;
import ca.uhn.fhir.util.BundleUtil;
import org.hl7.fhir.instance.model.api.IBaseBundle;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r5.model.Bundle;
import org.hl7.fhir.r5.model.NutritionProduct;


public class Nahrungsmittelstammdaten {

    FhirContext ctx= FhirContext.forR5();
    String url="https://hapi.fhir.org/baseR5";

    IGenericClient client=ctx.newRestfulGenericClient(url);



    public Nahrungsmittelstammdaten() {
    }



    public List<NutritionProduct> parseToNahrungsmittel() throws IOException {


        List<IBaseResource> nutritionproducts= new ArrayList<>();
        Bundle bundle= client.search().forResource(NutritionProduct.class).withProfile("http://example.org/StructureDefinition/TelematikNutritionProduct").returnBundle(Bundle.class).execute();
        System.out.println("fertig");


        nutritionproducts.addAll(BundleUtil.toListOfResources(ctx,bundle));

        while(bundle.getLink().get(1).getRelation().name().equals("NEXT")){
            bundle=client.loadPage().next(bundle).execute();
            nutritionproducts.addAll(BundleUtil.toListOfResources(ctx,bundle));

        }

        List<NutritionProduct> nutritionProductList = new ArrayList<>();

        for(IBaseResource nutritionproduct:nutritionproducts){
            NutritionProduct nutritionProductC= (NutritionProduct) nutritionproduct;
            nutritionProductList.add(nutritionProductC);

        }


        return nutritionProductList;
    }

    public List<NutritionProduct> parseAllToNahrungsmittel() throws IOException {


        List<IBaseResource> nutritionproducts= new ArrayList<>();
        Bundle bundle= client.search().forResource(NutritionProduct.class).returnBundle(Bundle.class).execute();
        System.out.println("fertig");


        nutritionproducts.addAll(BundleUtil.toListOfResources(ctx,bundle));
        int i=1;

        while(bundle.getLink().get(1).getRelation().name().equals("NEXT")){
            bundle=client.loadPage().next(bundle).execute();
            nutritionproducts.addAll(BundleUtil.toListOfResources(ctx,bundle));


            //System.out.println(i+"   halaman lline 73 "+ bundle.getLink().get(1).getRelation().getDisplay());
            i++;
        }

        List<NutritionProduct> nutritionProductList = new ArrayList<>();

        for(IBaseResource nutritionproduct:nutritionproducts){
            NutritionProduct nutritionProductC= (NutritionProduct) nutritionproduct;
            nutritionProductList.add(nutritionProductC);

        }


        return nutritionProductList;
    }



    public NutritionProduct getNutritionProduct(String nutritionProductId){
        NutritionProduct  nutritionProduct=client.read().resource(NutritionProduct.class).withId(nutritionProductId).execute();
        return nutritionProduct;

    }




}
