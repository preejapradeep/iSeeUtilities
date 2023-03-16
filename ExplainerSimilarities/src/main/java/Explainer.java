import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Explainer {
	
	private String expName; // anchors_image
	private String explainTechnique; // anchors_image_technique
	private ArrayList<String> explainTechniqueType; // Anchor and its parent nodes until finding http://www.w3id.org/iSeeOnto/explainer#ExplainabilityTechnique
	private String datasetType; // image
	private String concurrentness; // post-hoc
	private String scope; // local
	private String portability; // model-agnostic
	private String target_type; // prediction
	//private String output; // Annotated Image (individual of InformationContentEntity)
	private ArrayList<ArrayList<String>> outputType; // class of output and its parent nodes until finding http://semanticscience.org/resource/SIO_000015
	//private String explanationOutput; // Anchor Explanation 
	private ArrayList<String> explanationType; // the class of AnchorExplanation and all its parents nodes until finding http://linkedu.eu/dedalo/explanationPattern.owl#Explanation
	private String complexity; // low ?
	//private String aiMethod; // NN
	private ArrayList<ArrayList<String>> aiMethodType; // the class of NN and its parents
	//private String aiTask; // Classification
	private ArrayList<ArrayList<String>> aiTaskType; // the class of Classification and its parents
	private ArrayList<String> backend; // the backend used to implement the explainer
	
	

	public Explainer(String expName, String explainTechnique, ArrayList<String> explainTechniqueType,
			String datasetType, String concurrentness, String scope, String portability, String target_type,
			ArrayList<ArrayList<String>> outputType, ArrayList<String> explanationType,
			String complexity, ArrayList<ArrayList<String>> aiMethodType,
			ArrayList<ArrayList<String>> aiTaskType, ArrayList<String> backend) {
		this.expName = expName;
		this.explainTechnique = explainTechnique;
		this.explainTechniqueType = explainTechniqueType;
		this.datasetType = datasetType;
		this.concurrentness = concurrentness;
		this.scope = scope;
		this.portability = portability;
		this.target_type = target_type;
		//this.output = output;
		this.outputType = outputType;
		//this.explanationOutput = explanationOutput;
		this.explanationType = explanationType;
		this.complexity = complexity;
		//this.aiMethod = aiMethod;
		this.aiMethodType = aiMethodType;
		//this.aiTask = aiTask;
		this.aiTaskType = aiTaskType;
		this.backend = backend;
	}

	
	
	
	public String getExpName() {
		return expName;
	}




	public void setExpName(String expName) {
		this.expName = expName;
	}




	public String getExplainTechnique() {
		return explainTechnique;
	}




	public void setExplainTechnique(String explainTechnique) {
		this.explainTechnique = explainTechnique;
	}




	public ArrayList<String> getExplainTechniqueType() {
		return explainTechniqueType;
	}




	public void setExplainTechniqueType(ArrayList<String> explainTechniqueType) {
		this.explainTechniqueType = explainTechniqueType;
	}




	public String getDatasetType() {
		return datasetType;
	}




	public void setDatasetType(String datasetType) {
		this.datasetType = datasetType;
	}




	public String getConcurrentness() {
		return concurrentness;
	}




	public void setConcurrentness(String concurrentness) {
		this.concurrentness = concurrentness;
	}




	public String getScope() {
		return scope;
	}




	public void setScope(String scope) {
		this.scope = scope;
	}




	public String getPortability() {
		return portability;
	}




	public void setPortability(String portability) {
		this.portability = portability;
	}




	public String getTarget_type() {
		return target_type;
	}




	public void setTarget_type(String target_type) {
		this.target_type = target_type;
	}




	/*public String getOutput() {
		return output;
	}




	public void setOutput(String output) {
		this.output = output;
	}*/




	public ArrayList<ArrayList<String>> getOutputType() {
		return outputType;
	}




	public void setOutputType(ArrayList<ArrayList<String>> outputType) {
		this.outputType = outputType;
	}




	/*public String getExplanationOutput() {
		return explanationOutput;
	}




	public void setExplanationOutput(String explanationOutput) {
		this.explanationOutput = explanationOutput;
	}*/




	public ArrayList<String> getExplanationType() {
		return explanationType;
	}




	public void setExplanationType(ArrayList<String> explanationType) {
		this.explanationType = explanationType;
	}




	public String getComplexity() {
		return complexity;
	}




	public void setComplexity(String complexity) {
		this.complexity = complexity;
	}




	/*public String getAiMethod() {
		return aiMethod;
	}




	public void setAiMethod(String aiMethod) {
		this.aiMethod = aiMethod;
	}*/




	public ArrayList<ArrayList<String>> getAiMethodType() {
		return aiMethodType;
	}




	public void setAiMethodType(ArrayList<ArrayList<String>> aiMethodType) {
		this.aiMethodType = aiMethodType;
	}




	/*public String getAiTask() {
		return aiTask;
	}




	public void setAiTask(String aiTask) {
		this.aiTask = aiTask;
	}*/




	public ArrayList<ArrayList<String>> getAiTaskType() {
		return aiTaskType;
	}




	public void setAiTaskType(ArrayList<ArrayList<String>> aiTaskType) {
		this.aiTaskType = aiTaskType;
	}

	

	public ArrayList<String> getBackend() {
		return backend;
	}




	public void setBackend(ArrayList<String> backend) {
		this.backend = backend;
	}




	// auxiliar function to write a row in a csv with the explainer data
	public void writeCSVRow(FileWriter csvWriter) throws IOException { 
		csvWriter.append(this.expName);
		csvWriter.append(";");
		csvWriter.append(this.explainTechnique);
		csvWriter.append(";");
		csvWriter.append(this.explainTechniqueType.toString());
		csvWriter.append(";");
		csvWriter.append(this.datasetType);
		csvWriter.append(";");
		csvWriter.append(this.concurrentness);
		csvWriter.append(";");
		csvWriter.append(this.scope);
		csvWriter.append(";");
		csvWriter.append(this.portability);
		csvWriter.append(";");
		csvWriter.append(this.target_type);
		csvWriter.append(";");
		csvWriter.append(this.outputType.toString());
		csvWriter.append(";");
		csvWriter.append(this.explanationType.toString());
		csvWriter.append(";");
		csvWriter.append(this.complexity);
		csvWriter.append(";");
		csvWriter.append(this.aiMethodType.toString());
		csvWriter.append(";");
		csvWriter.append(this.aiTaskType.toString());
		csvWriter.append(";");
		csvWriter.append(this.backend.toString());
		csvWriter.append("\n");
	}
	
	
	
	
}
