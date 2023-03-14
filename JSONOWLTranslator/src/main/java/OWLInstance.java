import java.io.File;
import java.util.ArrayList;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDocumentFormat;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.model.PrefixManager;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasonerFactory;
import org.semanticweb.owlapi.util.DefaultPrefixManager;

/*
 * This class loads the iSeeOnto ontology and create a new instance for this ontology from a behaviour tree
 * 
 * @author Marta Caro-Martinez
 * */
public class OWLInstance {
	
	// file path where our ontology is
	private File iSeeOnto;
	
	// BT we want to translate
	private BehaviourTree behaviourTree;
	
	// URL that identifies our ontology
	private String base;
	
	
	public OWLInstance(String ontologyPath, BehaviourTree bt) {
		this.iSeeOnto = new File(ontologyPath);
		this.behaviourTree = bt;
		this.base = "https://www.w3id.org/iSeeOnto/BehaviourTree#";
		//this.base = "http://www.co-ode.org/ontologies/pizza/pizza.owl#";
	}
	
	// function to create an assertion --> including an instance for a class
	private void addAssertion(ArrayList<OWLClassAssertionAxiom> assertList, OWLDataFactory dataFactory, PrefixManager pm, String className, String instanceName){
		
		OWLClassAssertionAxiom classAssertion = null;
		
		// getting the class where we want to include the instance
		OWLClass my_class = dataFactory.getOWLClass(":" + className, pm);
		
		// creating the new instance for our class
		OWLNamedIndividual my_instance = dataFactory.getOWLNamedIndividual(":" + instanceName, pm);
		
		// including the new instance as an instance of the class
		// fst parameter: class where including the instance (second parameter)
		classAssertion = dataFactory.getOWLClassAssertionAxiom(my_class, my_instance);
		
		assertList.add(classAssertion);

	}
	
	// auxiliary function to get all the individuals from a class
	// original source: https://stackoverflow.com/questions/28072721/getting-all-individuals-of-a-specific-class-using-owlapi-and-jfact-reasoner
	private static ArrayList<String> printIndividualsByclass(OWLOntology ontology, String owlClass){
	    OWLReasonerFactory reasonerFactory = new StructuralReasonerFactory();
	    OWLReasoner reasoner = reasonerFactory.createNonBufferingReasoner(ontology);
	    
	    ArrayList<String> myIndividuals = new ArrayList<String>();
	    
	    for (OWLClass c : ontology.getClassesInSignature()) {
	        if (c.getIRI().getShortForm().equals(owlClass)){
	            NodeSet<OWLNamedIndividual> instances = reasoner.getInstances(c, false);
	            //System.out.println("Class : "+ c.getIRI().getShortForm());
	            for (OWLNamedIndividual i : instances.getFlattened()) {
	                //System.out.println(i.getIRI().getShortForm()); 
	                myIndividuals.add(i.getIRI().getShortForm());
	            }
	        }
	    }
	    
	    return myIndividuals;
	}
	
	
	/*
	 * Auxiliary function to create a relationship (object property) on the ontology between two instances
	 * */
	private void createObjectProperty(ArrayList<OWLObjectPropertyAssertionAxiom> myAssertions, OWLDataFactory dataFactory, PrefixManager pm1, PrefixManager pm2, PrefixManager pm3, String fst_instance, String scnd_instance, String relation_name) {
		
		OWLNamedIndividual fst_ind = dataFactory.getOWLNamedIndividual(":" + fst_instance, pm1);
		OWLNamedIndividual scnd_ind = dataFactory.getOWLNamedIndividual(":" + scnd_instance, pm2);
		
		OWLObjectProperty hasRelation = dataFactory.getOWLObjectProperty(":" + relation_name, pm3);
		
		OWLObjectPropertyAssertionAxiom propertyAssertion = dataFactory.getOWLObjectPropertyAssertionAxiom(hasRelation, fst_ind, scnd_ind);
		
		myAssertions.add(propertyAssertion);
			
	}
	
	
	/*
	 * Auxiliary function to create a relationship (data property) on the ontology between two instances
	 * */
	private void createDataProperty(ArrayList<OWLDataPropertyAssertionAxiom> myAssertions, OWLDataFactory dataFactory, PrefixManager pmProperty, PrefixManager pmIndividual, String property_name, String instance_name, String property_value) {
		
		OWLDataProperty hasProperty = dataFactory.getOWLDataProperty(":" + property_name, pmProperty);
		
		OWLNamedIndividual ind = dataFactory.getOWLNamedIndividual(":" + instance_name, pmIndividual);
		
		OWLDataPropertyAssertionAxiom dataPropertyAssertion = dataFactory.getOWLDataPropertyAssertionAxiom(hasProperty, ind, property_value);
		
		myAssertions.add(dataPropertyAssertion);
	}
	
	
	/*
	 * Auxiliary function to encapsulate the addition of the object properties related to the nodes
	 * */
	private void createNodePropsAssertions(Node myNode, Tree myTree, OWLDataFactory dataFactory, PrefixManager pm, ArrayList<OWLObjectPropertyAssertionAxiom> myObjPropAssertions){
		// if the node is the root, we have to include the root relationship
		if(myNode.getId().equals(myTree.getRoot())) {
			createObjectProperty(myObjPropAssertions, dataFactory, pm, pm, pm, myTree.getId(), myNode.getId(), "root_node");
		}
		
		// include the relationship between tree and nodes
		createObjectProperty(myObjPropAssertions, dataFactory, pm, pm, pm, myTree.getId(), myNode.getId(), "nodes");
		
		
		// adding the relationships between nodes
		int size_child = myNode.getChildren().size();
		if (size_child != 0) {
			for (int k = 0; k < size_child; k++) {
				String myChild = myNode.getChildren().get(k);
				
				if (k == 0) {
					createObjectProperty(myObjPropAssertions, dataFactory, pm, pm, pm, myNode.getId(), myChild, "firstChild");
				} else {
					createObjectProperty(myObjPropAssertions, dataFactory, pm, pm, pm, myNode.getChildren().get(k-1), myChild, "next");
				}
				
			}
		}
		
	}
	
