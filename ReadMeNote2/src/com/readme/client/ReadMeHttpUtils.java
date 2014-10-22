package com.readme.client;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.oauth.OAuth;
import net.oauth.OAuthAccessor;
import net.oauth.OAuthException;
import net.oauth.OAuthMessage;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;

import com.readme.json.JSONException;
import com.readme.json.JSONObject;




/**
 * 基于http的网络请求方法（用于OAuth认证和API操作）
 *
 * @author haiwen
 */
public class ReadMeHttpUtils {

    private static final HttpClient client = new DefaultHttpClient();

    /**
     * Get请求方法
     *
     * @param url
     * @param parameters
     * @param accessor
     * @return response
     * @throws IOException
     * @throws {@link ReadMeException}
     */
    public static HttpResponse doGet(String url, Map<String, String> parameters,
            OAuthAccessor accessor) throws IOException, ReadMeException {
        // add ynote parameters to the url
        OAuth.addParameters(url, parameters == null ? null : parameters.entrySet());
        HttpGet get = new HttpGet(url);
        // sign all parameters, including oauth parameters and ynote parameters
        // and add the oauth related information into the header        
        Header oauthHeader = getAuthorizationHeader(url, OAuthMessage.GET,
                parameters, accessor);
        get.addHeader(oauthHeader);
        HttpParams params = new BasicHttpParams();
        HttpClientParams.setRedirecting(params, false);
        get.setParams(params);
        HttpResponse response = client.execute(get);
        if ((response.getStatusLine().getStatusCode() / 100) != 2) {
            ReadMeException e = wrapYNoteException(response);
            throw e;
        }
        return response;
    }

    /**
     * post请求（encoded content type）
     *
     * @param url
     * @param formParams
     * @param accessor
     * @return
     * @throws IOException
     * @throws ReadMeException
     */
    public static HttpResponse doPostByUrlEncoded(String url,
            Map<String, String> formParams, OAuthAccessor accessor)
            throws IOException, ReadMeException {
        HttpPost post = new HttpPost(url);
        // for url encoded post, sign all the parameters, including oauth
        // parameters and form parameters
        Header oauthHeader = getAuthorizationHeader(url, OAuthMessage.POST,
                formParams, accessor);
        if (formParams != null) {
            // encode our ynote parameters
            List<NameValuePair> pairs = new ArrayList<NameValuePair>();
            for(Entry<String, String> entry : formParams.entrySet()) {
                pairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
            }
            UrlEncodedFormEntity entity = new UrlEncodedFormEntity(pairs, ReadMeConstants.ENCODING);
            post.setEntity(entity);
        }
        post.addHeader(oauthHeader);
        HttpResponse response = client.execute(post);
        if ((response.getStatusLine().getStatusCode() / 100) != 2) {
            ReadMeException e = wrapYNoteException(response);
            throw e;
        }
        return response;
    }

    /**
     * post请求（multipart content type）.上传大资源(如文件）用
     *
     * @param url
     * @param formParams
     * @param accessor
     * @return
     * @throws IOException
     * @throws ReadMeException
     */
    public static HttpResponse doPostByMultipart(String url,
            Map<String, Object> formParams, OAuthAccessor accessor)
            throws IOException, ReadMeException {
        HttpPost post = new HttpPost(url);
        Header oauthHeader = getAuthorizationHeader(url, OAuthMessage.POST,
                null, accessor);
        if (formParams != null) {
          
            MultipartEntity entity = new MultipartEntity(
                    HttpMultipartMode.BROWSER_COMPATIBLE, null,
                    Charset.forName("UTF-8"));
            for (Entry<String, Object> parameter : formParams.entrySet()) {
                if (parameter.getValue() instanceof File) {
                    // deal with file particular
                    entity.addPart(parameter.getKey(),
                            new FileBody((File)parameter.getValue()));
                } else if (parameter.getValue() != null){
                    entity.addPart(parameter.getKey(), new StringBody(
                            parameter.getValue().toString(),
                            Charset.forName(ReadMeConstants.ENCODING)));
                }
            }
            post.setEntity(entity);
        }
        post.addHeader(oauthHeader);
        HttpResponse response = client.execute(post);
        if ((response.getStatusLine().getStatusCode() / 100) != 2) {
            ReadMeException e = wrapYNoteException(response);
            throw e;
        }
        return response;
    }

    /**
     * 获取请求头
     *
     * @param url
     * @param parameters
     * @param accessor
     * @return
     * @throws IOException
     */
    private static Header getAuthorizationHeader(String url, String method,
            Map<String, String> parameters, OAuthAccessor accessor)
            throws IOException {
        try {
            OAuthMessage message = accessor.newRequestMessage(method,
                    url, parameters == null ? null : parameters.entrySet());
           return new BasicHeader("Authorization",
                    message.getAuthorizationHeader(null));
        } catch (OAuthException e) {
            throw new IOException("Fail to signature", e);
        } catch (URISyntaxException e) {
            throw new IOException("Invalid URL", e);
        }
    }

    /**
     * 如果状态码不是200，抛出异常r
     *
     * @param response
     * @return
     * @throws IOException
     */
    private static ReadMeException wrapYNoteException(HttpResponse response)
            throws IOException {
        int status = response.getStatusLine().getStatusCode();
        InputStream body = response.getEntity().getContent();
        if (status == HttpStatus.SC_INTERNAL_SERVER_ERROR) {
            String content = getResponseContent(body);
            try {
                JSONObject json = new JSONObject(content);
                return new ReadMeException(json);
            } catch (JSONException e) {
                throw new IOException(content);
            }
        } else if (body != null) {
            String content = getResponseContent(body);
            throw new IOException(content);
        } else {
            throw new RuntimeException(response.toString());
        }
    }

    /**
     * 处理响应体，获取content
     *
     * @param response
     * @return
     * @throws IOException
     */
    public static String getResponseContent(InputStream response)
            throws IOException {
        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream(1024);
            byte[] buffer = new byte[1024];
            int n = 0;
            while (-1 != (n = response.read(buffer))) {
                bytes.write(buffer, 0, n);
            }
            bytes.close();
            return new String(bytes.toByteArray(), ReadMeConstants.ENCODING);
        } finally {
            // release the http response
            response.close();
        }
    }

    /**
     * 分离content，存入map
     * @param content
     * @return
     */
    public static Map<String, String> parseOAuthResponse(String content) {
        Map<String, String> map = new HashMap<String, String>();
        if (content != null && !content.isEmpty()) {
            String[] items = content.split("&");
            for (String item : items) {
                int index = item.indexOf("=");
                String key = item.substring(0, index);
                String value = item.substring(index + 1);
                map.put(key, value);
            }
        }
        return map;
    }
}
