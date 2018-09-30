package com.strongholdgames.gameassistant;

import android.app.ListActivity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class TutorialActivity extends ListActivity {
    private static final String TAG = "SHG_" + TutorialActivity.class.getSimpleName();
    private List<Tutorial> tutorials;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        tutorials = new ArrayList<>();
        tutorials.add(new Tutorial("Captain", R.drawable.sc_captain, R.string.captain, "wBtvuFGmeOY"));
        tutorials.add(new Tutorial("Engineering", R.drawable.sc_engineering, R.string.engineering, "MreFAKMoJHI"));
        tutorials.add(new Tutorial("Helm", R.drawable.sc_helm, R.string.helm, "_sCK1_j6rmA"));
        tutorials.add(new Tutorial("Sensors", R.drawable.sc_sensors, R.string.sensors, "GlVN7GjmklM"));
        tutorials.add(new Tutorial("Weapons", R.drawable.sc_weapons, R.string.weapons, "GDMjSs63HfQ"));
        tutorials.add(new Tutorial("Shields", R.drawable.sc_shields, R.string.shields, "x9dZ0ovbQqo"));
        tutorials.add(new Tutorial("Tractor Beams", R.drawable.sc_tractorbeams, R.string.tractorbeams, "UP1kIlBmaNc"));
        tutorials.add(new Tutorial("Jump", R.drawable.sc_jumpdrive, R.string.jumpdrive, "Rk6q2mvz5YE"));
        tutorials.add(new Tutorial("Damage Control", R.drawable.sc_damagecontrol, R.string.damagecontrol, "3sUOV6RTI4E"));

        TutorialAdapter adapter = new TutorialAdapter(this, R.layout.tutrow, tutorials);
        setListAdapter(adapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Tutorial tutorial = tutorials.get(position);
        String ytid = tutorial.youtube;
        if (tutorial.youtube.length() > 0) {
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + ytid));
            List<ResolveInfo> list = getPackageManager().queryIntentActivities(i, PackageManager.MATCH_DEFAULT_ONLY);
            if (list.size() == 0) {
                // default youtube app not present or doesn't conform to the standard we know
                // use our own activity
                i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + ytid));
            }
            startActivity(i);
        } else {
            Toast.makeText(this, R.string.comingsoon, Toast.LENGTH_LONG).show();
        }
    }
}
