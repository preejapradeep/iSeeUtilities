import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

/*
 * This class executes the program
 * The idea is to get/download the json files from the BT editor (for a behaviour tree solution)
 * that should be already in the server. We have to implement that as a web service
 * We have to save BT instances in a new file of the ontology 
 * (in a similar way as BoschExplanationExperience for example). 
 * @author Marta Caro-Martinez
 * */

public class Main {

	public static void main(String[] args) {
		//JSONReader reader = new JSONReader("RandomTreeSimple.json");
		//JSONReader reader = new JSONReader("moreComplexBT.json");
		//JSONReader reader = new JSONReader("randomBTs.json");
		JSONReader reader = new JSONReader("gradCam.json");
		
		BehaviourTree BT = reader.getBT();
		
		System.out.println(BT.toString());
		
		
		//OWLInstance owlInstance = new OWLInstance("pizza.func.owl", BT);
		//OWLInstance owlInstance = new OWLInstance("iSeeOnto.owl", BT);
		//OWLInstance owlInstance = new OWLInstance("iSeeOnto_v2.owl", BT);
		OWLInstance owlInstance = new OWLInstance("iSeeOnto_explainers.owl", BT);
		
		
		try {
			owlInstance.addNewInstanceFromBT();
		} catch (OWLOntologyCreationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (OWLOntologyStorageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
