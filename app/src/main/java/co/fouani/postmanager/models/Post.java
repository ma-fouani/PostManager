package co.fouani.postmanager.models;

import static co.fouani.postmanager.utils.Utility.exists;

import java.io.Serializable;

//we implement serializable since we need to pass post objects inside the extras of intents
public class Post implements Serializable {
    private int id;
    private int userId;
    private String title;
    private String body;

    public Post() {
        //since creating an ID for a post has to be done by the server
        // let's mark any post created locally with a negative id
        // that way we can differentiate created_posts from edited_posts.
        id = -1;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getUserId() {
        return userId;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return exists(title) ? title : "";
    }

    public String getBody() {
        return  exists(body) ? body : "";
    }
}
