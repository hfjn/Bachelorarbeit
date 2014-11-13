package uos.jhoffjann.ObjFinder.Logic;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.io.File;
import java.util.Date;

/**
 * Created by jhoffjann on 13.11.14.
 */
public class Upload {
    public static String upload(String URL, File image) {
        try {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(URL);

            MultipartEntityBuilder entity = MultipartEntityBuilder.create();
            entity.addTextBody("name", new Date() + "");
            entity.addBinaryBody("file", image);

            httpPost.setEntity(entity.build());
            HttpResponse response = httpClient.execute(httpPost);

            int statusCode = response.getStatusLine().getStatusCode();

            if (statusCode != 200) {
                return "Error: " + statusCode + " Something in the uploading process went wrong";
            }

            if (response.getEntity() != null) {
                HttpEntity responseEntity = response.getEntity();
                String resStr = EntityUtils.toString(responseEntity);

                // parse to JSON
                JSONObject result = new JSONObject(resStr);
                String token = result.getString("message");
                responseEntity.consumeContent();
                return token;
            }

            return "Something went terrible wrong";
        } catch (Exception e)

        {
            e.printStackTrace();
        }
        return null;
    }
}
