package ch.actifsource.example.importwizard;

import java.util.Arrays;
import java.util.List;
import javax.annotation.CheckForNull;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import ch.actifsource.core.CorePackage;
import ch.actifsource.core.INode;
import ch.actifsource.core.PackagedResource;
import ch.actifsource.core.Resource;
import ch.actifsource.core.Statement;
import ch.actifsource.core.SubjectPredicate;
import ch.actifsource.core.job.Select;
import ch.actifsource.core.job.Update;
import ch.actifsource.core.set.IStatementSet;
import ch.actifsource.core.util.LiteralUtil;
import ch.actifsource.ui.wizard.importer.IImportContext;
import ch.actifsource.util.ObjectUtil;
import ch.actifsource.util.character.StringUtil;
import ch.actifsource.xml.importer.util.AbstractXMLElementHandler;
import ch.actifsource.xml.importer.util.AbstractXMLImportWizardAspect;
import ch.actifsource.xml.importer.util.IXMLElementContext;
import ch.actifsource.xml.importer.util.IXMLElementHandler;

/**
 * The Actifsource XML-Import-Wizard is a simple tool to import literals 
 * (as string, integer, double, text values) for existing resources.
 * The Actifsource XML-Import-Wizard is a simple tool to import literals (as string, integer, double, text values) for existing resources.

  SGUID references the subject by its GUID while PGUID references the literal by its GUID. Please note that there can be any prefix in front of SGUID and PGUID.

  The Actifsource XML-Import-Wizard understands the following format:

  <any-tag any-attr-SGUID='GUID' any-attr-PGUID='GUID'>VALUE1</any-tag>
  <any-tag any-attr-SGUID='GUID' any-attr-PGUID='GUID'>VALUE2</any-tag>
  <any-tag any-attr-SGUID='GUID' any-attr-PGUID='GUID'>VALUE3</any-tag>

  The GUID has to be provided in the 8-4-4-4-12 format: XXXXXXXX-XXXX-XXXX-XXXX-XXXXXXXXXXXX

  Please note that the following input means the same as above when the subject cardinality is greater than 1 for any literal type except text.
  <any-tag any-attr-SGUID='GUID' any-attr-PGUID='GUID'>VALUE1
   VALUE2
  VALUE3</any-tag>
  For text values newline means new line in the text.
 */
public class XmlImportWizardAspect extends AbstractXMLImportWizardAspect {
  
  public static final String SUBJECT_ATTRIBUTE_POSTFIX = "-SGUID";
  
  public static final String PREDICATE_ATTRIBUTE_POSTFIX = "-PGUID";
  
  
  @Override
  protected IXMLElementHandler getRootElementHandler(IImportContext context) {
    return new RootXMLElementHandler();
  }
 
  private static class ElementHandler extends AbstractXMLElementHandler {
    
    private final StringBuilder             fLiteralBuilder   = new StringBuilder();
    
    @CheckForNull
    private SubjectPredicate                fSubjectPredicate;
    

    @Override
    public void setAttribute(IXMLElementContext context, String name, Attributes attributes) throws SAXException {
      fSubjectPredicate = getSubjectPredicate(context, attributes);
      super.setAttribute(context, name, attributes);
    }
    
    @Override
    public void setCharacters(IXMLElementContext context, char[] ch, int start, int length) throws SAXException {
      if (fSubjectPredicate == null) return;
      fLiteralBuilder.append(ch, start, length);
    }
    
    @Override
    public void closeElement(IXMLElementContext context, String name) throws SAXException {
      if (fSubjectPredicate == null) return;
      String textLiteral = fLiteralBuilder.toString();
      if (textLiteral == null) return;
      context.getImportContext().incrementElementCount();
      
      if (isStringLiteral(context, fSubjectPredicate.predicate())) {
        updateStringLiteral(fSubjectPredicate, textLiteral, context);
        return;
      }
      if (isTextLiteral(context, fSubjectPredicate.predicate())) {
        updateTextLiteral(fSubjectPredicate, textLiteral, context);
        return;
      }
      context.getImportContext().putError("ERROR: " + context.getElementContextInfoName() + " Supported predicate range is 'StringLiteral' or 'TextLiteral'!");
      super.closeElement(context, name);
    }
    
    @Override
    public IXMLElementHandler createSubElementHandler(IXMLElementContext context, String name) throws SAXException {
      return new ElementHandler();
    }  
    
    /***********************
     * Internal
     ***********************/
    
    private static List<String> splitLines(final String textLiteral) {
      return Arrays.asList(StringUtil.getNewLinePattern().split(textLiteral));
    }
    
    private static boolean isStringLiteral(IXMLElementContext context, INode predicate) {
      return ObjectUtil.objectEquals(CorePackage.StringLiteral, Select.objectForAttribute(context.getImportContext().getReadJobExecutor(), CorePackage.Attribute_range, predicate, null));
    }
    
    private static boolean isTextLiteral(IXMLElementContext context, INode predicate) {
      return ObjectUtil.objectEquals(CorePackage.TextLiteral, Select.objectForAttribute(context.getImportContext().getReadJobExecutor(), CorePackage.Attribute_range, predicate, null));
    }
    
