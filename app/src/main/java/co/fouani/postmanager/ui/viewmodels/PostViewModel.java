package co.fouani.postmanager.ui.viewmodels;

import static co.fouani.postmanager.utils.Utility.exists;
import static co.fouani.postmanager.utils.Utility.shortenString;

import androidx.annotation.NonNull;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.lifecycle.MutableLiveData;

import co.fouani.postmanager.BR;
import co.fouani.postmanager.data.PostManagerClient;
import co.fouani.postmanager.models.Post;
import co.fouani.postmanager.models.PostBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//we will use this view model in two places, in the post edit sheet, and in the post details sheet.
public class PostViewModel extends BaseObservable {
    //the actual post
    Post post;

    //upon creation or edit of a post, the API will return either null or an actual post
    // this is an observable variable that we will use to know if the creation/update process have succeeded or failed
    public MutableLiveData<Post> saveResponse = new MutableLiveData<>();

    //this property will be two-way bounded to the progress bar
    @Bindable
    public boolean isProcessing = false;

    //in the details activity, this will serve as trimmed or full body of a post.
    @Bindable
    public String shortBody = "";

    //In the details activity, this function will be called by the button "Show More" button.
    public void toggleBody() {
        if (post != null && exists(post.getBody())) {
            shortBody = shortenString(post.getBody(), shortBody);
            //when shortBody is updated, we must notify any view that depends on it.
            notifyPropertyChanged(BR.shortBody);
        }
    }

    //this constructor will ensure that always this.post is not null, even when we are creating a new post.
    public PostViewModel(Post post) {
        this.post = ((post == null) ? new Post() : post);
        toggleBody();
    }

    //In the Post Bottom Sheet, use this, to determine the label, and to decide role of the "Save" button
    public boolean isCreate() {
        return post.getId() < 1;
    }


    //this function will help us block clicks, when the form is not right. for example when the title or body is empty
    @Bindable
    public boolean isSavable() {
        return exists(post.getTitle()) && exists(post.getBody()) && !isProcessing;
    }

    //getTitle will ensure that there is no nulls.
    @Bindable
    public String getTitle() {
        return post == null ? "" : post.getTitle();
    }

    //getBody will ensure that there is no nulls.
    @Bindable
    public String getBody() {
        return post == null ? "" : post.getBody();
    }

    //when the user sets a value, update post's title. also check if the form is now savable.
    public void setTitle(String title) {
        if (!post.getTitle().equals(title)) {
            post.setTitle(title);
            notifyPropertyChanged(BR.title);
            notifyPropertyChanged(BR.savable);
        }
    }

    //when the user sets a value, update post's body. also check if the form is now savable.
    public void setBody(String body) {
        if (!post.getBody().equals(body)) {
            post.setBody(body);
            notifyPropertyChanged(BR.body);
            notifyPropertyChanged(BR.savable);
        }
    }

    //when saving, update the value of isProcessing and notify views depending on its value.
    public void onSave() {
        isProcessing = true;
        notifyPropertyChanged(BR.isProcessing);

        //prepare the POST's body
        PostBody postBody = new PostBody(post.getTitle(), post.getBody());

        //execute the POST function with the above prepared post body, using the respective method (create/update)
        // and finally, the same callback will be used.
        if (isCreate())
            PostManagerClient.getInstance().create(postBody).enqueue(callback);
        else
            PostManagerClient.getInstance().update(post.getId(), postBody).enqueue(callback);
    }

    //this callback will update saveResponse with the suitable value, which will in turn update any
    // observer listening to its changes.
    //eventually isProcessing is no more, and views depending on it will be notified about that.
    Callback<Post> callback = new Callback<Post>() {
        @Override
        public void onResponse(@NonNull Call<Post> call, @NonNull Response<Post> response) {
            if (response.isSuccessful()) {
                saveResponse.setValue(response.body());
            } else
                saveResponse.setValue(null);
            isProcessing = false;
            notifyPropertyChanged(BR.isProcessing);
        }

        @Override
        public void onFailure(Call<Post> call, Throwable t) {
            saveResponse.setValue(null);
            isProcessing = false;
            notifyPropertyChanged(BR.isProcessing);
        }
    };

}
