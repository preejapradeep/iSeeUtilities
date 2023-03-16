import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.swing.text.html.HTMLDocument.Iterator;

import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.HasIRI;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLAxiom;
import org.semanticweb.owlapi.model.OWLClass;
import org.semanticweb.owlapi.model.OWLClassAssertionAxiom;
import org.semanticweb.owlapi.model.OWLClassExpression;
import org.semanticweb.owlapi.model.OWLDataFactory;
import org.semanticweb.owlapi.model.OWLDataProperty;
import org.semanticweb.owlapi.model.OWLDataPropertyAssertionAxiom;
import org.semanticweb.owlapi.model.OWLDocumentFormat;
import org.semanticweb.owlapi.model.OWLLogicalAxiom;
import org.semanticweb.owlapi.model.OWLNamedIndividual;
import org.semanticweb.owlapi.model.OWLObjectComplementOf;
import org.semanticweb.owlapi.model.OWLObjectIntersectionOf;
import org.semanticweb.owlapi.model.OWLObjectProperty;
import org.semanticweb.owlapi.model.OWLObjectVisitor;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import org.semanticweb.owlapi.model.OWLOntologyStorageException;
import org.semanticweb.owlapi.model.PrefixManager;
import org.semanticweb.owlapi.reasoner.NodeSet;
import org.semanticweb.owlapi.reasoner.OWLReasoner;
import org.semanticweb.owlapi.reasoner.OWLReasonerFactory;
import org.semanticweb.owlapi.reasoner.structural.StructuralReasonerFactory;
import org.semanticweb.owlapi.search.EntitySearcher;
import org.semanticweb.owlapi.search.Searcher;
import org.semanticweb.owlapi.util.DefaultPrefixManager;
import org.semanticweb.owlapi.util.OWLAPIStreamUtils;

public class GetExplainerProperties {
	
	// consts from information content entity
	public final String OBJECT_RECORD = "https://purl.org/heals/eo#object_record";
	public final String REASONING_MODE = "https://purl.org/heals/eo#Reasoning_Mode";
	public final String SYSTEM_TRACE = "https://purl.org/heals/eo#SystemTrace";
	public final String SYSTEM_RECOMMENDATION = "https://purl.org/heals/eo#SystemRecommendation";
	
