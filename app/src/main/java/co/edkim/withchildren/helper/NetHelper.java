package co.edkim.withchildren.helper;

/**
 * Created by Edward on 2014-08-24.
 */

import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRoute;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpProtocolParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import co.edkim.withchildren.R;

public abstract class NetHelper {

    static DefaultHttpClient _client;

    public static boolean isOnline(Context context) {
        try {
            ConnectivityManager conMan = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo.State wifi = conMan.getNetworkInfo(1).getState();
            if (wifi == NetworkInfo.State.CONNECTED
                    || wifi == NetworkInfo.State.CONNECTING) {
                return true;
            }

            NetworkInfo.State mobile = conMan.getNetworkInfo(0).getState(); // ConnectivityManager.TYPE_MOBILE
            if (mobile == NetworkInfo.State.CONNECTED
                    || mobile == NetworkInfo.State.CONNECTING) {
                return true;
            }

        } catch (NullPointerException e) {
            return false;
        }

        return false;
    }

    private static DefaultHttpClient getHttpClientInstance() {
        if (_client == null) {
            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));

            BasicHttpParams params = new BasicHttpParams();
            ConnManagerParams.setMaxTotalConnections(params, 100);
            ConnManagerParams.setMaxConnectionsPerRoute(params, new ConnPerRoute() {

                @Override

                public int getMaxForRoute(HttpRoute route) {
                    return 35;
                }

            });
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setUseExpectContinue(params, true);

            ThreadSafeClientConnManager connManager = new ThreadSafeClientConnManager(params, registry);
            _client = new DefaultHttpClient(connManager, params);
        }


        return _client;
    }

    public static String SendRESTRequest(Context context, String url) {
        return SendRESTRequest(context, url, false);
    }

    public static String SendRESTRequest(Context context, String url, boolean isPost) {
        String r = "";
        if (context == null || isOnline(context)) {
            StringBuilder builder = new StringBuilder();
            HttpClient client = getHttpClientInstance();

            try {
                HttpResponse response;

                if (isPost) {
                    HttpPost httpPost = new HttpPost(url);
                    //AuthHelper.getAuthorizationHeader(httpPost);

                    for (Header i : httpPost.getHeaders("Content-Type")) {
                        httpPost.removeHeader(i);
                    }

                    response = client.execute(httpPost);
                } else {
                    HttpGet httpGet = new HttpGet(url);
                    //AuthHelper.getAuthorizationHeader(httpGet);

                    for (Header i : httpGet.getHeaders("Content-Type")) {
                        httpGet.removeHeader(i);
                    }

                    response = client.execute(httpGet);
                }

                StatusLine statusLine = response.getStatusLine();
                int statusCode = statusLine.getStatusCode();
                if (statusCode == 200) {
                    HttpEntity entity = response.getEntity();
                    InputStream content = entity.getContent();
                    BufferedReader reader = new BufferedReader(
                            new InputStreamReader(content));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        builder.append(line);
                    }

                    return builder.toString();
                } else {
                    Log.e("Network", url + " : failed to read content.");
                }
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else
            r = Resources.getSystem().getString(R.string.turn_on_lte_internet);

        return r;
    }
}
