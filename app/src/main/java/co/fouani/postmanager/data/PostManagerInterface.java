package co.fouani.postmanager.data;

import java.util.List;

import co.fouani.postmanager.models.Post;
import co.fouani.postmanager.models.PostBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

//using the magic of retrofit, this interface will help us easily call the methods necessary for our application
public interface PostManagerInterface {

    //get all posts
    @GET("/posts")
    public Call<List<Post>> getAll();

    //get posts of certain user by userid
    @GET("/posts")
    public Call<List<Post>> getByUserId(@Query("userId") int userId);

    //get details of a particular post by id
    @GET("/posts/{id}")
    public Call<Post> getDetails(@Path("id") int id);

    //add a post
    @POST("/posts")
    public Call<Post> create(@Body PostBody post);


    //update a post
    @PUT("/posts/{id}")
    public Call<Post> update(@Path("id") int id, @Body PostBody post);


    //delete a post
    @DELETE("/posts/{id}")
    public Call<Post> delete(@Path("id") int id);


}
