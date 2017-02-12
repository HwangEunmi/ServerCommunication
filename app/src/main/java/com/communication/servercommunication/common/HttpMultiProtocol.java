package com.communication.servercommunication.common;

import android.os.AsyncTask;
import android.util.Log;

import com.communication.servercommunication.model.BaseData;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by 은미 on 2017-01-19.
 */

/*SOSContentData: form-data방식(Multipart) / body에 값을 넣어서 보냄
* SOSListData:    x-www-form-urlencoded(일반) 방식 / POST방식인데,
                  body에 값을 넣어서 보내는것이 아닌, url에 붙임(쿼리스트링)*/

/*서버와 통신을 하는 프로토콜 클래스*/
public class HttpMultiProtocol extends AsyncTask<BaseData, Void, BaseData> {

    private HttpURLConnection mUrlConnection;

    private URL mUrl;

    /*Multipart용*/
    private PrintWriter mWriter;

    private final String LINE_FEED = "\r\n";

    private final String CHARSET = "UTF-8";

    private final String TWO_HYPHENS = "--";

    private final String BOUNDARY = TWO_HYPHENS + System.currentTimeMillis() + TWO_HYPHENS;

    /*onPostExecute()메소드에 사용 될 인터페이스
    * (onPostExecute()는 뷰 변경 못하니까)*/
    private Utils.NetworkCheckCallback callback;

    /*생성자에 인터페이스 셋팅*/
    public HttpMultiProtocol(Utils.NetworkCheckCallback callback) {
        this.callback = callback;
    }

    @Override
    protected BaseData doInBackground(BaseData... params) {
        Log.d("TEST", "body : " + params);

        try {
            BaseData protocol = params[0];

            /*Request의 url주소*/
            String path;

            /*URL을 이용해서 서버통신 준비*/
            if (protocol.getmServiceUrl().equals(Constant.GET_BOARD_LIST_URL)) {
                path = Constant.GET_BOARD_LIST_URL + "?" + protocol.getParamData();

                /*서버통신 메소드 호출*/
                setURLsetting(path, protocol);
                mUrlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                /*Body에 값 써서 보내기*/
                  /*
                * OutputStream opstream = urlConnection.getOutputStream();
                * opstream.write(protocol.paramData.getBytes());
                *
                * opstream.flush();
                * opstream.close();
                  */
            } else if (protocol.getmServiceUrl().equals(Constant.GET_BOARD_CONTENT_URL)) {
                path = Constant.GET_BOARD_CONTENT_URL;

                /*서버통신 메소드 호출*/
                setURLsetting(path, protocol);
                mUrlConnection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);

                OutputStream opstream = mUrlConnection.getOutputStream();
                mWriter = new PrintWriter(new OutputStreamWriter(opstream, CHARSET), true);

                addFormField(mWriter, "boardSeq", protocol.getParamData());
            }

            /*만약 해당 통신이 Multipart라면, 마무리 Boundary 추가해주기*/
            if (protocol.getmServiceUrl().equals(Constant.GET_BOARD_CONTENT_URL)) {
                addBoundary(mWriter);
            }

            /*서버통신 결과 확인하는 곳*/
            int code = mUrlConnection.getResponseCode();
            if (code >= 200 && code < 300) { // 성공일 경우

                StringBuilder data = new StringBuilder();
                String buffer = null;

                /*서버로부터 결과값을 읽어오기*/
                BufferedReader reader = new BufferedReader(new InputStreamReader(mUrlConnection.getInputStream()));
                while ((buffer = reader.readLine()) != null) {
                    data.append(buffer);
                }

                String tempData = data.toString();

                /*해당하는 모델에 맞게 JSON 파싱*/
                Gson gson = new Gson();
                protocol.mDataClass = gson.fromJson(tempData, protocol.mDataClass.getClass());

                reader.close();
                mUrlConnection.disconnect();

                return protocol;

            }
            /*결과코드가 200번대가 아닐 경우(실패)*/
            else {
                Log.d("TEST", "Network Error! : " + code);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(BaseData protocol) {
        super.onPostExecute(protocol);
        if (callback != null) {
            callback.onSuccess(protocol);
            callback.onFail(protocol);
        }

        /*처리가 끝난 후 AsyncTask 취소시켜줌*/
        this.cancel(true);
    }

    /*URL을 이용해서 서버통신 준비하는 메소드*/
    private HttpURLConnection setURLsetting(String path, BaseData protocol) throws IOException {

        mUrl = new URL(path);
        mUrlConnection = (HttpURLConnection) mUrl.openConnection();

        mUrlConnection.setRequestMethod(protocol.getmRequestMethod());
        mUrlConnection.setUseCaches(false);
        mUrlConnection.setDefaultUseCaches(false);
        mUrlConnection.setDoInput(true);
        mUrlConnection.setDoOutput(true);

        /*default 크기 만큼 쓰겠다는 뜻*/
        mUrlConnection.setChunkedStreamingMode(0);
        mUrlConnection.setConnectTimeout(30000);
        mUrlConnection.setReadTimeout(10000);

        return mUrlConnection;
    }

    /*Multipart용 필드 메소드*/
    private void addFormField(PrintWriter writer, String name, String value) {
        writer.append(LINE_FEED);
        writer.append("--" + BOUNDARY).append(LINE_FEED);
        writer.append("Content-Disposition: form-data; name=\"" + name + "\"").append(LINE_FEED);
        writer.append("Content-Type: text/plain; charset=" + CHARSET).append(LINE_FEED);
        writer.append(LINE_FEED);
        writer.append(value);
        writer.flush();
    }

    /*Boundary 추가하는 메소드*/
    private void addBoundary(PrintWriter writer) {
        writer.append(LINE_FEED).flush();
        writer.append("--" + BOUNDARY + "--").append(LINE_FEED);
        writer.close();
    }
}
