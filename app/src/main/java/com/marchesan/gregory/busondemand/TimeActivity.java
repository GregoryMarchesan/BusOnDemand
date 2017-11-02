//package com.marchesan.gregory.busondemand;
//
//import android.app.Activity;
//import android.app.Dialog;
//import android.app.TimePickerDialog;
//import android.support.v4.app.DialogFragment;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.widget.TimePicker;
//
//public class TimeActivity extends DialogFragment {
//    int hour_x;
//    int minute_x;
//    public static final int TIME_PICKER_ID = 1;
//
//    private static final String TAG = LocalDialog.class.getCanonicalName();
//    private Activity activity = null;
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//    }
//
//    public static LocalDialog getInstance(LocalDialog.OnAddMarker listner)
//    {
//        LocalDialog fragmentDialog = new LocalDialog();
//        return fragmentDialog;
//    }
//
//    @Override
//    public void onAttach(Activity activity)
//    {
//        super.onAttach(activity);
//        this.activity = activity;
//    }
//
//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState)
//    {
//        return new TimePickerDialog(activity, kTimePickerListener, hour_x, minute_x, false);
//    }
//
////    @Override
////    protected Dialog onCreateDialog(int id) {
////
////    }
//
//    protected TimePickerDialog.OnTimeSetListener kTimePickerListener =
//            new TimePickerDialog.OnTimeSetListener(){
//                @Override
//                public void onTimeSet(TimePicker view, int hourOfDay, int minuteOfDay) {
//                    hour_x = hourOfDay;
//                    minute_x = minuteOfDay;
//                }
//            };
//}
