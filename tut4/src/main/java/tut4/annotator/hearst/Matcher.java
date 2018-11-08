package tut4.annotator.hearst;

import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;

import java.util.List;
import java.util.stream.Collectors;

public class Matcher {
	private List<Match> matches;

	public Matcher(JCas jCas, String typeOf) {
		this.matches = JCasUtil.select(jCas, Token.class).stream()
				.map(start -> new Match(jCas, start, typeOf))
				.collect(Collectors.toList());
	}

	public Matcher select(Match.Type type) {
		this.applyToMatches(m -> m.select(type));
		return this;
	}

	public Matcher read(String... values) {
		this.applyToMatches(m -> m.read(values));
		return this;
	}

	public Matcher skipPunctuation() {
		this.applyToMatches(Match::skipPunctuation);
		return this;
	}

	public List<Match> getMatches() {
		return this.matches;
	}

	private void applyToMatches(ThrowingConsumer<Match> consumer) {
		this.matches = this.matches.stream()
				.filter(m -> {
					try {
						consumer.accept(m);
						return true;
					} catch (Exception e) {
						return false;
					}
				})
				.distinct()
				.collect(Collectors.toList());
	}

	@FunctionalInterface
	private interface ThrowingConsumer<T> {
		void accept(T t) throws Exception;
	}
}
