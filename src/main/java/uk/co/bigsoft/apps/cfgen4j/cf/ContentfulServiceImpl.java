package uk.co.bigsoft.apps.cfgen4j.cf;

import com.contentful.java.cma.CMAClient;
import com.contentful.java.cma.model.CMAArray;
import com.contentful.java.cma.model.CMAContentType;

class ContentfulServiceImpl implements ContentfulService {

	private final CMAClient client;

	ContentfulServiceImpl(CMAClient client) {
		this.client = client;
	}

	@Override
	public CMAArray<CMAContentType> fetchAllContentTypes(String spaceId) {
		return client.contentTypes().fetchAll(spaceId);
	}

}
