package de.unihamburg.informatik.nlp4web.tutorial.tut5.annotator.kne.suppliers;

import de.unihamburg.informatik.nlp4web.tutorial.tut5.annotator.kne.KnownNE;
import org.apache.uima.UIMAFramework;
import org.apache.uima.util.Level;
import org.apache.uima.util.Logger;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class KNEListSupplier implements Supplier<Stream<KnownNE>> {

	private final Logger logger = UIMAFramework.getLogger(KNEListSupplier.class);
	private final File neList;

	public KNEListSupplier(File neList) {
		this.neList = neList;
	}

	@Override
	public Stream<KnownNE> get() {
		try {
			return Files.lines(Paths.get(neList.toURI()))
					.map(this::lineToKne)
					.filter(Optional::isPresent)
					.map(Optional::get);
		} catch (IOException e) {
			e.printStackTrace();
			return Stream.empty();
		}
	}

	private Optional<KnownNE> lineToKne(String line) {
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
