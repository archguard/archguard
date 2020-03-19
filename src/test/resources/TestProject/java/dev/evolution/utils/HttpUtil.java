package dev.evolution.utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Base64;

public class HttpUtil {

    public static String get(String url, String user, String password) {
        CredentialsProvider provider = new BasicCredentialsProvider();
//        UsernamePasswordCredentials credentials = new UsernamePasswordCredentials(user, password);
//        provider.setCredentials(AuthScope.ANY, credentials);
//        CloseableHttpClient httpclient = HttpClientBuilder.create().setDefaultCredentialsProvider(provider).build();
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            // 创建httpget.
            HttpGet httpget = new HttpGet(url);
            System.out.println("executing request " + httpget.getURI());
            if(user != null && password != null) {
                String encoding = Base64.getEncoder().encodeToString((user+":"+password).getBytes());
                httpget.setHeader(HttpHeaders.AUTHORIZATION, "Basic " + encoding);
            }
            // 执行get请求.
            CloseableHttpResponse response = httpclient.execute(httpget);
            try {
                // 获取响应实体
                HttpEntity entity = response.getEntity();
                System.out.println("--------------------------------------");
                // 打印响应状态
                System.out.println(response.getStatusLine());
                if (entity != null) {
                    // 打印响应内容长度
                    System.out.println("Response content length: " + entity.getContentLength());
                    // 打印响应内容
                    return EntityUtils.toString(entity);
                }
            } finally {
                response.close();
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭连接,释放资源
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "Error";
    }

    public static String get(String url) {
        return get(url,null,null);
    }

    public static String post(String url, String body, String user, String password) {
        // 创建默认的httpClient实例.
        CloseableHttpClient httpclient = HttpClients.createDefault();
        // 创建httppost
        HttpPost httppost = new HttpPost(url);
        if(user == null || password == null) {
            String encoding = Base64.getEncoder().encodeToString((user+":"+password).getBytes());
            httppost.setHeader(HttpHeaders.AUTHORIZATION, "Basic " + encoding);
        }
        // 创建参数队列
        try {
            HttpEntity entity = EntityBuilder.create().setText(body).build();
            httppost.setEntity(entity);
            System.out.println("executing request " + httppost.getURI() + " body size:"+ body.length());
            CloseableHttpResponse response = httpclient.execute(httppost);
            try {
                HttpEntity responseEntity = response.getEntity();
                if (responseEntity != null) {
                    System.out.println("--------------------------------------");
                    return EntityUtils.toString(responseEntity, "UTF-8");
                }
            } finally {
                response.close();
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭连接,释放资源
            try {
                httpclient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "ERROR";
    }

    public static String post(String url, String body) {
        return post(url,body,null,null);
    }


}
