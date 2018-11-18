package de.unihamburg.informatik.nlp4web.tutorial.tut5.annotator.kne;

public class KnownNE {
	public enum Type {
		LOC("LOC"),
		MISC("MISC"),
		ORG("ORG"),
		PER("PER");

		public final String tag;
		Type(String tag) {
			this.tag = tag;
		}

		public static Type byTag(String tag) {
			for (Type type : Type.values()) {
				if (type.tag.equals(tag)) {
					return type;
				}
			}
			return null;
		}
	}

	private final Type type;
	private final String name;

	public KnownNE(Type type, String name) {
		this.type = type;
		this.name = name;
	}


	public Type getType() {
		return type;
	}

	public String getName() {
		return name;
	}
}
