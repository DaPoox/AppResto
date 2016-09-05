package com.example.ecc.projetvesrion10;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {


    Button bttajouter;
    Button bttcherhcer;
    private  final String EXTRA_NAME="ajouter";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bttajouter= (Button) findViewById(R.id.ajout);
        bttcherhcer=(Button)findViewById(R.id.recherche);

    }

    public void choixBouton(View view) {

        switch(view.getId()){

            case R.id.ajout:
                Intent iii= new Intent(this,activity_ajout_restaurant.class);
                iii.putExtra(EXTRA_NAME, 1);
                startActivity(iii);
                break;

            case R.id.recherche:
                Intent iii2= new Intent(this,activity_recherche_restaurant.class);
                iii2.putExtra(EXTRA_NAME, 1);
                startActivity(iii2);
                break;
        }



    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
