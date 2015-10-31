package com.rentit.projectr;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.rentit.com.rentit.network.HttpRequest;
import com.rentit.com.rentit.network.NetworkConnection;
import com.rentit.com.rentit.network.URLConstants;
import com.rentit.parser.JsonParser;
import com.rentit.schema.RentalRequest;
import com.rentit.schema.RentalRequest.ItemType;
import com.rentit.projectr.ListRentalRequestsFragment.IonRentalRequestClicked;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by akhare on 31-Jul-15.
 */
public class MainActivity2 extends AppCompatActivity implements
        ProductsListFragment.IonProductListItemClicked,
        IonRentalRequestClicked,BaseFragment.IonFragmentWorkDone
{
    FragmentManager mFragmentManager;
    Fragment mProductListFragment;
    Fragment mProductDetailViewFragment;
    private ArrayList<NavigationDrawerItem> mDrawerItemsList;
    private NavigationDrawerItem.NavigationDrawerItemAdapter mDrawerAdapter;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList ;
    private final String TAG=MainActivity2.class.getSimpleName();
    private JsonParser mParser;
    private AlertDialog.Builder     mDialogBuilder;
    private Fragment mMyRequestsFragment,mRequestPendingOnMeFragment;
    private RentalRequest.IonRequestFetchStatusListener mListener;
    private ActionBarDrawerToggle mDrawerToggle;
    private Fragment mLastShownFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //.makeText(this,"MainActivity2",Toast.LENGTH_SHORT).show();

        mFragmentManager = getFragmentManager();
        mProductListFragment = new ProductsListFragment();
        mDrawerItemsList = new ArrayList<NavigationDrawerItem>();
        mParser = new JsonParser();
        setContentView(R.layout.activity_layout_main2);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);
        Log.d(TAG, "mDrawerList=" + mDrawerList + "mDrawerAdapter=" + mDrawerAdapter);
        mListener = new RequestStatusListner();
        mDialogBuilder = new AlertDialog.Builder(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                /*R.drawable.ic_drawer,*/ R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle("Title");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("TITLE");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        // Set the drawer toggle as the DrawerListener
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    private void setTitleAtActionBar(Fragment fragment){
        String title = "title";
        if(fragment instanceof ProductsListFragment)
            title = "All Products";
        else if(fragment instanceof ProductDetailViewFragment)
            title = "Product Detail";
        else if(fragment instanceof ListRentalRequestsFragment)
            title = "Request/Approvals";
        else if(fragment instanceof CreateProductFragment)
            title = "New Product";

        getSupportActionBar().setTitle(title);
    }



    @Override
    protected void onResume() {
        super.onResume();

        addMenuItems();
        mDrawerAdapter = new NavigationDrawerItem.NavigationDrawerItemAdapter(this,R.layout.row_drawer_content,mDrawerItemsList);
        mDrawerList.setAdapter(mDrawerAdapter);
        if( mLastShownFragment != null )
             replaceFragemtn(mLastShownFragment);
        else
             replaceFragemtn(mProductListFragment);
        RentalRequest.getMyRequestStatus(this, mListener);
        RentalRequest.getPendingApprovals(this, mListener);
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
    }

    private void addMenuItems(){
        if(mDrawerItemsList != null && mDrawerItemsList.isEmpty()){
            mDrawerItemsList.add( new NavigationDrawerItem(getDrawable(R.drawable.ic_requests),
                    ItemType.REQUEST_SENT_BY_ME.getName(),"0"));
            mDrawerItemsList.add( new NavigationDrawerItem(getDrawable(R.drawable.ic_approvals),
                    ItemType.REQUEST_PENDING_ON_ME.getName(),"0"));
            mDrawerItemsList.add( new NavigationDrawerItem(getDrawable(R.drawable.ic_new_product),
                    ItemType.CREATE_PRODUCT.getName(),""));
//            mDrawerItemsList.add( new NavigationDrawerItem(getDrawable(android.R.drawable.ic_menu_edit),
//                    ItemType.SET_IP.getName(),"0"));
        }
    }

    @Override
    public void onProductClicked(String productId) {
        mProductDetailViewFragment = new ProductDetailViewFragment();
        Bundle bundle = new Bundle();
        bundle.putString("productId", productId);
        mProductDetailViewFragment.setArguments(bundle);
        replaceFragemtn(mProductDetailViewFragment);
    }

    private void showDialog(int titleId,int msgId){
        mDialogBuilder.setTitle(getResources().getString(titleId))
                .setPositiveButton(R.string.title_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setMessage(getResources().getString(msgId));
        AlertDialog dialog = mDialogBuilder.create();

        dialog.show();

    }

    @Override
    public void onFragmentWorkDone(BaseFragment.ShowDialog code) {
        switch(code){
            case REQUEST_SENT:
                showDialog(R.string.title_request_sent,R.string.msg_request_sent);
                break;
            case REQUEST_STAUTS_UPDATED:
                showDialog(R.string.title_request_updated,R.string.msg_request_status_updated);
                break;
            case ERROR_OCCURED:
                showDialog(R.string.title_request_error,R.string.msg_error);
                break;
            case PRODUCT_CREATED_SUCCESSFULLY:
                showDialog(R.string.title_product_created,R.string.msg_product_created);
                break;
            case PRODUCT_CREATTION_FAILED:
                break;
            default:
                break;
        }
        replaceFragemtn(mProductListFragment);
    }

    private class RequestStatusListner implements RentalRequest.IonRequestFetchStatusListener{

        @Override
        public void onRequestsFetched(ArrayList<RentalRequest> rentalRequestsList) {
            ItemType reqType = ItemType.REQUEST_SENT_BY_ME;
            updateNavigationalDrawerItems(reqType.getName(),rentalRequestsList.size());
            mMyRequestsFragment = new ListRentalRequestsFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("rentalRequests", new ArrayList<RentalRequest>(rentalRequestsList));
            bundle.putSerializable("initiater", RentalRequestAdapter.RentalRequestsFor.TENANT);
            mMyRequestsFragment.setArguments(bundle);
        }

        @Override
        public void onApprovalsFetched(ArrayList<RentalRequest> approvalssList) {
            ItemType reqType = ItemType.REQUEST_PENDING_ON_ME;
            updateNavigationalDrawerItems(reqType.getName(),approvalssList.size());
            mRequestPendingOnMeFragment = new ListRentalRequestsFragment();
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList("rentalRequests", new ArrayList<RentalRequest>(approvalssList));
            bundle.putSerializable("initiater", RentalRequestAdapter.RentalRequestsFor.OWNER);
            mRequestPendingOnMeFragment.setArguments(bundle);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    private void updateNavigationalDrawerItems(String reqType,int length){
        if(mDrawerItemsList != null){
            for(NavigationDrawerItem item:mDrawerItemsList ){
                if(item.getmMenuTitle().equalsIgnoreCase(reqType)){
                    item.setmMenuExtra(Integer.toString(length));
                }
            }
            //mDrawerAdapter = new NavigationDrawerItem.NavigationDrawerItemAdapter(this,R.layout.row_drawer_content,mDrawerItemsList);
            Log.d(TAG,"in updateNavigationalDrawerItems:");
            mDrawerAdapter.notifyDataSetChanged();
        }
    }

    private void replaceFragemtn(Fragment fragment){
        if(fragment instanceof ProductsListFragment){
            RentalRequest.getMyRequestStatus(this, mListener);
            RentalRequest.getPendingApprovals(this, mListener);
        }
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.replace(R.id.content_frame, fragment);
        transaction.addToBackStack(fragment.getClass().getSimpleName());
        transaction.commit();
        mLastShownFragment = fragment;
        setTitleAtActionBar(fragment);
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "mFragmentManager.getBackStackEntryCount()" + mFragmentManager.getBackStackEntryCount());
        if (mFragmentManager.getBackStackEntryCount() == 0) {
            finish();
        } else {
            mFragmentManager.popBackStackImmediate();
            RentalRequest.getMyRequestStatus(this, mListener);
            RentalRequest.getPendingApprovals(this, mListener);
        }
    }
    @Override
    public void onRequestClicked(RentalRequest request) {
        if(request.getmState().equalsIgnoreCase("Rented") &&
                request.getmOwnerId().equalsIgnoreCase(PreferenceManager.getmInstance(MainActivity2.this).getUserName())){
                showDialog(R.string.title_already_rented,R.string.msg_already_rented);
            return;
        }
        Fragment fragment = new RentalRequestDetailFragment();
        Bundle bundle = new Bundle();
        Log.d(TAG,"request obj :"+request);
        bundle.putParcelable("request",request);
        fragment.setArguments(bundle);
        replaceFragemtn(fragment);
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }
     }

    private void selectItem(int position) {
        Fragment fragment = null;
        ItemType selectedItem = null;
        for(ItemType item :ItemType.values()){
            if(item.ordinal() == position)
                selectedItem = item;
        }
        Log.d(TAG,"position:"+position+" clicked.."+selectedItem);
        switch (selectedItem) {
            case REQUEST_SENT_BY_ME:
                fragment = mMyRequestsFragment;
                break;

            case REQUEST_PENDING_ON_ME:
                fragment = mRequestPendingOnMeFragment;
                break;

            case CREATE_PRODUCT:
                fragment = new CreateProductFragment();
                break;
            default:
                break;
        }

        if (fragment != null) {
            replaceFragemtn(fragment);

            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            mDrawerLayout.closeDrawer(mDrawerList);
     } else {
            Log.e(TAG, "Error in creating fragment");
        }
    }
}
