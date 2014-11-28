package uos.jhoffjann.server.logic;

/**
 * Created by Jannik on 28.11.14.
 */

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.codehaus.jackson.map.util.JSONPObject;

import java.net.URI;

/**
 * http://en.wikipedia.org/w/api.php?format=json&action=query&prop=revisions&titles=Hollywood&rvprop=content&rvsection=0&rvparse&continue
 */

public class WikiHandler {
    private static final String URL = "http://de.wikipedia.org/w/api.php";

    public static String getResponse(String name) {
        try {

            // build URL
            URIBuilder uriBuilder = new URIBuilder(URL);
            uriBuilder.addParameter("format", "json");
            uriBuilder.addParameter("action", "query");
            uriBuilder.addParameter("prop", "revisions");
            uriBuilder.addParameter("titles", name);
            uriBuilder.addParameter("rvprop", "content");
            uriBuilder.addParameter("rvsection", "0");
            uriBuilder.addParameter("rvparse", null);
            uriBuilder.addParameter("continue", null);

            URI uri = uriBuilder.build();
            HttpClient httpClient = HttpClientBuilder.create().build();
            HttpGet get = new HttpGet(uri);

            // get response
            HttpResponse response = httpClient.execute(get);

            int statusCode = response.getStatusLine().getStatusCode();

            if (statusCode != 200) {
                return "No further information available!";
            }

            if (response.getEntity() != null) {
                HttpEntity responseEntity = response.getEntity();
                String resStr = EntityUtils.toString(responseEntity);

                // parse to JSON
                JsonObject result = new JsonObject(resStr);
                String token = result.get()
            }


        } catch (Exception e)

        {
        }

    }
    // TODO get first n characters from WIKI
    return null;
}
}
