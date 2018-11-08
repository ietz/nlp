package de.unihamburg.informatik.nlp4web.tutorial.writer;

import org.apache.uima.UimaContext;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasConsumer_ImplBase;
import org.apache.uima.fit.descriptor.ConfigurationParameter;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceConfigurationException;
import org.apache.uima.resource.ResourceInitializationException;
import org.apache.uima.util.Level;

import de.tudarmstadt.ukp.dkpro.core.api.parameter.ComponentParameters;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;

public class WebWriter extends JCasConsumer_ImplBase {
	
    public static final String PIPELINE_IDENTIFIER = "PIPELINE_IDENTIFIER";
	
	int sentenceCount = 0;
	

	@Override
	public void process(JCas aJCas) throws AnalysisEngineProcessException {
		sentenceCount += aJCas.getAnnotationIndex(Sentence.class).size();
	}

	@Override
	public void collectionProcessComplete() throws AnalysisEngineProcessException {
		super.collectionProcessComplete();
		
		String pipelineIdentifier = (String) this.getContext().getConfigParameterValue(PIPELINE_IDENTIFIER);
		
		String message = String.format("Found %d sentences for pipeline %s\n", sentenceCount, pipelineIdentifier);
		getContext().getLogger().log(Level.INFO, message);
	}

}
