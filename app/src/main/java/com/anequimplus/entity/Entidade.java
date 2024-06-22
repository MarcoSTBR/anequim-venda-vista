package com.anequimplus.entity;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class Entidade {

    public abstract JSONObject geJSON() throws JSONException;
}
