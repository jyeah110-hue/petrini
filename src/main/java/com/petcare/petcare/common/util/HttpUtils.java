package com.petcare.petcare.common.util;

import java.util.Map;

import com.petcare.petcare.common.exception.ResponseExceptionHandler;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class HttpUtils {
    public static String request(String apiUrl, 
                                 String method,
                                 Map<String, String> headers,
                                 String body) throws Exception {
        
        HttpURLConnection conn = null;
        try {
            URL url = URI.create(apiUrl).toURL(); 
            conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod(method);
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(10000);

            if (headers != null) {
                //Map은 인덱스 없어서 for문 사용하려면 List로 변환해야함
                for (Map.Entry<String, String> entry : headers.entrySet()) {
                    conn.setRequestProperty(entry.getKey(), entry.getValue());
                }
            }

            if (body != null) {
                conn.setDoOutput(true);
                OutputStream os = conn.getOutputStream();
                os.write(body.getBytes(StandardCharsets.UTF_8));
                os.flush();
                os.close();
            }

            //성공이든 실패든 읽어서 처리해준다
            int status = conn.getResponseCode();
            InputStream is = (status >= 200 && status < 300) ? conn.getInputStream() : conn.getErrorStream();

            StringBuilder sb = new StringBuilder();
            if (is != null) {
                try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
                    String line;
                    while((line = br.readLine()) != null) {
                        sb.append(line);
                    }
                }
            }

            if (status < 200 || status >= 300) {
                throw new ResponseExceptionHandler(status, sb.toString());
            }

            return sb.toString();
        } 
        finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    public static String escapeJson(String str) {
        if (str == null) return "";
        return str.replace("\\", "\\\\")
                  .replace("\"", "\\\"")
                  .replace("\n", "\\n")
                  .replace("\r", "")
                  .replace("\t", "\\t");
    }    
}
