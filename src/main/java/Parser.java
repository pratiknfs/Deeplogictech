
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

public class Parser {
    private final String baseURL = "http://somebaseurl/gettimeStories";

    public ArrayList<Model> parseResponseStream(String stream) {
        ArrayList<Model> jsonResult = new ArrayList<>();

        try {

            JSONArray jsonarray = new JSONArray(stream);
            for (int i = 0; i < jsonarray.length(); i++) {
                Model tempDetail = new Model();
                JSONObject jsonobject = jsonarray.getJSONObject(i);
                String title = jsonobject.getString("title");
                String linkString = jsonobject.getString("link");
                URL linkUrl = new URL(linkString);
                tempDetail.setTitle(title);
                tempDetail.setLink(linkUrl);
                jsonResult.add(tempDetail);
            }
        } catch (NullPointerException | JSONException | MalformedURLException e) {
            //handle
        }
        return jsonResult;
    }

    public static String invokeGetMethod(String urlToRead) throws Exception {
        StringBuilder result = new StringBuilder();
        URL url = new URL(urlToRead);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(conn.getInputStream()))) {
            for (String line; (line = reader.readLine()) != null; ) {
                result.append(line);
            }
        }
        return result.toString();
    }

    public void main(String[] args) throws Exception
    {
        JSONObject responseStructure = new JSONObject();
        String project = invokeGetMethod(baseURL);
        ArrayList<Model> resultList = new ArrayList<>();
        resultList = parseResponseStream(project);
        responseStructure.put("latest news", resultList);
        System.out.println(responseStructure);


    }
}
