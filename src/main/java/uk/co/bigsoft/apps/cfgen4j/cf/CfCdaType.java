package uk.co.bigsoft.apps.cfgen4j.cf;

import com.squareup.javapoet.ClassName;

public class CfCdaType {

	public static ClassName CDAEntry = ClassName.get("com.contentful.java.cda", "CDAEntry");
	public static ClassName CDAAsset = ClassName.get("com.contentful.java.cda", "CDAAsset");
	public static ClassName ImageOption = ClassName.get("com.contentful.java.cda.image", "ImageOption");

}
