package de.unihamburg.informatik.nlp4web.tutorial.tut5.ner;

import java.util.List;

import de.unihamburg.informatik.nlp4web.tutorial.tut5.evaluation.Scorekeeper;
import de.unihamburg.informatik.nlp4web.tutorial.tut5.util.StreamUtil;
import org.apache.uima.UIMAFramework;
import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.jcas.JCas;
import org.apache.uima.util.Level;
import org.apache.uima.util.Logger;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.fit.util.JCasUtil;

import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import de.unihamburg.informatik.nlp4web.tutorial.tut5.type.NEIOBAnnotation;

/**
 * This class analaysis the classification result and prints the precision and
 * recall for each pos tag and prints the average precision and recall for all
 * pos tags and the accuracy
 *
 *
 */
public class ScoreNER extends JCasAnnotator_ImplBase
{
	private Logger logger = UIMAFramework.getLogger(ScoreNER.class);

	@Override
	public void process(JCas jCas) throws AnalysisEngineProcessException {
		logger.log(Level.INFO, "Start analyzing results");

		Scorekeeper<String> scorekeeper = new Scorekeeper<>();

		for (Token t : JCasUtil.select(jCas, Token.class)) {
			List<NEIOBAnnotation> neiobs = JCasUtil.selectCovered(NEIOBAnnotation.class, t);
			String gold = StreamUtil.mapFirstNotNull(neiobs, NEIOBAnnotation::getGoldValue);
			String predicted = StreamUtil.mapFirstNotNull(neiobs, NEIOBAnnotation::getPredictValue);

			scorekeeper.track(gold, predicted);
		}

		scorekeeper.logResults(logger, "NER-Tag");
	}
}
