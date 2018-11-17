package de.unihamburg.informatik.nlp4web.tutorial.tut5.util;

import java.util.Objects;
import java.util.function.Function;
import java.util.stream.StreamSupport;

public class StreamUtil {

	public static <S, T> T mapFirstNotNull(Iterable<S> ss, Function<S, T> mapFn) {
		return StreamSupport.stream(ss.spliterator(), false)
				.map(mapFn)
				.filter(Objects::nonNull)
				.findFirst()
				.orElse(null);
	}

}
