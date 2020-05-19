package ch.actifsource.example.importwizard;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import ch.actifsource.core.job.IWriteJobExecutor;
import ch.actifsource.ui.wizard.importcsv.CSVParser;
import ch.actifsource.ui.wizard.importcsv.CSVParser.Row;
import ch.actifsource.ui.wizard.importer.IImportContext;
import ch.actifsource.ui.wizard.importer.aspect.IGenericImportWizardAspect;

public class CsvImporter implements IGenericImportWizardAspect {
  
  @Override
  public void importFile(IImportContext context, InputStream imputStream) {
	IWriteJobExecutor writeJobExecutor = context.getWriteJobExecutor();
    CSVParser parser = new CSVParser(';', '"');
    try {
      List<Row> rows = parser.read(imputStream);
      rows.remove(0);
      for (Row row: rows) {
        // Check row size
        if (row.getValues().size() == 4) {
          context.putError("Error invalide row size: " + row.getValues().size());
          continue;
        }
        
        // Update info dialog import wizard
        context.incrementElementCount();
        
        String row_1 = row.getValues().get(0);
        String row_2 = row.getValues().get(1);
        String row_3 = row.getValues().get(2);
        String row_4 = row.getValues().get(3);
        
        
        // TODO Implementation use 'writeJobExecutor'
        
      }
    } catch (IOException e) {
      context.putError(e.getMessage());
    }
  }
  
}
