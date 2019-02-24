package uk.co.bigsoft.apps.cfgen4j.cf;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class ContentfulCacheInterceptor implements Interceptor {

	private File cacheFile;

	public ContentfulCacheInterceptor(String cacheFile) {
		this.cacheFile = new File(cacheFile);
	}

	@Override
	public Response intercept(Chain chain) throws IOException {

		if (cacheFile.exists()) {
			String content = new String(Files.readAllBytes(cacheFile.toPath()));
			return responseFactory(chain.request(), content);
		}

		Response r = chain.proceed(chain.request());
		String body = save(r);
		return responseFactory(chain.request(), body);
	}

	private Response responseFactory(Request request, String bodyText) {
		ResponseBody body = ResponseBody.create(MediaType.parse("application/json"), bodyText);
		return new Response.Builder().request(request) //
				.protocol(Protocol.HTTP_1_1) //
				.code(200) //
				.message("Body cached") //
				.body(body) //
				.build();
	}

	private String save(Response response) throws IOException {
		String body = response.body().string();
		if (!cacheFile.getParentFile().isDirectory()) {
			cacheFile.getParentFile().mkdirs();
		}
		FileWriter fw = new FileWriter(cacheFile);
		fw.write(body);
		fw.close();
		return body;
	}
}
