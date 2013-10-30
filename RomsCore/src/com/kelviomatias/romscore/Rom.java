package com.kelviomatias.romscore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

/**
 * 
 * @author kelvio
 */
@Root(name = "rom")
public class Rom implements Serializable {

	@Attribute
	private String id;

	@Attribute
	private String name;

	@Attribute(required = false)
	private String description;

	@ElementList(required = false)
	private List<ScreenShot> screenShots = new ArrayList<ScreenShot>();

	@Attribute(required = false)
	private String fileName;

	@Attribute(required = false)
	private int size;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<ScreenShot> getScreenShots() {
		return screenShots;
	}

	public void setScreenShots(List<ScreenShot> screenShots) {
		this.screenShots = screenShots;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

}
