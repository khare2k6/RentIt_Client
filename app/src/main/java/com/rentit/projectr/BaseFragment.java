package com.rentit.projectr;

import android.app.Fragment;

/**
 * Created by akhare on 11-Aug-15.
 */
public class BaseFragment extends Fragment {
    public enum ShowDialog{
        REQUEST_SENT,
        REQUEST_STAUTS_UPDATED,
        ERROR_OCCURED,
        ALREADY_RENTED,
        PRODUCT_CREATED_SUCCESSFULLY,
        PRODUCT_CREATTION_FAILED,
        DONT_SHOW_ANYTHING
    }
    public interface IonFragmentWorkDone{
        public void onFragmentWorkDone(ShowDialog code);
    }
}
