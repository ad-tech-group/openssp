package io.freestar.ssp.aerospike.data;

import com.google.gson.Gson;
import org.junit.Test;

import static org.junit.Assert.*;

public class CookieSyncDTOTest {

    @Test
    public void setFsuid() {
    }

    @Test
    public void getFsuid() {
    }

    @Test
    public void lookup() {
    }

    @Test
    public void add() {
    }

    @Test
    public void isDirty() {

        Gson gson = new Gson();

        CookieSyncDTO dto = new CookieSyncDTO();
        assertFalse(dto.isDirty());
        dto.setFsuid("frogs-uid");
        assertTrue(dto.isDirty());
        String json = gson.toJson(dto);
        assertEquals("{\"dsp_uids\":{},\"fsuid\":\"frogs-uid\"}", json);
        CookieSyncDTO dto2 = gson.fromJson(json, CookieSyncDTO.class);
        assertFalse(dto2.isDirty());

        dto.clearDirty();
        assertFalse(dto.isDirty());
        json = gson.toJson(dto);
        assertEquals("{\"dsp_uids\":{},\"fsuid\":\"frogs-uid\"}", json);
        dto2 = gson.fromJson(json, CookieSyncDTO.class);
        assertFalse(dto2.isDirty());

        DspCookieDto dC = new DspCookieDto();
        assertFalse(dC.isDirty());
        dC.setShortName("wombat");
        assertTrue(dC.isDirty());
        dC.clearDirty();
        assertFalse(dC.isDirty());

        dto.add(dC);
        assertTrue(dto.isDirty());
        assertFalse(dC.isDirty());
        dto.clearDirty();
        assertFalse(dto.isDirty());
        assertFalse(dC.isDirty());

        dC.setUid("foobar");
        assertTrue(dto.isDirty());
        assertTrue(dC.isDirty());
    }
}