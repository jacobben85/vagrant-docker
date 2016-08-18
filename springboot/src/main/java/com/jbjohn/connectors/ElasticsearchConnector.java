package com.jbjohn.connectors;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.jbjohn.properties.Configurations;

import java.net.InetAddress;
import org.springframework.stereotype.Component;

/**
 * Client class for connecting to Elasticsearch prior to Dari support.
 */
public final class ElasticsearchConnector {

    private static final Logger LOGGER = LoggerFactory.getLogger(ElasticsearchConnector.class);
    
    private Configurations config;
    
    public ElasticsearchConnector(Configurations conf){
        config = conf;
    }
    
    /**
     * Create a Lazy Client so it is only loaded when the get method is used.
     * By making this variable static and final it will only be loaded once per JVM session.
     */
    public Client create() throws Exception {
        // Pull values from the Tomcat Context XML.
        String cluster = config.getEscluster();
        String server = config.getEshost();
        Integer port = config.getEsport();
        String timeout = null;
        Boolean enableCompression = false;
        LOGGER.info("cluster :" + cluster);
        LOGGER.info("host :" + server);
        LOGGER.info("port :" + Integer.toString(port));

        // default timeout to 5 seconds
        if (timeout == null) {
            timeout = "20s";
        }

        // default compression to false
        if (enableCompression == null) {
            enableCompression = false;
        }

        // Configure the cluster settings.
        Settings settings = Settings.settingsBuilder()
                .put("cluster.name", cluster)
                .put("transport.tcp.connect_timeout", timeout)
                .put("transport.tcp.compress", enableCompression)
                .build();

        // Create a new transport client.
        InetSocketTransportAddress address = new InetSocketTransportAddress(InetAddress.getByName(server), port);
        LOGGER.info("Address : " + address.toString());
        TransportClient transportClient = TransportClient.builder().settings(settings).build();
        transportClient = transportClient.addTransportAddress(address);

        LOGGER.info("Elasticsearch client created successfully!");
        return transportClient;
    }

    public void destroy(Client currentClient) {
        // Properly shut down the client when it is destroyed.
        if (currentClient != null) {
            currentClient.close();
            LOGGER.info("Elasticsearch client destroyed successfully!");
        }
    }
}