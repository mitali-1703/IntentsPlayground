package com.example.android.intentsplayground;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.example.android.intentsplayground.databinding.ActivityIntentsPlaygroundBinding;

public class IntentsPlaygroundActivity extends AppCompatActivity {

    private static final int REQUEST_COUNT = 0 ;
    ActivityIntentsPlaygroundBinding b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Intents Playground");    //Another method to change label name of an activity
        setupLayout();

        setupHideErrorForEditText();
    }

    //Initial setup

    private void setupLayout() {
        b = ActivityIntentsPlaygroundBinding.inflate(getLayoutInflater()) ;
        setContentView(b.getRoot());
    }

    private void setupHideErrorForEditText() {
       TextWatcher myTextWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                hideError();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };

        b.outlinedTextField.getEditText().addTextChangedListener(myTextWatcher);
        b.sendData.getEditText().addTextChangedListener(myTextWatcher);

    }


    //Event Handlers

    public void sendExplicitBtn(View view) {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    public void sendImplicitBtn(View view) {

        //Validate data input
        String input = b.outlinedTextField.getEditText().getText().toString().trim();
        if(input.isEmpty()){
            b.outlinedTextField.setError("Please enter something!");
            return;
        }

        //Validate intent type
        int type = b.radioGroup.getCheckedRadioButtonId();

        //Handling Intent cases using switch case

        //Warning generated:- Resource ids created will be non-final so use if-else

//        switch (type){
//            case R.id.openWebpage:
//                openWebpage(input);
//                break;
//            case R.id.dialNumber:
//                dialNumber(input);
//                break;
//            case R.id.shareText:
//                shareText(input);
//                break;
//            default:
//                Toast.makeText(this, "Please select an intent type!", Toast.LENGTH_SHORT).show();
//        }

        //Handling Intent cases using if-else

        if(type==R.id.openWebpage)
            openWebpage(input);
        else if(type==R.id.dialNumber)
            dialNumber(input);
        else if(type==R.id.shareText)
            shareText(input);
        else
            Toast.makeText(this, "Please select an intent type!", Toast.LENGTH_SHORT).show();
    }

    public void sendDataBtn(View view) {
        //Validate data input
        String input = b.sendData.getEditText().getText().toString().trim();
        if(input.isEmpty()){
            b.sendData.setError("Please enter something!");
            return;
        }

        //Get count
        int initialCount = Integer.parseInt(input);

        //Method-1----->
        //Create and send Intent
//        Intent intent = new Intent(this,MainActivity.class);
//        intent.putExtra("initialCount",initialCount);    //In Intents, data is sent thru intent object with name as the key thru which we can access data
//        startActivity(intent);


        //Method-2----->
        Intent intent = new Intent(this,MainActivity.class);

        //Create Bundle
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.INITIAL_COUNT_KEY,initialCount);
        bundle.putInt(Constants.MIN_VALUE,-100);
        bundle.putInt(Constants.MAX_VALUE,100);

        //Pass Bundle
        intent.putExtras(bundle);
        startActivity(intent);


        //To receive data back from Main Activity
        startActivityForResult(intent,REQUEST_COUNT);
    }

    //To receive data sent by Main Activity

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_COUNT && resultCode == RESULT_OK){
            //Get data
            int count = data.getIntExtra(Constants.FINAL_VALUE,Integer.MIN_VALUE);
            //Show data
            b.result.setText("Final count received : "+count);
            b.result.setVisibility(View.VISIBLE);
        }
    }


    //Implicit intent sender

    private void dialNumber(String number) {
        //Check for valid number
        if(!number.matches("^\\d{10}$")){
            b.outlinedTextField.setError("Invalid Mobile Number!");
            return;
        }

        //Good to go , send intent
        Uri uri = Uri.parse("tel:" + number);
        Intent intent = new Intent(Intent.ACTION_DIAL,uri);
        startActivity(intent);

        hideError();
    }

    private void shareText(String text) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, text);
        sendIntent.setType("text/plain");

        Intent shareIntent = Intent.createChooser(sendIntent, "Share text via");
        startActivity(shareIntent);

    }

    private void openWebpage(String url) {
        //Check for valid url
        if(!url.matches("^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]")){
            b.outlinedTextField.setError("Invalid Url!");
            return;
        }

        //Good to go , send intent
        Uri uri = Uri.parse(url);
                Intent intent = new Intent(Intent.ACTION_VIEW,uri);
                startActivity(intent);

        hideError();
    }


    //Utils

    private void hideError(){
        b.outlinedTextField.setError(null);
    }


}

