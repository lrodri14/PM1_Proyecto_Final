package com.example.myapplication;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class VolleyMultipartRequest extends Request<NetworkResponse> {
    private final String LINE_FEED = "\r\n";
    private final String boundary;
    private final Map<String, String> headers;
    private final Response.Listener<NetworkResponse> mListener;
    private final Response.ErrorListener mErrorListener;
    private final Map<String, String> params;
    private final Map<String, DataPart> fileUploads;

    public VolleyMultipartRequest(String url, Map<String, String> headers, Map<String, String> params, Map<String, DataPart> fileUploads, Response.Listener<NetworkResponse> listener, Response.ErrorListener errorListener) {
        super(Request.Method.POST, url, errorListener);
        this.boundary = "Volley-" + System.currentTimeMillis();
        this.headers = headers;
        this.params = params;
        this.fileUploads = fileUploads;
        this.mListener = listener;
        this.mErrorListener = errorListener;
        setShouldRetryServerErrors(true);
        setRetryPolicy(new DefaultRetryPolicy(5000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return headers != null ? headers : super.getHeaders();
    }

    @Override
    public String getBodyContentType() {
        return "multipart/form-data;boundary=" + boundary;
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutputStream = new DataOutputStream(outputStream);

        try {
            // Add params
            if (params != null && !params.isEmpty()) {
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    buildTextPart(dataOutputStream, entry.getKey(), entry.getValue());
                }
            }

            // Add files
            if (fileUploads != null && !fileUploads.isEmpty()) {
                for (Map.Entry<String, DataPart> entry : fileUploads.entrySet()) {
                    buildFilePart(dataOutputStream, entry.getKey(), entry.getValue());
                }
            }

            // Add end boundary
            byte[] endBoundary = (LINE_FEED + "--" + boundary + "--" + LINE_FEED).getBytes(StandardCharsets.UTF_8);
            dataOutputStream.write(endBoundary);

            return outputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected Response<NetworkResponse> parseNetworkResponse(NetworkResponse response) {
        return Response.success(response, HttpHeaderParser.parseCacheHeaders(response));
    }

    @Override
    protected void deliverResponse(NetworkResponse response) {
        mListener.onResponse(response);
    }

    @Override
    public void deliverError(VolleyError error) {
        mErrorListener.onErrorResponse(error);
    }

    private void buildTextPart(DataOutputStream dataOutputStream, String key, String value) throws IOException {
        dataOutputStream.writeBytes("--" + boundary + LINE_FEED);
        dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"" + key + "\"" + LINE_FEED);
        dataOutputStream.writeBytes("Content-Type: text/plain; charset=UTF-8" + LINE_FEED);
        dataOutputStream.writeBytes(LINE_FEED);
        dataOutputStream.write(value.getBytes(StandardCharsets.UTF_8));
        dataOutputStream.writeBytes(LINE_FEED);
    }

    private void buildFilePart(DataOutputStream dataOutputStream, String key, DataPart dataPart) throws IOException {
        dataOutputStream.writeBytes("--" + boundary + LINE_FEED);
        dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"" + key + "\"; filename=\"" + dataPart.getFileName() + "\"" + LINE_FEED);
        dataOutputStream.writeBytes("Content-Type: " + dataPart.getType() + LINE_FEED);
        dataOutputStream.writeBytes(LINE_FEED);

        byte[] fileData = dataPart.getContent();
        ByteArrayInputStream fileInputStream = new ByteArrayInputStream(fileData);
        int bytesRead;
        byte[] buffer = new byte[1024];
        while ((bytesRead = fileInputStream.read(buffer)) != -1) {
            dataOutputStream.write(buffer, 0, bytesRead);
        }
        fileInputStream.close();

        dataOutputStream.writeBytes(LINE_FEED);
    }
}