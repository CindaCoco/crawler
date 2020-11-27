import org.apache.http.Header;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.junit.Test;
import sun.net.www.http.HttpClient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;

public class HttpTest {


    @Test
    public void test1() throws IOException {
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        BasicClientCookie cookie = new BasicClientCookie("name","fuck");
        CookieStore cookieStore = new BasicCookieStore();
        cookieStore.addCookie(cookie);
        CloseableHttpClient client = httpClientBuilder.setDefaultCookieStore(cookieStore).build();


        HttpRequestBase httpGet = new HttpGet("https://search.jd.com/Search?keyword=%E6%89%8B%E6%9C%BA");
        Header[] headers = new Header[]{
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
        httpGet.setHeaders(headers);
        CloseableHttpResponse response = client.execute(httpGet);
        System.out.println("###############################");
        String html = EntityUtils.toString(response.getEntity(),"utf8");
        FileOutputStream fos = new FileOutputStream(new File("test.html"));
        fos.write(html.getBytes());
        System.out.println("###############################");


    }
}
