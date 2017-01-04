package tau.smlab.syntech.dependencygraph.views;

import java.util.ArrayList;

import org.eclipse.xtext.util.Pair;

/**
 * This class save the information needed for the view:
 * For two nodes save the names on the edges between them.
 * 
 * for AG Dependency Graph:
 * The nodes will be pair of asm/gar.
 * The commonEdgeNames will be common vars.
 * 
 * for VAR Dependency Graph:
 * The nodes will be pair of vars.
 * The commonEdgeNames will be all gar/asm names that both vars appear.
 * 
 * 
 * 
 */



public class EdgeInfo {
    //for each MyPair object: the string is the name of the node, and the int is the index of the node.
    ArrayList<MyPair<String, Integer>> nodes = new ArrayList<MyPair<String,Integer>> (2);
    ArrayList<String> commonEdgesNames;
    
   /**
   * C'tor
   * @param nodes2
   * @param commonEdgesNames
   */
    
    public EdgeInfo(ArrayList<MyPair<String, Integer>> nodes2, ArrayList<String> commonEdgesNames) {
      this.nodes = nodes2;
      this.commonEdgesNames = commonEdgesNames;
    }
    /**
     * 
     * @return nodes
     */

    public ArrayList<tau.smlab.syntech.dependencygraph.views.MyPair<String, Integer>> getNodes() {
      return nodes;
    }
    /**
     * 
     * @param nodes
     */
    public void setNodes(ArrayList<MyPair<String, Integer>> nodes) {
      this.nodes = nodes;
    }
    /**
     * 
     * @return commonEdgesNames
     */

    public ArrayList<String> getCommonEdgesNames() {
      return commonEdgesNames;
    }
    /**
     * 
     * @param commonEdgesNames
     */
    public void setCommonEdgesNames(ArrayList<String> commonEdgesNames) {
      this.commonEdgesNames = commonEdgesNames;
    }
    
    
    
    
}



