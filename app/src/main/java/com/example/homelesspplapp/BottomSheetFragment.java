package com.example.homelesspplapp;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Random;

import static android.content.Context.NOTIFICATION_SERVICE;

public class BottomSheetFragment extends BottomSheetDialogFragment {

    AlertDialog.Builder builder;

    EditText edit_mobile, edit_amt;
    Button btn;
    String strgEdtMobile;

    //String mail = "imran.wingherry@gmail.com";

    String ORDER_ID;
    String CUST_ID;
    String checksum;
    String amount;

    //generate MID with Paytm official site.  more readme.md
    public static final String MID = "akzrgs09242822019298"; //  Replace with your Merchant Id
    public static final String INDUSTRY_TYPE_ID = "Retail";
    public static final String CHANNEL_ID = "WAP";

    //while production website = "DEFAULT";
    //public static final String WEBSITE = "DEFAULT";

    //while testing
    public static final String WEBSITE = "WEBSTAGING";

    // while testing
    public static final String CALLBACK_URL = "https://securegw-stage.paytm.in/theia/paytmCallback?ORDER_ID=";

    // while production change this
    // public static final String CALLBACK_URL = "https://securegw.paytm.in/theia/paytmCallback?//ORDER_ID=ORDER_ID";


    public BottomSheetFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =inflater.inflate(R.layout.bottomsheet, container, false);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        builder=new AlertDialog.Builder(getActivity());

        btn = view.findViewById(R.id.btn);
        edit_amt = view.findViewById(R.id.amount);

        // amount = edit_amt.getText().toString().trim();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                amount = edit_amt.getText().toString();

