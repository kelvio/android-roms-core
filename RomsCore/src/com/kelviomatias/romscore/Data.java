package com.kelviomatias.romscore;

import java.util.ArrayList;
import java.util.List;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

/**
 *
 * @author kelvio
 */
@Root(name = "roms")
public class Data {

    @ElementList(type = Console.class, required = false)
    private List<Console> consoles = new ArrayList<Console>();
    
    
    public List<Console> getConsoles() {
        return consoles;
    }

    public void setConsoles(List<Console> consoles) {
        this.consoles = consoles;
    }
}
