package lam.fooapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import io.socket.emitter.Emitter;
import lam.fooapp.activity.EndlessRecyclerViewScrollListener;
import lam.fooapp.activity.order.FoodAdapter;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MainFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 *
 */
public class MainFragment extends Fragment {

    private EndlessRecyclerViewScrollListener scrollListener;
    private RecyclerView mMessagesView;
    private ArrayList<String> mMessages = new ArrayList<String>();
    private RecyclerView.Adapter mAdapter;
    public MainFragment() {
        // Required empty public constructor
        super();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    //    MangoApplication.communicator.socketio.on("new-food",newFoodListener);
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
        mMessagesView = (RecyclerView) view.findViewById(R.id.mainRecycleView);
        mMessagesView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mMessagesView.setAdapter(mAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(view.getContext());
        scrollListener=new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                loadNextDataFromApi(page);
            }
        };
    }
    public void loadNextDataFromApi(Integer offset){

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mAdapter = new FoodAdapter(context,null, null);
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
     //   MangoApplication.communicator.socketio.off("new-food",newFoodListener);
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


    private void addFood(String newFoodName) {
        mMessages.add(newFoodName);
//        mAdapter.notifyItemInserted(mMessages.size() - 1);
        if(mMessages.size()==10) mMessages.clear();
        mAdapter.notifyDataSetChanged();
        scrollToBottom();

    }
    private void scrollToBottom() {
        mMessagesView.scrollToPosition(mAdapter.getItemCount() - 1);
    }

    private Emitter.Listener newFoodListener = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            final String text=args[0].toString();
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    System.out.println("VASDASDASDSSD");
                    addFood("AAAAAAAAAAA");
                }
            });
        }
    };
}
