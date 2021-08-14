package co.fouani.postmanager.ui.viewmodels;

import androidx.annotation.NonNull;
import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import co.fouani.postmanager.data.PostManagerClient;
import co.fouani.postmanager.models.Post;
import co.fouani.postmanager.models.PostBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//we will be using this view model to control the main activity
// it extends BaseObservable since it has... well... "Observable" properties.
public class MainActivityViewModel extends BaseObservable {
    //response from the API will be filled in this variable.
    // I could have used ObservableList; but it's not life-cycle aware
    // and that code will be longer.
    public MutableLiveData<List<Post>> posts = new MutableLiveData<>();

    //this will be used to control the progress bar
    @Bindable
    public boolean isProcessing = false;

    //function to get current user's posts.
    public void getUserPosts() {

        //since there is a long process, it's better update our variable
        //and notify any view that depends on its value.
        isProcessing = true;
        notifyPropertyChanged(BR.isProcessing);

        //using Retrofit, we enqueue a new request to the network to get data.
        PostManagerClient.getInstance().getByUserId().enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(@NonNull Call<List<Post>> call, @NonNull Response<List<Post>> response) {
                //success! we have new data! let's save them.
                posts.setValue(response.body());

                //now that our process has stopped, update the variable
                // and also notify dependant views or variables.
                isProcessing = false;
                notifyPropertyChanged(BR.isProcessing);
            }

            @Override
            public void onFailure(@NonNull Call<List<Post>> call, @NonNull Throwable t) {
                //something prevented us from receiving new data.
                posts.setValue(null);
                isProcessing = false;
                notifyPropertyChanged(BR.isProcessing);
            }
        });
    }

    //explanation is similar to the above method. :-)
    public void deletePost(int id) {
        isProcessing = true;
        notifyPropertyChanged(BR.isProcessing);

        PostManagerClient.getInstance().delete(id).enqueue(new Callback<Post>() {
            @Override
            public void onResponse(@NonNull Call<Post> call, @NonNull Response<Post> response) {
                if (response.isSuccessful()) {
                    posts.getValue().add(0, response.body());
                }
                isProcessing = false;
                notifyPropertyChanged(BR.isProcessing);
            }

            @Override
            public void onFailure(@NonNull Call<Post> call, @NonNull Throwable t) {
                isProcessing = false;
                notifyPropertyChanged(BR.isProcessing);
            }
        });
    }

}
