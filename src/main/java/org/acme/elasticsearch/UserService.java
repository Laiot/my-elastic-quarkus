package org.acme.elasticsearch;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import org.elasticsearch.client.RestClient;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;

/*
 * This is the business layer of our application.
 * It stores and load the Users from the Elasticsearch Instance.
 * We use Vert.x JsonObject to serialize the Object before sending it to Elasticsearch.
 * In order to deserialize the object from Elasticsearch, we again use Vert.x JsonObject.
 */
@ApplicationScoped
public class UserService {
    
    // We're injecting an Elasticsearch low level RestClient into our service.
    @Inject
    RestClient restClient;

    // Request to add a new User, indexing it by its ID 
    public void index(User user) throws IOException{
        Request request = new Request("PUT","/users/_doc/" + user.id);
        request.setJsonEntity(JsonObject.mapFrom(user).toString());
        restClient.performRequest(request);
    }

    // Request to get an User by its ID
    public User get(String id) throws IOException{
        Request request = new Request("GET", "/users/_doc/" + id);
        Response response = restClient.performRequest(request);
        String responseBody = EntityUtils.toString(response.getEntity());
        JsonObject json = new JsonObject(responseBody);
        return json.getJsonObject("_source").mapTo(User.class);
    }

    public List<User> searchByAge(int age) throws IOException{
        return search("age", Integer.toString(age));
    }

    public List<User> searchByName(String name) throws IOException{
        return search("name", name);
    }

    // Request for general GET by non-unique attributes of User
    public List<User> search(String term, String match) throws IOException{
        Request request = new Request("GET", "/users/_search");

        /*
         * The following constructs a JSON Object like the following:
         * {
         *      "query":{
         *          "match":{
         *              "<term>":"<match>"
         *          }        
         *      }
         * }
         */
        JsonObject queryJson = 
            new JsonObject().put("query", 
            new JsonObject().put("match", 
            new JsonObject().put(term, match)));

        request.setJsonEntity(queryJson.encode());
        Response response = restClient.performRequest(request);
        String responseBody = EntityUtils.toString(response.getEntity());
        JsonObject json = new JsonObject(responseBody);
        JsonArray hits = json.getJsonObject("hits").getJsonArray("hits");
        List<User> results = new ArrayList<>(hits.size());
        for (int i = 0; i < hits.size(); i++){
            JsonObject hit = hits.getJsonObject(i);
            User user = hit.getJsonObject("_source").mapTo(User.class);
            results.add(user);
        }
        return results;
    }
}
