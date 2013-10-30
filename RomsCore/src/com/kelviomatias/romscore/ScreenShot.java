package com.kelviomatias.romscore;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root(name = "rom")
public class ScreenShot {

	@Attribute
	private String url;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
