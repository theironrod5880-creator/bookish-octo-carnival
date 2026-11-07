package com.concepts5880.portal;

import android.content.Context;
import android.content.res.AssetManager;
import fi.iki.elonen.NanoHTTPD;
import java.io.InputStream;
import java.io.IOException;

public class WebServerService extends NanoHTTPD {

    private final Context context;

    public WebServerService(Context context, int port) {
        super(port);
        this.context = context;
    }

    @Override
    public Response serve(IHTTPSession session) {
        String uri = session.getUri();

        // 1. Backend API Endpoint Route
        if (uri.equals("/api/data")) {
            String jsonResponse = "{\"status\":\"success\", \"message\":\"Hello from the embedded Java backend!\"}";
            return newFixedLengthResponse(Response.Status.OK, "application/json", jsonResponse);
        }

        // 2. Static Web Assets Routing
        if (uri.equals("/")) {
            uri = "/index.html";
        }

        String mimeType = getMimeType(uri);
        try {
            AssetManager assetManager = context.getAssets();
            InputStream input = assetManager.open("web" + uri);
            return newChunkedResponse(Response.Status.OK, mimeType, input);
        } catch (IOException e) {
            return newFixedLengthResponse(Response.Status.NOT_FOUND, "text/plain", "404 Not Found");
        }
    }

    private String getMimeType(String uri) {
        if (uri.endsWith(".html")) return "text/html";
        if (uri.endsWith(".js")) return "application/javascript";
        if (uri.endsWith(".css")) return "text/css";
        if (uri.endsWith(".json")) return "application/json";
        if (uri.endsWith(".png")) return "image/png";
        return "text/plain";
    }
}
