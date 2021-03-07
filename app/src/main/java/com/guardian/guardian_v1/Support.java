package com.guardian.guardian_v1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class Support extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.appThemeColor));
        }

        TextView link = (TextView) findViewById(R.id.websiteText);
        String linkText = "<a href='http://guardianapp.ir'>وبسایت گاردین</a>";
        link.setText(Html.fromHtml(linkText));
        link.setMovementMethod(LinkMovementMethod.getInstance());
//        // 2) How to place email address
//        TextView email = (TextView) findViewById(R.id.emailText);
//        String emailText = "<a href=\"mailto:support@guardianapp.ir\">ایمیل پشتیبانی</a>";
//        email.setText(Html.fromHtml(emailText));
//        email.setMovementMethod(LinkMovementMethod.getInstance());


        Button backButton = (Button) findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Support.this, Main.class);
                startActivity(i);
                finish();

            }
        });


        // email / telegram / whatsapp
        Button emailBtn = (Button) findViewById(R.id.emailButton);
        emailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                Uri data = Uri.parse("mailto:support@guardianapp.ir?subject=" + Uri.encode("مشکل در استفاده از برنامه"));
                intent.setData(data);
                startActivity(intent);
            }
        });

        Button telegramBtn = (Button) findViewById(R.id.telegramButton);
        telegramBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent telegram = new Intent(Intent.ACTION_VIEW , Uri.parse("https://telegram.me/GuardianApp_Support"));
                startActivity(telegram);
            }
        });

        Button whatsappBtn = (Button) findViewById(R.id.whatsappButton);
        whatsappBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String number = "+98 9305006036";
                String url = "https://api.whatsapp.com/send?phone="+number;
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(Support.this, Main.class);
        startActivity(i);
        finish();
    }

    public void clickOnBtn(View v) {
        Intent i = new Intent(Support.this, Main.class);
        startActivity(i);
        finish();
    }
}