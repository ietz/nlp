package de.unihamburg.informatik.nlp4web.tutorial.tut5.annotator.kne.suppliers;

import de.unihamburg.informatik.nlp4web.tutorial.tut5.annotator.kne.KnownNE;
import org.apache.uima.UIMAFramework;
import org.apache.uima.util.Level;
import org.apache.uima.util.Logger;

import java.io.File;
import java.util.Optional;

public class KNEListSupplier extends FileKNESupplier {

	private Logger logger = UIMAFramework.getLogger(KNEListSupplier.class);

	public KNEListSupplier(File neList) {
		super(neList);
	}

	@Override
	protected Optional<KnownNE> lineToKne(String line) {
		String[] parts = line.split(" ", 2);

		if (parts.length != 2) {
			return Optional.empty();
		}

		return KnownNE.Type.byTag(parts[0])
				.map(t -> new KnownNE(t, parts[1]))
				.or(() -> {
					logger.log(Level.WARNING, String.format("Unknown NE Type \"%s\"", parts[0]));
					return Optional.empty();
				});

	}
}
