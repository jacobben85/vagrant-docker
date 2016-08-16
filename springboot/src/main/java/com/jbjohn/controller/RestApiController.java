package com.jbjohn.controller;

import com.jbjohn.connectors.ElasticsearchConnector;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.common.collect.HppcMaps;
import org.elasticsearch.search.SearchHit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.jbjohn.connectors.ElasticsearchWrapper;
import org.elasticsearch.client.Client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.index.query.QueryBuilders;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

/**
 */
@RestController
@RequestMapping(value = "/rest", produces = {"application/json"})
public class RestApiController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ElasticsearchConnector.class);

    @RequestMapping("")
    public Map<String, Object> home() {

        Map<String, Object> model = new HashMap<>();
        model.put("title", "Spring gradle");

        return model;
    }
    
    @RequestMapping("/search")
    public Map<String, Object> search(@RequestParam(required = false)
                                          final String q) {

        /**
         * @TODO Improve this part
         * If there are no results or if the query is incomplete add AI.
         */
        Map<String, Object> responseMap = new HashMap<>();
        responseMap.put("count", 0);

        if (q != null) {
            Client client = ElasticsearchWrapper.getClient();
            SearchResponse response = client.prepareSearch()
                    .setQuery(QueryBuilders.queryStringQuery(q))
                    .execute()
                    .actionGet();
            SearchHit[] results = response.getHits().getHits();
            ArrayList<Map<String, Object>> resultsArray = new ArrayList<>();
            for(SearchHit hit : results){
                Map<String, Object> resultMap = hit.sourceAsMap();
                if (resultMap != null) {
                    resultsArray.add(resultMap);
                }
            }
            responseMap.put("count", resultsArray.size());
            responseMap.put("results", resultsArray);
        }

        return responseMap;
    }

    @RequestMapping("/get")
    public Map<String, Object> get() {
        Client client = ElasticsearchWrapper.getClient();
        return client.prepareGet("index", "type", "1").execute().actionGet().getSourceAsMap();
    }

    @RequestMapping("/put")
    public String put() {
        Client client = ElasticsearchWrapper.getClient();
        try {

            IndexRequest indexRequest = new IndexRequest("index", "type", "1")
                    .source(jsonBuilder()
                            .startObject()
                            .field("id", "1")
                            .field("name", "Joe Smith")
                            .field("description", "Joe Smith description")
                            .endObject());
            UpdateRequest updateRequest = new UpdateRequest("index", "type", "1")
                    .doc(jsonBuilder()
                            .startObject()
                            .field("id", "1")
                            .field("name", "Joe Smith")
                            .field("description", "Joe Smith description")
                            .endObject())
                    .upsert(indexRequest);

            UpdateResponse response = client.update(updateRequest).get();

            return Long.toString(response.getVersion());
        } catch (IOException | InterruptedException | ExecutionException e) {
            LOGGER.error("Exception sending data to ES", e);
        }
        return "0";
    }
}
