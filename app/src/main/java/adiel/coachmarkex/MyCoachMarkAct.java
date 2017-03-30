package adiel.coachmarkex;

import android.content.Context;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import adiel.customcornedbeef.BubbleCoachMark;
import adiel.customcornedbeef.CoachMark;
import adiel.customcornedbeef.MultiPunchHoleCoachMark;


public class MyCoachMarkAct extends AppCompatActivity {

    private CoachMark mPunchHoleCoachMark;
    private CoachMark mBubbleCoachMark;
    FrameLayout fl;
    private Toolbar toolbar;
    private Menu myMenu;
    private View menuButton;
    private ImageButton imageSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_coach_mark);
        toolbar= (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setCustomView(R.layout.toolbar_main_explore);

        CoachMark.CoachMarkBuilder builder = createBuilder();
        displayCoachMark(builder);

//        createBubbleCoachMarks();



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        myMenu =menu;
        inflater.inflate(R.menu.main_explore, menu);
//        imageSearch = (ImageButton) menu.findItem(R.id.action_search);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }


    private void createBubbleCoachMarks() {
        final Context context = getApplicationContext();

        final View anchorTextView = findViewById(R.id.four);

        mBubbleCoachMark = new BubbleCoachMark.BubbleCoachMarkBuilder(
                context, anchorTextView, "This is a bubble coach mark!")
                .setTargetOffset(0.25f)
                .setShowBelowAnchor(true)
                .setPadding(10)
                .setOnShowListener(new CoachMark.OnShowListener() {
                    @Override
                    public void onShow() {
                        Toast.makeText(context, "Bubble coach mark shown!", Toast.LENGTH_SHORT).show();
                    }
                })
                .setOnDismissListener(new CoachMark.OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        Toast.makeText(context, "Bubble coach mark dismissed!", Toast.LENGTH_SHORT).show();
                    }
                })
                .setTimeout(0)
                .build();


        getWindow().getDecorView().getRootView().post(new Runnable() {
            @Override
            public void run() {
                mBubbleCoachMark.show();
            }
        });
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

    private CoachMark.CoachMarkBuilder createBuilder(){
        final Context context = getApplicationContext();
        final View anchorLinearLayoutHoldButton = findViewById(R.id.activity_my_coach_mark);

        LinearLayout punchholeContent = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.wish_trip_xoach_mark_content, null);
        View [] viewFirstSession = getViews(new int[]{R.id.one,R.id.three,R.id.four},new int[]{R.id.action_search});
      //  View [] viewSecondSession = getViews(new int[]{R.id.two},new int[0]);
        //View[][] views = new View[][]{viewFirstSession,viewSecondSession};
        View[][] views = new View[][]{viewFirstSession};

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
    private View[] getViews(int [] layoutViewsIds,int[] toolbarViewsIds){
        View[] views = new View[4];
        int counter=0;
        for (int i = 0; i < layoutViewsIds.length; i++) {
            views[i]=findViewById(layoutViewsIds[i]);
            counter=i;
        }


        for (int j=(counter+1) ; j < toolbarViewsIds.length+counter+1; j++) {
            //View view=toolbar.findViewById(toolbarViewsIds[j-counter-1]);

//            MenuItem item = toolbar.getMenu().findItem(R.id.action_search);
       //     Log.d("adiel","wtf(item)!!! "+imageSearch.toString()+" j="+j);


        }
//        //MyToolbarActionItemTarget myToolbarActionItemTarget = new MyToolbarActionItemTarget(toolbar,R.id.action_search);
//        Log.d("adiel","wtf(item)!!! "+myToolbarActionItemTarget.getView().toString()+" j=");
//        views[3]=myToolbarActionItemTarget.getView();
//      ;
        return views;
    }


    public void oneClicked(View view) {
        Toast.makeText(this, "oneClicked", Toast.LENGTH_SHORT).show();

        CoachMark.CoachMarkBuilder coachMarkBuilder = createBuilder();
        displayCoachMark(coachMarkBuilder);
    }
}
