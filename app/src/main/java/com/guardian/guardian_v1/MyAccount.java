package com.guardian.guardian_v1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.guardian.guardian_v1.Transmission.EditWorker;

public class MyAccount extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);

        ImageButton backButton = (ImageButton) findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MyAccount.this, SelectNavigation.class);
                startActivity(i);
                finish();

            }
        });
        Context thisCtx = this;
        Button editButton = (Button) findViewById(R.id.editButton);
        EditText editText = (EditText)findViewById(R.id.newPassword);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditWorker editWorker = new EditWorker(thisCtx);
                editWorker.execute("edit",TokenManager.getInstance().getToken(),editText.getText().toString());
            }
        });
    }
}