package de.unihamburg.informatik.nlp4web.tutorial.tut5.annotator.kne;

import de.unihamburg.informatik.nlp4web.tutorial.tut5.annotator.kne.suppliers.KNEListSupplier;
import de.unihamburg.informatik.nlp4web.tutorial.tut5.annotator.kne.suppliers.geonameKNESupplier;
import de.unihamburg.informatik.nlp4web.tutorial.tut5.annotator.kne.suppliers.jrcKNESupplier;
import de.unihamburg.informatik.nlp4web.tutorial.tut5.type.KnownNEAnnotation;
import org.ahocorasick.trie.Trie;
import org.apache.uima.UimaContext;
import org.apache.uima.fit.component.JCasAnnotator_ImplBase;
import org.apache.uima.jcas.JCas;
import org.apache.uima.resource.ResourceInitializationException;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class KnownNEAnnotator extends JCasAnnotator_ImplBase {

	private Map<KnownNE.Type, Trie> tries;

	@Override
	public void initialize(UimaContext context) throws ResourceInitializationException {
		super.initialize(context);

		Map<KnownNE.Type, Trie.TrieBuilder> builders = new HashMap<>();

		Stream.of(
				//new KNEListSupplier(new File("src/main/resources/ner/deu.list")),
				//new KNEListSupplier(new File("src/main/resources/ner/deu.list")),
				new jrcKNESupplier(new File("src/main/resources/ner/entities"))
				//new geonameKNESupplier(new File("src/main/resources/ner/cities500.txt"))
		)
				.flatMap(Supplier::get)
				.forEach(kne -> builders.computeIfAbsent(kne.getType(), t -> Trie.builder()).addKeyword(kne.getName()));

		tries = new HashMap<>();
		builders.forEach((t, b) -> tries.put(t, b.build()));
	}

	@Override
	public void process(JCas jCas) {
		String text = jCas.getDocumentText();

		tries.forEach((type, trie) -> trie.parseText(text, emit -> {
			KnownNEAnnotation annotation = new KnownNEAnnotation(jCas, emit.getStart(), emit.getEnd()+1);
			annotation.setTypeOf(type.tag);
			annotation.addToIndexes();
			return true;
		}));
	}
}
