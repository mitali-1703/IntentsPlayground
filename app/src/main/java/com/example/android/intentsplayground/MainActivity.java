package com.example.android.intentsplayground;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.intentsplayground.databinding.ActivityMainBinding;
import com.example.android.intentsplayground.R;

public class MainActivity<pulbic> extends AppCompatActivity {

    int quantity = 0 ;
    int min,max;
    private ActivityMainBinding b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(b.getRoot());

        setupEventHandlers();
        getInitialCount();
    }

    private void getInitialCount() {
//        quantity = getIntent().getIntExtra("Initial Count",0);   //getIntExtra() coz we had sent integer and receiving the intent by accessing the key we gave and if not found then setting default value to zero.
//        b.qty.setText(""+quantity);


        //Receiving data thru Bundle
        Bundle bundle = getIntent().getExtras();
        quantity = getIntent().getIntExtra(Constants.INITIAL_COUNT_KEY,0);
        min =  getIntent().getIntExtra(Constants.MIN_VALUE,Integer.MIN_VALUE);
        max = getIntent().getIntExtra(Constants.MAX_VALUE,Integer.MAX_VALUE);
        b.qty.setText(""+quantity);

        if(quantity!=0){
            b.button4.setVisibility(View.VISIBLE);
        }
    }

    private void setupEventHandlers() {
        b.incBtn.setOnClickListener(v -> {
            increment();
        });

        b.decBtn.setOnClickListener(v -> {
            decrement();
        });
    }


    public void increment() {
        quantity++;
        b.qty.setText("" + quantity);
    }

    public void decrement() {
        quantity--;
        TextView qtyTV = findViewById(R.id.qty);
        qtyTV.setText("" + quantity);
    }

    public void sendBack(View view) {
        //Validate count
        if(quantity>=min && quantity<=max){
            Intent intent = new Intent();
            intent.putExtra(Constants.FINAL_VALUE,quantity);
            setResult(RESULT_OK,intent);

            //Close the activity
            finish();
        }
        //Not in range
        else {
            Toast.makeText(this, "Not in range!", Toast.LENGTH_SHORT).show();
        }
    }
}