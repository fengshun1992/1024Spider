package main.java.method;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class HtmlParser implements Runnable {
    private String html;
    private int no;

    // Use precompile
    private static Pattern TITLE_PATTERN = Pattern.compile("<title>(.*?)\\|");
    private static Pattern IMGURL_PATTERN = Pattern.compile("<img src=\"(http.*?)\"");

    public HtmlParser(String html, int no) {
        this.html = html;
        this.no = no + 1;
    }

    @Override
    public void run() {
        System.out.println("==========第" + no + "篇============");
        Matcher m = TITLE_PATTERN.matcher(html);
        String title = "";
        List<String> list = new ArrayList();

        if (m.find()) {
            title = m.group(1);
            title = title.replace(" ", "").replace("，", "").replace("、", "").replace("。", "");
            if (title.length() >= 30) {
                title = title.substring(30);
            }
            System.out.println(title);
        }
        m = IMGURL_PATTERN.matcher(html);
        while (m.find()) {
            list.add(m.group(1));
        }

        for (String imageUrl : list) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            new Thread(new ImageCreator(imageUrl, title)).start();
        }
    }

}
