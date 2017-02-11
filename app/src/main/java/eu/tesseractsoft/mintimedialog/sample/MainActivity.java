package eu.tesseractsoft.mintimedialog.sample;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

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
}
