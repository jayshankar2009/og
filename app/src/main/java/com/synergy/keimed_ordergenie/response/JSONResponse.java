package com.synergy.keimed_ordergenie.response;

import java.util.List;

import com.synergy.keimed_ordergenie.model.m_customerlist;

/**
 * Created by prakash on 12/07/16.
 */
public class JSONResponse {


    private List<m_customerlist> customerlists;

    public void setCustomerlists(List<m_customerlist> customerlists) {
        this.customerlists = customerlists;
    }

    public List<m_customerlist> getCustomerlists() {
        return customerlists;
    }

    private m_customerlist[] m_customerlists;

    public m_customerlist[] getAndroid() {
        return m_customerlists;
    }

}
