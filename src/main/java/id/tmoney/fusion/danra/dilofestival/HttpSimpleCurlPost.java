package id.tmoney.fusion.danra.dilofestival;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.AbstractHttpEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.json.simple.JSONObject;

public class HttpSimpleCurlPost {
    final private static String BASE_API_URL = "https://api.mainapi.net/tmoney/1.0.0/";
    final private static String BASE_API_KEY = "_SET_IT_WITH_YOUR_OWN_KEY_";
    
    public static void main(String[] args) {
        String userName = "_YOUR_OWN_TMONEY_ACCOUNT_";
        String password = "_YOUR_OWN_TMONEY_PASSWORD_";
        String terminal = "_ASKED_TO_INSTRUCTOR_";
        
        if(BASE_API_KEY.equalsIgnoreCase("") || BASE_API_KEY.equalsIgnoreCase("_SET_IT_WITH_YOUR_OWN_KEY_")) {
            System.out.println("Set your own key from our system (www.mainapi.net)");
            return;
        } else if(userName.equalsIgnoreCase("") || userName.equalsIgnoreCase("_YOUR_OWN_TMONEY_ACCOUNT_")) {
            System.out.println("Set your own T-MONEY account");
            return;
        } else if(password.equalsIgnoreCase("") || password.equalsIgnoreCase("_YOUR_OWN_TMONEY_PASSWORD_")) {
            System.out.println("Set your own T-MONEY password");
            return;
        } else if(terminal.equalsIgnoreCase("") || terminal.equalsIgnoreCase("_ASKED_TO_INSTRUCTOR_")) {
            System.out.println("Asked for the Terminal to the instructor");
            return;
        }
        
        JSONObject signIn = new JSONObject();
        
        signIn.put("userName", userName);
        signIn.put("password", password);
        signIn.put("terminal", terminal);
        
        String jsonSignIn = signIn.toString();
        String apiUrl = BASE_API_URL + "sign-in";
        
        HttpSimpleCurlPost httpSimpleCurlPost = new HttpSimpleCurlPost();
        HttpPost httpPost = httpSimpleCurlPost.setConnectivity(apiUrl);
        httpSimpleCurlPost.execRequest(jsonSignIn, httpPost);
    }
    
    HttpPost setConnectivity(String apiUrl) {
        HttpPost httpPost = new HttpPost(apiUrl);
        
        httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
        httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Authorization", "Bearer " + BASE_API_KEY);
                
        return httpPost;
    }
    
    void execRequest(String jsonString, HttpPost httpPost) {
        try {
            doHttpRequest(jsonString, httpPost);
        } catch(UnsupportedEncodingException e) {
            System.out.println("Error in encoding the URL : " + e.toString());
        } catch(IOException e) {
            System.out.println("Error in I/O : " + e.toString());
        } catch(Exception e) {
            System.out.println("Error in sending the request : " + e.toString());
        } finally {
            httpPost.releaseConnection();
        }
    }
    
    void doHttpRequest(String jsonString, HttpPost httpPost) throws UnsupportedEncodingException, IOException {
        HttpResponse httpResponse = null;
        
        String line = "";
        StringBuffer resultBuffer = new StringBuffer();
        List <NameValuePair> valuePair = new ArrayList <NameValuePair>();
        
        valuePair.add(new BasicNameValuePair("Content-Type", "application/x-www-form-urlencoded"));
        
        AbstractHttpEntity httpEntity = new UrlEncodedFormEntity(valuePair, HTTP.UTF_8);
        httpEntity.setContentType("application/x-www-form-urlencoded; charset=UTF-8");
        httpEntity.setContentEncoding("UTF-8");
        
        // httpPost.setEntity(new StringEntity(jsonString));
        // httpPost.setEntity(new UrlEncodedFormEntity(valuePair, HTTP.UTF_8));
        
        HttpClient httpClient = HttpClientBuilder.create().build();
        httpResponse = httpClient.execute(httpPost);
        
        System.out.println("Request parameters from my device : " + jsonString);
        System.out.println("Response code from the API server : " + httpResponse.getStatusLine().getStatusCode());
        
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
        
        while((line = bufferedReader.readLine()) != null) {
            resultBuffer.append(line);
        }
        
        System.out.println(resultBuffer.toString());
    }
}