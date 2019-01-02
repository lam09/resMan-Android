package lam.fooapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import io.socket.emitter.Emitter;
import lam.fooapp.communication.rests.RestRequest;
import lam.fooapp.model.Food;

public class MainFoodFragment extends Fragment {
    private EndlessRecyclerViewScrollListener scrollListener;
    private RecyclerView mFoodListView;
    private ArrayList<Food> foodList = new ArrayList<Food>();
    private RecyclerView.Adapter mAdapter;
    public MainFoodFragment() {
        // Required empty public constructor
        super();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MangoApplication.communicator.foodApi.getFoods(0,5,onFoodDataCallback);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mFoodListView = (RecyclerView) view.findViewById(R.id.mainRecycleView);
        mFoodListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mFoodListView.setAdapter(mAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        mFoodListView.setLayoutManager(linearLayoutManager);
        scrollListener=new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                loadNextDataFromApi(page);
            }
        };
        mFoodListView.addOnScrollListener(scrollListener);
    }
    public void loadNextDataFromApi(Integer offset){
        System.out.println("load next data from Api " + offset);
        MangoApplication.communicator.foodApi.getFoods(offset,5,onFoodDataCallback);
    }


    RestRequest.DataCallback<List<Food>> onFoodDataCallback = new RestRequest.DataCallback() {
        @Override
        public void onDataRecieved(final String result) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                   // System.out.println("Hello, I received a response from server: "+result);
                    Type listType = new TypeToken<List<Food>>(){}.getType();
                    List<Food>foodsReceived = (List<Food>)new Gson().fromJson(result,listType);
                    foodList.addAll(foodsReceived);
                    mAdapter.notifyDataSetChanged();
                    //scrollToBottom();
                }
            });

        }

        @Override
        public void onError() {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {

                }
            });

        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mAdapter = new FoodAdapter(context, foodList);
        if (context instanceof Activity){
            // this.listener = (MainActivity) context;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (Activity.RESULT_OK != resultCode) {
            getActivity().finish();
            return;
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


    private void addFood(Food food) {
        foodList.add(food);
//        mAdapter.notifyItemInserted(mMessages.size() - 1);
        if(foodList.size()==10) foodList.clear();
        mAdapter.notifyDataSetChanged();
        scrollToBottom();
    }
    private void scrollToBottom() {
        mFoodListView.scrollToPosition(mAdapter.getItemCount() - 1);
    }


}
