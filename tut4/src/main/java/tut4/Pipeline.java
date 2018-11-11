package tut4;


import static org.apache.uima.fit.factory.AnalysisEngineFactory.createEngine;
import static org.apache.uima.fit.factory.CollectionReaderFactory.createReader;

import java.io.IOException;

import org.apache.uima.UIMAException;
import org.apache.uima.analysis_engine.AnalysisEngine;
import org.apache.uima.collection.CollectionReader;
import org.apache.uima.fit.pipeline.SimplePipeline;

import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordLemmatizer;
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordParser;
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordPosTagger;
import de.tudarmstadt.ukp.dkpro.core.stanfordnlp.StanfordSegmenter;
import tut4.annotator.hearst.HearstAnnotator;
import tut4.reader.TextReader;
import tut4.writer.ChunkWriter;
import tut4.writer.HeastWriter;
import tut4.writer.LemmaWriter;
import tut4.writer.POSWriter;


public class Pipeline
{

    public static void main(String[] args)
        throws UIMAException, IOException
    {

		CollectionReader reader = createReader(TextReader.class,
				TextReader.PARAM_DIRECTORY_NAME, "src/main/resources/documents");

		AnalysisEngine seg = createEngine(StanfordSegmenter.class);
        
        AnalysisEngine lem = createEngine(StanfordLemmatizer.class);
        
        AnalysisEngine pos = createEngine(StanfordPosTagger.class);
        // StanfordParser is kind of heavy. You might want to increase the heap space.
        // e.g. add -Xmx1500m to the VM arguments in the Run Configurations arguments tab.
        // Also better start with not too much text to process.
    	AnalysisEngine parse = createEngine(StanfordParser.class);
    	
    	AnalysisEngine hearst = createEngine(HearstAnnotator.class);
    	
    	AnalysisEngine posWriter = createEngine(POSWriter.class);
    	AnalysisEngine lemmaWriter = createEngine(LemmaWriter.class);
    	AnalysisEngine chunkWriter = createEngine(ChunkWriter.class);
		AnalysisEngine heastWriter = createEngine(HeastWriter.class);
    	
        SimplePipeline.runPipeline(
        		reader,
        		seg,
                pos,
        		lem,
        		parse,
        		hearst,
        		// posWriter,
        		// lemmaWriter,
        		// chunkWriter,
				heastWriter
        );
    }
}
