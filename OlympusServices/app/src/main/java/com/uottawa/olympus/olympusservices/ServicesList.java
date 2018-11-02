package com.uottawa.olympus.olympusservices;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;

import java.util.Iterator;
import java.util.List;

public class ServicesList extends AppCompatActivity implements NewServiceDialogFragment.NoticeDialogListener, EditServiceDialogFragment.NoticeDialogListener{

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services_list);
        DBHelper dbHelper = new DBHelper(this);
        List<String[]> serviceslist = dbHelper.getAllServices();
        Service[] services = new Service[(serviceslist.size())];
        Iterator iter = serviceslist.iterator();
        for (int i=0; i<serviceslist.size();i++){
            String[] current = (String[])iter.next();
            services[i] = new Service(current[0], Double.parseDouble(current[1]));
        }

        mRecyclerView = (RecyclerView) findViewById(R.id.Services);


        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new MyAdapter(services, this);
        mRecyclerView.setAdapter(mAdapter);



    }
    public void addService(View view) {
        DialogFragment newFragment = new NewServiceDialogFragment();
        newFragment.show(getSupportFragmentManager(), "addService");
    }
    public void editService(View view, String name) {
        DialogFragment newFragment = new EditServiceDialogFragment();
        newFragment.show(getSupportFragmentManager(), "editService");
        Bundle args = new Bundle();
        args.putString("name", name);
        newFragment.setArguments(args);
    }
    //add new service
    @Override
    public void onDialogNew(DialogFragment dialog) {
        DBHelper dbHelper = new DBHelper(this);
        String name = (String)dialog.getArguments().get("name");
        Double rate = (Double)dialog.getArguments().get("rate");
        dbHelper.addService(new Service(name,rate));
        dialog.dismiss();
        this.recreate();
    }
    //user clicked cancel
    @Override
    public void onDialogNevermind(DialogFragment dialog) {

    }
    //edits service with info from dialog
    @Override
    public void onDialogEdit(DialogFragment dialog) {
        DBHelper dbHelper = new DBHelper(this);
        String name = (String)dialog.getArguments().get("name");
        Double rate = (Double)dialog.getArguments().get("rate");
        dbHelper.updateService(new Service(name,rate));
        dialog.dismiss();
        this.recreate();
    }
    //deletes service with info from dialog
    @Override
    public void onDialogDelete(DialogFragment dialog) {
        DBHelper dbHelper = new DBHelper(this);
        String name = (String)dialog.getArguments().get("name");
        dbHelper.deleteService(name);
        dialog.dismiss();
        this.recreate();
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ServicesHolder> {

        private Service[] services;
        private Context context;

        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder

        // Provide a suitable constructor (depends on the kind of dataset)
        public MyAdapter(Service[] services, Context context) {
            this.services = services;
        }

        // Create new views (invoked by the layout manager)
        @NonNull
        @Override
        public ServicesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item, parent, false);
            ServicesHolder vh = new ServicesHolder(v);
            return vh;
        }

        // Replace the contents of a view (invoked by the layout manager)
        @Override
        public void onBindViewHolder(ServicesHolder holder, int position) {
            Service service = services[position];
            holder.name.setText(service.getName());
            holder.rate.setText(""+service.getRate());



        }

        // Return the size of your dataset (invoked by the layout manager)
        @Override
        public int getItemCount() {
            return services.length;
        }

        class ServicesHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

            TextView name;
            TextView rate;

            public ServicesHolder(View row){
                super(row);
                name = row.findViewById(R.id.Name);
                rate = row.findViewById(R.id.Rate);
                row.setOnClickListener(this);
            }
            @Override
            public void onClick(View view) {
                TextView nameview = (TextView)view.findViewById(R.id.Name);
                String name = nameview.getText().toString();
                editService(view, name);

            }


        }


    }

}