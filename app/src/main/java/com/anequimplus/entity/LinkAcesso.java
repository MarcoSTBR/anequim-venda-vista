package com.anequimplus.entity;

import com.anequimplus.tipos.Link;

import java.net.URL;


public class LinkAcesso {

    private int id ;
    private Link link ;
    private URL url ;

    public LinkAcesso(int id, Link link, URL url) {
        this.id = id;
        this.link = link;
        this.url = url;
    }
/*

    public LinkAcesso(JSONObject j) throws JSONException, MalformedURLException {
        id = j.getInt("ID");
        link = Link.valueOf(j.getString("LINK")) ;
        url = new URL(j.getString("URL"));
    }

    public JSONObject getJSON(){
        JSONObject j = new JSONObject();
        try {
            j.put("ID",id);
            j.put("LINK",link.valor);
            j.put("URL",url.toString());

        } catch (JSONException e) {
            e.printStackTrace();
           // Toast.makeText(, e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return j ;
    }
*/

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Link getLink() {
        return link;
    }

    public void setLink(Link link) {
        this.link = link;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }
}
