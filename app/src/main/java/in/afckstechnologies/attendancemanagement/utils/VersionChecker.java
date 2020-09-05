package in.afckstechnologies.attendancemanagement.utils;

import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;

import org.jsoup.Jsoup;

import java.io.IOException;


public class VersionChecker extends AsyncTask<String, String, String> {

    String newVersion = "";

    //String pkgname=MyApplicatio.getContext().getPackageName();
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected String doInBackground(String... params) {

        try {
            newVersion = Jsoup.connect("https://play.google.com/store/apps/details?id=" + MyApplicatio.getInstance().getPackageName() + "&hl=en")
                    .timeout(30000)
                    .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                    .referrer("http://www.google.com")
                    .get()
                    .select(".hAyfc .htlgb")
                    .get(7)
                    .ownText();


        } catch (IOException e) {
            e.printStackTrace();
        }

        return newVersion;


       /* StringBuilder sb = new StringBuilder();

        try (Reader reader = new InputStreamReader(
                new URL("https://play.google.com/store/apps/details?id=" + MyApplicatio.getContext().getPackageName() + "&hl=en")
                        .openConnection()
                        .getInputStream()
                , "UTF-8"
        )) {
            while (true) {
                int ch = reader.read();
                if (ch < 0) {
                    break;
                }
                sb.append((char) ch);
            }
        } catch (MalformedURLException ex) {
            Log.d("ERROR", ex.getMessage());
            return null;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Log.d("ERROR", e.getMessage());
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("ERROR", e.getMessage());
            return null;
        }

        String parts[] = sb.toString().split("Current Version");
        String parts1 = newVersion = parts[1].substring(parts[1].indexOf("htlgb") + 7);
        newVersion = parts1.substring(parts1.indexOf("htlgb") + 7);
        String parts2[] = newVersion.split("<");
        newVersion = parts2[0];
        // newVersion = parts[1].substring(parts[1].indexOf("htlgb") + 7, parts[1].indexOf("htlgb") + 11).trim().toString();
        return newVersion;*/

    }
}

/*
 .select(".hAyfc .htlgb")
         .get(3)
         .ownText();*/
