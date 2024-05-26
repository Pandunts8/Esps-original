package com.example.esps;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class CompanyAdapter extends ArrayAdapter<Company> {
    private Context context;
    private List<Company> companyList;

    public CompanyAdapter(Context context, List<Company> companyList) {
        super(context, R.layout.list_layout, companyList);
        this.context = context;
        this.companyList = companyList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_layout, parent, false);
            holder = new ViewHolder();
            holder.textViewName = convertView.findViewById(R.id.textViewName);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Company company = getItem(position);
        if (company != null) {
            holder.textViewName.setText(company.getName());
        }

        return convertView;
    }

    static class ViewHolder {
        TextView textViewName;
    }
}
