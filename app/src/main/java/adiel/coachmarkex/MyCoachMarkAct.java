package adiel.coachmarkex;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import adiel.customcornedbeef.CoachMark;
import adiel.customcornedbeef.WishTripPunchHoleCoachMark;


public class MyCoachMarkAct extends AppCompatActivity {

    private CoachMark mPunchHoleCoachMark;
    FrameLayout fl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_coach_mark);



        CoachMark.CoachMarkBuilder coachMarkBuilder = createBuilder(R.id.one);

        displayCoachMark(coachMarkBuilder);



    }
    private void displayCoachMark(CoachMark.CoachMarkBuilder coachMarkBuilder){
        mPunchHoleCoachMark= coachMarkBuilder.build();

        getWindow().getDecorView().getRootView().post(new Runnable() {
            @Override
            public void run() {
                mPunchHoleCoachMark.show();
            }
        });
    }

    private CoachMark.CoachMarkBuilder createBuilder(int target){
        final Context context = getApplicationContext();
        final View anchorLinearLayoutHoldButton = findViewById(R.id.activity_my_coach_mark);

        LinearLayout punchholeContent = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.wish_trip_xoach_mark_content, null);
        View [] views = getViews(new int[]{R.id.one,R.id.two,R.id.three,R.id.four});

        CoachMark.CoachMarkBuilder coachMarkBuilder = new WishTripPunchHoleCoachMark.PunchHoleCoachMarkBuilder(
                context, anchorLinearLayoutHoldButton, punchholeContent,views)
                .setTargetView(views[0])
                .setOnTargetClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(context, "The target punch hole clicked!", Toast.LENGTH_SHORT).show();
                        mPunchHoleCoachMark.dismiss();
                    }
                })
                .setOnGlobalClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(context, "The coach mark clicked!", Toast.LENGTH_SHORT).show();
                    }
                })
                .setOnNextBtnListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       // mPunchHoleCoachMark.changeTarget(newTArget);
                    }
                })
                //.setNextView(newTArget)
                .setTimeout(0);


        return coachMarkBuilder;
    }
    private View[] getViews(int [] ids){
        View[] views = new View[ids.length];
        for (int i = 0; i < ids.length; i++) {
            views[i]=findViewById(ids[i]);
        }
        return views;
    }


}