	public final String CLASSIF_RECOMMENDATION = "http://www.w3id.org/iSeeOnto/aimodel#ClassificationRecommendation";
	public final String REGRES_RECOMMENDATION = "http://www.w3id.org/iSeeOnto/aimodel#RegressionRecommendation";
	public final String EXPLANATION_MODALITY = "https://purl.org/heals/eo#ExplanationModality";
	public final String AUDIO_MODALITY = "http://www.w3id.org/iSeeOnto/explainer#AudioModality";
	public final String VIDEO_MODALITY = "http://www.w3id.org/iSeeOnto/explainer#VisualModality";
	
	
	public final String AITASKGOAL = "http://www.w3id.org/iSeeOnto/aimodel#AITaskGoal";
	public final String EXPLANATIONGOAL = "https://purl.org/heals/eo#ExplanationGoal";
	public final String USERQUESTION = "http://www.w3id.org/iSeeOnto/user#UserQuestion";
	public final String HOWQUESTION = "http://www.w3id.org/iSeeOnto/user#HowQuestion";
	public final String WHATQUESTION = "http://www.w3id.org/iSeeOnto/user#WhatQuestion";
	public final String WHENQUESTION = "http://www.w3id.org/iSeeOnto/user#WhenQuestion";
	public final String WHEREQUESTION = "http://www.w3id.org/iSeeOnto/user#Where_Question";
	public final String WHYQUESTION = "http://www.w3id.org/iSeeOnto/user#WhyQuestion";
	public final String EXPLANATION = "http://linkedu.eu/dedalo/explanationPattern.owl#Explanation";
	public final String CASEBASEDEXPLANATION = "https://purl.org/heals/eo#CaseBasedExplanation";
	public final String CONTEXTUALEXPLANATION = "https://purl.org/heals/eo#ContextualExplanation";
	public final String INTROSPECTIVEEXPLANATION = "http://www.w3id.org/iSeeOnto/explainer#Introspective_Explanation";
	public final String RASIONALISATIONEXPLANATION = "http://www.w3id.org/iSeeOnto/explainer#Rationalisation_Explanation";
	public final String CONTRASTIVEEXPLANATION = "https://purl.org/heals/eo#ContrastiveExplanation";
	public final String COUNTERFACTUALEXPLANATION = "https://purl.org/heals/eo#CounterfactualExplanation";
	public final String EVERYDAYEXPLANATION = "https://purl.org/heals/eo#EverydayExplanation";
	public final String CLINICALPEARLS = "https://purl.org/heals/eo#ClinicalPearl";
	public final String FEATUREINFLUENCEEXPLANATION = "http://www.w3id.org/iSeeOnto/explainer#Feature_Influence_Explanation";
	public final String ANCHOREXPLANATION = "http://www.w3id.org/iSeeOnto/explainer#Anchor_Explanation";
	public final String CONTRASTINGFEATURE = "http://www.w3id.org/iSeeOnto/explainer#Contrasting_Feature_Importance_Explanation";
	public final String CONTRIBUTIODISTRIBUTION = "http://www.w3id.org/iSeeOnto/explainer#Contribution_Distribution_Explanation";
	public final String SALIENCYMAP = "http://www.w3id.org/iSeeOnto/explainer#Saliency_Map";
	public final String SENSITIVITYMAP = "http://www.w3id.org/iSeeOnto/explainer#Sensitivity_Map";
	public final String NEIGHBOURHOODEXPLANATION = "http://www.w3id.org/iSeeOnto/explainer#Neighbourhood_Explanation";
	public final String PROTOTYPEEXPLANATION = "http://www.w3id.org/iSeeOnto/explainer#Prototype_Explanation";
	public final String SCIENTIFICEXPLANATION = "https://purl.org/heals/eo#scientificExplanation";
	public final String EVIDENCEBASEDEXP = "https://purl.org/heals/eo#Evidence_Based_Explanation";
	public final String MECHANISTICEXP = "https://purl.org/heals/eo#Mechanistic_Explanation"; 
	public final String SEMIFACTUALEXP = "http://www.w3id.org/iSeeOnto/explainer#Semi-factual_Explanation"; 
	public final String SIMULATIONBASEDEXP = "https://purl.org/heals/eo#SimulationBasedExplanation";
	public final String STATISTICALEXP = "https://purl.org/heals/eo#StatisticalExplanation";
	public final String TRACESBASEDEXP = "https://purl.org/heals/eo#TraceBasedExplanation";
	
	
	// file path where our ontology is
	
