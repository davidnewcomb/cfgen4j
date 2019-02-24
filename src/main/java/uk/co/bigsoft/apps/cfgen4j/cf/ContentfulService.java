package uk.co.bigsoft.apps.cfgen4j.cf;

import com.contentful.java.cma.model.CMAArray;
import com.contentful.java.cma.model.CMAContentType;

public interface ContentfulService {

	public CMAArray<CMAContentType> fetchAllContentTypes(String spaceId);

}
