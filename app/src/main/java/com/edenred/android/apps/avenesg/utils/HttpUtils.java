package com.edenred.android.apps.avenesg.utils;

import android.util.Log;

import com.edenred.android.apps.avenesg.constant.Urls;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by zhaoxin on 2015/7/22.
 * webservice获取网络数据
 */
public class HttpUtils {

    public HttpUtils() {
    }

    public String putParam(HashMap<String, String> params, String MethodName) {
        //获取所有HashMap键值集合
//        Set<String> set = params.keySet();
//        Iterator<String> it = set.iterator();
//        List<String> list = new ArrayList<String>();
//        while (it.hasNext()) {
//            list.add(it.next());
//        }
        String parmstr = "";
//        for (int i = 0; i < list.size(); i++) {
//            String key = list.get(i);
//            String value = params.get(key);
//            parmstr += "                <" + key + ">" + value + "</" + key + ">\n";
//        }
        for (HashMap.Entry<String, String> entry : params.entrySet()) {

            if(entry.getKey().equals("list"))
            {
                parmstr += "                " + entry.getValue() + "\n";
            }else
            {
                parmstr += "                <" + entry.getKey() + ">" + entry.getValue() + "</" + entry.getKey() + ">\n";
            }

        }
        Log.e("parmstr", parmstr);
        //请求头文件
        String str = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
                "<soap:Envelope xmlns:soap=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\">\n" +
                "    <soap:Body>\n" +
                "        <" + MethodName + " xmlns=\"http://app.webservice.crm.accor.com/\">\n" +
                "            <arg0 xmlns=\"\">\n" +
                parmstr +
                "            </arg0>\n" +
                "        </" + MethodName + ">\n" +
                "    </soap:Body>\n" +
                "</soap:Envelope>";

        InputStream inStream = new ByteArrayInputStream(str.getBytes());
        String result = "error";
        try {
            String soap = readSoapFile(inStream, params);
            byte[] data = soap.getBytes();
            result = getHttpByUrl(data);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("返回数据   ", e.getMessage());
        }
        Log.e("返回数据   ", result);
        return result;
    }


    private String getHttpByUrl(byte[] data) throws Exception {
        // 提交Post请求  
        URL url = new URL(Urls.HOST);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setConnectTimeout(5 * 1000);
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "application/soap+xml; charset=utf-8");
        conn.setRequestProperty("Content-Length", String.valueOf(data.length));
        OutputStream outStream = conn.getOutputStream();
        outStream.write(data);
        outStream.flush();
        outStream.close();
        if (conn.getResponseCode() == 200) {
            // 解析返回信息
            String res = inputStream2String(conn.getInputStream());
            return res;
        }
        return "error";
    }

    private String readSoapFile(InputStream inStream, HashMap<String, String> params) throws Exception {
        // 从流中获取文件信息  
        byte[] data = readInputStream(inStream);
        String soapxml = new String(data);
        return replace(soapxml, params);
    }


    /**
     * 读取流信息
     *
     * @param inputStream
     * @return
     * @throws Exception
     */
    private byte[] readInputStream(InputStream inputStream) throws Exception {
        byte[] buffer = new byte[1024];
        int len = -1;
        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
        while ((len = inputStream.read(buffer)) != -1) {
            outSteam.write(buffer, 0, len);
        }
        outSteam.close();
        inputStream.close();
        return outSteam.toByteArray();
    }


    /**
     * 替换文件中占位符
     *
     * @param xml
     * @param params
     * @return
     * @throws Exception
     */
    private String replace(String xml, Map<String, String> params) throws Exception {
        String result = xml;
        if (params != null && !params.isEmpty()) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                String name = "\\$" + entry.getKey();
                Pattern pattern = Pattern.compile(name);
                Matcher matcher = pattern.matcher(result);
                if (matcher.find()) {
                    result = matcher.replaceAll(entry.getValue());
                }
            }
        }
        return result;
    }


    /**
     * 解析XML文件
     *
     * @return
     * @throws Exception
     */
//    private static String parseResponseXML(InputStream inStream) throws Exception {
//        XmlPullParser parser = Xml.newPullParser();
//        parser.setInput(inStream, "UTF-8");
//        int eventType = parser.getEventType();// 产生第一个事件
//        while (eventType != XmlPullParser.END_DOCUMENT) {
//            // 只要不是文档结束事件
//            switch (eventType) {
//                case XmlPullParser.START_TAG:
//                    String name = parser.getName();// 获取解析器当前指向的元素的名称
//                    if (methodName.equals(name)) {
//                        return parser.nextText();
//                    }
//                    break;
//            }
//            eventType = parser.next();
//        }
//        return null;
//    }
    public static String inputStream2String(InputStream is) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int i = -1;
        while ((i = is.read()) != -1) {
            baos.write(i);
        }
        is.close();
        return baos.toString();
    }

}
