package fixme.fixme2;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SendPOST implements Runnable {
    private Map<String, String> map;
    private String endpoint;
    SendPOST(String endpoint, Map<String, String> map) {
        this.map = map;
        this.endpoint = endpoint;
    }
    @Override
    public void run() {
        HttpParams httpParameters = new BasicHttpParams();
// Set the timeout in milliseconds until a connection is established.
// The default value is zero, that means the timeout is not used.
        int timeoutConnection = 3000;
        HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
// Set the default socket timeout (SO_TIMEOUT)
// in milliseconds which is the timeout for waiting for data.
        int timeoutSocket = 5000;
        HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

// create HttpClient
        HttpClient httpclient = new DefaultHttpClient(httpParameters);
        HttpPost httppost = new HttpPost(endpoint);

// Request parameters and other properties.
        try {
            List<NameValuePair> params = new ArrayList<NameValuePair>(2);
            for(Map.Entry<String, String> e : map.entrySet())
                params.add(new BasicNameValuePair(e.getKey(), e.getValue()));



            httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
            if(LoginData.logged) {
                httppost.setHeader("Username", LoginData.username);
                httppost.setHeader("Password", LoginData.password);
            }
//Execute and get the response.
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                InputStream instream = entity.getContent();
                try {
                    // do something useful
                } finally {
                    instream.close();
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}