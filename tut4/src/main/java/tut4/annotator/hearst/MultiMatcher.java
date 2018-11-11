package tut4.annotator.hearst;

import org.apache.uima.jcas.JCas;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MultiMatcher {

	private List<Matcher> matchers = new ArrayList<>();
	private final JCas jCas;

	public MultiMatcher(JCas jCas) {
		this.jCas = jCas;
	}

	// creates a matcher for a certain type of Hearst Pattern
	public Matcher forType(String typeOf) {
		Matcher matcher = new Matcher(jCas, typeOf);
		this.matchers.add(matcher);
		return matcher;
	}

	// returns all the matches
	public List<Match> getAllMatches() {
		return matchers.stream()
				.flatMap(m -> m.getMatches().stream())
				.distinct()
				.collect(Collectors.toList());
	}

}
