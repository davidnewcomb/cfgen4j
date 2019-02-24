package uk.co.bigsoft.apps.cfgen4j.gen.ref;

import java.util.Collection;
import java.util.Set;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.contentful.java.cma.model.CMAArray;
import com.contentful.java.cma.model.CMAContentType;

import uk.co.bigsoft.apps.cfgen4j.gen.NameConverter;

public class Refs {

	private static Logger L = LoggerFactory.getLogger(Refs.class);

	private NameConverter nameConverter = new NameConverter();
	private TreeMap<String, JavaClassInfo> map = new TreeMap<>();
	private final String packageName;

	public Refs(String packageName) {
		this.packageName = packageName;
	}

	public void addForwardReferences(CMAArray<CMAContentType> contentTypes) {

		L.info("Found types:");

		for (CMAContentType ct : contentTypes.getItems()) {

			JavaClassInfo info = new JavaClassInfo();
			info.setId(ct.getId());
			info.setClassName(nameConverter.generateJavaClassName(ct.getId()));
			L.info("    id={} -> class={}", info.getId(), info.getClassName());

			map.put(ct.getId(), info);
		}

	}

	public JavaClassInfo get(String id) {
		return map.get(id);
	}

	public String getPackageName() {
		return packageName;
	}

	public Collection<JavaClassInfo> getInfos() {
		return map.values();
	}

	public Set<String> getIds() {
		return map.keySet();
	}
}
