package tau.smlab.syntech.dependencygraph.views;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.xtext.nodemodel.INode;
import org.eclipse.xtext.nodemodel.util.NodeModelUtils;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.ui.editor.XtextEditor;

import tau.smlab.syntech.services.SpectraGrammarAccess;
import tau.smlab.syntech.spectra.LTLAsm;
import tau.smlab.syntech.spectra.LTLGar;
import tau.smlab.syntech.spectra.TemporalExpression;
import tau.smlab.syntech.spectra.TemporalPrimaryExpr;
import tau.smlab.syntech.spectra.VarDecl;
import tau.smlab.syntech.typesystem.TypeSystemUtils;

public class AGDependencyGraphModel {
  
  ArrayList<NodeInfo> GarAsmNodeList;
  ArrayList<EdgeInfo> GarAsmEdgeList;
  XtextResource resource;
  AGDependencyGraphController controller;
  final int numberOfChars = 25;
  final String DOTS = "...";
  final String GAR = "G ";
  final String ASM = "A ";

  @Inject SpectraGrammarAccess grammarAccess;
  
  /**
   * C'tor
   * @param xtextEditor
   * @param controller
   */
  public AGDependencyGraphModel(XtextResource resource, AGDependencyGraphController controller) {
    this.resource = resource;
    this.controller = controller;
    this.GarAsmNodeList = new ArrayList<NodeInfo>();
    this.GarAsmEdgeList = new ArrayList<EdgeInfo>();
  }
  
  /**
   * C'tor
   * @param controller
   */
  
  public AGDependencyGraphModel(AGDependencyGraphController controller) {
    this.controller = controller;
    this.GarAsmNodeList = new ArrayList<NodeInfo>();
    this.GarAsmEdgeList = new ArrayList<EdgeInfo>();
  }

  /**
   * set xtextResource field and call the function that calculate GarAsmNodeList and GarAsmEdgeList.
   * update controller with the updated GarAsm Lists.
   * @param xtextResource
   */
  public void setXtextResource(XtextResource resource) {
    this.resource = resource;
    calculateGarAsmNodeList(resource);
    calculateGarAsmEdgeList();
    updateController();
    
  }
  
  public void updateController () {
    controller.setGarAsmLists(GarAsmNodeList, GarAsmEdgeList);
  }

  public ArrayList<NodeInfo> getGarAsmNodeList() {
    return GarAsmNodeList;
  }

  public ArrayList<EdgeInfo> getGarAsmEdgeList() {
    return GarAsmEdgeList;
  }
  
