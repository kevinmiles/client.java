package com.enigmabridge;

import com.enigmabridge.comm.EBConnectorManager;

import java.security.SecureRandom;

/**
 * Global object for performing EB requests.
 * Created by dusanklinec on 27.04.16.
 */
public class EBEngine {
    /**
     * Connection manager
     */
    protected EBConnectorManager conMgr;

    /**
     * Shared secure random instance
     */
    protected SecureRandom rnd;

    /**
     * Default EB settings for using EB service.
     */
    protected EBSettings defaultSettings;

    public EBConnectorManager getConMgr() {
        if (conMgr == null){
            conMgr = new EBConnectorManager();
        }
        return conMgr;
    }

    public SecureRandom getRnd() {
        if (rnd == null){
            rnd = new SecureRandom();
        }
        return rnd;
    }

    public EBSettings getDefaultSettings() {
        return defaultSettings;
    }

    public void setDefaultSettings(EBSettings defaultSettings) {
        this.defaultSettings = defaultSettings;
    }
}