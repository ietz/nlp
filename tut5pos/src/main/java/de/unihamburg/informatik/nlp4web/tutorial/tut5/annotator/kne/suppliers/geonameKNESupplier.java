package de.unihamburg.informatik.nlp4web.tutorial.tut5.annotator.kne.suppliers;

import de.unihamburg.informatik.nlp4web.tutorial.tut5.annotator.kne.KnownNE;

import java.io.File;
import java.util.Optional;

public class geonameKNESupplier extends FileKNESupplier {
	public geonameKNESupplier(File neList) {
		super(neList);
	}

	@Override
	protected Optional<KnownNE> lineToKne(String line) {
		String[] parts = line.split("\t");

		if (parts.length < 2) {
			return Optional.empty();
		}

		return Optional.of(new KnownNE(KnownNE.Type.LOC, parts[1]));
	}
}
