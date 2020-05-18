package ch.actifsource.example.importwizard.generic.javamodel;

import ch.actifsource.util.collection.IMultiMapOrdered;
import ch.actifsource.core.dynamic.*;

@edu.umd.cs.findbugs.annotations.SuppressWarnings("EQ_DOESNT_OVERRIDE_EQUALS")
public class Language extends DynamicResource implements ILanguage {

  public static final ch.actifsource.core.dynamic.IDynamicResource.IFactory<ILanguage> FACTORY = new ch.actifsource.core.dynamic.IDynamicResource.IFactory<ILanguage>() {
    
    @Override
    public ILanguage create() {
      return new Language();
    }
    
    @Override
    public ILanguage create(IDynamicResourceRepository resourceRepository, ch.actifsource.core.Resource resource) {
      return new Language(resourceRepository, resource);
    }
  
  };

  public Language() {
    super(ILanguage.TYPE_ID);
  }
  
  public Language(IDynamicResourceRepository resourceRepository, ch.actifsource.core.Resource resource) {
    super(resourceRepository, resource, ILanguage.TYPE_ID);
  }

  // attributes
  
  @Override
  public java.lang.String selectName() {
    return _getSingleAttribute(java.lang.String.class, ch.actifsource.core.CorePackage.NamedResource_name);
  }
    
  public void setName(java.lang.String name) {
     _setSingleAttribute(ch.actifsource.core.CorePackage.NamedResource_name, name);
  }

  // relations
  
  @Override
  public java.util.List<? extends ch.actifsource.example.importwizard.generic.javamodel.IParameterLine> selectParameterLine() {
    return _getList(ch.actifsource.example.importwizard.generic.javamodel.IParameterLine.class, ch.actifsource.example.importwizard.generic.GenericPackage.Language_parameterLine);
  }

  public Language setParameterLine(java.util.List<? extends ch.actifsource.example.importwizard.generic.javamodel.IParameterLine> parameterLine) {
    _setList(ch.actifsource.example.importwizard.generic.GenericPackage.Language_parameterLine, parameterLine);
    return this;
  }
    
  @Override
  public ch.actifsource.core.javamodel.IClass selectTypeOf() {
    return _getSingle(ch.actifsource.core.javamodel.IClass.class, ch.actifsource.core.CorePackage.Resource_typeOf);
  }

  public Language setTypeOf(ch.actifsource.core.javamodel.IClass typeOf) {
    _setSingle(ch.actifsource.core.CorePackage.Resource_typeOf, typeOf);
    return this;
  }
    
  // accept property value visitor
  @Override
  public void accept(IPropertyValueVisitor visitor) {
    // attributes
    _acceptSingleAttribute(java.lang.String.class, ch.actifsource.core.CorePackage.NamedResource_name, visitor);
    // relations
    _acceptList(ch.actifsource.example.importwizard.generic.javamodel.IParameterLine.class, ch.actifsource.example.importwizard.generic.GenericPackage.Language_parameterLine, visitor);
    _acceptSingle(ch.actifsource.core.javamodel.IClass.class, ch.actifsource.core.CorePackage.Resource_typeOf, visitor);
  }

  // toMeRelations
  
  public static ch.actifsource.example.importwizard.generic.javamodel.ILanguage selectToMeParameterLine(ch.actifsource.example.importwizard.generic.javamodel.IParameterLine object) {
    return _getToMeSingle(object.getRepository(), ch.actifsource.example.importwizard.generic.javamodel.ILanguage.class, ch.actifsource.example.importwizard.generic.GenericPackage.Language_parameterLine, object.getResource());
  }
  
}
/* Actifsource ID=[4d723cb5-db37-11de-82b8-17be2e034a3b,45fce62e-991b-11ea-8568-8df113ebd62f,2gQ6sFHyZT8NCiQwT4EATUqP3Iw=] */
