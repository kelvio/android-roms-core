package com.kelviomatias.romscore.util;

import com.kelviomatias.romscore.Console;
import com.kelviomatias.romscore.DataSource;

public class ConsoleAppEngineUtil {

	public static String getUrlForConsoleRoms(Console console) {
		String n = console.getName();
		if (n.equals("Atari 2600")) {
			return DataSource.ATARI_2600_INDEX_URL;
		} else if (n.equals("Atari 5200")) {
			return DataSource.ATARI_5200_INDEX_URL;
		} else if (n.equals("Atari Jaguar")) {
			return DataSource.ATARI_JAGUAR_INDEX_URL;
		} else if (n.equals("Atari Lynx")) {
			return DataSource.ATARI_LYNX_INDEX_URL;
		} else if (n.equals("CPS1")) {
			return DataSource.CPS1_INDEX_URL;
		} else if (n.equals("CPS2")) {
			return DataSource.CPS2_INDEX_URL;
		} else if (n.equals("Comodore 64")) {
			return DataSource.COMODORE_64_INDEX_URL;
		} else if (n.equals("Game Boy Advance")) {
			return DataSource.GAME_BOY_ADVANCE_INDEX_URL;
		} else if (n.equals("MAME")) {
			return DataSource.MAME_INDEX_URL;
		} else if (n.equals("Namco System 22")) {
			return DataSource.NAMCO_SYSTEM_22_INDEX_URL;
		} else if (n.equals("Neo Geo")) {
			return DataSource.NEO_GEO_INDEX_URL;
		} else if (n.equals("Neo Geo CD")) {
			return DataSource.NEO_GEO_CD_INDEX_URL;
		} else if (n.equals("Neo Geo Pocket")) {
			return DataSource.NEO_GEO_POCKET_INDEX_URL;
		} else if (n.equals("Nintendo")) {
			return DataSource.NINTENDO_INDEX_URL;
		} else if (n.equals("Nintendo 64")) {
			return DataSource.NINTENDO_64_INDEX_URL;
		} else if (n.equals("Nintendo Game Cube")) {
			return DataSource.NINTENDO_GAME_CUBE_INDEX_URL;
		} else if (n.equals("Playstation")) {
			return DataSource.PLAYSTATION_INDEX_URL;
		} else if (n.equals("Playstation 2")) {
			return DataSource.PLAYSTATION_2_INDEX_URL;
		} else if (n.equals("Sega CD")) {
			return DataSource.SEGA_CD_INDEX_URL;
		} else if (n.equals("Sega Dreamcast")) {
			return DataSource.SEGA_DREAMCAST_INDEX_URL;
		} else if (n.equals("Sega Game Gear")) {
			return DataSource.SEGA_GAME_GEAR_INDEX_URL;
		} else if (n.equals("Sega Genesis")) {
			return DataSource.SEGA_GENESIS_INDEX_URL;
		} else if (n.equals("Sega Master System")) {
			return DataSource.SEGA_MASTER_SYSTEM_INDEX_URL;
		} else if (n.equals("Sega Model 2")) {
			return DataSource.SEGA_MODEL_2_INDEX_URL;
		} else if (n.equals("Sega Saturn")) {
			return DataSource.SEGA_SATURN_INDEX_URL;
		} else if (n.equals("Super Nintendo")) {
			return DataSource.SUPER_NINTENDO_INDEX_URL;
		} else if (n.equals("Game Boy Color")) {
			return DataSource.GAME_BOY_COLOR_INDEX_URL;
		} else if (n.equals("Nintendo DS")) {
			return DataSource.NINTENDO_DS_INDEX_URL;
		}
		return null;
	}

}
