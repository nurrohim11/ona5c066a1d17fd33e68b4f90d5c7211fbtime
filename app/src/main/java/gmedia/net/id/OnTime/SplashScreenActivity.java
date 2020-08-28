package gmedia.net.id.OnTime;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.AlphaAnimation;

public class SplashScreenActivity extends AppCompatActivity {

    private static boolean splashLoaded = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        AlphaAnimation animation1 = new AlphaAnimation(0f, 1.0f);
        animation1.setDuration(1500);

        if (!splashLoaded) {

            int secondsDelayed = 2;
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
                    finish();
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                }
            }, secondsDelayed * 1000);

        }else{
            //Intent goToMainActivity = new Intent(SplashScreen.this, DaftarVideo.class);
            Intent goToMainActivity = new Intent(SplashScreenActivity.this, LoginActivity.class);
            goToMainActivity.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(goToMainActivity);
            finish();
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        }
    }
}