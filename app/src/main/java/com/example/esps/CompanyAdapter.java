package com.example.esps; // Use your actual package name

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class CompanyAdapter extends ArrayAdapter<Company> {
    private Activity context;
    private List<Company> companyList;

    public CompanyAdapter(Activity context, List<Company> companyList) {
        super(context, R.layout.list_layout, companyList); // list_layout is your custom layout for each list item
        this.context = context;
        this.companyList = companyList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.list_layout, parent, false); // Make sure to use false here

        TextView textViewName = listViewItem.findViewById(R.id.textViewName);
        Company company = companyList.get(position);
        textViewName.setText(company.getName());

        return listViewItem;
    }
}

