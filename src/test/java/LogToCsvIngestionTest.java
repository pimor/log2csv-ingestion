import builder.LogToCsvIngestion;
import com.holdenkarau.spark.testing.SharedJavaSparkContext;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.ApiLogParser;

import java.io.File;
import java.io.Serializable;

/**
 * Created on 20/09/2017.
 */
public class LogToCsvIngestionTest extends SharedJavaSparkContext implements Serializable {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogToCsvIngestionTest.class);
    private static final String FILE_NAME = "apilog.sample";
    private static final String OUTPUT_DIR = "target";
    private static final String TABLE_NAME = "apilog";
    private static final String H2_CONNECTION = "jdbc:h2:mem:test";
    File outputCsvDir = null;


    /**
     * Ensure that the output dir doesn't exist. Otherwise it'll fail the Spark writing.
     * @throws Exception
     */
    @Before
    public void cleanUp() throws Exception {
        outputCsvDir = File.createTempFile("output-data-", "");
        outputCsvDir.delete();
    }

    /**
     * Test the ingestion pipeline: transform input file into a CSV file and load the CSV into the H2 database
     * @throws Exception
     */
    @Test
    public void IngestLogLineTest() throws Exception {
        LogToCsvIngestion logToCsv = new LogToCsvIngestion();

        // read the sample api log file
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(FILE_NAME).getFile());

        // transform
        logToCsv.transformLogFileIntoCsv(jsc(), file.getCanonicalPath(), outputCsvDir);

        // load
        logToCsv.loadCsvIntoH2(outputCsvDir, H2_CONNECTION , "", "", TABLE_NAME, ApiLogParser.LOG_FIELDS_SCHEMA);
    }
}
