package de.unihamburg.informatik.nlp4web.tutorial.tut3.writer;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasConsumer_ImplBase;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.util.Level;

import de.unihamburg.informatik.nlp4web.tutorial.tut3.type.BISentence;
import de.unihamburg.informatik.nlp4web.tutorial.tut3.type.BIToken;

public class TokensPerSentenceWriter extends JCasConsumer_ImplBase {

	@Override
	public void process(JCas jcas) throws AnalysisEngineProcessException {
		for (BISentence sentence : JCasUtil.select(jcas, BISentence.class)) {
			int tokenCount = JCasUtil.selectCovered(BIToken.class, sentence).size();
			String message = String.format("Sentence contains %d tokens:\n%s\n", tokenCount, sentence.getCoveredText());
			getContext().getLogger().log(Level.INFO, message);
		}
	}

}
