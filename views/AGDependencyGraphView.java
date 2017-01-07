package tau.smlab.syntech.dependencygraph.views;

import java.util.ArrayList;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.ui.editor.XtextEditor;
import org.eclipse.zest.core.widgets.Graph;
import org.eclipse.zest.core.widgets.GraphConnection;
import org.eclipse.zest.core.widgets.GraphNode;
import org.eclipse.zest.layouts.LayoutStyles;
import org.eclipse.zest.layouts.algorithms.SpringLayoutAlgorithm;
import org.eclipse.zest.layouts.algorithms.TreeLayoutAlgorithm;

public class AGDependencyGraphView extends ViewPart  {
  
  public static final String ID = "tau.smlab.syntech.dependencygraph.views.AGDependencyGraphView";
  private Graph graph;
  private int layout = 1;
  private AGDependencyGraphController controller;
  private ArrayList<NodeInfo> GarAsmNodeList;
  private ArrayList<EdgeInfo> GarAsmEdgeList; 
    
  
  public AGDependencyGraphView(String contentGeneratorId) {
    super();
    controller = new AGDependencyGraphController(this);
  }

  public AGDependencyGraphView() {
    super();
    controller = new AGDependencyGraphController(this);
  }
  



  public void createPartControl(Composite parent) {

    // Graph will hold all other objects
    graph = new Graph(parent, SWT.NONE);

    // create the graph with the right nodes and connections.
    init();

    graph.setLayoutAlgorithm(new SpringLayoutAlgorithm(LayoutStyles.NO_LAYOUT_NODE_RESIZING), true);
    // Selection listener on graphConnect or GraphNode is not supported
    // see https://bugs.eclipse.org/bugs/show_bug.cgi?id=236528
    graph.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent e) {
        System.out.println(e);
      }

    });

  }
  
  public void init() {

    clearGraph(graph);
    updateGarAsmNodeEdgeList();
    createGraph();

  }
  
  
  public void updateGarAsmNodeEdgeList ()
  {
    //find active page
    IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
    IEditorPart activeEditor = page.getActiveEditor();
    //Go over the xtext by the controller and model.
    //It's update GarAsmNodeList and GarAsmEdgeList lists.
    if (activeEditor instanceof XtextEditor) {
        XtextEditor xtextEditor = (XtextEditor) activeEditor;
        xtextEditor.getDocument().readOnly((XtextResource resource) -> {
        controller.setXtextResource(resource);
        return null;
          });
      }
  }

  public void createGraph () {
    
    
    //create lists of the GraphNode
    ArrayList<GraphNode> graphNodes = new ArrayList<GraphNode> ();

    //create GraphNode for each asm/gar.
    for (int i=0; i < GarAsmNodeList.size(); i++) {
      NodeInfo nodeInfo = GarAsmNodeList.get(i);
      GraphNode node = new GraphNode(graph, SWT.NONE, nodeInfo.getNodeName());
      if (nodeInfo.getNodeName().startsWith("G"))
        //Red color for LTLGar nodes
        node.setBackgroundColor(new Color(null,242,144,144));
      else {
        //Blue color for LTLAsm nodes
        node.setBackgroundColor(new Color(null,177,177,247));;
       }
      graphNodes.add(node);
      }
    //create GraphConnection (edge) for each pair of asm/gar with shared variables.
      for (int j=0; j < GarAsmEdgeList.size(); j++) {
        EdgeInfo edge = GarAsmEdgeList.get(j);
        int indexOfNode1 = edge.getNodes().get(0).getElement1();
        int indexOfNode2 = edge.getNodes().get(1).getElement1();
        // Create edge between two nodes with shared vars
        GraphConnection graphConnection = new GraphConnection(graph, SWT.NONE,
                        graphNodes.get(indexOfNode1), graphNodes.get(indexOfNode2));
        
        //Thickness of edge represents the relative number of shared variables
        graphConnection.setLineWidth(edge.getCommonEdgesNames().size());
        
        //Hovering over edge should show the relevant shared variables.
        String textOnEdge = "";
        for (int i=0; i < edge.getCommonEdgesNames().size(); i++){
          textOnEdge += edge.getCommonEdgesNames().get(i);
          if (i != edge.getCommonEdgesNames().size()-1) {
            textOnEdge += "\n";
          }
        }
        IFigure tooltip1 = new Label(textOnEdge);

        graphConnection.setTooltip(tooltip1);
      }
     

  }
  public void setLayoutManager() {
          switch (layout) {
          case 1:
                  graph.setLayoutAlgorithm(new TreeLayoutAlgorithm(
                                  LayoutStyles.NO_LAYOUT_NODE_RESIZING), true);
                  layout++;
                  break;
          case 2:
                  graph.setLayoutAlgorithm(new SpringLayoutAlgorithm(
                                  LayoutStyles.NO_LAYOUT_NODE_RESIZING), true);
                  layout = 1;
                  break;

          }

  }
  
  public void setGarAsmNodeList(ArrayList<NodeInfo> garAsmNodeList) {
    GarAsmNodeList = garAsmNodeList;
  }

  public void setGarAsmEdgeList(ArrayList<EdgeInfo> garAsmEdgeList) {
    GarAsmEdgeList = garAsmEdgeList;
  }


  /**
   * Passing the focus request to the viewer's control.
   */
  public void setFocus() {
    init ();
  }

  public void setGarAsmLists(ArrayList<NodeInfo> garAsmNodeList2, ArrayList<EdgeInfo> garAsmEdgeList2) {
    this.GarAsmNodeList = garAsmNodeList2;
    this.GarAsmEdgeList = garAsmEdgeList2;
  }

  
  
  
  
  public void clearGraph( Graph graph )
  {       
      Object[] objects = graph.getConnections().toArray() ;           
      for (int i = 0 ; i < objects.length; i++)
      {
          GraphConnection graCon = (GraphConnection) objects[i];
          if(!graCon.isDisposed())
              graCon.dispose();
      }            

      objects = graph.getNodes().toArray();       
      for (int i = 0; i < objects.length; i++)
      {
          GraphNode graNode = (GraphNode) objects[i];
          if(!graNode.isDisposed())
              graNode.dispose();
      }
  }

}

  


