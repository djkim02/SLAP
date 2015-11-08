package com.djkim.slap.menubar;

import android.app.Activity;
import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.djkim.slap.R;
import com.djkim.slap.models.User;
import com.djkim.slap.models.Utils;

import java.io.InputStream;
import java.net.URL;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Calvin on 10/27/15.
 */
public class menuProfile extends Fragment {

    private View rootview;
    private CircleImageView m_imageView;
    private Bitmap bitmap;
    private URL urlImage;
    private ArrayAdapter<String> listAdapter;
    User user;

    public menuProfile() {
        bitmap = null;
        urlImage = null;
        user = Utils.get_current_user();
    }

    public menuProfile(View rootview) {
        this.rootview = rootview;
        //listAdapter = new ArrayAdapter<String>(this, R.layout.menu_profile, user.get_hacker_skills());
        //Note: If you want to add something do listAdapter.add("String");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.menu_profile, container, false);
        m_imageView = (CircleImageView) rootview.findViewById(R.id.imageView1);
        TextView name = (TextView) rootview.findViewById(R.id.Name);
        //TextView age = (TextView) rootview.findViewById(R.id.Age);
        //TextView location = (TextView) rootview.findViewById(R.id.Location);
        name.setText(user.get_name());
        //location.setText(user.get_location());
        //age.setText(user.get_age());


        try {
            urlImage = new URL("https://graph.facebook.com/"+ user.get_facebook_id().toString() +"/picture?type=large");
            Log.d("Error:", "Value:" + urlImage.toString());
            bitmap = BitmapFactory.decodeStream(urlImage.openConnection().getInputStream());
            Log.d("Error:", "Value:" + bitmap.toString());
        } catch(Exception e) {
            e.printStackTrace();
        }
        //Log.d("Error:", "Value:" + bitmap.toString() );
        m_imageView.setImageBitmap(bitmap);

        return rootview;
    }
}
