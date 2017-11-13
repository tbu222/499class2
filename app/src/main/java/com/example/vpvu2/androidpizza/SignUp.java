package com.example.vpvu2.androidpizza;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.vpvu2.androidpizza.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

public class SignUp extends AppCompatActivity {
    MaterialEditText edtPhone, edtName,edtPassword;
    Button btnSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        edtName= (MaterialEditText)findViewById(R.id.edtName);
        edtPassword= (MaterialEditText)findViewById(R.id.edtPassword);
        edtPhone= (MaterialEditText)findViewById(R.id.edtPhone);

        btnSignup=(Button)findViewById(R.id.btnSignUp);

        //init firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference table_user = database.getReference("User");
        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final ProgressDialog mDialog = new ProgressDialog(SignUp.this);
                mDialog.setMessage("please Waiting...........");
                mDialog.show();
                table_user.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //check already account
                        if(dataSnapshot.child(edtPhone.getText().toString()).exists())
                        {
                            mDialog.dismiss();
                            Toast.makeText(SignUp.this,"account is exist",Toast.LENGTH_SHORT).show();}
                        else{
                            mDialog.dismiss();
                            User user=new User(edtName.getText().toString(),edtPassword.getText().toString());
                            table_user.child(edtPhone.getText().toString()).setValue(user);
                            Toast.makeText(SignUp.this,"Sign Up successfully!",Toast.LENGTH_SHORT).show();
                            finish();
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
