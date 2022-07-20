package com.example.contactsprj;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ContactsListAdapter extends BaseAdapter implements Filterable {

    protected Context context;
    protected ContactList contacts;
    protected ContactList filteredItems;

    private Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ContactList filteredItems = new ContactList();
            if (constraint == null || constraint.length() == 0) {
                filteredItems.addAll(contacts);
            } else {
                String filterString = constraint.toString().toLowerCase();

                for (Contact contact : contacts) {
                    String name = contact.getFirstName() + contact.getLastName();
                    if (name.toLowerCase().startsWith(filterString.toLowerCase())) {
                        filteredItems.add(contact);
                    }
                }
            }

            FilterResults result = new FilterResults();
            result.values = filteredItems;
            return result;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredItems.clear();
            filteredItems.addAll((ArrayList)results.values);

            notifyDataSetChanged();
        }
    };

    public ContactsListAdapter(Context context, ContactList contacts) {
        this.context = context;
        this.contacts = contacts;
        this.filteredItems = new ContactList(contacts);
    }

    @Override
    public int getCount() {
        return filteredItems.size();
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
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;

        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.contacts_list_row, parent, false);

        }
        TextView names = row.findViewById(R.id.ContactsRowNames);
        TextView letters = row.findViewById(R.id.ContactsRowFirst2Letters);

        String firstLastName = filteredItems.get(position).getFirstName() + " " + filteredItems.get(position).getLastName();
        names.setText(firstLastName);
        letters.setText(filteredItems.get(position).getNameFirst2Letters());

        return row;
    }

    @Override
    public Filter getFilter() {
        return filter;
    }
}