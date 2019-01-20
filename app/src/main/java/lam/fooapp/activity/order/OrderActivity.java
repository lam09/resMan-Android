package lam.fooapp.activity.order;

import android.animation.Animator;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import lam.fooapp.activity.CircleAnimationUtil;
import lam.fooapp.activity.DoneOnEditorActionListener;
import lam.fooapp.activity.EndlessRecyclerViewScrollListener;
import lam.fooapp.MangoApplication;
import lam.fooapp.R;
import lam.fooapp.communication.rests.RestRequest;
import lam.fooapp.model.Food;
import lam.fooapp.model.Order;
import lam.fooapp.model.OrderForm;

public class OrderActivity extends AppCompatActivity implements OrderListListenner {
    private EndlessRecyclerViewScrollListener scrollListener;
    RecyclerView foodListView,orderView;
    private RecyclerView.Adapter foodListAdapter,currentOrderAdapter;
    ArrayList<Food>foodList = new ArrayList<Food>();
    ArrayList<Food> orderedFoods = MangoApplication.currentSelectedFoods;

    boolean orderShown = false,orderSendBtnLock = false;
    EditText tableNoEditText;
    TextView foodCountTextView,totalPriceTextView;

    View bottomBarLayout;

    int activityState=1; //1 - new order, 2- edit order


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        bottomBarLayout = (View) findViewById(R.id.bottomBarLayout);
        bottomBarLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animate(v);
            }
        });
        setSupportActionBar(toolbar);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                 //       .setAction("Action", null).show();
                sendOrder();
            }
        });
        MangoApplication.communicator.foodApi.getFoods(0,5,onFoodDataCallback);


        tableNoEditText = (EditText)findViewById(R.id.table_no);
        tableNoEditText.setOnEditorActionListener(new DoneOnEditorActionListener()); //hide keyboard when click enter

        foodCountTextView= (TextView) findViewById(R.id.foodCountTextView);
        totalPriceTextView = (TextView) findViewById(R.id.totalPriceTextView);

        updateBottomBarInfo();
    }
    public void showEditTableNoDialog() {
        DialogFragment tableEditDialog = new TableEditDialog();
        tableEditDialog.show(getSupportFragmentManager(), "missiles");
    }
    public void showDialog() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        CustomOrderDialog newFragment = new CustomOrderDialog();
        newFragment.show(fragmentManager, "dialog");
    /*    if (mIsLargeLayout) {
            // The device is using a large layout, so show the fragment as a dialog
            newFragment.show(fragmentManager, "dialog");
        } else {
            // The device is smaller, so show the fragment fullscreen
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            // For a little polish, specify a transition animation
            transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            // To make it fullscreen, use the 'content' root view as the container
            // for the fragment, which is always the root view for the activity
            transaction.add(android.R.id.content, newFragment)
                    .addToBackStack(null).commit();
        }*/
    }
    public void sendOrder()
    {
        if(orderSendBtnLock) return;
        OrderForm orderForm = new OrderForm();
        Integer[] foodListSerial= new Integer[MangoApplication.currentSelectedFoods.size()];
        int i=0;
        if(foodListSerial.length==0)return;
        for(Food f: MangoApplication.currentSelectedFoods){
            foodListSerial[i] = MangoApplication.currentSelectedFoods.get(i).getSerial();
            i++;
        }
        orderForm.setFoodSerialList(foodListSerial);
        String num = tableNoEditText.getText().toString();
        if(num.equals("")) {
            tableNoEditText.setError("table is required");
            tableNoEditText.requestFocus();
            return;
        }

        orderForm.setTableNo(Integer.parseInt(num));
        MangoApplication.communicator.foodApi.sendOrder(orderForm,onOrderSent);
        orderSendBtnLock = true;
    }


    private void updateBottomBarInfo(){
        if(MangoApplication.currentSelectedFoods.size()>0){
            Integer count = MangoApplication.currentSelectedFoods.size();
            String text = count + " food(s) added to order";// + R.string.with_food;
            foodCountTextView.setText(text);
            Double total = 0.00;
            for(Food f:MangoApplication.currentSelectedFoods){
                total+= Double.valueOf(f.getPrice());
            }
            DecimalFormat decimalFormat = new DecimalFormat("#.00");

            totalPriceTextView.setText( decimalFormat.format(total));
        }
        else {
            foodCountTextView.setText("No food selected");//R.string.no_food);
            totalPriceTextView.setText("0.00");
        }
    }

    private void clearOrder()
    {
        MangoApplication.currentSelectedFoods.clear();
        tableNoEditText.setText("");
        orderView.animate().translationY(orderView.getHeight());
        foodListAdapter.notifyDataSetChanged();
        foodCountTextView.setText(R.string.no_food);
        totalPriceTextView.setText("0.00");
    }

    RestRequest.DataCallback<Order> onOrderSent = new RestRequest.DataCallback<Order>() {
        @Override
        public void onDataRecieved(final String result) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Snackbar.make((View)findViewById(R.id.orderFoodMenuView), "Order sent successful", Snackbar.LENGTH_SHORT)
                           .setAction("Action", null).show();
                    System.out.println("recieved order " + result);
                    clearOrder();
                    Type orderType = new TypeToken<Order>(){}.getType();
                    Order orderReceived = (Order) new Gson().fromJson(result,orderType);

                    FragmentManager fm = getSupportFragmentManager();
                    CustomOrderDialog editNameDialogFragment = CustomOrderDialog.newInstance(orderReceived.getOrderNo().toString());
                    editNameDialogFragment.show(fm, "fragment_edit_name");

                    orderSendBtnLock = false;
                }
            });

        }

        @Override
        public void onError() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Snackbar.make(getCurrentFocus(), "Order error", Snackbar.LENGTH_LONG)
                           .setAction("Action", null).show();
                    orderSendBtnLock = false;
                }
            });
        }
    };


