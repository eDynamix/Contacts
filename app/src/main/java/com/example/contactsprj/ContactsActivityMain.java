package com.example.contactsprj;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.google.gson.Gson;

import java.util.Collections;
import java.util.Comparator;

public class ContactsActivityMain extends Activity {

    Button clearFilter, addNewAcc;
    EditText filterTxt;
    ListView contactsListView;
    ContactsListAdapter contactsListAdapter;
    ContactList contactsArr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_contacts);

        clearFilter = findViewById(R.id.ContactsClearFilter);
        addNewAcc = findViewById(R.id.ContactsAddNewAccountButton);
        filterTxt = findViewById(R.id.ContactsFilterByName);

        loadDataFromSP();
        for(Contact c:contactsArr){
            System.out.println(c);
        }
        loadDataToList();
        listOnClick();
        filterTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence cs, int i, int i1, int i2) {
                ContactsActivityMain.this.contactsListAdapter.getFilter().filter(cs.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        clearFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterTxt.setText("");
            }
        });

        addNewAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ContactsActivityMain.this, ContactsActivityAddNewContact.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        filterTxt = findViewById(R.id.ContactsFilterByName);
        filterTxt.setText("");
        loadDataFromSP();
        loadDataToList();
        listOnClick();
    }

    @Override
    protected void onPause() {
        super.onPause();
        filterTxt = findViewById(R.id.ContactsFilterByName);
        filterTxt.setSelected(false);
    }

    private void loadDataFromSP(){
        SharedPreferences SP = getSharedPreferences("ContactsData", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = SP.getString("contacts", null);
        contactsArr = gson.fromJson(json, ContactList.class);

        if(contactsArr == null){
            contactsArr = new ContactList();
        }

        sortByName();
    }

    private void loadDataToList(){
        contactsListView = findViewById(R.id.ContactsList);
        contactsListAdapter = new ContactsListAdapter(this, contactsArr);
        contactsListView.setAdapter(contactsListAdapter);
    }

    private void listOnClick(){
        contactsListView.setClickable(true);
        contactsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Intent intent = new Intent(ContactsActivityMain.this, ContactsActivityClickInList.class);
                intent.putExtra("position", position);
                startActivity(intent);
            }
        });
    }

    private void sortByName(){
        Collections.sort(contactsArr, new Comparator<Contact>() {
            @Override
            public int compare(Contact c1, Contact c2) {
                String c1Full = c1.getFirstName()+c1.getLastName();
                String c2Full = c2.getFirstName()+c2.getLastName();
                return c1Full.toLowerCase().compareTo(c2Full.toLowerCase());
            }
        });
    }
}