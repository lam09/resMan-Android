package lam.fooapp.activity.admin;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import lam.fooapp.R;
import lam.fooapp.activity.BasicMangoActivity;
import lam.fooapp.model.RestaurantRegisterForm;

public class CreateNewRestaurantActivity extends BasicMangoActivity {

    EditText eName,eAddress,eMobile;
    Button btn;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentLayout(R.layout.activity_restaurant_createnew);
        btn =(Button)findViewById(R.id.btn_create_new);
        eName= (EditText)findViewById(R.id.input_restaurant_name);
        eAddress= (EditText)findViewById(R.id.input_restaurant_address);
        eMobile= (EditText)findViewById(R.id.input_restaurant_mobile);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!validate()) return;
                String name = eName.getText().toString();
                String address = eAddress.getText().toString();
                String mobile = eMobile.getText().toString();
                RestaurantRegisterForm form = new RestaurantRegisterForm();
                form.setName(name);
                form.setAddress(address);
                form.setTelefon(mobile);
            }
        });
    }
    private boolean validate(){
            boolean valid = true;

            String name = eName.getText().toString();
            String address = eAddress.getText().toString();
            String mobile = eMobile.getText().toString();


            if (name.isEmpty() || name.length() < 3) {
                eName.setError("at least 3 characters");
                valid = false;
            } else {
                eName.setError(null);
            }

            if (address.isEmpty()) {
                eAddress.setError("Enter Valid Address");
                valid = false;
            } else {
                eAddress.setError(null);
            }
            if (mobile.isEmpty() || mobile.length()!=10) {
                eMobile.setError("Enter Valid Mobile Number");
                valid = false;
            } else {
                eMobile.setError(null);
            }
            return valid;
        }

}
