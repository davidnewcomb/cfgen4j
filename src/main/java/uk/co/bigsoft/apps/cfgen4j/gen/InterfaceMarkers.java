package uk.co.bigsoft.apps.cfgen4j.gen;

import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;

public class InterfaceMarkers {

	HashMap<String, TreeSet<String>> interfaceToIds = new HashMap<>();
	HashMap<String, TreeSet<String>> idTointerfaces = new HashMap<>();

	void add(String interfaceName, TreeSet<String> cfIds) {
		interfaceToIds.put(interfaceName, cfIds);

		for (String id : cfIds) {
			TreeSet<String> interfaces = idTointerfaces.get(id);
			if (interfaces == null) {
				interfaces = new TreeSet<String>();
				idTointerfaces.put(id, interfaces);
			}
			interfaces.add(interfaceName);
		}
	}

	Set<String> getInterfaceNames() {
		return interfaceToIds.keySet();
	}

	HashMap<String, TreeSet<String>> getClassInterfaces() {
		return idTointerfaces;
	}

}