//    private void makeFlyAnimation(ImageView targetView) {
//
//        RelativeLayout destView = (RelativeLayout) findViewById(R.id.bottomBarLayout);
//
//        new CircleAnimationUtil().attachActivity(this).setTargetView(targetView).setMoveDuration(500).setDestView(destView).setAnimationListener(new Animator.AnimatorListener() {
//            @Override
//            public void onAnimationStart(Animator animation) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animator animation) {
//               // addItemToCart();
//                Toast.makeText(OrderActivity.this, "Continue Shopping...", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onAnimationCancel(Animator animation) {
//
//            }
//
//            @Override
//            public void onAnimationRepeat(Animator animation) {
//
//            }
//        }).startAnimation();
//
//
//    }


    private void animate(View view)
    {
         if(!orderShown) {
            System.out.println("showing order view");
            orderShown = true;
            orderView.setVisibility(View.VISIBLE);

// Start the animation
            orderView.animate()
                    .translationY(orderView.getHeight())
                    .alpha(1.0f);
//                    .setListener(null);
        }
        else  {
            orderShown=false;
            orderView.animate().translationY(0);
        }
    }






    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        menu.getItem(0).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                //showEditTableNoDialog();
                showDialog();
                return false;
            }
        });
        return true;
    }


    @Override
    protected void onStart() {
        super.onStart();
        foodListAdapter = new FoodAdapter(getApplicationContext(), foodList,this);
      /*  if (context instanceof Activity){
            // this.listener = (MainActivity) context;
        }*/
        foodListView = (RecyclerView) findViewById(R.id.orderFoodMenuView);
        foodListView.setLayoutManager(new LinearLayoutManager(this.getApplicationContext()));
        foodListView.setAdapter(foodListAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        foodListView.setLayoutManager(linearLayoutManager);
        scrollListener=new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                loadNextDataFromApi(page);
            }
        };
        foodListView.addOnScrollListener(scrollListener);

        orderView = (RecyclerView) findViewById(R.id.orderSelectedFoodsView);
        currentOrderAdapter = new OrderedFoodAdapter(getApplicationContext(),orderedFoods,this);
        orderView.setLayoutManager(new LinearLayoutManager(this.getApplicationContext()){
            @Override
            public boolean canScrollHorizontally() {
                return true;
            }
        });
        orderView.setAdapter(currentOrderAdapter);
        orderView.animate().translationY(orderView.getHeight());
    }

    public void loadNextDataFromApi(Integer offset){
        System.out.println("load next data from Api " + offset);
        MangoApplication.communicator.foodApi.getFoods(offset,5,onFoodDataCallback);
    }


    RestRequest.DataCallback<List<Food>> onFoodDataCallback = new RestRequest.DataCallback<List<Food>>()  {
        @Override
        public void onDataRecieved(final String result) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // System.out.println("Hello, I received a response from server: "+result);
                    Type listType = new TypeToken<List<Food>>(){}.getType();
                    List<Food>foodsReceived = (List<Food>)new Gson().fromJson(result,listType);
                    foodList.addAll(foodsReceived);
                    foodListAdapter.notifyDataSetChanged();
                    //scrollToBottom();
                }
            });

        }

        @Override
        public void onError() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {

                }
            });

        }
    };

    private void addFood(Food food) {
        foodList.add(food);
//        mAdapter.notifyItemInserted(mMessages.size() - 1);
       // if(foodList.size()==10) foodList.clear();
        foodListAdapter.notifyDataSetChanged();
        scrollToBottom();
    }
    private void scrollToBottom() {
        foodListView.scrollToPosition(foodListAdapter.getItemCount() - 1);
    }


    @Override
    public void addFoodToOrderList(final Food food) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                System.out.println("Adding food "+food.getTitle() + " to order");
                MangoApplication.currentSelectedFoods.add(food);
                System.out.println(orderedFoods.size());
                updateBottomBarInfo();
                currentOrderAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void removeFoodToOrderList(final Food food) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                System.out.println("removing food "+food.getTitle() + " from order");
                MangoApplication.currentSelectedFoods.remove(food);
                updateBottomBarInfo();
                currentOrderAdapter.notifyDataSetChanged();
            }
        });
    }

    public interface OnFilterOrder{
        void onFilterSelected(Order.OrderState orderState);
    }
}
