package com.example.imagemachine;

import  android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CustomAdapter extends BaseAdapter {
    //CustomAdapter for listview item
    Context context;
    String machineName[], machineType[];
    LayoutInflater inflater;

    public CustomAdapter(Context applicationContext, String[] machineName, String[] machineType){
        this.context = applicationContext;
        this.machineName = machineName;
        this.machineType = machineType;
        inflater = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return machineName.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.machine_list, null);
        TextView mName = (TextView)view.findViewById(R.id.tvMachineName);
        TextView mType = (TextView)view.findViewById(R.id.tvMachineType);
        mName.setText(machineName[i]);
        mType.setText(machineType[i]);
        return view;
    }
}
