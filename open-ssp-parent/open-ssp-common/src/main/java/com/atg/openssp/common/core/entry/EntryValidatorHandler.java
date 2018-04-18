package com.atg.openssp.common.core.entry;

import com.atg.openssp.common.demand.ParamValue;
import com.atg.openssp.common.exception.RequestException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public abstract class EntryValidatorHandler {
    public abstract List<ParamValue> validateEntryParams(HttpServletRequest request) throws RequestException;
}
