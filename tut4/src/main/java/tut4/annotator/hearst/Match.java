package tut4.annotator.hearst;

import de.tudarmstadt.ukp.dkpro.core.api.lexmorph.type.pos.POS_PUNCT;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import de.tudarmstadt.ukp.dkpro.core.api.syntax.type.constituent.NP;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import tut4.Util;
import tut4.type.Entity;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Match {

	public enum Type {
		HYPERONYM,
		HYPONYM,
	}

	private final JCas jCas;
	public final String typeOf;
	private Token ptr;
	private List<Token> history = new ArrayList<>();
	private NP hyperonymNP;
	private NP hyponymNP;


	public Match(JCas jCas, Token start, String typeOf) {
		this.jCas = jCas;
		this.ptr = start;
		this.typeOf = typeOf;
	}

	public void read(String[] values) throws MatchException {
		for (String value : values) {
			if (!ptr.getCoveredText().equals(value)) {
				throw new MatchException();
			}

			this.history.add(ptr);
			this.advancePointer();
		}
	}

	public void select(Type type) {
		Token excluded = history.isEmpty() ? null : history.get(history.size()-1);
		// Select NP at next token
		NP selected = this.selectContaining(ptr, excluded);

		switch (type) {
			case HYPONYM:
				hyponymNP = selected;
				break;
			case HYPERONYM:
				hyperonymNP = selected;
				break;
		}

		// Skip ptr to first token after selected NP
		ptr = JCasUtil.selectFollowing(Token.class, selected, 1).get(0);
	}

	public void skipPunctuation() {
		while (!JCasUtil.selectCovered(POS_PUNCT.class, ptr).isEmpty()) {
			this.advancePointer();
		}
	}

	private void advancePointer() {
		ptr = JCasUtil.selectFollowing(Token.class, ptr, 1).get(0);
	}

	/**
	 * Select the largest NP covering token that does not contain the
	 * @param token The token that should be covered
	 * @param excluding The token that should not be covered
	 */
	private NP selectContaining(Token token, Token excluding) {
		List<NP> covering = JCasUtil.selectCovering(NP.class, token);
		NP mostSpecific = covering.get(covering.size()-1);
		if (excluding == null) {
			return mostSpecific;
		} else {
			return covering.stream()
					.filter(np -> !Util.covers(np, excluding))
					.findFirst()
					.orElse(covering.get(covering.size() - 1));
		}
	}

	public List<Entity> getHyperonyms() {
		return EntityUtil.getCoveredEntities(jCas, hyperonymNP, history);
	}

	public List<Entity> getHyponyms() {
		return EntityUtil.getCoveredEntities(jCas, hyponymNP, history);
	}


	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Match) {
			Match that = (Match) obj;
			return Objects.equals(this.ptr, that.ptr)
					&& Objects.equals(this.history, that.history)
					&& Objects.equals(this.hyperonymNP, that.hyperonymNP)
					&& Objects.equals(this.hyponymNP, that.hyponymNP);
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return Objects.hash(ptr, history, hyperonymNP, hyponymNP);
	}

	private class MatchException extends Exception {
	}
}
