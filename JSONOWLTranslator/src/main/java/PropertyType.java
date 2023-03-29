
/*
 * Enumerate with the types of properties we can have to define when defining a BehaviourTree
 * 
 * @author Marta Caro-Martinez
 * */

// TO BE UPDATED WITH ALL THE RELATIONSHIPS WE HAVE

public enum PropertyType {
	hasData, // a BehaviourTree hasData some Data
	selected_tree, // Data has selected a tree max 1 tree
	trees, // Data has some trees
	root_node, // a Tree has a root node
	nodes // a Tree has some nodes
}