	/*
	 * Auxiliary function to create assertion of individuals when the node is an explanation method
	 * */
	private void createExplanationNodeClassAssertions(ArrayList<OWLClassAssertionAxiom> myClassAssertions, OWLDataFactory dataFactory, PrefixManager pm, PrefixManager pmw3, OWLOntology o, Node myNode) throws Exception {
		// Check if the explainer exists in the catalogue
		ArrayList<String> myIndividuals = printIndividualsByclass(o, "Explainer");
		
		//if(myIndividuals.contains(currentNode.getInstance())) {
		if(myIndividuals.contains("LIMETabular")) {
			// if it exists, add the explainer as property of the node
		
			addAssertion(myClassAssertions, dataFactory, pm, "KeyLiteralValuePair", myNode.getId() + "VP");
			
			addAssertion(myClassAssertions, dataFactory, pmw3, "Dictionary", myNode.getId() + "Dictionary");
			
			// the instance name has to be the same than the explainer in the catalogue on the ontology
		
		} else {
			throw new Exception("That explainer does not exist. Please, include it on this website: ********** TODO");
		}
	}
	
	
	/*
	 * Auxiliary function to create assertion of properties when the node is an explanation method
	 * */
	private void createExplanationNodePropsAssertions(ArrayList<OWLObjectPropertyAssertionAxiom> myObjPropAssertions, ArrayList<OWLDataPropertyAssertionAxiom> myDataPropAssertions, OWLDataFactory dataFactory, PrefixManager pm, PrefixManager pmw3, Node myNode) {
		// relation between node and dictionary
		createObjectProperty(myObjPropAssertions, dataFactory, pm, pmw3, pm, myNode.getId(), myNode.getId() + "Dictionary", "properties");
		
		//relation between dictionary and key-value
		createObjectProperty(myObjPropAssertions, dataFactory, pmw3, pm, pmw3, myNode.getId() + "Dictionary", myNode.getId() + "VP", "hadDictionaryMember");

		
		// here we are going to include the property value to indicate which is the explainer to execute
		
		// Assertion pair_key "explainer"
		createDataProperty(myDataPropAssertions, dataFactory, pm, pm, "pair_key", myNode.getId() + "VP", "explainer");
		
		// Assertion value iri of explainer to execute
		//createDataProperty(myAssertions, dataFactory, pmw3, pm, "value", myNode.getId() + "VP", "http://www.semanticweb.org/isee/iseeonto/2022/9/30#" + myNode.getInstance());
		
		createDataProperty(myDataPropAssertions, dataFactory, pmw3, pm, "value", myNode.getId() + "VP", "http://www.semanticweb.org/isee/iseeonto/2022/9/30#" + "LIMETabular");
		
	}
	
