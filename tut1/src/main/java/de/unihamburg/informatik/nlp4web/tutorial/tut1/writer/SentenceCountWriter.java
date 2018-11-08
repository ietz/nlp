package de.unihamburg.informatik.nlp4web.tutorial.tut1.writer;

import java.util.stream.StreamSupport;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.cas.text.AnnotationIndex;
import org.apache.uima.fit.component.JCasConsumer_ImplBase;
import org.apache.uima.jcas.JCas;
import org.apache.uima.util.Level;

import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Sentence;

public class SentenceCountWriter extends JCasConsumer_ImplBase {
	
	private int sentences = 0;

	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {
		AnnotationIndex<Sentence> sentences = jcas.getAnnotationIndex(Sentence.class);
		this.sentences += StreamSupport.stream(sentences.spliterator(), false).count();
	}

	@Override
	public void collectionProcessComplete() throws AnalysisEngineProcessException {
		super.collectionProcessComplete();

		// Output: Found 28 sentences
		getContext().getLogger().log(
			Level.INFO,
			String.format("Found %d sentences\n\n", this.sentences)
		);
	}
}
