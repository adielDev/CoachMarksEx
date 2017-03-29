package adiel.coachmarkex;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import adiel.customcornedbeef.CoachMark;
import adiel.customcornedbeef.MultiPunchHoleCoachMark;


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
        View [] viewFirstSession = getViews(new int[]{R.id.one,R.id.three,R.id.four});
        View [] viewSecondSession = getViews(new int[]{R.id.two});
        View[][] views = new View[][]{viewFirstSession,viewSecondSession};

        CoachMark.CoachMarkBuilder coachMarkBuilder = new MultiPunchHoleCoachMark.PunchHoleCoachMarkBuilder(
                context, anchorLinearLayoutHoldButton, punchholeContent)
                .setTargetView(views)
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
