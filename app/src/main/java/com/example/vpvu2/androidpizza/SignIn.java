package com.example.vpvu2.androidpizza;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.vpvu2.androidpizza.Common.Common;
import com.example.vpvu2.androidpizza.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

public class SignIn extends AppCompatActivity {
    EditText edtPhone, edtPassword;
    Button btnSignIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        edtPassword = (MaterialEditText) findViewById(R.id.edtPassword);
        edtPhone = (MaterialEditText) findViewById(R.id.edtPhone);
        btnSignIn = (Button) findViewById(R.id.btnSignIn);

        //init firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProgressDialog mDialog = new ProgressDialog(SignIn.this);
                mDialog.setMessage("please Waiting...........");
                mDialog.show();


                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //check user exists in database or not

                        if (dataSnapshot.child(edtPhone.getText().toString()).exists()) {


                            //get user information
                            mDialog.dismiss();
                            User user = dataSnapshot.child(edtPhone.getText().toString()).getValue(User.class);
                            user.setPhone_num(edtPhone.getText().toString());
                            if (user.getPassword().equals(edtPassword.getText().toString())) {
                                {
                                    Intent homeIntent =new Intent(SignIn.this,Home.class);
                                    Common.currentUser = user;
                                    startActivity(homeIntent);
                                    finish();
                                }
                            } else {
                                Toast.makeText(SignIn.this, "Wrong Password", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            mDialog.dismiss();
                            Toast.makeText(SignIn.this, "User doesnot exist", Toast.LENGTH_SHORT).show();

                        }
                    }


                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }

        });


    }
}
