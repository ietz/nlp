package de.unihamburg.informatik.nlp4web.tutorial.tut5.annotator.kne.suppliers;

import de.unihamburg.informatik.nlp4web.tutorial.tut5.annotator.kne.KnownNE;

import java.io.File;
import java.util.Optional;

public class jrcKNESupplier extends FileKNESupplier {
	public jrcKNESupplier(File neList) {
		super(neList);
	}

	@Override
	protected Optional<KnownNE> lineToKne(String line) {
		String[] parts = line.split("\t");

		if (parts.length != 4) {
			return Optional.empty();
		}

		return getType(parts[1])
				.map(type -> new KnownNE(type, getEntityName(parts[3])));
	}

	private Optional<KnownNE.Type> getType(String s) {
		switch (s) {
			case "P":
				return Optional.of(KnownNE.Type.PER);
			case "O":
				return Optional.of(KnownNE.Type.ORG);
			default:
				return Optional.empty();
		}
	}

	private String getEntityName(String s) {
		return s.replace('+', ' ');
	}
}
