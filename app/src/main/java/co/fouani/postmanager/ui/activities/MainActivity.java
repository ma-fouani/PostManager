package co.fouani.postmanager.ui.activities;

import static co.fouani.postmanager.utils.Utility.exists;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import co.fouani.postmanager.R;
import co.fouani.postmanager.models.Post;
import co.fouani.postmanager.ui.viewmodels.MainActivityViewModel;
import co.fouani.postmanager.ui.customviews.PostEditSheet;
import co.fouani.postmanager.ui.PostsAdapter;

//Main Activity
public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    PostsAdapter adapter;
    MainActivityViewModel viewModel;

    //The below static variables will be used as keys and actions for our intents.
    public static String REFRESH = "refresh";
    public static String EDIT_POST = "edit_post";
    public static String DELETE_POST = "delete_post";
    public static String POST = "post";
    public static String POSITION = "position";

    //Since our recyclerView resides inside MainActivity, any thing from outside for any reason that needs to
    //refresh it, will have to send a broadcast using the REFRESH action
    BroadcastReceiver refreshReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            getPosts();
        }
    };

    //Send a Broadcast with the Post that you need to update, and it will be transferred to the
    // PostEditBottomSheet
    BroadcastReceiver editReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Post post = (Post) intent.getSerializableExtra(POST);
            if (post != null) PostEditSheet.show(MainActivity.this, post);
        }
    };

    //Deletion has a small extra step in it. you will get a dialog to confirm the deletion,
    // then upon confirmation, the post will be removed from the list.
    BroadcastReceiver deleteReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int pos = intent.getIntExtra(POSITION, -1);
            if (pos >= 0) {
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context)
                        .setTitle("Delete Post")
                        .setMessage("You are about to delete this post. Are you sure you want to continue?")
                        .setNegativeButton("Cancel", null)
                        .setPositiveButton("Delete",
                                (dialog, which) -> {
                                    viewModel.deletePost(adapter.posts.get(pos).getId());
                                    adapter.posts.remove(pos);
                                    adapter.notifyDataSetChanged();
                                })
                        .setCancelable(true);

                AlertDialog alert = alertBuilder.create();
                alert.setOnShowListener(dialogInterface -> {
                    alert.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(R.color.black);
                    alert.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(R.color.black);
                });
                alert.show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //this is to make the status bar white.
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        getWindow().setStatusBarColor(Color.WHITE);

        setupRecyclerView();
        setupViewModel();

        //register the broadcast receivers. Now they will be listening via the correct action,
        // and will do their job upon request.
        registerReceiver(refreshReceiver, new IntentFilter(REFRESH));
        registerReceiver(deleteReceiver, new IntentFilter(DELETE_POST));
        registerReceiver(editReceiver, new IntentFilter(EDIT_POST));
    }

    //this is called from the CreatePost FAB.
    public void showCreatePostSheet(View v) {
        PostEditSheet.show(MainActivity.this, null);
    }

    //Get... posts!
    private void getPosts() {
        viewModel.getUserPosts();
    }

    //sets up the view model. eh walla!
    private void setupViewModel() {
        viewModel = new MainActivityViewModel();
        //observe the mutable live data list, any change to it must be reflected to the UI
        viewModel.posts.observe(this, posts -> {
            if (exists(posts)) {
                //in case of success, pass the posts to the adapter which in turn will update
                // the recyclerview content.
                adapter.setList(posts);
                adapter.notifyDataSetChanged();
            } else if (posts == null)
                Toast.makeText(this, "Sorry, failed to get posts.", Toast.LENGTH_LONG).show();
            else Toast.makeText(this, "No posts yet!", Toast.LENGTH_SHORT).show();
        });

        //this will fetch the posts for the first time.
        getPosts();
    }

    //setup the recycler view.
    private void setupRecyclerView() {
        recyclerView = findViewById(R.id.recycler);
        adapter = new PostsAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
    }
}