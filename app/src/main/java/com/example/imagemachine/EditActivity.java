package com.example.imagemachine;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class EditActivity extends AppCompatActivity implements View.OnClickListener{
    EditText machineIDET, machineNameET, machineTypeET, machineQRET, machineDateET;

    Button btnEditSave;

    DatePickerDialog machineDatePD;

    SimpleDateFormat dateFormatter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        machineIDET = (EditText) findViewById(R.id.etEditMID);
        machineNameET = (EditText) findViewById(R.id.etEditMName);
        machineTypeET = (EditText) findViewById(R.id.etEditMType);
        machineQRET = (EditText) findViewById(R.id.etEditMQR);
        machineDateET = (EditText) findViewById(R.id.etEditMDate);

        dateFormatter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);

        btnEditSave = (Button) findViewById(R.id.btnEditSave);

        Intent intent = getIntent();
//
        Bundle extras = intent.getExtras();

        setDateField();
//
        machineIDET.setText(String.valueOf(extras.getInt("machineID")));
        machineNameET.setText(extras.getString("machineName"));
        machineTypeET.setText(extras.getString("machineType"));
        String qr = String.valueOf(extras.getInt("machineQR"));
        machineQRET.setText(qr);
        machineDateET.setText(extras.getString("machineDate"));

//        btnEditSave.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                MachineDB db = MachineDB.getInstance(EditActivity.this);
//
//                Machine newMachine = new Machine();
//
//                newMachine.ID = Integer.parseInt(machineIDET.getText().toString());
//                newMachine.name = machineNameET.getText().toString();
//                newMachine.type = machineTypeET.getText().toString();
//                newMachine.qr = Integer.parseInt(machineQRET.getText().toString());
//                newMachine.date = machineDateET.getText().toString();
//
//                db.updateMachine(newMachine);
//
//                AlertDialog.Builder alertDialog = new AlertDialog.Builder(EditActivity.this);
//                alertDialog.setIcon(android.R.drawable.ic_dialog_info)
//                        .setTitle("Info")
//                        .setMessage("Updated 1 data")
//                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                dialogInterface.dismiss();
//                                finish();
//                            }
//                        })
//                        .show();
//            }
//        });

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
