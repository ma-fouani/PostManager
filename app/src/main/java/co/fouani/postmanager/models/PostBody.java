package co.fouani.postmanager.models;

//POST body will be used with HTTP POST methods
public class PostBody {
    public int userId;
    public String title;
    public String body;

    public PostBody(String title, String body) {
        this.title = title;
        this.body = body;
        this.userId = 1; //for testing purposes.
    }
}
