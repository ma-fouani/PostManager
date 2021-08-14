package co.fouani.postmanager.utils;

import android.app.Activity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.databinding.BindingAdapter;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.w3c.dom.Text;

import java.util.Collection;

import co.fouani.postmanager.R;
import co.fouani.postmanager.models.Post;

public class Utility {

    //this function is called from within the layouts to help us decide whether to show or hide
    // certain views (progress bar...)
    public static int getVisibility(boolean isVisible) {
        return isVisible ? View.VISIBLE : View.GONE;
    }

    //this function is to toggle between long and short value of certain string. 60 is the limit.
    public static String shortenString(String source, String alt) {
        return alt.equals(source) || !exists(alt) ? source.substring(0, 60) + "..." : source;
    }

    //below functions check if a string or a collection are null or empty.
    public static boolean exists(String v) {
        return v != null && !v.trim().isEmpty();
    }

    public static boolean exists(Collection<?> v) {
        return v != null && !v.isEmpty();
    }

}
