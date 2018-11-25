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

public abstract class FileKNESupplier implements Supplier<Stream<KnownNE>> {

	private final File neList;

	public FileKNESupplier(File neList) {
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

	protected abstract Optional<KnownNE> lineToKne(String line);
}
