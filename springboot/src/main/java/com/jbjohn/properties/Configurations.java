/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jbjohn.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 *
 * @author jacobbj
 */
@Component
@ConfigurationProperties(prefix = "config")
public class Configurations {

    private String eshost;
    private int esport;
    private String escluster;

    public String getEshost() {
        return eshost;
    }

    public int getEsport() {
        return esport;
    }

    public String getEscluster() {
        return escluster;
    }

    public void setEshost(String eshost) {
        this.eshost = eshost;
    }

    public void setEsport(int esport) {
        this.esport = esport;
    }

    public void setEscluster(String escluster) {
        this.escluster = escluster;
    }
    
}
