package com.folio.common;

import org.json.JSONException;
import org.json.JSONObject;

public class Auth {

    public static Auth auth = new Auth();

    public static String ANDROID_APP_CLIENT = "androidapp";
    public static String ANDROID_APP_SECRET = "secret";

    private JSONObject accessToken;

    public void setToken(JSONObject accessToken){
        this.accessToken = accessToken;
    }

    public JSONObject getToken(){
        return this.accessToken;
    }

    public static boolean shouldRefreshToken(JSONObject serverResponse){
        try{
            if(serverResponse.getString("error").equals("invalid_token") && serverResponse.getString("error_description").equals("The access token provided has expired")){
                // Get a new token here
                return true;
            }
            return false;
        }
        catch(JSONException e){
            e.printStackTrace();
        }
        return false;
    }
}
