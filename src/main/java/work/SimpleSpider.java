package main.java.work;

import main.java.method.GetArticleList;
import main.java.method.HtmlParser;
import main.java.utils.StreamToStringUtil;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 1024图片简单爬虫
 */
public class SimpleSpider {

    //1024域名
    public static final String DOMAIN = "http://d2.sku117.org/pw/";
    //15 zipaitoupai 16 luochujiqing 49 toukuiyuanchuang /73自由网盘  25
    public static int fid = 16;
    //所爬页码
    public static int page = 1;
    //该页文章列表
    public static String PAGE_URL = DOMAIN + "thread.php?fid=" + fid + "&page=" + page;
    //文章url通配符
    public static String ARTICLE_URL = "htm_data/" + fid + "/\\d{4}/\\d{1,}.html";

    public static void dowork() {
        // HttpClient 超时配置
        RequestConfig globalConfig = RequestConfig.custom()
                .setCookieSpec(CookieSpecs.STANDARD)
                .setConnectionRequestTimeout(6000).setConnectTimeout(6000)
                .build();
        CloseableHttpClient httpClient = HttpClients.custom()
                .setDefaultRequestConfig(globalConfig).build();
        System.out.println("5秒后开始抓图……");

        // 创建一个GET请求
        HttpGet httpGet = new HttpGet(PAGE_URL);
        httpGet.addHeader(
                "User-Agent",
                "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2311.152 Safari/537.36");
        httpGet.addHeader(
                "Cookie",
                "_gat=1; nsfw-click-load=off; gif-click-load=on; _ga=GA1.2.1861846600.1423061484");
        List<String> articleList = new ArrayList<String>();

        try {
            CloseableHttpResponse response = httpClient.execute(httpGet);
            InputStream in = response.getEntity().getContent();
            String html = StreamToStringUtil.convertStreamToString(in);
            articleList = GetArticleList.getItem(html);
            if (articleList == null || articleList.size() <= 0) {
                System.err.println("错误");
                return;
            }
            System.out.println("该页共有" + articleList.size() + "篇");
        } catch (Exception e) {
            e.printStackTrace();
        }

        //开线程 爬前10篇
        for (int i = 0; i < 10; i++) {
            try {
                // 不敢爬太快
                Thread.sleep(5000);
                // 发送请求，并执行
                HttpGet httpGet2 = new HttpGet(articleList.get(i));
                CloseableHttpResponse response2 = httpClient.execute(httpGet2);
                InputStream in2 = response2.getEntity().getContent();
                String html2 = StreamToStringUtil.convertStreamToString(in2);
                // 网页内容解析
                new Thread(new HtmlParser(html2, i)).start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        SimpleSpider.dowork();
    }
}
