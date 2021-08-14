package co.fouani.postmanager.ui.activities;

import static co.fouani.postmanager.ui.activities.MainActivity.POST;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import co.fouani.postmanager.R;
import co.fouani.postmanager.databinding.ActivityPostDetailsBinding;
import co.fouani.postmanager.models.Post;
import co.fouani.postmanager.ui.viewmodels.PostViewModel;

public class PostDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //get the value of post passed via intent
        Post post = (Post) getIntent().getSerializableExtra(POST);

        //instantiate the viewModel using the above post.
        PostViewModel viewModel = new PostViewModel(post);

        //create binding using ActivityPostDetailsBinding, which was automatically created when we created
        // the layout called activity_post_details with layout tags
        ActivityPostDetailsBinding binding = ActivityPostDetailsBinding.inflate(getLayoutInflater());

        //pass the viewModel to the binding object... and it will do the magic of setting values, changing labels, setting actions...
        binding.setPost(viewModel);
        //now that our binding is ready, we will use it as a Content View.
        setContentView(binding.getRoot());

        //set the status bar to white.
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(Color.WHITE);

        //to show the back button. and since Main activity is declared as the parent of this activity in the Manifest,
        // when back is clicked, main activity will be its destination.
        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}