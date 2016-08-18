/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jbjohn.connectors;

import com.jbjohn.properties.Configurations;
import org.elasticsearch.client.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author jacobbj
 */
public class ElasticsearchWrapper {
    
    private static Client client;
    private static final Logger LOGGER = LoggerFactory.getLogger(ElasticsearchWrapper.class);
    
    public static Client getClient(Configurations config) {
        
        if (client == null) {
            try {
                ElasticsearchConnector esClient = new ElasticsearchConnector(config);
                client = esClient.create();
            } catch (Exception ex) {
                LOGGER.error("Exception creating Elasticsearch Connector", ex);
            }
        }
        
        return client;
    }
}
