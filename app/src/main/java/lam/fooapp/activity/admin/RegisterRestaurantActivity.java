package lam.fooapp.activity.admin;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import lam.fooapp.MangoApplication;
import lam.fooapp.R;
import lam.fooapp.activity.BasicMangoActivity;
import lam.fooapp.communication.Communicator;
import lam.fooapp.model.RestaurantRegisterForm;

public class RegisterRestaurantActivity extends BasicMangoActivity implements Communicator.DataReceiverCallback {


    EditText _nameText;
    EditText _addressText;
   EditText _resEmailText;
    EditText _mobileText;
    EditText _resWebsiteText;
     Button _signupButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_restaurant);
        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createRestaurant();
            }
        });
    }
    private void createRestaurant(){
        RestaurantRegisterForm form = new RestaurantRegisterForm();
        form.setName(_nameText.getText().toString());
        form.setAddress(_addressText.getText().toString());
        form.setTelefon(_mobileText.getText().toString());
        MangoApplication.communicator.createNewRestaurant(form,this);
    }

    @Override
    public void onDataRecieved(String result) {

    }

    @Override
    public void onError() {

    }
}
