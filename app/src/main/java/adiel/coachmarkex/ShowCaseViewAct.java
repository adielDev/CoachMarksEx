package adiel.coachmarkex;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.github.amlcurran.showcaseview.ShowcaseView;

import adiel.coachmarkex.fromsample.ToolbarActionItemTarget;

public class ShowCaseViewAct extends AppCompatActivity {


    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_case_view);
        toolbar= (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


            //ViewTarget navigationButtonViewTarget = ViewTargets.navigationButtonViewTarget(toolbar);
        createCoachMark(R.id.action_search);
        //createCoachMark(R.id.action_tune);


    }

    private void createCoachMark(int target) {
        ToolbarActionItemTarget searcItem = new ToolbarActionItemTarget(toolbar,R.id.action_search);
        new ShowcaseView.Builder(this)
                .withMaterialShowcase()
                .setTarget(searcItem )
                //.setStyle(R.style.CustomShowcaseTheme2)
                .setContentText("Here's how to highlight items on a toolbar")
                .build()
                .show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_explore, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

}
