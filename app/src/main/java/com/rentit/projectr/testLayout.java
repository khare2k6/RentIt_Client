package com.rentit.projectr;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.HashMap;

/**
 * Created by akhare on 11-Aug-15.
 */
public class testLayout extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_product_create);
        final TestCustomView tcv = (TestCustomView) findViewById(R.id.tcv_test);

//        HashMap<String,String> map = new HashMap<String,String>();
//        map.put("Row1","Ro12");
//        map.put("Row2", "Ro22");
//        map.put("Row3", "Ro32");
//        tcv.setNameValues(map,true);
//
//        Button btn = (Button) findViewById(R.id.btn_test);
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                NameValueView vi = (NameValueView) tcv.findViewById("Row1".hashCode());
//                Toast.makeText(testLayout.this, "fetched:" + vi.getValue(), Toast.LENGTH_SHORT).show();
//
//            }
//        });

    }
}
