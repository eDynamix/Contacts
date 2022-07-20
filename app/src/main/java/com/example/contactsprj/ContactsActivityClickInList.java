package com.example.contactsprj;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.Collections;
import java.util.Comparator;

public class ContactsActivityClickInList extends Activity {

    EditText firstName, lastName, email, address, phone;
    TextView first2Letters;
    Button backButton, editButton, deleteButton, saveButton;

    ContactList contactsArr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_click_in_list);

        Intent intent = getIntent();
        int position = intent.getIntExtra("position", -2);

        loadDataFromSP();
        Contact contact = new Contact(contactsArr.get(position));

        backButton = findViewById(R.id.ContactsInfoBackButton);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        firstName = findViewById(R.id.ContactsInfoFirstNameEditText);
        lastName = findViewById(R.id.ContactsInfoLastNameEditText);
        email = findViewById(R.id.ContactsInfoEmailEditText);
        address = findViewById(R.id.ContactsInfoAddressEditText);
        phone = findViewById(R.id.ContactsInfoPhoneEditText);
        first2Letters = findViewById(R.id.ContactsInfoFirst2Letters);

        firstName.setText(contact.getFirstName());
        lastName.setText(contact.getLastName());
        email.setText(contact.getEmail());
        address.setText(contact.getAddress());
        phone.setText(contact.getTelNum());
        first2Letters.setText(contact.getNameFirst2Letters());

        EditText[] editTexts = {firstName, lastName, email, address, phone};
        for(EditText e:editTexts){
            e.setInputType(InputType.TYPE_NULL);
            e.setSelected(false);
        }

        deleteButton = findViewById(R.id.DeleteContactInfoButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(ContactsActivityClickInList.this).setTitle("Delete Confirmation").setMessage("Are you sure you want to delete this contact?").setIcon(R.drawable.ic_contacts_delete)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                loadDataFromSP();
                                contactsArr.remove(position);
                                saveData();
                                finish();
                            }
                        }).setNegativeButton("No", null).show();
            }
        });

        saveButton = findViewById(R.id.SaveContactInfoButton);
        saveButton.setClickable(false);

        editButton = findViewById(R.id.ContactsInfoEditButton);

        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(EditText e:editTexts){
                    e.setInputType(InputType.TYPE_CLASS_TEXT);
                    e.setSelected(false);

                    saveButton.setClickable(true);
                    saveButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String firstNameStr = firstName.getText().toString();
                            String lastNameStr = lastName.getText().toString();
                            String emailStr = email.getText().toString();
                            String addressStr = address.getText().toString();
                            String phoneStr = phone.getText().toString();
                            try{
                                Contact contactEdited = new Contact(firstNameStr, lastNameStr, emailStr, addressStr, phoneStr);
                                System.out.println(contactEdited);
                                contactsArr.set(position, contactEdited);
                                saveData();
                                finish();
                            } catch (UnsupportedOperationException e1){
                                new AlertDialog.Builder(ContactsActivityClickInList.this).setTitle("Invalid data").setMessage("You must include a First Name and a Phone Number to save your contact")
                                        .setNeutralButton("OK", null).show();
                            }catch (IllegalArgumentException e){
                                new AlertDialog.Builder(ContactsActivityClickInList.this).setTitle("Invalid data").setMessage("The email must be valid and the phone number must only contain numbers.")
                                        .setNeutralButton("OK", null).show();
                            }



                        }
                    });
                }
            }
        });
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
                return c1Full.toLowerCase().compareTo(c2Full.toLowerCase());
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
}