	private OWLOntologyManager manager;
	private OWLOntology ontology;
	private OWLReasoner reasoner; 
	private OWLDataFactory dataFactory; 
	private PrefixManager prefixManagerExplainer;
	private PrefixManager prefixManagerSemanticWeb;
	private PrefixManager prefixManagerPurl;
	private PrefixManager prefixManagerAIModel;
	private PrefixManager prefixObo;
	private PrefixManager prefixSemanticScience;
	
	
	public GetExplainerProperties(String path) throws OWLOntologyCreationException {
		
		// Obtain a copy of a manager to manage a set of ontologies
		this.manager = OWLManager.createOWLOntologyManager();
		
		// loading an ontology (iSeeOnto in this case) from a file
		this.ontology = manager.loadOntologyFromOntologyDocument(new File(path));
		
		OWLReasonerFactory reasonerFactory = new StructuralReasonerFactory();
		this.reasoner = reasonerFactory.createNonBufferingReasoner(this.ontology);
		
		// getting the data and managers necessary to get data from the ontology
		this.dataFactory = manager.getOWLDataFactory();
		this.prefixManagerExplainer = new DefaultPrefixManager(null, null, "http://www.w3id.org/iSeeOnto/explainer#");
		
		this.prefixManagerSemanticWeb = new DefaultPrefixManager(null, null, "http://www.semanticweb.org/marta/ontologies/2023/0/iSeeOnto#");
		//this.prefixManagerSemanticWeb = new DefaultPrefixManager(null, null, "http://www.semanticweb.org/isee/iseeonto/2022/9/30#");
		this.prefixManagerPurl = new DefaultPrefixManager(null, null, "https://purl.org/heals/eo#");
		this.prefixManagerAIModel = new DefaultPrefixManager(null, null, "http://www.w3id.org/iSeeOnto/aimodel#");
	
		this.prefixObo = new DefaultPrefixManager(null, null, "http://purl.obolibrary.org/obo/");
		this.prefixSemanticScience = new DefaultPrefixManager(null, null, "http://semanticscience.org/resource/");
		//this.prefixNN = new DefaultPrefixManager(null, null, "http://purl.obolibrary.org/obo/");
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
	
	// get the name of the individual
	private String getName(String original, char character) {
		return original.substring(original.lastIndexOf(character) + 1, original.lastIndexOf(">"));
	}
	
	/*
	 * function to get the explainabilityTechnique of an explainer
	 * */
private Set<OWLNamedIndividual> getObjectPropertyValueSet(String name, String relation, PrefixManager prefixManagerInd, PrefixManager prefixManagerRel) {
		OWLNamedIndividual my_instance = this.dataFactory.getOWLNamedIndividual(":" + name, prefixManagerInd);
		//System.out.println(my_instance);
		OWLObjectProperty hasRelation = this.dataFactory.getOWLObjectProperty(":" + relation, prefixManagerRel);
		//System.out.println(hasRelation);
		
		Set<OWLNamedIndividual> values = this.reasoner.getObjectPropertyValues(my_instance, hasRelation).getFlattened();
		
		return values;
		//String prop = values.toString();
		//System.out.println(values);
		//System.out.println("primer elemento" + values.iterator().next());
		//System.out.println("segundo elemento" + values.iterator().next());
		//System.out.println(prop);
		/*if (prop.contains("#"))
			return getName(prop, '#');
		else
			return getName(prop, '/');*/
	}
	
	
	private String getObjectPropertyValue(String individual, String relation, boolean simple, PrefixManager prefixManagerInd, PrefixManager prefixManagerRel) {
		Set<OWLNamedIndividual> explain_techSet = getObjectPropertyValueSet(individual, relation, prefixManagerInd, prefixManagerRel); 
		
		String explain_tech = "";
		
		// if the value of that field is not multiple
		if (simple) {
			// we only get first value
			explain_tech = explain_techSet.iterator().next().toString();
		} else {
			// if it is multiple, we get the whole array
			explain_tech = explain_techSet.toString();
		}
		
		return explain_tech;
	}
	

	private ArrayList<String> getParents(String property, String limit){
		OWLNamedIndividual my_instance = this.dataFactory.getOWLNamedIndividual(":" + property, this.prefixManagerSemanticWeb);
		//System.out.println("hola");
		//System.out.println(my_instance);
		
		NodeSet<OWLClass> n = this.reasoner.getTypes(my_instance);
		//System.out.println(n);
		ArrayList<String> my_parents = new ArrayList<String>();
		
		int i = 0;
		boolean found = false;
		String parent = "";
		while (i < n.getNodes().toArray().length && !found) {
			if(n.getNodes().toArray()[i].toString().indexOf('<') != -1) {
				parent = getName(n.getNodes().toArray()[i].toString(), '<');
				//System.out.println("parent");
				//System.out.println(parent);
				if (parent.equals(limit)) {
					found = true;
				} else {
					my_parents.add(parent);
					i++;
				}
			} else {
				System.out.println("Error reading the parent nodes");
				System.out.println(limit);
				System.out.println(parent);
				found = true;
			}
			
			//System.out.println(parent);
			
		}
		
		//System.out.println(n.getNodes());
		//System.out.println(getName(n.getNodes().toArray()[1].toString(), '<')); // get the iri of the class which the individual belongs to
		
		return my_parents;
	}
	
	
	private ArrayList<String> getParentsClass(String my_ind, String limit, PrefixManager pmClass){
		
		// creating the instance that should be included inside the class if that instance had to exist
		
		// getting the class where we want to include the instance
		OWLClass my_class = dataFactory.getOWLClass(":" + my_ind, pmClass);
		//System.out.println("Mi clase");
		//System.out.println(my_class);
		// creating the new instance for our class
		OWLNamedIndividual my_instance = dataFactory.getOWLNamedIndividual(":" + my_ind, this.prefixManagerSemanticWeb);
		
		OWLClassAssertionAxiom classAssertion = this.dataFactory.getOWLClassAssertionAxiom(my_class, my_instance);
		
		this.manager.addAxiom(this.ontology, classAssertion);
		
		ArrayList<String> my_parents = getParents(my_ind, limit);
		
		this.manager.removeAxiom(this.ontology, classAssertion);
		
		return my_parents;
	}
	
	/*
	 * Function to read all the properties of a specific Explainar and create an Explainer object
	 * */
	// TODO I have to include the types of ExplainabilityTechniqueType, Information Content Entity (output), AI MEthod, AI task 
	private Explainer getExplainer(String individual) {
		
		System.out.println("hola");
		
		Set<OWLNamedIndividual> explain_techIRISet = getObjectPropertyValueSet(individual, "utilises", this.prefixManagerSemanticWeb, this.prefixManagerExplainer); 
		String explain_techIRI = explain_techIRISet.iterator().next().toString();
		String explain_tech = getName(explain_techIRI, '#');
		System.out.println(explain_tech);
		
		// rdf:type ?explainability_technique ; 
		// https://stackoverflow.com/questions/47628797/how-to-get-the-class-of-individual-in-owl-api
		// private ArrayList<String> explainTechniqueType; // Anchor and its parent nodes until finding http://www.w3id.org/iSeeOnto/explainer#ExplainabilityTechnique
		ArrayList<String> explainTechniqueType = getParents(explain_tech, "http://www.w3id.org/iSeeOnto/explainer#ExplainabilityTechnique");
		System.out.println(explainTechniqueType.toString());
		
		// isCompatibleWithFeatureTypes ?datasetType ; 
		Set<OWLNamedIndividual> datasetTypeIRI = getObjectPropertyValueSet(explain_tech, "isCompatibleWithFeatureTypes", this.prefixManagerSemanticWeb, this.prefixManagerExplainer); 
		String datasetType = datasetTypeIRI.iterator().next().toString();
		System.out.println(datasetType);
		
		
		// exp:hasConcurrentness ?concurrentness ; 
		Set<OWLNamedIndividual> concurrIRI = getObjectPropertyValueSet(explain_tech, "hasConcurrentness", this.prefixManagerSemanticWeb, this.prefixManagerExplainer); 
		String concurr = concurrIRI.iterator().next().toString();
		System.out.println(concurr);
		
		
		// exp:hasExplanationScope ?scope ; 
		Set<OWLNamedIndividual> scopeIRI = getObjectPropertyValueSet(explain_tech, "hasExplanationScope", this.prefixManagerSemanticWeb, this.prefixManagerExplainer); 
		String scope = scopeIRI.iterator().next().toString();
		System.out.println(scope);
		
		// exp:hasPortability ?portability ; 
		Set<OWLNamedIndividual> portabilityIRI = getObjectPropertyValueSet(explain_tech, "hasPortability", this.prefixManagerSemanticWeb, this.prefixManagerExplainer); 
		String portability = portabilityIRI.iterator().next().toString();
		System.out.println(portability);
		
		// exp:targetType ?target_type ; 
		Set<OWLNamedIndividual> target_typeIRI = getObjectPropertyValueSet(explain_tech, "targetType", this.prefixManagerSemanticWeb, this.prefixManagerExplainer); 
		String target_type =  target_typeIRI.iterator().next().toString();
		System.out.println(target_type);
	
		// pur:hasPresentation ?expoutput ; 
		Set<OWLNamedIndividual> outputIRI = getObjectPropertyValueSet(explain_tech, "hasPresentation", this.prefixManagerSemanticWeb, this.prefixManagerPurl); 
		ArrayList<String> outputIRIList = new ArrayList<String>();
		java.util.Iterator<OWLNamedIndividual> it = outputIRI.iterator(); 
		while (it.hasNext()) {
			outputIRIList.add(it.next().toString());
		}
		//System.out.println(outputIRIList);
		
		
		PrefixManager prefixInformationContentEntity = null;
		ArrayList<ArrayList<String>> presentationType = new ArrayList<ArrayList<String>>();
		for (int i = 0; i < outputIRIList.size(); i++) {
			String output = outputIRIList.get(i);//
			String output_string = output.substring(1, output.lastIndexOf(">"));
			String output_iri = "";
			if (VIDEO_MODALITY.equals(output_string) || AUDIO_MODALITY.equals(output_string) || CLASSIF_RECOMMENDATION.equals(output_string) || REGRES_RECOMMENDATION.equals(output_string) || EXPLANATION_MODALITY.equals(output_string) ||  OBJECT_RECORD.equals(output_string) || REASONING_MODE.equals(output_string) || SYSTEM_TRACE.equals(output_string) || SYSTEM_RECOMMENDATION.equals(output_string) || AITASKGOAL.equals(output_string) || EXPLANATIONGOAL.equals(output_string) || USERQUESTION.equals(output_string) ||
					USERQUESTION.equals(output_string) || HOWQUESTION.equals(output_string) || WHATQUESTION.equals(output_string) || WHENQUESTION.equals(output_string) || WHEREQUESTION.equals(output_string) ||
					WHYQUESTION.equals(output_string) || EXPLANATION.equals(output_string) || CASEBASEDEXPLANATION.equals(output_string) || CONTEXTUALEXPLANATION.equals(output_string) || INTROSPECTIVEEXPLANATION.equals(output_string) || RASIONALISATIONEXPLANATION.equals(output_string) || CONTRASTIVEEXPLANATION.equals(output_string) ||
					COUNTERFACTUALEXPLANATION.equals(output_string) || EVERYDAYEXPLANATION.equals(output_string) || CLINICALPEARLS.equals(output_string) || FEATUREINFLUENCEEXPLANATION.equals(output_string) || CONTRASTINGFEATURE.equals(output_string) || CONTRIBUTIODISTRIBUTION.equals(output_string) || SALIENCYMAP.equals(output_string) ||
					SENSITIVITYMAP.equals(output_string) || NEIGHBOURHOODEXPLANATION.equals(output_string) || PROTOTYPEEXPLANATION.equals(output_string) || SCIENTIFICEXPLANATION.equals(output_string) || EVIDENCEBASEDEXP.equals(output_string) || MECHANISTICEXP.equals(output_string) ||
					SEMIFACTUALEXP.equals(output_string) || SIMULATIONBASEDEXP.equals(output_string) || STATISTICALEXP.equals(output_string) || TRACESBASEDEXP.equals(output_string)) { 
				output_iri = getName(output, '#');
				prefixInformationContentEntity = new DefaultPrefixManager(null, null, output.substring(1,output.lastIndexOf("#")+1)); 
				
			} else {
				output_iri = getName(output, '/');
				prefixInformationContentEntity = this.prefixSemanticScience;
			}
			// output type -> class of output and its parent nodes until finding http://semanticscience.org/resource/SIO_000015
			// pur:hasPresentation ?expoutput ; 
			presentationType.add(getParentsClass(output_iri, "http://semanticscience.org/resource/SIO_000015", prefixInformationContentEntity));
		}
		
		System.out.println("presentation");
		System.out.println(presentationType);
		
		// exp:hasOutputType ?explanation_type ; 
		Set<OWLNamedIndividual> outputTypeIRI = getObjectPropertyValueSet(explain_tech, "hasOutputType", this.prefixManagerSemanticWeb, this.prefixManagerExplainer); 
		String outputType = outputTypeIRI.iterator().next().toString(); 
		
		// private ArrayList<String> explanationType; // the class of AnchorExplanation and all its parents nodes until finding http://linkedu.eu/dedalo/explanationPattern.owl#Explanation
		ArrayList<String> explanationType = getParentsClass(getName(outputType, '#'), "http://linkedu.eu/dedalo/explanationPattern.owl#Explanation", this.prefixManagerPurl);
		System.out.println("explanation_type");
		System.out.println(explanationType);
		
		
		// exp:hasComplexity ?complexity ; 
		Set<OWLNamedIndividual> complexityIRI = getObjectPropertyValueSet(explain_tech, "hasComplexity", this.prefixManagerSemanticWeb, this.prefixManagerExplainer); //// true
		String complexity = complexityIRI.iterator().next().toString();
		System.out.println(complexity);
		
		
		// exp:hasApplicableModelType ?aimethod ; 
		//String aimethod = getObjectPropertyValue(explain_tech, "hasApplicableMethodType", this.prefixManagerSemanticWeb, this.prefixManagerExplainer); 
		// "hasApplicableMethodType"
		Set<OWLNamedIndividual> aimethodIRI = getObjectPropertyValueSet(explain_tech, "hasApplicableMethodType", this.prefixManagerSemanticWeb, this.prefixManagerExplainer); 		
		  
		
		// iterating all the ai methods applicable selected by the user
		ArrayList<String> aimethodIRIList = new ArrayList<String>();
		java.util.Iterator<OWLNamedIndividual> itm = aimethodIRI.iterator(); 
		while (itm.hasNext()) {
			aimethodIRIList.add(itm.next().toString());
		}
		
		ArrayList<ArrayList<String>> aiMethodType = new ArrayList<ArrayList<String>>();
		PrefixManager prefixAiMethod = null;
		// for each ai method, check its parents
		for (int i = 0; i < aimethodIRIList.size(); i++) {
			String aimethod = aimethodIRIList.get(i);
			String aimethod_string = aimethod.substring(1, 11);
			String aimethod_iri = "";
			//System.out.println("aimethod");
			//System.out.println(aimethod_string);
			//http://www.w3id.org/iSeeOnto/aimodel#Reinforcement_Learning
			if(aimethod_string.equals("http://www") || aimethod_string.equals("https://pu")) {
				aimethod_iri = getName(aimethod, '#');
				prefixAiMethod = new DefaultPrefixManager(null, null, aimethod.substring(1,aimethod.lastIndexOf("#")+1)); 
			} else {
				aimethod_iri = getName(aimethod, '/');
				prefixAiMethod = this.prefixObo; 
			}
			
			aiMethodType.add(getParentsClass(aimethod_iri, "https://purl.org/heals/eo#ArtificialIntelligenceMethod", prefixAiMethod));
		}
		
		
		System.out.println(aiMethodType);
		
		
		// exp:applicableProblemType ?aitask .
		// multiple
		
		Set<OWLNamedIndividual> aitaskIRI = getObjectPropertyValueSet(explain_tech, "applicableProblemType", this.prefixManagerSemanticWeb, this.prefixManagerExplainer); 		
		ArrayList<String> aitaskIRIList = new ArrayList<String>();
		java.util.Iterator<OWLNamedIndividual> it_task = aitaskIRI.iterator(); 
		
		//getting all the tasks selected by the user
		while (it_task.hasNext()) {
			aitaskIRIList.add(it_task.next().toString());
		}
		
		ArrayList<ArrayList<String>> aiTaskType = new ArrayList<ArrayList<String>>();
		PrefixManager prefixAiTask = null;
		// for each ai method, check its parents
		for (int i = 0; i < aitaskIRIList.size(); i++) {
			String aitask = aitaskIRIList.get(i);
			//String aitask_string = aitask.substring(1, 11);
			String aitask_iri = "";
			//System.out.println("aitask");
			//System.out.println(aitask);
			
			aitask_iri = getName(aitask, '#');
			prefixAiTask = new DefaultPrefixManager(null, null, aitask.substring(1,aitask.lastIndexOf("#")+1)); 
			aiTaskType.add(getParentsClass(aitask_iri, "https://purl.org/heals/eo#AITask", prefixAiTask));
		}
		
		System.out.println(aiTaskType);
		
		//Set<OWLNamedIndividual> backendIRI = getObjectPropertyValueSet(explain_tech, "hasBackend", this.prefixManagerSemanticWeb, this.prefixManagerExplainer); 
		//String backend = backendIRI.iterator().next().toString();
		//System.out.println(backend);
		
		
		// backend
		Set<OWLNamedIndividual> backendIRI = getObjectPropertyValueSet(explain_tech, "hasBackend", this.prefixManagerSemanticWeb, this.prefixManagerExplainer); 
		ArrayList<String> backendIRIList = new ArrayList<String>();
		java.util.Iterator<OWLNamedIndividual> it_back = backendIRI.iterator(); 
		
		//getting all the tasks selected by the user
		while (it_back.hasNext()) {
			backendIRIList.add(it_back.next().toString());
		}
		
		System.out.println(backendIRIList);
		
		//ArrayList<ArrayList<String>> backendType = new ArrayList<ArrayList<String>>();
		//PrefixManager prefixbackend = null;
		// for each ai method, check its parents
		//for (int i = 0; i < backendIRIList.size(); i++) {
			//String backend = backendIRIList.get(i);
			//String backend_string = backend.substring(1, 11);
			//String backend_iri = "";
			//System.out.println("aitask");
			//System.out.println(aitask);
			
			//backend_iri = getName(backend, '#');
			//prefixbackend = new DefaultPrefixManager(null, null, backend.substring(1,backend.lastIndexOf("#")+1)); 
			//backendType.add(getParentsClass(backend_iri, "http://www.w3id.org/iSeeOnto/explainer#Implementation_Framework", prefixAiTask)); // backendclass !!!!!!!!!!!!!!!!!
			
			
		//}
		
		
		System.out.println("-----------------------------");
		Explainer thisExplainer = new Explainer(individual.replace('_', '/'), explain_techIRI, explainTechniqueType, datasetType, concurr, scope, portability, target_type, presentationType, explanationType, complexity, aiMethodType, aiTaskType, backendIRIList);
	
		return thisExplainer;
	}
	
	
	private void writeExplainerCSV(String nameFile, ArrayList<Explainer> explainers) {
		FileWriter csvWriter;
		try {
			csvWriter = new FileWriter(nameFile);
			
			// first row
			csvWriter.append("Explainer");
			csvWriter.append(";");
			csvWriter.append("ExplainabilityTechnique");
			csvWriter.append(";");
			csvWriter.append("ExplainabilityTechniqueType");
			csvWriter.append(";");
			csvWriter.append("DatasetType");
			csvWriter.append(";");
			csvWriter.append("Concurrentness");
			csvWriter.append(";");
			csvWriter.append("Scope");
			csvWriter.append(";");
			csvWriter.append("Portability");
			csvWriter.append(";");
			csvWriter.append("TargetType");
			csvWriter.append(";");
			csvWriter.append("OutputType");
			csvWriter.append(";");
			csvWriter.append("ExplanationType");
			csvWriter.append(";");
			csvWriter.append("Complexity");
			csvWriter.append(";");
			csvWriter.append("AIMethodType");
			csvWriter.append(";");
			csvWriter.append("AITaskType");
			csvWriter.append(";");
			csvWriter.append("Backend");
			csvWriter.append("\n");
			
			for(int i = 0; i < explainers.size(); i++) {
				explainers.get(i).writeCSVRow(csvWriter);
			}
			
			csvWriter.flush();
			csvWriter.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	} 
	 
	 
	// execute the creation of an instance in the ontology from the BT
	public void saveData() {
		
		
		ArrayList<String> myIndividuals = printIndividualsByclass(this.ontology, "Explainer");
		ArrayList<Explainer> explainerOnto = new ArrayList<Explainer>();
		System.out.println(myIndividuals);
		
		
		for (int i = 0; i < myIndividuals.size(); i++) {
			// explainability technique - explainer utilises 
			System.out.println(myIndividuals.get(i));
			
			explainerOnto.add(getExplainer(myIndividuals.get(i)));
			
		}
		
		writeExplainerCSV("explainers.csv", explainerOnto);
		
		
		OWLNamedIndividual my_instance = this.dataFactory.getOWLNamedIndividual(":ExamlpeTechnique", this.prefixManagerSemanticWeb);
		//System.out.println(my_instance.getEntityType());

		//System.out.println(this.reasoner.getTypes(my_instance));
		
		// writing the result in a new file
		//IRI destination = IRI.create(new File("updated-iSeeOnto.owl"));
        //manager.saveOntology(ontology, new OWLXMLDocumentFormat(), destination);
		
		//OWLDocumentFormat format = this.manager.getOntologyFormat(ontology);   
		//try {
		//	this.manager.saveOntology(this.ontology, format, destination);
		//} catch (OWLOntologyStorageException e) {
			// TODO Auto-generated catch block
		//	e.printStackTrace();
		//}
		
	}
}
