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

public class AGDependencyGraphView extends ViewPart {
  public AGDependencyGraphView(String contentGeneratorId) {
    super();
    controller = new AGDependencyGraphController(this);
  }

  public AGDependencyGraphView() {
    super();
    controller = new AGDependencyGraphController(this);
  }
  
  public static final String ID = "tau.smlab.syntech.dependencygraph.views.AGDependencyGraphView";
  private Graph graph;
  private int layout = 1;
  private AGDependencyGraphController controller;
  private ArrayList<NodeInfo> GarAsmNodeList;
  private ArrayList<EdgeInfo> GarAsmEdgeList; 
    


  public void createPartControl(Composite parent) {

    // Graph will hold all other objects
      graph = new Graph(parent, SWT.NONE);

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
        

          graph.setLayoutAlgorithm(new SpringLayoutAlgorithm(
                          LayoutStyles.NO_LAYOUT_NODE_RESIZING), true);
          // Selection listener on graphConnect or GraphNode is not supported
          // see https://bugs.eclipse.org/bugs/show_bug.cgi?id=236528
          graph.addSelectionListener(new SelectionAdapter() {
                  @Override
                  public void widgetSelected(SelectionEvent e) {
                          System.out.println(e);
                  }

          });

          
  
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
  }

  public void setGarAsmLists(ArrayList<NodeInfo> garAsmNodeList2, ArrayList<EdgeInfo> garAsmEdgeList2) {
    this.GarAsmNodeList = garAsmNodeList2;
    this.GarAsmEdgeList = garAsmEdgeList2;
  }


  
}


//EObject content = resource.getContents().get(0);
//EList<EObject> ListOfEObject = content.eContents();
//ArrayList<LTLGar> garList = new ArrayList<LTLGar> ();
//ArrayList<GraphNode> garListNodes = new ArrayList<GraphNode> ();
//ArrayList<LTLAsm> asmList = new ArrayList<LTLAsm> ();
//ArrayList<GraphNode> asmListNodes = new ArrayList<GraphNode> ();
//ArrayList<EObject> GarsAsms = new ArrayList<EObject>();
//
//
//String name = null;
//GraphNode node = null;
//
//for (int i=0; i < ListOfEObject.size()-1; i++){
//if(ListOfEObject.get(i) instanceof LTLGar)
//{
//  LTLGar gar = (LTLGar) ListOfEObject.get(i);      
//  garList.add(gar);
//  GarsAsms.add(gar);
//  
//  name = "G " + gar.getName();
////  if (null == name){
////    name = "gar";
////  }
//  node = new GraphNode(graph, SWT.NONE, name);
//  node.setBackgroundColor(new Color(null,242,144,144));;
//  garListNodes.add(node);
//
//}
//if(ListOfEObject.get(i) instanceof LTLAsm)
//{
//  LTLAsm asm = (LTLAsm) ListOfEObject.get(i);
//  asmList.add(asm);
//  name = "A " + asm.getName();
//  
//  
//  node = new GraphNode(graph, SWT.NONE, name);
//  node.setBackgroundColor(new Color(null,177,177,247));;
//
//  asmListNodes.add(node);
//  GarsAsms.add(asm);
//
//}
//}
//TemporalExpression temporalExpresison = null;
//ArrayList<ArrayList<VarDecl>> varsGar = new ArrayList<ArrayList<VarDecl>> ();// finding all vars in the gar. the index is the same of the garList.
//int size = garList.size();
//for (int i = 0; i < size; i++){
//for (int j = i+1; j < size; j++){
// temporalExpresison =  garList.get(i).getTemporalExpr();
// List<TemporalPrimaryExpr> tpeList = TypeSystemUtils.getAllTemporaryPrimaryExprs(temporalExpresison);
// ArrayList<VarDecl> vars = new ArrayList<VarDecl> ();
// for (TemporalPrimaryExpr te: tpeList){
//   VarDecl var = TypeSystemUtils.extractVarDeclFromTemporalPrimaryExpr(te);
//   if (var != null) {
//     vars.add(var);
//   }
//}
// varsGar.add(vars);
//}
//}
//// Do your AST related stuff here

// Optionally return a result
//return null;
//});
//}
//



// Graph will hold all other objects
//   graph = new Graph(parent, SWT.NONE);
// now a few nodes
//GraphNode node1 = new GraphNode(graph, SWT.NONE, "Jim");
//GraphNode node2 = new GraphNode(graph, SWT.NONE, "Jack");
//GraphNode node3 = new GraphNode(graph, SWT.NONE, "Joe");
//GraphNode node4 = new GraphNode(graph, SWT.NONE, "Bill");
//// Lets have a directed connection
//new GraphConnection(graph, ZestStyles.CONNECTIONS_DIRECTED, node1,
//      node2);
//// Lets have a dotted graph connection
//new GraphConnection(graph, ZestStyles.CONNECTIONS_DOT, node2, node3);
//// Standard connection
//new GraphConnection(graph, SWT.NONE, node3, node1);
//// Change line color and line width
//GraphConnection graphConnection = new GraphConnection(graph, SWT.NONE,
//      node1, node4);
//graphConnection.changeLineColor(parent.getDisplay().getSystemColor(
//      SWT.COLOR_GREEN));
//// Also set a text
//graphConnection.setText("This is a text");
//graphConnection.setHighlightColor(parent.getDisplay().getSystemColor(
//      SWT.COLOR_RED));
//graphConnection.setLineWidth(3);
//
