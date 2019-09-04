package com.example.imagemachine;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    ListView machineList;
    TextView machineIDTV, machineNameTV, machineTypeTV, machineQRTV, machineDateTV;
    int machineID[];
    String machineName[] = {"Machine 1", "Machine 2", "Machine 3", "Machine 4", "Machine 5", "Machine 6", "Machine 7", "Machine 8", "Machine 9", "Machine 10"};
    String machineType[] = {"Type 1", "Type 2", "Type 3", "Type 1", "Type 4", "Type 5", "Type 5", "Type 4", "Type 5", "Type 5"};
    int machineQR[];
    String machineDate[];

    //Refresh the listview when going back from other activity
    @Override
    protected void onResume() {
        super.onResume();
        setCustomAdapter("name");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        machineList = (ListView)findViewById(R.id.lvMachine);

        setCustomAdapter("name");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fabAdd);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //        .setAction("Action", null).show();
                startActivity(new Intent(MainActivity.this, AddActivity.class));
            }
        });

        //Go to Edit Activity when 1 of the listview item is clicked
        machineList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this, EditActivity.class);
//                intent.putExtra("machineName", );
//                MachineDB machineDB = MachineDB.getInstance(MainActivity.this);

//                Machine machine = machineDB;

                //Send the clicked item information to Edit Activity
                Bundle extras = new Bundle();
                extras.putInt("machineID", machineID[i]);
                extras.putString("machineName", machineName[i]);
                extras.putString("machineType", machineType[i]);
                extras.putInt("machineQR", machineQR[i]);
                extras.putString("machineDate", machineDate[i]);
                intent.putExtras(extras);
                startActivity(intent);
            }
        });
    }

    //Handles the listview item appearance
    private void setCustomAdapter(String order) {
        MachineDB machineDB = MachineDB.getInstance(this);

        List<Machine> machines = machineDB.getAllMachine(order);

        machineID = new int[machines.size()];
        machineName = new String[machines.size()];
        machineType = new String[machines.size()];
        machineQR = new int[machines.size()];
        machineDate = new String[machines.size()];

        for(int i = 0; i < machines.size(); i++){
            machineID[i] = machines.get(i).ID;
            machineName[i] = machines.get(i).name;
            machineType[i] = machines.get(i).type;
            machineQR[i] = machines.get(i).qr;
            machineDate[i] = machines.get(i).date;
        }

        CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(), machineName, machineType);
        machineList.setAdapter(customAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    //Handles the action button event
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        //Refresh the listview item
        if (id == R.id.action_refresh) {
            setCustomAdapter("name");

            return true;
        }
        //Sort the listview item by name or type
        else if (id == R.id.action_sort){
            final String sort[] = {"Name", "Type"};
            final AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
            alertDialog
                    .setTitle("Sort By")
                    .setItems(sort, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if("Name".equals(sort[i])){
                                setCustomAdapter("name");
                            }
                            else if("Type".equals((sort[i]))){
                                setCustomAdapter("type");
                            }
                        }
                    })
                    .show();
        }
        //Scan qr code in new activity
        else if(id == R.id.action_qr){
            startActivity(new Intent(MainActivity.this, QRActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }
}
