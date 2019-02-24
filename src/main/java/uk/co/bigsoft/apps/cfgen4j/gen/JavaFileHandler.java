package uk.co.bigsoft.apps.cfgen4j.gen;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.squareup.javapoet.JavaFile;

public class JavaFileHandler {

	private static Logger L = LoggerFactory.getLogger(JavaFileHandler.class);

	public void write(JavaFile javaFile, String path) throws IOException {

		if (L.isInfoEnabled()) {
			log(javaFile, path);
		}
		javaFile.writeTo(new File(path));
	}

	private void log(JavaFile javaFile, String path) {
		StringBuilder sb = new StringBuilder();
		sb.append(path);
		sb.append(File.separator);
		sb.append(javaFile.packageName.replace(".", File.separator));
		sb.append(File.separator);
		sb.append(javaFile.typeSpec.name);
		sb.append(".java");
		L.info("Writting: " + sb.toString());
	}

}
