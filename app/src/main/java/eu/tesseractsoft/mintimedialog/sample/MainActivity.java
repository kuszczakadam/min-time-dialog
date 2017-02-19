package eu.tesseractsoft.mintimedialog.sample;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import butterknife.ButterKnife;
import butterknife.OnClick;
import eu.tesseractsoft.mintimedialog.MinTimeDialog;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.open_tests:
                // Open Testing Activity
                startActivity(new Intent(this, TestingActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @OnClick(R.id.btnSimpleShow)
    public void simpleShow(){
        MinTimeDialog dialog = MinTimeDialog.createMinTimeDialog(this, "Simple processing", 2000);
        dialog.show();
        dialog.dismiss();
    }

    @OnClick(R.id.btnSimple2Show)
    public void simple2Show(){
        final MinTimeDialog dialog = MinTimeDialog.createMinTimeDialog(this, "Simple 2 processing", 2000);
        dialog.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
            }
        }, 4000);

    }

    private void options(){
        MinTimeDialog dialog = new MinTimeDialog(this);
        dialog.setMinShownTimeMs(0);//in millisec
        dialog.setSilentDismiss(false);
        dialog.setAutoDismissAfterMinShownTime(false);
        dialog.show();
        dialog.dismissForced();
    }
}
