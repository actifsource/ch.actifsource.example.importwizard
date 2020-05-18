package ch.actifsource.example.importwizard.generic.javamodel;

import ch.actifsource.util.collection.IMultiMapOrdered;
import ch.actifsource.core.dynamic.*;

@edu.umd.cs.findbugs.annotations.SuppressWarnings("EQ_DOESNT_OVERRIDE_EQUALS")
public class ParameterLine extends DynamicResource implements IParameterLine {

  public static final ch.actifsource.core.dynamic.IDynamicResource.IFactory<IParameterLine> FACTORY = new ch.actifsource.core.dynamic.IDynamicResource.IFactory<IParameterLine>() {
    
    @Override
    public IParameterLine create() {
      return new ParameterLine();
    }
    
    @Override
    public IParameterLine create(IDynamicResourceRepository resourceRepository, ch.actifsource.core.Resource resource) {
      return new ParameterLine(resourceRepository, resource);
    }
  
  };

  public ParameterLine() {
    super(IParameterLine.TYPE_ID);
  }
  
  public ParameterLine(IDynamicResourceRepository resourceRepository, ch.actifsource.core.Resource resource) {
    super(resourceRepository, resource, IParameterLine.TYPE_ID);
  }

  // attributes
  
  @Override
  public java.util.List<java.lang.String> selectLine() {
    return _getListAttribute(java.lang.String.class, ch.actifsource.example.importwizard.generic.GenericPackage.ParameterLine_line);
  }
    
  public void setLine(java.util.List<java.lang.String> line) {
     _setListAttribute(ch.actifsource.example.importwizard.generic.GenericPackage.ParameterLine_line, line);
  }

  @Override
  public java.lang.String selectName() {
    return _getSingleAttribute(java.lang.String.class, ch.actifsource.core.CorePackage.NamedResource_name);
  }
    
  public void setName(java.lang.String name) {
     _setSingleAttribute(ch.actifsource.core.CorePackage.NamedResource_name, name);
  }

  // relations
  
  @Override
  public ch.actifsource.core.javamodel.IClass selectTypeOf() {
    return _getSingle(ch.actifsource.core.javamodel.IClass.class, ch.actifsource.core.CorePackage.Resource_typeOf);
  }

  public ParameterLine setTypeOf(ch.actifsource.core.javamodel.IClass typeOf) {
    _setSingle(ch.actifsource.core.CorePackage.Resource_typeOf, typeOf);
    return this;
  }
    
  // accept property value visitor
  @Override
  public void accept(IPropertyValueVisitor visitor) {
    // attributes
    _acceptListAttribute(java.lang.String.class, ch.actifsource.example.importwizard.generic.GenericPackage.ParameterLine_line, visitor);
    _acceptSingleAttribute(java.lang.String.class, ch.actifsource.core.CorePackage.NamedResource_name, visitor);
    // relations
    _acceptSingle(ch.actifsource.core.javamodel.IClass.class, ch.actifsource.core.CorePackage.Resource_typeOf, visitor);
  }

}
/* Actifsource ID=[4d723cb5-db37-11de-82b8-17be2e034a3b,45fce631-991b-11ea-8568-8df113ebd62f,NyOa0zxfljHjgI0T0RzZ7G3cWh4=] */
