package de.unihamburg.informatik.nlp4web.tutorial.tut5.annotator.kne.suppliers;

import de.unihamburg.informatik.nlp4web.tutorial.tut5.annotator.kne.KnownNE;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class KNEListSupplier implements Supplier<Stream<KnownNE>> {

	private final File neList;

	public KNEListSupplier(File neList) {
		this.neList = neList;
	}


	@Override
	public Stream<KnownNE> get() {
		try {
			return Files.lines(Paths.get(neList.toURI()))
					.map(KNEListSupplier::lineToKne);
		} catch (IOException e) {
			e.printStackTrace();
			return Stream.empty();
		}
	}

	private static KnownNE lineToKne(String line) {
		String[] parts = line.split(" ", 2);
		return new KnownNE(KnownNE.Type.byTag(parts[0]), parts[1]);
	}

}
