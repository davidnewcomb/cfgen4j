package uk.co.bigsoft.apps.cfgen4j.cf;

import com.contentful.java.cma.CMAClient;
import com.contentful.java.cma.CMAClient.Builder;

import uk.co.bigsoft.apps.cfgen4j.cfg.Cfg;

public class ContentfulServiceFactory {

	public ContentfulService create(Cfg cfg) {

		Builder clientBuilder = new CMAClient //
				.Builder() //
						.setApplication("cfgen4j", cfg.getVersion()) //
						.setAccessToken(cfg.getAccessToken()) //
						.setCoreEndpoint(cfg.getCoreEndpoint()) //
						.setUploadEndpoint(cfg.getUploadEndpoint());

		if (cfg.getCacheFile() != null) {
			addCaching(cfg.getCacheFile(), clientBuilder);
		}
		CMAClient client = clientBuilder.build();

		return new ContentfulServiceImpl(client);
	}

	private void addCaching(String file, Builder clientBuilder) {

		ContentfulCacheInterceptor cacher = new ContentfulCacheInterceptor(file);
		okhttp3.OkHttpClient.Builder callFactory = clientBuilder //
				.defaultCoreCallFactoryBuilder() //
				.addInterceptor(cacher);

		clientBuilder.setCoreCallFactory(callFactory.build());
	}

}
