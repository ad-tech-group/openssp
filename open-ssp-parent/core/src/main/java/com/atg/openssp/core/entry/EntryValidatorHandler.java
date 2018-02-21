package com.atg.openssp.core.entry;

import com.atg.openssp.common.demand.ParamValue;
import com.atg.openssp.common.exception.RequestException;

import javax.servlet.http.HttpServletRequest;

public abstract class EntryValidatorHandler {
    public abstract ParamValue validateEntryParams(HttpServletRequest request) throws RequestException;
}
