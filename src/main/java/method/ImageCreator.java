package main.java.method;

import main.java.work.SimpleSpider;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

public class ImageCreator implements Runnable {
    private static int count = 0;
    private String imageUrl;
    private String title;
    // 存储路径
    private static final String basePath = "E:/1024_" + SimpleSpider.fid + "_" + SimpleSpider.page + "/";

    public ImageCreator(String imageUrl, String title) {
        this.imageUrl = imageUrl;
        this.title = title;
    }

    @Override
    public void run() {
        File dir = new File(basePath + title);
        if (!dir.exists()) {
            dir.mkdirs();
            System.out.println("图片存放于" + basePath + title + "目录下");
        }
        String imageName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1);
        try {
            File file = new File(basePath + title + "/" + imageName);
            OutputStream os = new FileOutputStream(file);
            // 创建一个url对象
            URL url = new URL(imageUrl);
            URLConnection con = url.openConnection();
            con.setConnectTimeout(5 * 1000);
            String referer = url.getProtocol() + "://" + url.getHost();
            con.setRequestProperty("Referer", referer);
            HttpClient http = new DefaultHttpClient();

            InputStream is = null;
            try {
                HttpGet hg = new HttpGet(imageUrl);
                HttpResponse hr = http.execute(hg);
                HttpEntity he = hr.getEntity();
                if (he != null) {
                    is = he.getContent();
                    byte[] buff = new byte[1024];
                    while (true) {
                        int readed = is.read(buff);
                        if (readed == -1) {
                            break;
                        }
                        byte[] temp = new byte[readed];
                        System.arraycopy(buff, 0, temp, 0, readed);
                        // 写入文件
                        os.write(temp);
                    }
                    count = count + 1;
                    System.out.println("第" + count + "张妹子:"
                            + file.getAbsolutePath());
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (is != null) {
                    is.close();
                }
                os.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
