package hackgt.crowder.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.example.hackgt.R;

import hackgt.crowder.model.Event;

public class AddEventActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
    }

    public void submit(View view) {
        String nameOfEvent = ((EditText)findViewById(R.id.nameOfEventEdit)).getText().toString();
        String description = ((EditText)findViewById(R.id.descriptionEdit)).getText().toString();

        String address = ((EditText)findViewById(R.id.locationEdit)).getText().toString();
        // Get location coords using the address through google maps
        double latitude = 33.776586;
        double longitude = -84.395968;

        double price = Double.parseDouble(((EditText) findViewById(R.id.priceEdit)).getText().toString());
        String startTime = ((EditText)findViewById(R.id.startTimeEdit)).getText().toString();
        String endTime = ((EditText)findViewById(R.id.endTimeEdit)).getText().toString();

        Event event = new Event(latitude, longitude, nameOfEvent, 0, startTime, endTime, price);

        // Communicate back to the server

        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
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