    @CheckForNull
    private static Resource createResource(String guid, IXMLElementContext context) {
      try {
        Resource resource = new Resource(guid);
        if (Select.exists(context.getImportContext().getReadJobExecutor(), resource)) return resource; 
        context.getImportContext().putError("ERROR: "+ context.getElementContextInfoName() +" Guid: '"+ guid +"'not exist!");
      } catch (Exception e) {
        context.getImportContext().putError("ERROR: "+context.getElementContextInfoName()+ " Guid: '" + guid + "' " + e.getMessage()+"!");
      }
      return null;
    }
    
    @CheckForNull
    private static String getValueFromAttribute(Attributes attributes, String postFixAttributeName, IXMLElementContext context) {
      String attributeValue = null;
      for (int index = 0; index < attributes.getLength(); index++) {
        String attributeName = attributes.getQName(index);
        if (attributeName.endsWith(postFixAttributeName)) {
          if (attributeValue != null) {
            context.getImportContext().putError("ERROR: "+ context.getElementContextInfoName() + " Attribute '"+attributeName+"' already exist!");
            return null;
          }
          attributeValue = attributes.getValue(index);
        }
      }
      return attributeValue;
    }
    
    private static SubjectPredicate getSubjectPredicate(IXMLElementContext context, Attributes attributes) {
      String subjectGuid = getValueFromAttribute(attributes, SUBJECT_ATTRIBUTE_POSTFIX, context);
      if (context.getImportContext().hasErrors()) return null;
      String predicateGuid = getValueFromAttribute(attributes, PREDICATE_ATTRIBUTE_POSTFIX, context);
      if (context.getImportContext().hasErrors()) return null;
      if (subjectGuid == null && predicateGuid == null) return null;
      if (subjectGuid == null) {
        context.getImportContext().putError("ERROR: "+   context.getElementContextInfoName() +" Attribute 'any-attr-SGUID' is missing!");
        return null;
      }
      if (predicateGuid == null) {
        context.getImportContext().putError("ERROR: "+ context.getElementContextInfoName() +" Attribute 'any-attr-PGUID' is missing!");
        return null;
      }
      Resource subject = createResource(subjectGuid, context);
      if (subject == null || context.getImportContext().hasErrors()) return null;
      Resource predicate = createResource(predicateGuid, context);
      if (predicate == null || context.getImportContext().hasErrors()) return null;
      PackagedResource subjectPackage = Select.asPackagedResource(context.getImportContext().getReadJobExecutor(), subject);
      return new SubjectPredicate(subjectPackage, predicate);
    }
    
    private void updateStringLiteral(SubjectPredicate subjectPredicate, String textLiteral, IXMLElementContext context) {
      IStatementSet currentStatements = Select.statementsForAttribute(context.getImportContext().getReadJobExecutor(), subjectPredicate.predicate(), subjectPredicate.subject());
      int lineNumber = -1;
      // Update lines
      for (String line: splitLines(textLiteral)) {
        lineNumber++;
        if (lineNumber < currentStatements.size()) {
          Statement lineStatement = currentStatements.get(lineNumber);
          if (ObjectUtil.objectEquals(line, LiteralUtil.getStringValue(lineStatement.object()))) continue;
          Update.modify(context.getImportContext().getWriteJobExecutor(), currentStatements.get(lineNumber), LiteralUtil.create(line));
          context.getImportContext().incrementModifiedCount();
          continue;
        }
        Update.createStatement(context.getImportContext().getWriteJobExecutor(), subjectPredicate.getPackage(), subjectPredicate.subject(), subjectPredicate.predicate(), LiteralUtil.create(line));
        context.getImportContext().incrementCreateCount();
      }
      // Delete lines
      if (lineNumber + 1 >= currentStatements.size()) return;
      for (int index = lineNumber + 1; index < currentStatements.size(); index++) {
        Update.disposeStatement(context.getImportContext().getWriteJobExecutor(), currentStatements.get(index));
        context.getImportContext().incrementDisposeCount();
      }
    }
    
    private void updateTextLiteral(SubjectPredicate subjectPredicate, String textLiteral, IXMLElementContext context) {
      Statement currentStatement = Select.statementForAttributeOrNull(context.getImportContext().getReadJobExecutor(), subjectPredicate.predicate(), subjectPredicate.subject());
      if (currentStatement == null) {
        Update.createStatement(context.getImportContext().getWriteJobExecutor(), subjectPredicate.getPackage(), subjectPredicate.subject(), subjectPredicate.predicate(), LiteralUtil.create(textLiteral));
        context.getImportContext().incrementCreateCount();
        return;
      }
      if (ObjectUtil.objectEquals(textLiteral, LiteralUtil.getStringValue(currentStatement.object()))) return;
      Update.modify(context.getImportContext().getWriteJobExecutor(), currentStatement, LiteralUtil.create(textLiteral));
      context.getImportContext().incrementModifiedCount();
    }
  }
  
  private static class RootXMLElementHandler extends AbstractXMLElementHandler {
    
    @Override
    public IXMLElementHandler createSubElementHandler(IXMLElementContext context, String name) throws SAXException {
      return new ElementHandler();
    }
  }
}
