import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;

public class Main {
	public static void main(String[] args) {
		
		
		GetExplainerProperties explainerProperties;
		try {
			//explainerProperties = new GetExplainerProperties("iseeonto-tmp.rdf");
			explainerProperties = new GetExplainerProperties("iSeeOnto.rdf");
			
			//explainerProperties = new GetExplainerProperties("iseeonto_withexamples.rdf");
			explainerProperties.saveData();
		} catch (OWLOntologyCreationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}
}
