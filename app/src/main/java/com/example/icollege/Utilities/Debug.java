package com.example.icollege.Utilities;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class Debug
{
    public static void This(String text) {
        Log.d(Constants.DTag, text);
    }
    public static void ToastShort(Context context, String text)
    {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }
    public static void ToastLong(Context context, String text)
    {
        Toast.makeText(context, text, Toast.LENGTH_LONG).show();
    }
}
