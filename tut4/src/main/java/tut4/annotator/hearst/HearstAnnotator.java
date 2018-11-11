package tut4.annotator.hearst;

import org.apache.uima.analysis_engine.AnalysisEngineProcessException;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.jcas.JCas;

import tut4.type.Entity;
import tut4.type.HearstAnnotation;

public class HearstAnnotator extends JCasAnnotator_ImplBase {

	@Override
	public void process(JCas aJCas) throws AnalysisEngineProcessException {
		MultiMatcher m = new MultiMatcher(aJCas);
		m.forType("NP_such_as_NP").select(Match.Type.HYPERONYM).skipPunctuation().read("such", "as").select(Match.Type.HYPONYM);
		m.forType("such_NP_as_NP").read("such").select(Match.Type.HYPERONYM).read("as").select(Match.Type.HYPONYM);
		m.forType("and/or_other").select(Match.Type.HYPONYM).skipPunctuation().read("and", "other").select(Match.Type.HYPERONYM);
		m.forType("and/or_other").select(Match.Type.HYPONYM).skipPunctuation().read("or", "other").select(Match.Type.HYPERONYM);
		m.forType("NP_including_NP").select(Match.Type.HYPERONYM).read(",", "including").select(Match.Type.HYPONYM);
		m.forType("NP_especiall_NP").select(Match.Type.HYPERONYM).read(",", "especially").select(Match.Type.HYPONYM);

		for (Match match : m.getAllMatches()) {
			annotateHyponyms(aJCas, match);
		}
	}
	
	private void annotateHyponyms(JCas jCas, Match match) {
		for (Entity hyperonym : match.getHyperonyms()) {
			for (Entity hyponym : match.getHyponyms()) {
				HearstAnnotation annotation = new HearstAnnotation(jCas);
				annotation.setBegin(hyponym.getBegin());
				annotation.setEnd(hyponym.getEnd());
				annotation.setTypeOf(match.typeOf);
				annotation.setHyperonym(hyperonym.getName());
				annotation.setHyponym(hyponym.getName());
				annotation.addToIndexes();
			}
		}
	}
}
