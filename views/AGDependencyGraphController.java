package tau.smlab.syntech.dependencygraph.views;

import java.util.ArrayList;

import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.ui.editor.XtextEditor;

public class AGDependencyGraphController {
  
  private ArrayList<NodeInfo> GarAsmNodeList;
  private ArrayList<EdgeInfo> GarAsmEdgeList;
  private XtextResource resource;
  private AGDependencyGraphModel model;
  private AGDependencyGraphView view;
  
  /**
   * c'tor
   * @param resource
   * @param view
   */
  
  public AGDependencyGraphController(XtextResource resource, AGDependencyGraphView view) {
    this.view = view;
    model = new AGDependencyGraphModel(resource, this);
  }

  /**
   * c'tor
   * @param view
   */
  public AGDependencyGraphController(AGDependencyGraphView view) {
    this.view = view;
    model = new AGDependencyGraphModel(this);
  }

  /**
   * set active XtextEditor to this controller, and to the model field.
   * @param xtextEditor2
   */
  public void setXtextResource(XtextResource resource2) {
    this.resource = resource2;
    setActiveXtextResourceToModel(resource);
  }
  
  /**
   * Set the active xtextEditor to the model
   * @param xtextEditor
   */
  public void setActiveXtextResourceToModel(XtextResource xtextEditor)
  {
    model.setXtextResource(xtextEditor);
  }

  /**
   * update GarAsmNodeList and GarAsmEdgeList and then update view field, with those lists.
   * @param garAsmNodeList2
   * @param garAsmEdgeList2
   */
  public void setGarAsmLists(ArrayList<NodeInfo> garAsmNodeList2, ArrayList<EdgeInfo> garAsmEdgeList2) {
    this.GarAsmNodeList = garAsmNodeList2;
    this.GarAsmEdgeList = garAsmEdgeList2;
    setGarAsmListsView();
    
  }
  /**
   * update view with the updated GarAsmNodeList and GarAsmEdgeList fields.
   */
  
  public void setGarAsmListsView() {
    view.setGarAsmLists(GarAsmNodeList, GarAsmEdgeList);
  }
  
  
}
