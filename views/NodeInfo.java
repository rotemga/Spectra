package tau.smlab.syntech.dependencygraph.views;

import java.util.ArrayList;

/**
 * 
 * This class save the node name and the names on the edges from this node.
 * 
 * For AG Dependency Graph:
 * The NodeName is the name of the gar/asm. (if there isn't name, it's the first 10 chars)
 * The edgesNames is the names of the vars that appear explicitly or implicitly.
 *
 * For VAR Dependency Graph:
 * The NodeName is the name of the var.
 * The edgesNames is the name this var appear explicitly or implicitly.
 *
 */

public class NodeInfo {
 
  String NodeName;
  ArrayList <String> edgesNames;
  /**
   * C'tor
   * @param nodeName
   * @param edgesNames
   */
  public NodeInfo(String nodeName, ArrayList<String> edgesNames) {
    NodeName = nodeName;
    this.edgesNames = edgesNames;
  }
  
  
  public String getNodeName() {
    return NodeName;
  }
  public void setNodeName(String nodeName) {
    NodeName = nodeName;
  }
  public ArrayList<String> getEdgesNames() {
    return edgesNames;
  }
  public void setEdgesNames(ArrayList<String> edgesNames) {
    this.edgesNames = edgesNames;
  }
  
}