  public void calculateGarAsmNodeList(XtextResource resource) {

      EObject content = resource.getContents().get(0);
      EList<EObject> ListOfEObject = content.eContents();
      ArrayList<LTLGar> garList = new ArrayList<LTLGar> ();
      ArrayList<LTLAsm> asmList = new ArrayList<LTLAsm> ();


      
      for (int i=0; i < ListOfEObject.size(); i++){
        if(ListOfEObject.get(i) instanceof LTLGar)
        {
          LTLGar gar = (LTLGar) ListOfEObject.get(i);      
          garList.add(gar);
          
        }
        if(ListOfEObject.get(i) instanceof LTLAsm)
        {
          LTLAsm asm = (LTLAsm) ListOfEObject.get(i);
          asmList.add(asm);


        }
      }
      TemporalExpression temporalExpresison = null;
      
      // go over garList and for each gar, create NodeInfo
      
      int size = garList.size();
      for (int i = 0; i < size; i++){
        
        ArrayList<String> varsNames = new ArrayList<String>();
        //find the TemporalExpression in current gar
        temporalExpresison =  garList.get(i).getTemporalExpr();
        //find the list of TemporalPrimaryExpr in temporalExpresison
        List<TemporalPrimaryExpr> tpeList = TypeSystemUtils.getAllTemporaryPrimaryExprs(temporalExpresison);
        //ArrayList<VarDecl> vars = new ArrayList<VarDecl> ();
        //go over tpeList and for each TemporalPrimaryExpr find the var in it, if exist.
        for (TemporalPrimaryExpr tpe: tpeList){
          VarDecl var = TypeSystemUtils.extractVarDeclFromTemporalPrimaryExpr(tpe);
          if (var != null) {
             //vars.add(var);
            if(!(varsNames.contains(var.getName())))
                varsNames.add(var.getName());
           }
        }
        String name = garList.get(i).getName();
        if (name == null){
          //if the name is null, find the first numberOfChars that written in file.
          INode node = NodeModelUtils.getNode(garList.get(i));
          name = NodeModelUtils.getTokenText(node);

          name = name.replaceFirst("^guarantee ", "");
          name = name.replaceFirst("^gar ", "");

          int size_name = name.length();
          if(size_name > numberOfChars ){
            name =  name.substring(0, numberOfChars);
            name += DOTS;
          }
        }
        String garName = GAR + name;
        NodeInfo node = new NodeInfo(garName, varsNames);
        GarAsmNodeList.add(node);
      }
      // go over asmList and for each asm, create NodeInfo

      size = asmList.size();
      for (int i = 0; i < size; i++){
        
        ArrayList<String> varsNames = new ArrayList<String>();
        //find the TemporalExpression in current asm
        temporalExpresison =  asmList.get(i).getTemporalExpr();
        //find the list of TemporalPrimaryExpr in temporalExpresison
        List<TemporalPrimaryExpr> tpeList = TypeSystemUtils.getAllTemporaryPrimaryExprs(temporalExpresison);
        //ArrayList<VarDecl> vars = new ArrayList<VarDecl> ();
        //go over tpeList and for each TemporalPrimaryExpr find the var in it, if exist.
        for (TemporalPrimaryExpr tpe: tpeList){
          VarDecl var = TypeSystemUtils.extractVarDeclFromTemporalPrimaryExpr(tpe);
          if (var != null) {
             //vars.add(var);
            if(!(varsNames.contains(var.getName())))
              varsNames.add(var.getName());
           }
        }
        
        String name = asmList.get(i).getName();
        if (name == null){
          //if the name is null, find the first numberOfChars that written in file.
          INode node = NodeModelUtils.getNode(asmList.get(i));
          name = NodeModelUtils.getTokenText(node);

          name = name.replaceFirst("^assumption ", "");
          name = name.replaceFirst("^asm ", "");

          int size_name = name.length();
          if (numberOfChars < size_name) {
            name =  name.substring(0, numberOfChars);
            name += DOTS;
          }
        }
        
        
        String asmName = ASM + name;
        NodeInfo node = new NodeInfo(asmName, varsNames);
        GarAsmNodeList.add(node);
      }


  }
  

  
  public void calculateGarAsmEdgeList() {
    int size = GarAsmNodeList.size();
    for (int i=0; i<size; i++) {
      for (int j=i+1; j<size ;j++) {
        NodeInfo node1 = GarAsmNodeList.get(i);
        NodeInfo node2 = GarAsmNodeList.get(j);
        ArrayList<String> commonVars = intersection(node1.getEdgesNames(), node2.getEdgesNames());
        if (commonVars.isEmpty())
          continue;
        ArrayList<MyPair<String,Integer>> nodes = new ArrayList<MyPair<String, Integer>> (2);
        MyPair<String, Integer> pair1 = MyPair.createPair(node1.getNodeName(), i);
        MyPair<String, Integer> pair2 = MyPair.createPair(node2.getNodeName(), j);
        nodes.add(pair1);
        nodes.add(pair2);
        EdgeInfo edgesBetweenNodes = new EdgeInfo(nodes, commonVars);
        GarAsmEdgeList.add(edgesBetweenNodes);


        
      }
    }
  }
  

  public <T> ArrayList<T> intersection(ArrayList<T> list1, ArrayList<T> list2) {
    ArrayList<T> list = new ArrayList<T>();

    for (T t : list1) {
        if(list2.contains(t)) {
            list.add(t);
        }
    }

    return list;
}
  
}
