package tut4.annotator.hearst;

import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Lemma;
import de.tudarmstadt.ukp.dkpro.core.api.segmentation.type.Token;
import de.tudarmstadt.ukp.dkpro.core.api.syntax.type.constituent.NP;
import org.apache.uima.fit.util.JCasUtil;
import org.apache.uima.jcas.JCas;
import org.apache.uima.jcas.tcas.Annotation;
import tut4.Util;
import tut4.type.Entity;

import java.util.ArrayList;
import java.util.List;

public class EntityUtil {

	public static List<Entity> getCoveredEntities(JCas jCas, NP covering, List<Token> excluded) {
		List<Entity> result = new ArrayList<>();
		for (NP np : Util.selectLeaves(NP.class, covering)) {
			List<Token> entityTokens = new ArrayList<>(JCasUtil.selectCovered(Token.class, np));
			entityTokens.removeAll(excluded);

			Entity entity = new Entity(jCas);
			entity.setBegin(entityTokens.stream().mapToInt(Annotation::getBegin).min().orElse(0));
			entity.setEnd(entityTokens.stream().mapToInt(Annotation::getEnd).max().orElse(0));
			setName(entity);

			result.add(entity);
		}

		return result;
	}

	/**
	 * Builds the name from the contained lemmata, and keeps the document text between them
	 * i.e. "stringed instruments" -> "string" + " " + "instrument"
	 */
	private static void setName(Entity entity) {
		String docText = entity.getCAS().getDocumentText();
		StringBuilder sb = new StringBuilder();
		List<Lemma> lemmata = JCasUtil.selectCovered(Lemma.class, entity);
		for (int i = 0; i < lemmata.size(); i++) {
			if (i > 0) {
				sb.append(docText, lemmata.get(i-1).getEnd(), lemmata.get(i).getBegin());
			}

			sb.append(lemmata.get(i).getValue());
		}

		entity.setName(sb.toString());
	}
}
