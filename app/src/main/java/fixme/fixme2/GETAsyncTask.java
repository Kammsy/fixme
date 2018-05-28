package fixme.fixme2;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.BufferedReader;
import java.io.DataInput;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class GETAsyncTask  extends AsyncTask<String, Void, String> {
    private String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }
    private GetResultable resultReceiver;

    public GETAsyncTask(GetResultable resultReceiver) {
        this.resultReceiver = resultReceiver;
    }

    public String GET(String url) {
        InputStream inputStream = null;
        String result = "";
        try {



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

            HttpGet request = new HttpGet(url);
            System.out.println("Robie taska");
            if(LoginData.logged) {
                request.setHeader("Username", LoginData.username);
                request.setHeader("Password", LoginData.password);
            }


            // make GET request to the given URL
            HttpResponse httpResponse = httpclient.execute(request);

            // receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // convert inputstream to string
            if (inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";
            System.out.println("Result = " + result);

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        return result;
    }

    @Override
    protected String doInBackground(String... urls) {

        return GET(urls[0]);
    }

    // onPostExecute displays the results of the AsyncTask.
    @Override
    protected void onPostExecute(String result) {
        //Toast.makeText(getBaseContext(), "Received!", Toast.LENGTH_LONG).show();
        System.out.println("Odebrano " + result);

        resultReceiver.ProcessResults(result);
    }
}
