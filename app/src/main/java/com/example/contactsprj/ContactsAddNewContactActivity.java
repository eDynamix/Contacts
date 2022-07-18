package com.example.contactsprj;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.gson.Gson;

import java.util.Collections;
import java.util.Comparator;

public class ContactsAddNewContactActivity extends Activity {

    private ContactList contactsArr;
    private Button backBtn, saveContactBtn;
    private EditText firstName, lastName, email, phone, address;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts_add_new_contact);

        backBtn = findViewById(R.id.ContactsBackButton);
        saveContactBtn = findViewById(R.id.SaveContactButton);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        saveContactBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firstName = findViewById(R.id.ContactsFirstNameEditText);
                lastName = findViewById(R.id.ContactsLastNameEditText);
                email = findViewById(R.id.ContactsEmailEditText);
                phone = findViewById(R.id.ContactsPhoneEditText);
                address = findViewById(R.id.ContactsAddressEditText);

                String firstNameStr = firstName.getText().toString();
                String lastNameStr = lastName.getText().toString();
                String emailStr = email.getText().toString();
                String phoneStr = phone.getText().toString();
                String addressStr = address.getText().toString();

                try {
                    Contact contact = new Contact(firstNameStr, lastNameStr, emailStr, addressStr, phoneStr);
                    loadDataFromSP();
                    contactsArr.add(contact);
                    saveData();

                    finish();
                } catch (UnsupportedOperationException e1){
                    new AlertDialog.Builder(ContactsAddNewContactActivity.this).setTitle("Invalid data").setMessage("You must include at least a First Name and a Phone Number to save your contact")
                            .setNeutralButton("OK", null).show();
                } catch (IllegalArgumentException e){
                    new AlertDialog.Builder(ContactsAddNewContactActivity.this).setTitle("Invalid data").setMessage("The email must be valid and the phone number must only contain numbers.")
                            .setNeutralButton("OK", null).show();
                }

            }
        });

    }
    
    private void saveData(){
        SharedPreferences SP = getSharedPreferences("ContactsData", MODE_PRIVATE);
        SharedPreferences.Editor editor = SP.edit();
        Gson gson = new Gson();
        String json = gson.toJson(contactsArr);
        editor.putString("contacts", json);
        editor.apply();

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

    private void sortByName(){
        Collections.sort(contactsArr, new Comparator<Contact>() {
            @Override
            public int compare(Contact c1, Contact c2) {
                String c1Full = c1.getFirstName()+c1.getLastName();
                String c2Full = c2.getFirstName()+c2.getLastName();
                return c1Full.compareTo(c2Full);
            }
        });
    }
}