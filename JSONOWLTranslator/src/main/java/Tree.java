import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Set;

/*
 * Class where we are going to represent a Tree. A BehaviourTree can have more than one Tree
 * 
 * @author Marta Caro-Martinez
 * */

public class Tree {

	private String id;
	private String instance;
	private String description;
	/*private String version;
	private String scope;*/
	private String root;
	private Hashtable properties;
	private ArrayList<Node> nodes;

	public Tree() {
		this.id = "";
		this.instance = "";
		this.description = "";
		//this.version = "";
		//this.scope = "";
		this.root = "";
		this.properties =  new Hashtable();
		this.nodes = new ArrayList<Node>();
	}
	
	// Getters and setters

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getInstance() {
		return instance;
	}

	public void setInstance(String title) {
		this.instance = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	/*public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}*/

	public String getRoot() {
		return root;
	}

	public void setRoot(String root) {
		this.root = root;
	}

	public Hashtable getProperties() {
		return properties;
	}

	public void setProperties(Hashtable properties) {
		this.properties = properties;
	}

	public ArrayList<Node> getNodes() {
		return nodes;
	}

	public void setNodes(ArrayList<Node> nodes) {
		this.nodes = nodes;
	}

	
	public String toString() {
		String result = "Tree [id=" + id + ", instance=" + instance + ", description=" + description + ", root=" + root + ", properties= "; //+ properties+ ", nodes=" + nodes + "]";
		
		Set<String> setOfKeys = this.properties.keySet();
		
		for (String key : setOfKeys) {
			result += "key: " + key + "; value: " + this.properties.get(key) + "\n";
        }
		
		result += "nodes: \n";
		
		for(int i = 0; i < this.nodes.size(); i++) {
			result += "node " + i + " " + this.nodes.get(i).toString() + " \n";
		}
		
		return result;
	}
	

	// function to find a node in our list using the node id
	public Node getNodeById(String id) {
		Node nodeToFind = null;
		int i = 0;
		boolean found = false;
		while (i < this.getNodes().size() && !found) {
			if (this.getNodes().get(i).getId() == id) {
				found = true;
				nodeToFind = this.getNodes().get(i);
			} else {
				i++;
			}
		}
		return nodeToFind;
	}
	
	
	
}
