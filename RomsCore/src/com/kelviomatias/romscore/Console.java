package com.kelviomatias.romscore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

/**
 * 
 * @author kelvio
 */
@Root(name = "console")
public class Console implements Serializable {

	private Map<String, String> extensionMap = new HashMap<String, String>() {
		{
			this.put("Atari Jaguar", ".zip");
			this.put("Sega Saturn", ".rar");
			this.put("Sega CD", ".rar");
			this.put("Playstation 2", ".7z");
			this.put("Atari 7800", ".zip");
			this.put("Sega Dreamcast", ".rar");
			this.put("Sega Master System", ".zip");
			this.put("Comodore 64", ".zip");
			this.put("Game Boy Advance", ".zip");
			this.put("Super Nintendo", ".zip");
			this.put("Game Boy Color", ".zip");
			this.put("Nintendo DS", ".zip");
			this.put("Sega Game Gear", ".zip");
			this.put("Playstation", ".7z");
			this.put("Neo Geo CD", ".rar");
			this.put("Nintendo 64", ".zip");
			this.put("Neo Geo Pocket", ".zip");
			this.put("Atari 5200", ".zip");
			this.put("MAME", ".zip");
			this.put("Atari Lynx", ".zip");
			this.put("Sega Model 2", ".zip");
			this.put("Atari 2600", ".zip");
			this.put("CPS1", ".zip");
			this.put("CPS2", ".zip");
			this.put("Nintendo", ".zip");
			this.put("Sega Genesis", ".zip");
			this.put("Nintendo Game Cube", ".7z");
			this.put("Neo Geo", ".zip");
		}
	};

	@ElementList(required = false)
	private List<Rom> roms = new ArrayList<Rom>();

	@Attribute
	private String name;

	@Attribute
	private String emulatorName;

	@Attribute
	private String emulatorUrl;

	private String emulatorPackage;

	public List<Rom> getRoms() {
		return roms;
	}

	public void setRoms(List<Rom> roms) {
		this.roms = roms;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmulatorName() {
		return emulatorName;
	}

	public void setEmulatorName(String emulatorName) {
		this.emulatorName = emulatorName;
	}

	public String getEmulatorPackage() {

		if (this.emulatorPackage == null) {
			if (this.emulatorUrl != null) {
				// https://play.google.com/store/apps/details?id=com.kelviomatias.romspremium
				this.emulatorPackage = this.emulatorUrl
						.substring(this.emulatorUrl.indexOf("=") + 1);
			}
		}

		return emulatorPackage;
	}

	public String getEmulatorUrl() {
		return emulatorUrl;
	}

	public void setEmulatorUrl(String emulatorUrl) {
		this.emulatorUrl = emulatorUrl;

	}

	public String getDefaultExtension() {
		return this.extensionMap.get(this.getName());
	}

	public boolean hasEmulatorRegistered() {
		return this.emulatorName != null && !this.emulatorName.isEmpty();
	}

}
