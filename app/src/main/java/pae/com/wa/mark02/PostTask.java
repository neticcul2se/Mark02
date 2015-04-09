package pae.com.wa.mark02;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.TextView;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class PostTask extends AsyncTask<String,Void,String> {
    private Context mContext;
    private ArrayList<BasicNameValuePair> mData;
    public PostTask(Context context, ArrayList<BasicNameValuePair> data){
        mContext =context;
        mData=data;

    }
    @Override
    protected String doInBackground(String... urls){

        return postData(urls[0]);
    }
    @Override
    protected void onPostExecute(String result2){
        TextView txt2=(TextView)((Activity)mContext).findViewById(R.id.txt2);
        txt2.setText(result2);
    }
    private String postData(String strUrl){
        String charset = Charset.defaultCharset().displayName();
        String strResult="";
        try {

            String requsetBody= setRequestBody(charset);
            URL url = new URL(strUrl);
            HttpURLConnection con =(HttpURLConnection)url.openConnection();
            con.setDoOutput(true);
            con.setRequestProperty("Keep-Alive","115");

            con.setRequestProperty("Content-Type","application/x-www-form-urlencoded; charset="+charset);
            con.setFixedLengthStreamingMode(requsetBody.length());
            OutputStream out = con.getOutputStream();
            out.write(requsetBody.getBytes(charset));
            out.flush();
            strResult=readStream(con.getInputStream());

        }catch (Exception e){
            e.printStackTrace();
        }
        return strResult;
    }
    private String setRequestBody(String charset)throws UnsupportedEncodingException {
        StringBuilder sb=new StringBuilder();
        for (int i = 0; i < mData.size() ; i++) {
            NameValuePair item=mData.get(i);
            sb.append(URLEncoder.encode(item.getName(), charset));
            sb.append("=");
            sb.append(URLEncoder.encode(item.getValue(),charset));
            if (i != (mData.size()-1)) {
                sb.append("&");

            }


        }
        return sb.toString();
    }
    private String readStream(InputStream in){
        BufferedReader reader =null;
        StringBuilder sb = new StringBuilder();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line=reader.readLine())!=null){
                sb.append(line + "\n");
            }
        }catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(reader !=null) {
                try {
                    reader.close();

                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();

    }


}
