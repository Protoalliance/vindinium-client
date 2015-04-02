package com.protoalliance.vindiniumclient.dto;

import com.google.api.client.util.Key;

/**
 * Created by bstempi on 9/15/14.
 */
public class ApiKey {

    @Key
    private final String key;

    @Key
    private final String map;

    public ApiKey(String key, String map) {
        this.key = key;
        this.map = map;
    }

    public String getKey() {
        return key;
    }
    public String getMap() { return map; }
}
