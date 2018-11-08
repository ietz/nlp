package tut4;

import org.apache.uima.cas.text.AnnotationFS;
import org.apache.uima.fit.util.JCasUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Util {

	public static boolean covers(AnnotationFS covering, AnnotationFS covered) {
		return covering.getBegin() <= covered.getBegin() && covering.getEnd() >= covered.getEnd();
	}

	public static <S extends T, T extends AnnotationFS> List<T> selectLeaves(Class<T> type, S root) {
		List<T> covered = new ArrayList<>(JCasUtil.selectCovered(type, root));
		covered.add(root);

		// Filter covered so that only those remain, that do not cover any further annotations
		return covered.stream()
				.filter(a -> covered.stream().noneMatch(b -> a != b && Util.covers(a, b)))
				.collect(Collectors.toList());
	}

}
