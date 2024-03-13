package com.jbazann.inventorytracking.ui;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.annotation.PostConstruct;

//TODO document
@Repository
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class BuildManifest {
    
    @Autowired
    private JacksonJsonParser parser;
    @Autowired
    private ObjectMapper mapper;

    private Map<String,Object> manifestMap; 

    @PostConstruct
    private void loadManifest() {

        InputStream postcssStream = Thread.currentThread()
            .getContextClassLoader()
            .getResourceAsStream("postcss-manifest.json");
        InputStream webpackStream = Thread.currentThread()
            .getContextClassLoader()
            .getResourceAsStream("webpack-manifest.json");

        try{

            if(webpackStream == null || postcssStream == null);// TODO Exceptions
            // Parse webpack-manifest.json
            String manifestJson = mapper.writeValueAsString(mapper.readValue(webpackStream, JsonNode.class));
            manifestMap = parser.parseMap(manifestJson);
            // Parse and append postcss-manifest.json
            manifestJson = mapper.writeValueAsString(mapper.readValue(postcssStream, JsonNode.class));
            manifestMap.putAll(parser.parseMap(manifestJson));

            }
        catch(StreamReadException e){
            e.printStackTrace();
        }catch(DatabindException e){
            e.printStackTrace();
        }catch(JsonProcessingException e){
            e.printStackTrace();
        }catch(IOException e) { // TODO Exceptions
            e.printStackTrace();
        }
    }

    public String get(String manifestEntry) {
        if( !manifestMap.containsKey(manifestEntry) ) return "";
        return (String) manifestMap.get(manifestEntry);
    }

    public String getAt(String manifestEntry, String resourcePath) {
        if( get(manifestEntry).startsWith(resourcePath) ) return manifestEntry;
        return resourcePath + get(manifestEntry);
    }

}
