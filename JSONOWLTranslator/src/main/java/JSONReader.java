

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Map;
import java.util.Scanner;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/*
 * Class to read the JSON from the editor and create the objects of Node and BehaviourTree classes. 
 * The builder should receive a JSON file and return an object of the BehaviourTree class.
 * 
 * @author Marta Caro-Martinez
 * */

public class JSONReader {
	
	private JsonParser parser; // parser JSON - Java Objects, elements
	private String myTreeJson; // String with the content from the JSON file
	
	public JSONReader(String fileName) {
		this.parser = new JsonParser();
		this.myTreeJson = readFile(fileName);
	}
	
	// This function receives the path where the JSON file is
	// return the JSON in String format
	private String readFile(String fileName) {
		String myJSONString = "";
		
		// check if the file exists
		try {
			// create the file 
			File myJSONfile = new File(fileName);
			
			// Object to read data
			Scanner reader = new Scanner(myJSONfile);
		    
			// read each line and append to the string
			while (reader.hasNextLine()) {
				String line = reader.nextLine();
				myJSONString += line;
			}
			reader.close();
			
			
		}catch(FileNotFoundException e) {
			System.out.println("The file does not exist");
			e.printStackTrace();
		}
		
		return myJSONString;
		
	}

	// function to add values for BT attributes (except for its trees)
	// fst argument: bt to edit with the data from the JSON
	// scnd argument: the data kept in the behaviour tree object
	// trd argument: more data kept in the tree object (this should be in the bt object)
	private BehaviourTree loadBTfromJSON(BehaviourTree bt, JsonObject myData) {
		
		if (myData.has("id")) {
			bt.setId(myData.get("id").getAsString());
		}
		if (myData.has("description")) {
			bt.setDescription(myData.get("description").getAsString());
		}
		if (myData.has("title")) { ////////////////////////// look over again
			bt.setTitle(myData.get("title").getAsString() + "ExplanationExperience");
		}
		/*if (myData.has("version")) { ////////////////////////// look over again
			bt.setVersion(btData.get("version").getAsString());
		}
		if (myData.has("scope")) { ////////////////////////// look over again
			bt.setScope(btData.get("scope").getAsString());
		}*/
		if (myData.has("selectedTree")) { 
			bt.setSelectedTreeId(myData.get("selectedTree").getAsString());
		}
		
		//// 
		/*if(myData.has("custom_nodes")) {
			//bt.setCustomNodes(myData.get("custom_nodes").getAsJsonArray());
			System.out.println(myData.get("custom_nodes"));
			ArrayList<String> custom_nodes = new ArrayList<String>();
			JsonArray my_array_json = myData.get("custom_nodes").getAsJsonArray();
			for (int i = 0; i < my_array_json.size(); i++) {
				custom_nodes.add(my_array_json.get(i).getAsInt());
			}
			
		}*/
		
		return bt;
	}
	
	
	// function to get a NodeType from a String
	private NodeType getNodeTypeFromString(String node_type) {
		NodeType myNodeType = null;
		
		// // *********** nodes that have to be there for sure
		if (node_type.equals("Sequence")) {
			myNodeType = NodeType.SequenceNode;
		} else if (node_type.equals("Priority")) {
			myNodeType = NodeType.PriorityNode;
		}  else if (node_type.equals("Explanation Method")) {
			myNodeType = NodeType.ExplanationMethodNode;
		} 
		
		
		//*********** new nodes
		else if (node_type.equals("Replacement")) {
			myNodeType = NodeType.Replacement;
		} else if (node_type.equals("Variant")) {
			myNodeType = NodeType.Variant;
		} else if (node_type.equals("Complement")) {
			myNodeType = NodeType.Complement;
		} else if (node_type.equals("Suplement")) {
			myNodeType = NodeType.Supplement;
		} else if (node_type.equals("Custom")) {
			myNodeType = NodeType.Custom;
		} 
		
		
		// *********** to remove?	
		else if (node_type.equals("Repeat Until Failure")) {
			myNodeType = NodeType.RepeatUntilFailureNode;
		} else if (node_type.equals("Repeat At")) {
			myNodeType = NodeType.RepeatAtXNode;
		} else if (node_type.equals("Evaluation Method")) {
			myNodeType = NodeType.EvaluationMethodNode;
		} else if (node_type.equals("Condition")) {
			myNodeType = NodeType.ConditionNode;
		} else if (node_type.equals("Failer")) {
			myNodeType = NodeType.FailerNode;
		} else if (node_type.equals("Succeeder")) {
			myNodeType = NodeType.SucceederNode;
		} else if (node_type.equals("Inverter")) {
			myNodeType = NodeType.InverterNode;
		} else if (node_type.equals("Limit At Activations")) {
			myNodeType = NodeType.LimitAtActivationsNode;
		} else if (node_type.equals("Repeat Until Success")) {
			myNodeType = NodeType.RepeatUntilSuccessNode;
		} 
		
		return myNodeType;
	}
	
