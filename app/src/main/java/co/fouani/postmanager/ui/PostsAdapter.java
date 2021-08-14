package co.fouani.postmanager.ui;

import static co.fouani.postmanager.ui.activities.MainActivity.POSITION;
import static co.fouani.postmanager.utils.Utility.exists;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import co.fouani.postmanager.R;
import co.fouani.postmanager.models.Post;
import co.fouani.postmanager.ui.activities.MainActivity;
import co.fouani.postmanager.ui.activities.PostDetailsActivity;
import co.fouani.postmanager.ui.customviews.SwipeRevealLayout;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.PostViewHolder> {
    //list of posts to be displayed
    public List<Post> posts = new ArrayList<>();
    public Activity act;

    //everytime this is called, it will append the list of posts.
    public void setList(List<Post> posts) {
        if (exists(posts)) this.posts.addAll(posts);
    }

    //reference to the Activity holding the recyclerview is needed to start Details Activity, and send broadcasts
    public PostsAdapter(Activity act) {
        this.act = act;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate the suitable layout
        View holder = LayoutInflater.from(parent.getContext()).inflate(R.layout.swipable_list_item, parent, false);
        return new PostViewHolder(holder);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, final int position) {
        //bind the post to the current view holder
        holder.bind(posts.get(position));

        //when a post is clicked/tapped, open the details activity.
        holder.mainLayout.setOnClickListener(view -> {
            Intent intent = new Intent(act, PostDetailsActivity.class);
            //current post is passed through the Extras of the intent
            intent.putExtra(MainActivity.POST, posts.get(position));
            act.startActivity(intent);
            //if this row is opened, close it.
            holder.swiper.close(true);
        });

        //when edit is clicked, send a broadcast to the main activity to open this post in the
        //bottom sheet. Actually you just send it to whoever is listening to this action,
        // and it's up to that particular activity to do its job: in the future, we might want to
        // edit this post in a completely different manner, so, we only change what the receiver does on the other end.
        holder.edit.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.EDIT_POST);
            intent.putExtra(MainActivity.POST, posts.get(position));
            act.sendBroadcast(intent);
            holder.swiper.close(true);
        });
        //similar to above.
        holder.delete.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.DELETE_POST);
            intent.putExtra(POSITION, position);
            act.sendBroadcast(intent);
            holder.swiper.close(true);
        });

        //these lines are to enable animating the rows upon scrolling up and down.
        setAnimation(holder.itemView, position);
    }

    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position != prev_position) {
            Animation animation = AnimationUtils.loadAnimation(act, position > prev_position ? R.anim.up_from_bottom : R.anim.bottom_from_top);
            viewToAnimate.startAnimation(animation);
            prev_position = position;
        }

    }

    private int prev_position = 0;

    @Override
    public int getItemCount() {
        return posts.size();
    }

    //the post view holder resembles what views each row has.
    // here we can assign values, after inflating the needed layout inside the adapter's onCreateViewHolder
    class PostViewHolder extends RecyclerView.ViewHolder {
        TextView title, body;
        SwipeRevealLayout swiper;
        View mainLayout, edit, delete;

        //who is who in the layout file.
        public PostViewHolder(@NonNull View view) {
            super(view);
            swiper = view.findViewById(R.id.swiper);
            mainLayout = view.findViewById(R.id.main_layout);
            title = view.findViewById(R.id.title);
            edit = view.findViewById(R.id.edit);
            delete = view.findViewById(R.id.delete);
            body = view.findViewById(R.id.body);
        }

        //assign values to the title and body.
        public void bind(Post post) {
            title.setText(post.getTitle());
            body.setText(post.getBody());
        }

    }

    @Override
    public void onViewDetachedFromWindow(@NotNull PostViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        holder.itemView.clearAnimation();
    }

}
