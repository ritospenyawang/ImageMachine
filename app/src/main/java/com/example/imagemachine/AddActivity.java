package com.example.imagemachine;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnSave;

    EditText machineIDET, machineNameET, machineTypeET, machineQRET, machineDateET;

    DatePickerDialog machineDatePD;

    SimpleDateFormat dateFormatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        machineIDET = (EditText) findViewById(R.id.etAddMID);
        machineNameET = (EditText) findViewById(R.id.etAddMName);
        machineTypeET = (EditText) findViewById(R.id.etAddMType);
        machineQRET = (EditText) findViewById(R.id.etAddMQR);

        dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);

        machineDateET = (EditText) findViewById(R.id.etAddMDate);
        machineDateET.setShowSoftInputOnFocus(false);

        btnSave = (Button) findViewById(R.id.btnSave);

        setDateField();

        if(checkData()){
            saveNewMachine();
        }
    }

    //Checks if all input has been filled
    private boolean checkData() {
        if(machineIDET.getText().toString() != "" && machineNameET.getText().toString() != "" && machineTypeET.getText().toString() != "" && machineQRET.getText().toString() != "" && machineDateET.getText().toString() != ""){
            return true;
        }
        else return false;
    }

    //Function to save new machine
    private void saveNewMachine() {
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MachineDB db = MachineDB.getInstance(AddActivity.this);

                Machine newMachine = new Machine();

                newMachine.ID = Integer.parseInt(machineIDET.getText().toString());
                newMachine.name = machineNameET.getText().toString();
                newMachine.type = machineTypeET.getText().toString();
                newMachine.qr = Integer.parseInt(machineQRET.getText().toString());
                newMachine.date = machineDateET.getText().toString();

                db.addMachine(newMachine);

                AlertDialog.Builder alertDialog = new AlertDialog.Builder(AddActivity.this);
                alertDialog.setIcon(android.R.drawable.ic_dialog_info)
                        .setTitle("Info")
                        .setMessage("Added 1 new data")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                finish();
                            }
                        })
                        .show();

//                Snackbar.make(view, "Added 1 new data", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//                try{
//                    Toast.makeText(getApplicationContext(), "Added 1 new data", Toast.LENGTH_LONG);
//                    Thread.sleep(2000);
//                    finish();
//                }catch (Exception e){
//
//                }
            }
        });


    }

    //Set date picker dialog
    private void setDateField() {
        machineDateET.setOnClickListener(this);

        Calendar newCalendar = Calendar.getInstance();
        machineDatePD = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, month, day);
                machineDateET.setText(dateFormatter.format(newDate.getTime()));
            }
        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    @Override
    public void onClick(View view) {
        if(view == machineDateET){
            machineDatePD.show();
        }
    }
}
