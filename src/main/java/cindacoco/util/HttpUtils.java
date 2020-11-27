package cindacoco.util;

import org.apache.http.Header;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

@Component
public class HttpUtils {
    static Header[] headers = new Header[]{
            new BasicHeader("User-Agent",
                    "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:65.0) Gecko/20100101 Firefox/65.0"),
            new BasicHeader("Accept",
                    "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8"),
            new BasicHeader("Accept-Language","en-US,en;q=0.8,zh-CN;q=0.5,zh;q=0.3"),
            new BasicHeader("Referer","https://www.jd.com/"),
            new BasicHeader("DNT","1"),
            new BasicHeader("Connection","keep-alive"),
            new BasicHeader("Upgrade-Insecure-Requests","1"),
            new BasicHeader("TE","Trailers")
    };
    private PoolingHttpClientConnectionManager cm;
    HttpUtils(){
        this.cm = new PoolingHttpClientConnectionManager();
        this.cm.setMaxTotal(100);
        this.cm.setDefaultMaxPerRoute(10);
    }
    /**
     * 根据请求地址下载页面
     * @param url
     * @return html
     */
    public String doGet(String url){
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(this.cm).build();

        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeaders(headers);
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpGet);
            if(response.getStatusLine().getStatusCode()==200){
                if(response.getEntity()!=null){
                    return EntityUtils.toString(response.getEntity(),"utf8");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(response!=null){
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return "";
    }


    public String doGetImage(String url){
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(this.cm).build();

        HttpGet httpGet = new HttpGet(url);
        httpGet.setHeaders(headers);
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpGet);
            if(response.getStatusLine().getStatusCode()==200){
                if(response.getEntity()!=null){
                    //获取图片后缀
                    String extName = url.substring(url.lastIndexOf("."));

                    //创建图片名，重命名图片
                    String picName = UUID.randomUUID().toString()+extName;
                    //下载图片
                    OutputStream outputStream = new FileOutputStream(new File("D:\\爬虫数据",picName));
                    response.getEntity().writeTo(outputStream);
                    //返回图片名称
                    return picName;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(response!=null){
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return "";
    }

    private RequestConfig getConfig(){
        return RequestConfig.custom()
                .setSocketTimeout(10000)
                .setConnectTimeout(1000)
                .setConnectionRequestTimeout(500)
                .build();
    }
}
