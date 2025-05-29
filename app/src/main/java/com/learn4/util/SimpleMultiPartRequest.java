package com.learn4.util;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SimpleMultiPartRequest extends Request<String> {

    private final Response.Listener<String> mListener;
    private final Map<String, String> mStringParams = new HashMap<>();
    private final Map<String, DataPart> mFileParams = new HashMap<>();
    private final String boundary = "Volley-" + UUID.randomUUID();
    private final String mimeType = "multipart/form-data;boundary=" + boundary;
    private Map<String, String> headers = new HashMap<>();

    public SimpleMultiPartRequest(int method, String url,
                                  Response.Listener<String> listener,
                                  Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        mListener = listener;
    }

    public void addStringParam(String key, String value) {
        mStringParams.put(key, value);
    }

    public void addFile(String paramName, byte[] fileData, String fileName, String contentType) {
        mFileParams.put(paramName, new DataPart(fileName, fileData, contentType));
    }

    @Override
    public String getBodyContentType() {
        return mimeType;
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            // String params
            for (Map.Entry<String, String> entry : mStringParams.entrySet()) {
                writeFormField(bos, entry.getKey(), entry.getValue());
            }
            // File params
            for (Map.Entry<String, DataPart> entry : mFileParams.entrySet()) {
                writeFileField(bos, entry.getKey(), entry.getValue());
            }
            // Closing boundary
            bos.write(("--" + boundary + "--\r\n").getBytes());
        } catch (IOException e) {
            throw new RuntimeException("Encoding not supported: " + e);
        }
        return bos.toByteArray();
    }

    private void writeFormField(OutputStream out, String name, String value) throws IOException {
        out.write(("--" + boundary + "\r\n").getBytes());
        out.write(("Content-Disposition: form-data; name=\"" + name + "\"\r\n\r\n").getBytes());
        out.write((value + "\r\n").getBytes());
    }

    private void writeFileField(OutputStream out, String name, DataPart dataFile) throws IOException {
        out.write(("--" + boundary + "\r\n").getBytes());
        out.write(("Content-Disposition: form-data; name=\"" + name + "\"; filename=\"" + dataFile.fileName + "\"\r\n").getBytes());
        out.write(("Content-Type: " + dataFile.type + "\r\n\r\n").getBytes());
        out.write(dataFile.content);
        out.write("\r\n".getBytes());
    }

    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        try {
            String parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.success(new String(response.data), HttpHeaderParser.parseCacheHeaders(response));
        }
    }

    @Override
    protected void deliverResponse(String response) {
        mListener.onResponse(response);
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return headers != null ? headers : super.getHeaders();
    }

    // Helper class for file part
    public static class DataPart {
        public String fileName;
        public byte[] content;
        public String type;

        public DataPart(String name, byte[] data, String type) {
            this.fileName = name;
            this.content = data;
            this.type = type;
        }
    }
}