	/*
	 * Function to include the BehavoiurTree, and all related individuals and properties, into the ontology
	 * */
	private void includeAssertions(ArrayList<OWLClassAssertionAxiom> myClassAssertions, ArrayList<OWLObjectPropertyAssertionAxiom> myObjPropAssertions, ArrayList<OWLDataPropertyAssertionAxiom> myDataPropAssertions, OWLDataFactory dataFactory, PrefixManager pm, PrefixManager pmw3, PrefixManager pmSemantic, OWLOntology o) throws Exception{
		
		// BT instance
		addAssertion(myClassAssertions, dataFactory, pm, "BehaviourTree", this.behaviourTree.getTitle());
		
		
		// create the relationship between the solution and its explanation experience
		// TODO - CHANGE THE NAME OF THE EXPLANATION EXPERIENCE FOR THE ACTUAL EXPLANATION EXPERIENCE
		createObjectProperty(myObjPropAssertions, dataFactory, pmSemantic, pm, pmSemantic, "ExampleExplanationExperience", this.behaviourTree.getTitle(), "hasSolution");

		
		// create object property to indicate the selected tree to the BT
		createObjectProperty(myObjPropAssertions, dataFactory, pm, pm, pm, this.behaviourTree.getTitle(), this.behaviourTree.getSelectedTreeId(), "selectedTree");

		
		for (int i = 0; i < this.behaviourTree.getTrees().size(); i++) {
			Tree myTree = this.behaviourTree.getTrees().get(i);
			
			// add a new individual for each tree
			addAssertion(myClassAssertions, dataFactory, pm, "Tree", myTree.getId());
			
			// create the property to indicate the BT has that tree 
			createObjectProperty(myObjPropAssertions, dataFactory, pm, pm, pm, this.behaviourTree.getTitle(), myTree.getId(), "trees");
			
			
			for (int j = 0; j < myTree.getNodes().size(); j++) {
				Node myNode = myTree.getNodes().get(j);	
				
				// doing the same for all the nodes
				addAssertion(myClassAssertions, dataFactory, pm, myNode.getType().toString(), myNode.getId());
				
				createNodePropsAssertions(myNode, myTree, dataFactory, pm, myObjPropAssertions);
				
				// if the node is an explanation method node, we have to include the explainer to execute
				if(myNode.getType() == NodeType.ExplanationMethodNode) {
					
					// create the individuals related to the dictionary that indicates the explainer to execute
					createExplanationNodeClassAssertions(myClassAssertions, dataFactory, pm, pmw3, o, myNode);
					
					
					// create the properties related to the dictionary that indicates the explainer to execute
					createExplanationNodePropsAssertions(myObjPropAssertions, myDataPropAssertions, dataFactory, pm, pmw3, myNode);
						
					
				} 
				
			}
		}
		
	}
	
	
	// Create the instances and add them to the ontology
	private OWLOntology createNewInstance(OWLOntologyManager m, OWLOntology o) {
		// manager to create the data
		OWLDataFactory dataFactory = m.getOWLDataFactory();
		PrefixManager prefixManager = new DefaultPrefixManager(null, null, this.base);
		PrefixManager prefixManagerW3 = new DefaultPrefixManager(null, null, "http://www.w3.org/ns/prov#"); 
		PrefixManager prefixManagerSematic = new DefaultPrefixManager(null, null, "http://www.semanticweb.org/isee/iseeonto/2022/9/30#");
		
		
		try {
			
			// creating all the assertions
			ArrayList<OWLClassAssertionAxiom> myClassAssertions = new ArrayList<OWLClassAssertionAxiom>();
			ArrayList<OWLObjectPropertyAssertionAxiom> myObjPropAssertions = new ArrayList<OWLObjectPropertyAssertionAxiom>();
			ArrayList<OWLDataPropertyAssertionAxiom> myDataPropAssertions = new ArrayList<OWLDataPropertyAssertionAxiom>();
			
			
			includeAssertions(myClassAssertions, myObjPropAssertions, myDataPropAssertions, dataFactory, prefixManager, prefixManagerW3, prefixManagerSematic, o);
				
			// checking with pizza
			//ArrayList<OWLClassAssertionAxiom> classAssertion = assertPizza(dataFactory, prefixManager);
			
			for(int i = 0; i < myClassAssertions.size(); i++) {
				// include the new assertions into the ontology
				m.addAxiom(o, myClassAssertions.get(i));
			}
			for(int i = 0; i < myObjPropAssertions.size(); i++) {
				// include the new assertions into the ontology
				m.addAxiom(o, myObjPropAssertions.get(i));
			}
			for(int i = 0; i < myDataPropAssertions.size(); i++) {
				// include the new assertions into the ontology
				m.addAxiom(o, myDataPropAssertions.get(i));
			}
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return o;
	}
	
	// execute the creation of an instance in the ontology from the BT
	public void addNewInstanceFromBT() throws OWLOntologyCreationException, OWLOntologyStorageException{
		
		// Obtain a copy of a manager to manage a set of ontologies
		OWLOntologyManager manager = OWLManager.createOWLOntologyManager();
		
		// loading an ontology (iSeeOnto in this case) from a file
		OWLOntology ontology = manager.loadOntologyFromOntologyDocument(this.iSeeOnto);
		
		// Getting all the classes of the ontology
		/*for (OWLClass cls : ontology.getClassesInSignature()) {
	        System.out.println(cls);
	    }*/
		
		createNewInstance(manager, ontology);
		
		// writing the result in a new file
		IRI destination = IRI.create(new File("updated-iSeeOnto.owl"));
        //manager.saveOntology(ontology, new OWLXMLDocumentFormat(), destination);
		
		OWLDocumentFormat format = manager.getOntologyFormat(ontology);   
		manager.saveOntology(ontology, format, destination);
	}
}
