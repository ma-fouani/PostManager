package co.fouani.postmanager.data;

import java.util.List;

import co.fouani.postmanager.models.Post;
import co.fouani.postmanager.models.PostBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;

//this is to centralize the network and data requests.
public class PostManagerClient {
    //base url from which we will retrieve data
    private static final String BASE_URL = "https://jsonplaceholder.typicode.com";

    //only one instance of our client will be created over the whole application.
    private static PostManagerClient instance;
    PostManagerInterface postsInterface;

    //instantiate the retrofit object which will help us send the requests to the API
    public PostManagerClient() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        postsInterface = retrofit.create(PostManagerInterface.class);
    }

    //make sure we only initialize INSTANCE only once
    public static PostManagerClient getInstance() {
        if (instance == null) {
            instance = new PostManagerClient();
        }
        return instance;
    }

    //get posts created by a single user.
    public Call<List<Post>> getByUserId() {
        //for the sake of testing, I'll make the userId=1
        return postsInterface.getByUserId(1);
    }

    //get all posts.
    public Call<List<Post>> getAll() {
        return postsInterface.getAll();
    }

    //get details of a post
    public Call<Post> getDetails(int id) {
        return postsInterface.getDetails(id);
    }

    //create a new post
    public Call<Post> create(PostBody post) {
        return postsInterface.create(post);
    }

    //update a certain post
    public Call<Post> update(int id, PostBody post) {
        return postsInterface.update(id, post);
    }

    //delete a certain post by id
    public Call<Post> delete(int id) {
        return postsInterface.delete(id);
    }

}
