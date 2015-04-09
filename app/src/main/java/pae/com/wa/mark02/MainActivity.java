package pae.com.wa.mark02;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button bload = (Button)findViewById(R.id.btn1);

        DownTask task = new DownTask();
        task.execute("http://128.199.230.75/genkey.php");

        bload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<BasicNameValuePair> data=new ArrayList<BasicNameValuePair>();
                data.add(new BasicNameValuePair("username","wachira"));
                data.add(new BasicNameValuePair("password","ouyporn"));

                PostTask taskpost = new PostTask(MainActivity.this,data);
                taskpost.execute("http://128.199.230.75/authen.php");

            }
        });
    }
    private String downloadTest(String strUrl){
        String strResult="";
        try {
            URL url=new URL(strUrl);

            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestProperty("Keep-Alive","115");
            strResult = readStream(con.getInputStream());
        }catch (Exception e){
            e.printStackTrace();
        }
        return strResult;
    }

    private String readStream(InputStream in) {
        BufferedReader reader=null;
        StringBuilder sb = new StringBuilder();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line=reader.readLine())!=null){
                sb.append(line+"\n");
            }
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            if (reader!=null){
                try {
                    reader.close();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }


    private class DownTask extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... urls){
            return downloadTest(urls[0]);
        }
        @Override
        protected void onPostExecute(String jsonstr){
            String msg="";

            try {
                JSONObject json = new JSONObject(jsonstr);

                msg=json.getString("key");


            }catch (JSONException e){
                e.printStackTrace();
            }

            TextView txt =(TextView)findViewById(R.id.txt1);
            txt.setText(msg);

        }


    }
}