package lam.fooapp.activity.order;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;

import lam.fooapp.MangoApplication;
import lam.fooapp.R;
import lam.fooapp.model.Order;

public class CustomOrderDialog  extends DialogFragment {
    /** The system calls this to get the DialogFragment's layout, regardless
     of whether it's being displayed as a dialog or an embedded fragment. */
    Order order;
    public void setOrder(Order order){
        this.order = order;
    }

    public static CustomOrderDialog newInstance(String orderJson) {
        CustomOrderDialog frag = new CustomOrderDialog();
        Bundle args = new Bundle();
        args.putString("orderJson", orderJson);
        frag.setArguments(args);
        return frag;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout to use as dialog or embedded fragment
        return inflater.inflate(R.layout.order_view, container);
    }

    /** The system calls this only when creating the layout in a dialog. */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // The only reason you might override this method when using onCreateView() is
        // to modify any dialog characteristics. For example, the dialog includes a
        // title by default, but your custom layout might not need it. So here you can
        // remove the dialog title, but you must call the superclass to get the Dialog.
        final Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        return dialog;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        String orderJson = getArguments().getString("orderJson", "0");
        order = new Gson().fromJson(orderJson,Order.class);
        TextView orderNumberTextView = (TextView)view.findViewById(R.id.order_confirm_dialog_num);
        orderNumberTextView.setText(order.getOrderNo().toString());
        Button ok = (Button) view.findViewById(R.id.order_dialog_btn_ok);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }
}