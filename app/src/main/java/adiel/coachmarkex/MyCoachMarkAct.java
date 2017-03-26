package adiel.coachmarkex;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import adiel.customcornedbeef.CoachMark;
import adiel.customcornedbeef.PunchHoleCoachMark;


public class MyCoachMarkAct extends AppCompatActivity {

    private CoachMark mPunchHoleCoachMark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_coach_mark);

        final Context context = getApplicationContext();
        final View anchorLinearLayoutHoldButton = findViewById(R.id.activity_my_coach_mark);

        TextView punchholeContent = (TextView) LayoutInflater.from(context).inflate(R.layout.sample_customised_punchhole_content, null);
        punchholeContent.setText(R.string.punchhole_message_text);

        mPunchHoleCoachMark = new PunchHoleCoachMark.PunchHoleCoachMarkBuilder(
                context, anchorLinearLayoutHoldButton, punchholeContent)
                .setTargetView(findViewById(R.id.punch_hole_coach_mark_target))
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
                .setTimeout(0)
                .build();

        getWindow().getDecorView().getRootView().post(new Runnable() {
            @Override
            public void run() {
                mPunchHoleCoachMark.show();
            }
        });


    }
}