	// function to get a Hashtable from a Json that represents the properties of the Node or Tree
	private Hashtable getPropertiesFromJSON(JsonObject myData) {
		JsonObject myProperties = myData.getAsJsonObject("properties");
		Hashtable properties = new Hashtable();
		
		for(Map.Entry<String, JsonElement> entry : myProperties.entrySet()) {
			properties.put(entry.getKey(), entry.getValue());
		}
		return properties;
	}
	
	// function to add values for Node attributes
	private Node loadNodeFromJson(JsonObject myData) {
		Node myNode = new Node();
		
		// setting attributes for the node
		if (myData.has("id")) {
			myNode.setId(myData.get("id").getAsString());
		}
		if (myData.has("Concept")) {
			String concept = myData.get("Concept").getAsString();
			NodeType my_concept = getNodeTypeFromString(concept);
			myNode.setType(my_concept);
		}
		if (myData.has("Instance")) {
			myNode.setInstance(myData.get("Instance").getAsString());
		}
		if (myData.has("description")) {
			myNode.setDescription(myData.get("description").getAsString());
		}
		if (myData.has("properties")) {
			Hashtable properties = getPropertiesFromJSON(myData);
			myNode.setProperties(properties);
		} 
		
		
		if (myData.has("firstChild")) {
			JsonObject nextChild = myData.getAsJsonObject("firstChild");
			
			ArrayList<String> myChildren = new ArrayList();
			myChildren.add(nextChild.get("Id").getAsString());
			
			
			while(nextChild.has("Next") && !nextChild.get("Next").isJsonNull()) {
				nextChild = nextChild.getAsJsonObject("Next");
				myChildren.add(nextChild.get("Id").getAsString());
			}
			
			//System.out.println(myChildren);
			
			myNode.setChildren(myChildren);
		}
				
		return myNode;
	}
	
	// function to add values for Tree attributes
	private Tree loadTreeFromJSON(JsonObject myData) {

		Tree myTree = new Tree();
		
		/*if (myData.has("version")) {
			myTree.setVersion(myData.get("version").getAsString());
		}
		if (myData.has("scope")) {
			myTree.setScope(myData.get("scope").getAsString());
		}*/
		if (myData.has("id")) {
			myTree.setId(myData.get("id").getAsString());
		}
		if (myData.has("Instance")) {
			myTree.setInstance(myData.get("Instance").getAsString());
		} 
		if (myData.has("description")) {
			myTree.setDescription(myData.get("description").getAsString());
		}
		if (myData.has("root")) {
			
			if (!myData.get("root").isJsonNull()) {
				
				myTree.setRoot(myData.get("root").getAsString());
			} 
		}
		if (myData.has("properties")) {
			Hashtable properties = getPropertiesFromJSON(myData);
			myTree.setProperties(properties);
		}
		
		
		if (myData.has("nodes")) {
			JsonObject myNodes = myData.getAsJsonObject("nodes");
			ArrayList<Node> nodeList = new ArrayList<Node>();
			
			// we iterate the nodes 
			for(Map.Entry<String, JsonElement> entry : myNodes.entrySet()) {
			    //System.out.println("Key = " + entry.getKey() + " Value = " + entry.getValue() );
			    
			    Node nodeElement = loadNodeFromJson(entry.getValue().getAsJsonObject());
				nodeList.add(nodeElement);
			}
			
			// add the array of nodes to the tree
			myTree.setNodes(nodeList);
		} else {
			System.out.println("Error: it seems the JSON file does not have the right format about key 'nodes' ");
		}
		
		return myTree;
	}
	
	// https://studytrails.com/2016/09/12/java-google-json-parse-json-to-java-tree/
	public BehaviourTree getBT() {
		BehaviourTree myBT = new BehaviourTree();
		
		// parse the JSON file
		JsonElement element = this.parser.parse(this.myTreeJson);
				
		
		// check if the element is a JSON object, it should be
		if (element.isJsonObject()) {

			// get the Json object
			JsonObject behaviourTreeAtt = element.getAsJsonObject();
			
				
				// first level: details about the BT
				myBT = loadBTfromJSON(myBT, behaviourTreeAtt);			
				
				// second level: details about each tree
				if (behaviourTreeAtt.has("trees")) {
					
					JsonArray myTrees = behaviourTreeAtt.getAsJsonArray("trees");
					ArrayList<Tree> treeList = new ArrayList<Tree>();
					for(int i = 0; i < myTrees.size(); i++) {
						Tree treeElement = loadTreeFromJSON(myTrees.get(i).getAsJsonObject());
						treeList.add(treeElement);
					}
					
					// add the array of trees to the behaviour tree
					myBT.setTrees(treeList);
					
					// third level: details about each node (inside the loadTreeFromJSON)
				} else {
					System.out.println("Error: it seems the JSON file does not have the right format about key 'trees' ");
				}
			
		} 
		
		else {
			System.out.println("Error: it seems the JSON file does not have the right format");
		}
		
		
		
		return myBT;
	}
	
	
	// Getters and setters
	
	public String getMyTreeJson() {
		return myTreeJson;
	}

	public void setMyTreeJson(String myTreeJson) {
		this.myTreeJson = myTreeJson;
	}
}