                if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS}, 101);
                }

                amount = edit_amt.getText().toString();


                if (amount.isEmpty() || amount.matches("0")) {
                    edit_amt.setError("Empty Fields");

                } else {
                    Toast.makeText(getActivity(), "Processing", Toast.LENGTH_SHORT).show();
                    generateChecksum();
                }


            }
        });





        // Inflate the layout for this fragment
        return view;
    }
    @Override
    public void onStart() {
        super.onStart();

        //initOrderId();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    private void generateChecksum() {

        Random r = new Random(System.currentTimeMillis());
        ORDER_ID = "ORDER" + (1 + r.nextInt(2)) * 1000
                + r.nextInt(10000);

        Random r2 = new Random(System.currentTimeMillis());
        CUST_ID = "CUST" + (1 + r.nextInt(2)) * 1000
                + r2.nextInt(10000);


        // or replace local host with your live server address
        String url = "https://ramesh143.000webhostapp.com/paytm web api/generateChecksum.php";

        HashMap<String, String> params = new HashMap<>();

        params.put("MID", MID);
        params.put("ORDER_ID", ORDER_ID);
        params.put("CUST_ID", CUST_ID);
        params.put("INDUSTRY_TYPE_ID", INDUSTRY_TYPE_ID);
        params.put("CHANNEL_ID", CHANNEL_ID);
        params.put("TXN_AMOUNT", amount);
        params.put("WEBSITE", WEBSITE);
        params.put("CALLBACK_URL", CALLBACK_URL + ORDER_ID);
       // params.put("MOBILE_NO", String.valueOf(edit_mobile));
        //params.put("EMAIL", "test@gmail.com");

        JSONObject param = new JSONObject(params);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, param, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                checksum = response.optString("CHECKSUMHASH");
                if (checksum.trim().length() != 0) {

                    onStartTransaction();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getActivity(), "" + error, Toast.LENGTH_SHORT).show();
                //error.printStackTrace();

                if (error instanceof NetworkError) {
                } else if (error instanceof ServerError) {
                } else if (error instanceof AuthFailureError) {
                } else if (error instanceof ParseError) {
                } else if (error instanceof NoConnectionError) {
                } else if (error instanceof TimeoutError) {
                    Toast.makeText(getContext(),
                            "Oops. Timeout error!",
                            Toast.LENGTH_LONG).show();

                }
            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(getActivity()).add(jsonObjectRequest);

    }

    public void onStartTransaction() {

        // while Production
        // PaytmPGService.getProductionService(); //should be override

        PaytmPGService Service = PaytmPGService.getStagingService();
        HashMap<String, String> paramMap = new HashMap<>();

        // these are mandatory parameters

        paramMap.put("MID", MID);
        paramMap.put("ORDER_ID", ORDER_ID);
        paramMap.put("CUST_ID", CUST_ID);
        paramMap.put("INDUSTRY_TYPE_ID", INDUSTRY_TYPE_ID);
        paramMap.put("TXN_AMOUNT", amount);
        paramMap.put("WEBSITE", WEBSITE);
        paramMap.put("CHANNEL_ID", CHANNEL_ID);
        paramMap.put("CALLBACK_URL", CALLBACK_URL + ORDER_ID);
       // paramMap.put("MOBILE_NO", String.valueOf(edit_mobile));
        paramMap.put("CHECKSUMHASH", checksum);

        PaytmOrder Order = new PaytmOrder(paramMap);

        Service.initialize(Order, null);

        Service.startPaymentTransaction(getActivity(), true, true,
                new PaytmPaymentTransactionCallback() {
                    @Override
                    public void someUIErrorOccurred(String inErrorMessage) {

                        Toast.makeText(getContext(), "UI Error " + inErrorMessage, Toast.LENGTH_LONG).show();
                        // Some UI Error Occurred in Payment Gateway Activity.
                        // // This may be due to initialization of views in
                        // Payment Gateway Activity or may be due to //
                        // initialization of webview. // Error Message details
                        // the error occurred.
                    }


                    @Override
                    public void onTransactionResponse(Bundle inResponse) {
                        Log.d("LOG", "Payment Transaction is successful " + inResponse);
                        Toast.makeText(getContext(), "Payment Transaction response " + inResponse.toString(), Toast.LENGTH_LONG).show();


                        if (inResponse.toString().contains("SUCCESS")){

                            builder.setMessage("Before Going To Book...See The Studio Calender.. Select Yes To Continue")
                                    .setCancelable(false)
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {


                                            createNotificationChannel();
                                            Intent intent = new Intent(getContext(), MainActivity.class);

                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                                            PendingIntent pendingIntent = PendingIntent.getActivity(getContext(), 0, intent, 0);


                                            String  CHANNEL_ID="1";

                                            NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext(),CHANNEL_ID)

                                                    .setSmallIcon(R.mipmap.ic_launcher)

                                                    .setContentTitle("GetShops")

                                                    .setContentText("Transaction Success")

                                                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)

                                                    // Set the intent that will fire when the user taps the notification

                                                    .setContentIntent(pendingIntent)

                                                    .setAutoCancel(true);

                                            //  createNotificationChannel();



                                            NotificationManager notificationManager = (NotificationManager) getActivity().getSystemService(NOTIFICATION_SERVICE);

                                            notificationManager.notify(1,builder.build());



                                        }
                                    });

                            //Creating dialog box
                            AlertDialog alert = builder.create();
                            //Setting the title manually
                            alert.setTitle("Events Alert!");
                            alert.show();

                        }




                    }

                    private void createNotificationChannel() {

                        // Create the NotificationChannel, but only on API 26+ because

                        // the NotificationChannel class is new and not in the support library

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

                            CharSequence name = "ramesh";

                            String description = "zing zing";

                            int importance = NotificationManager.IMPORTANCE_DEFAULT;

                            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);

                            channel.setDescription(description);

                            // Register the channel with the system; you can't change the importance

                            // or other notification behaviors after this

                            NotificationManager notificationManager = getActivity().getSystemService(NotificationManager.class);

                            notificationManager.createNotificationChannel(channel);


                        }


                    }
                        @Override
                    public void networkNotAvailable() { // If network is not
                        // available, then this
                        // method gets called.

                        Toast.makeText(getContext(), "Network connection error: Check your internet connectivity", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void clientAuthenticationFailed(String inErrorMessage) {

                        Toast.makeText(getContext(), "Authentication failed: Server error" + inErrorMessage.toString(), Toast.LENGTH_LONG).show();
                        // This method gets called if client authentication
                        // failed. // Failure may be due to following reasons //
                        // 1. Server error or downtime. // 2. Server unable to
                        // generate checksum or checksum response is not in
                        // proper format. // 3. Server failed to authenticate
                        // that client. That is value of payt_STATUS is 2. //
                        // Error Message describes the reason for failure.
                    }

                    @Override
                    public void onErrorLoadingWebPage(int iniErrorCode,
                                                      String inErrorMessage, String inFailingUrl) {

                        Toast.makeText(getContext(), "Unable to load web page " + iniErrorCode + inErrorMessage + inFailingUrl, Toast.LENGTH_LONG).show();
                    }

                    // had to be added: NOTE
                    @Override
                    public void onBackPressedCancelTransaction()
                    {
                        Toast.makeText(getActivity(), "Back Pressed, Transaction Cancelled", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onTransactionCancel(String inErrorMessage, Bundle inResponse) {
                        Log.d("LOG", "Payment Transaction Failed " + inErrorMessage);
                        Toast.makeText(getContext(), "Payment Transaction Failed " + inErrorMessage, Toast.LENGTH_LONG).show();
                    }

                });
    }


}
   /* @Override
    public void onStart() {
        super.onStart();

        //initOrderId();
       getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    private void generateChecksum() {

        Random r = new Random(System.currentTimeMillis());
        ORDER_ID = "ORDER" + (1 + r.nextInt(2)) * 1000
                + r.nextInt(10000);

        Random r2 = new Random(System.currentTimeMillis());
        CUST_ID = "CUST" + (1 + r.nextInt(2)) * 1000
                + r2.nextInt(10000);


        // or replace local host with your live server address
        // String url = "http://192.168.0.19/paytm/generateChecksum.php";

        String url ="https://ramesh143.000webhostapp.com/paytm web api/generateChecksum.php";

        HashMap<String, String> params = new HashMap<>();

        params.put("MID", MID);
        params.put("ORDER_ID", ORDER_ID);
        params.put("CUST_ID", CUST_ID);
        params.put("INDUSTRY_TYPE_ID", INDUSTRY_TYPE_ID);
        params.put("CHANNEL_ID", CHANNEL_ID);
        params.put("TXN_AMOUNT", amount);
        params.put("WEBSITE", WEBSITE);
        params.put("CALLBACK_URL", CALLBACK_URL + ORDER_ID);
       // params.put("MOBILE_NO", String.valueOf(edit_mobile));
        //params.put("EMAIL", "test@gmail.com");

        JSONObject param = new JSONObject(params);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, param, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                checksum = response.optString("CHECKSUMHASH");
                if (checksum.trim().length() != 0) {

                    onStartTransaction();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getActivity(), "" + error, Toast.LENGTH_SHORT).show();
                //error.printStackTrace();

                if (error instanceof NetworkError) {
                } else if (error instanceof ServerError) {
                } else if (error instanceof AuthFailureError) {
                } else if (error instanceof ParseError) {
                } else if (error instanceof NoConnectionError) {
                } else if (error instanceof TimeoutError) {
                    Toast.makeText(getContext(),
                            "Oops. Timeout error!",
                            Toast.LENGTH_LONG).show();

                }
            }
        });
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(
                10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(getActivity()).add(jsonObjectRequest);

    }

    public void onStartTransaction() {

        // while Production
        // PaytmPGService.getProductionService(); //should be override

        PaytmPGService Service = PaytmPGService.getStagingService();
        HashMap<String, String> paramMap = new HashMap<>();

        // these are mandatory parameters

        paramMap.put("MID", MID);
        paramMap.put("ORDER_ID", ORDER_ID);
        paramMap.put("CUST_ID", CUST_ID);
        paramMap.put("INDUSTRY_TYPE_ID", INDUSTRY_TYPE_ID);
        paramMap.put("TXN_AMOUNT", amount);
        paramMap.put("WEBSITE", WEBSITE);
        paramMap.put("CHANNEL_ID", CHANNEL_ID);
        paramMap.put("CALLBACK_URL", CALLBACK_URL + ORDER_ID);
       // paramMap.put("MOBILE_NO", String.valueOf(edit_mobile));
        paramMap.put("CHECKSUMHASH", checksum);

        PaytmOrder Order = new PaytmOrder(paramMap);

        Service.initialize(Order, null);

        Service.startPaymentTransaction(getActivity(), true, true,
                new PaytmPaymentTransactionCallback() {
                    @Override
                    public void someUIErrorOccurred(String inErrorMessage) {

                        Toast.makeText(getActivity(), "UI Error " + inErrorMessage, Toast.LENGTH_LONG).show();
                        // Some UI Error Occurred in Payment Gateway Activity.
                        // // This may be due to initialization of views in
                        // Payment Gateway Activity or may be due to //
                        // initialization of webview. // Error Message details
                        // the error occurred.
                    }


                    @Override
                    public void onTransactionResponse(Bundle inResponse) {
                        Log.d("LOG", "Payment Transaction is successful " + inResponse);
                        Toast.makeText(getActivity(), "Payment Transaction response " + inResponse.toString(), Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void networkNotAvailable() { // If network is not
                        // available, then this
                        // method gets called.

                        Toast.makeText(getActivity(), "Network connection error: Check your internet connectivity", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void clientAuthenticationFailed(String inErrorMessage) {

                        Toast.makeText(getActivity(), "Authentication failed: Server error" + inErrorMessage.toString(), Toast.LENGTH_LONG).show();
                        // This method gets called if client authentication
                        // failed. // Failure may be due to following reasons //
                        // 1. Server error or downtime. // 2. Server unable to
                        // generate checksum or checksum response is not in
                        // proper format. // 3. Server failed to authenticate
                        // that client. That is value of payt_STATUS is 2. //
                        // Error Message describes the reason for failure.
                    }

                    @Override
                    public void onErrorLoadingWebPage(int iniErrorCode,
                                                      String inErrorMessage, String inFailingUrl) {

                        Toast.makeText(getActivity(), "Unable to load web page " + iniErrorCode + inErrorMessage + inFailingUrl, Toast.LENGTH_LONG).show();
                    }

                    // had to be added: NOTE
                    @Override
                    public void onBackPressedCancelTransaction() {
                        Toast.makeText(getActivity(), "Back Pressed, Transaction Cancelled", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onTransactionCancel(String inErrorMessage, Bundle inResponse) {
                        Log.d("LOG", "Payment Transaction Failed " + inErrorMessage);
                        Toast.makeText(getActivity(), "Payment Transaction Failed " + inErrorMessage, Toast.LENGTH_LONG).show();
                    }

                });
    }

   *//* @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
*//*

}
*/