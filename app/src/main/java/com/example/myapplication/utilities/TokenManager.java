package com.example.myapplication.utilities;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaSession2;

public class TokenManager {
    private static final String AUTH_TOKEN_KEY = "auth_token";

    private SharedPreferences sharedPreferences;

    public TokenManager(Context context) {
        sharedPreferences = context.getSharedPreferences("auth", Context.MODE_PRIVATE);
    }

    public void saveAuthToken(String authToken) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(AUTH_TOKEN_KEY, authToken);
        editor.apply();
    }

    public String getAuthToken() {
        return sharedPreferences.getString(AUTH_TOKEN_KEY, null);
    }
}