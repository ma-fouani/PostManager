package co.fouani.postmanager.ui.customviews;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import co.fouani.postmanager.R;
import co.fouani.postmanager.databinding.SheetPostBinding;
import co.fouani.postmanager.models.Post;
import co.fouani.postmanager.ui.activities.MainActivity;
import co.fouani.postmanager.ui.viewmodels.PostViewModel;

public class PostEditSheet extends BottomSheetDialog {

    //necessary for broadcasting messages
    AppCompatActivity activity;
    //View Model suitable for this sheet
    PostViewModel viewModel;

    private PostEditSheet setActivity(AppCompatActivity activity) {
        this.activity = activity;
        return this;
    }

    private PostEditSheet setPost(Post post) {
        //setup the view model and pass the post to it.
        viewModel = new PostViewModel(post);
        return this;
    }

    public PostEditSheet(@NonNull Context context) {
        super(context);
    }

    public PostEditSheet(@NonNull Context context, int theme) {
        super(context, theme);
    }

    protected PostEditSheet(@NonNull Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);

    }


    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        //inflate the sheet with the suitable layout
        View sheetView = getLayoutInflater().inflate(R.layout.sheet_post, null);

        //observe any changes to saveResponse, if value is null, it means that the save process didn't succeed.
        // if not, then we succeeded, and we will send a broadcast to the main activity in order to update the list.
        viewModel.saveResponse.observe(activity,
                result -> {
                    if (result == null) {
                        Toast.makeText(getContext(), "Failed to save changes", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent(MainActivity.REFRESH);
                        intent.putExtra(MainActivity.POST, viewModel.saveResponse.getValue());
                        activity.sendBroadcast(intent);
                        dismiss();
                        Toast.makeText(getContext(), "Saved Successfully!", Toast.LENGTH_SHORT).show();
                    }
                });

        //binding initialization.
        SheetPostBinding binding = SheetPostBinding.bind(sheetView);

        //pass the view model to the binding object in order to set values, hide/show views, setup labels...
        binding.setVm(viewModel);

        //since close simply dismisses the sheet, and it's not a data related method,
        // we can't rely on the viewModel to do that; it's not its job. we simply handle it
        // from the sheet itself.
        binding.close.setOnClickListener(view -> dismiss());

        //now that our sheet view is ready, we'll use it as a content view.
        setContentView(sheetView);
    }

    //this is just to simplify the call of the bottom sheet to initialize and show.
    public static void show(AppCompatActivity activity, Post post) {
        new PostEditSheet(activity).setActivity(activity).setPost(post).show();
    }
}
