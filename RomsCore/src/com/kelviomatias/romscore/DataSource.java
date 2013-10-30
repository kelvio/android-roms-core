package com.kelviomatias.romscore;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import org.simpleframework.xml.core.Persister;

import android.content.res.Resources.NotFoundException;

public class DataSource {

	private static Data data = new Data();
	
	public static final String BASE_URL = "http://romsserverkelviomatias.appspot.com/";
	public static final String CONSOLE_INDEX_URL = BASE_URL + "index.xml";
	public static final String ATARI_2600_INDEX_URL = BASE_URL + "atari_2600.xml";
	public static final String ATARI_5200_INDEX_URL = BASE_URL + "atari_5200.xml";
	public static final String ATARI_JAGUAR_INDEX_URL = BASE_URL + "atari_jaguar.xml";
	public static final String ATARI_LYNX_INDEX_URL = BASE_URL + "atari_lynx.xml";
	public static final String CPS1_INDEX_URL = BASE_URL + "cps1.xml";
	public static final String CPS2_INDEX_URL = BASE_URL + "cps2.xml";
	public static final String COMODORE_64_INDEX_URL = BASE_URL + "comodore_64.xml";
	public static final String GAME_BOY_ADVANCE_INDEX_URL = BASE_URL + "gba.xml";
	public static final String GAME_BOY_COLOR_INDEX_URL = BASE_URL + "gbc.xml";
	public static final String MAME_INDEX_URL = BASE_URL + "mame.xml";
	public static final String NAMCO_SYSTEM_22_INDEX_URL = BASE_URL + "namco_system_22.xml";
	public static final String NEO_GEO_INDEX_URL = BASE_URL + "neo_geo.xml";
	public static final String NEO_GEO_CD_INDEX_URL = BASE_URL + "neo_geo_cd.xml";
	public static final String NEO_GEO_POCKET_INDEX_URL = BASE_URL + "neo_geo_pocket.xml";
	public static final String NINTENDO_INDEX_URL = BASE_URL + "nes.xml";
	public static final String NINTENDO_64_INDEX_URL = BASE_URL + "n64.xml";
	public static final String NINTENDO_DS_INDEX_URL = BASE_URL + "nds.xml";
	public static final String NINTENDO_GAME_CUBE_INDEX_URL = BASE_URL + "ngc.xml";
	public static final String PLAYSTATION_INDEX_URL = BASE_URL + "psx.xml";
	public static final String PLAYSTATION_2_INDEX_URL = BASE_URL + "playstation_2.xml";
	public static final String SEGA_CD_INDEX_URL = BASE_URL + "sega_cd.xml";
	public static final String SEGA_GAME_GEAR_INDEX_URL = BASE_URL + "sega_game_gear.xml";
	public static final String SEGA_DREAMCAST_INDEX_URL = BASE_URL + "sega_dreamcast.xml";
	public static final String SEGA_GENESIS_INDEX_URL = BASE_URL + "sega_genesis.xml";
	public static final String SEGA_MASTER_SYSTEM_INDEX_URL = BASE_URL + "master_system.xml";
	public static final String SEGA_MODEL_2_INDEX_URL = BASE_URL + "sega_model_2.xml";
	public static final String SEGA_SATURN_INDEX_URL = BASE_URL + "sega_saturn.xml";
	public static final String SUPER_NINTENDO_INDEX_URL = BASE_URL + "snes.xml";

	public static final String GET_ROM_DOWNLOAD_URL = BASE_URL + "get_download_url";

	
	public static void initData() throws NotFoundException, Exception {
		data = new Persister().read(Data.class, new URL(
				CONSOLE_INDEX_URL).openStream());

		Collections.sort(data.getConsoles(), new Comparator<Console>() {

			@Override
			public int compare(Console lhs, Console rhs) {
				return lhs.getName().compareTo(rhs.getName());
			}

		});

	}

	public static void initRoms(Console console, String url)
			throws NotFoundException, Exception {

		List<Rom> roms = new Persister().read(DataSource.Container.class,
				new URL(url).openStream()).getRoms();
		console.getRoms().clear();
		console.getRoms().addAll(roms);

		Collections.sort(console.getRoms(), new Comparator<Rom>() {

			@Override
			public int compare(Rom lhs, Rom rhs) {
				return lhs.getName().compareTo(rhs.getName());
			}

		});
	}

	

	public static Data getData() {
		return data;
	}

	public static void setData(Data data) {
		DataSource.data = data;
	}

	@Root(name = "data")
	public static class Container {

		@ElementList(required = false)
		private List<Rom> roms = new ArrayList<Rom>();

		public List<Rom> getRoms() {
			return roms;
		}

		public void setRoms(List<Rom> roms) {
			this.roms = roms;
		}

	}

}
