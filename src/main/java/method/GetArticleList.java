package main.java.method;

import main.java.work.SimpleSpider;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 每一页文章列表
 */
public class GetArticleList {

    public static List<String> getItem(String html) {
        List<String> list = new ArrayList<String>();
        Pattern p = Pattern.compile(SimpleSpider.ARTICLE_URL);
        Matcher m = p.matcher(html);
        while (m.find()) {
            // 将抽取的文章url进队
            list.add(SimpleSpider.DOMAIN + m.group(0));
            System.out.println(SimpleSpider.DOMAIN + m.group(0));
        }
        if (list.size() > 0) {
            List<String> tempList = new ArrayList();
            for (String i : list) {
                // 去重
                if (!tempList.contains(i)) {
                    tempList.add(i);
                }
            }
            return tempList;
        }
        return null;
    }

}
