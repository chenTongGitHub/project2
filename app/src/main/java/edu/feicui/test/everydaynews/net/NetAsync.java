package edu.feicui.test.everydaynews.net;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * Created by Administrator on 16-9-22.
 */
public class NetAsync extends AsyncTask<Request,Object,Response>{
    ProgressDialog dialog;
    HttpURLConnection connection = null;
    OnResultFinishListener mListener;
    public NetAsync(Context context,OnResultFinishListener mListener){
        dialog=ProgressDialog.show(context,"","加载中...");
        this.mListener=mListener;
    }




    @Override
    protected Response doInBackground(Request... params) {
        //开启AsyncTask时，只传了一个参数：在MyHttp中 async.execute(request);
        Request request=params[0];
        Response response=new Response();
        try {
            URL url=new URL(request.url);
            connection= (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(Constants.CONNECT_TIMEOUT);
            connection.setReadTimeout(Constants.READ_TIMEOUT);
            //get
            if (request.type==Constants.GET){
                connection.setRequestMethod("GET");
            //post
            }else{
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                OutputStream outputStream=connection.getOutputStream();
                outputStream.write(Utils.getUrl(request.params,Constants.POST).getBytes());
            }
            int code=connection.getResponseCode();
            response.code=code;
            Log.e("aaa", "doInBackground: "+"***"+code+"————"+response.code);
            if(code==connection.HTTP_OK){
                InputStream inputStream=connection.getInputStream();
                byte[]bytes=new byte[1024];
                int len;
                StringBuffer buffer=new StringBuffer();
                while((len=inputStream.read(bytes))!=-1){
                    buffer.append(new String(bytes,0,len));
                }
                //拿到了结果
                response.result=buffer.toString();
            }



        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (connection != null) {
                connection.disconnect();
            }
        }



        return response;
    }

    @Override
    protected void onPostExecute(Response o) {
        super.onPostExecute(o);
        //拿到结果
        dialog.dismiss();
        Response response=  o;
        if (o.code!=connection.HTTP_OK){//失败
            mListener.failed(response);
        }else{//成功
            mListener.success(response);
        }
    }
}
