package gmedia.net.id.OnTime.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import android.util.TypedValue;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {

    public static int dpToPx(Context context,int dp) {
        Resources r = context.getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    public static String package_name (Context context) {
        return context.getApplicationContext().getPackageName();
    }

    public static String loginActivity(Context context){
        return context.getApplicationContext().getPackageName()+".LoginActivity";
    }

    public static String formatTgl(String tgl){
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = fmt.parse(tgl);
            SimpleDateFormat fmtOut = new SimpleDateFormat("dd MMMM yyyy");
            return fmtOut.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String formatDate(String fBefore, String fAfter, String tgl){
        SimpleDateFormat fmt = new SimpleDateFormat(fBefore);
        try {
            Date date = fmt.parse(tgl);
            SimpleDateFormat fmtOut = new SimpleDateFormat(fAfter);
            return fmtOut.format(date);
        }catch (ParseException e){
            e.printStackTrace();
        }
        return null;
    }

    public static String formatTime(String time){
        SimpleDateFormat fmt = new SimpleDateFormat("HH:mm:ss");
        try {
            Date date = fmt.parse(time);
            SimpleDateFormat fmtOut = new SimpleDateFormat("HH:mm");
            return fmtOut.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String customFormatTimestamp(Date timestamp, String format ){
        SimpleDateFormat sdFormat = new SimpleDateFormat(format);
        String date = sdFormat.format(timestamp);
        return date;
    }

    public static Double parseNullDouble(String s) {
        double result = 0;
        if (s != null && s.length() > 0) {
            try {
                result = Double.parseDouble(s);
            } catch (Exception e) {
                e.printStackTrace();

            }
        }
        return result;
    }

    public static  String formatRupiah(String number) {

        double value = parseNullDouble(number);

        NumberFormat format = NumberFormat.getCurrencyInstance();
        DecimalFormatSymbols symbols = ((DecimalFormat) format).getDecimalFormatSymbols();

        symbols.setCurrencySymbol("");
        ((DecimalFormat) format).setDecimalFormatSymbols(symbols);
        format.setMaximumFractionDigits(0);

        String hasil = String.valueOf(format.format(value));

        return hasil;
    }

    public static boolean safeToTakePicture = false;

    public static boolean afterSnapCamera=false;
}
