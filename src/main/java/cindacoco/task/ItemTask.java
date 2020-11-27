package cindacoco.task;

import cindacoco.mapper.JDMapper;
import cindacoco.pojo.JD;
import cindacoco.util.HttpUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class ItemTask {

    @Autowired
    HttpUtils httpUtils;

    @Autowired
    JDMapper jdMapper;


    private static final ObjectMapper MAPPER = new ObjectMapper();


    //当下载任务完成后 间隔多长时间 进行下一次任务
    @Scheduled(fixedDelay = 100*1000)
    public void itemTask() throws Exception{
        String url = "https://search.jd.com/Search?keyword=%E6%89%8B%E6%9C%BA&wq=%E6%89%8B%E6%9C%BA&s=116&click=0&page=";
        for(int i=1;i<20;i+=2){
            String html = httpUtils.doGet(url+i);
            this.parse(html);
        }

    }

    private void parse(String html) throws Exception{
        Document doc = Jsoup.parse(html);

        Elements spuElements = doc.select("div#J_goodsList > ul > li");
        for (Element spuElement : spuElements) {
            String spu = spuElement.attr("data-spu");
            Elements skuElements = spuElement.select("li.ps-item");
            for (Element skuElement : skuElements) {
                String sku = skuElement.select("[data-sku]").attr("data-sku");
                JD jd = new JD();
                jd.setSku(sku);
                List<JD> jds = jdMapper.findAll(jd);
                if(jds.size()>0){
                    continue;
                }
                jd.setSpu(spu);
                String itemUrl = "https://item.jd.com/"+sku+".html";
                jd.setUrl(itemUrl);

                String img = "https:" + skuElement.select("img[data-sku]").first().attr("data-lazy-img");
                String bigImg = img.replace("/n7/", "/n1/");
                String finalImage = this.httpUtils.doGetImage(bigImg);
                jd.setPic(finalImage);
                String priceUrl = "https://p.3.cn/prices/mgets?skuIds=J_" + sku;
                String priceJson = this.httpUtils.doGet(priceUrl);
                double price = MAPPER.readTree(priceJson).get(0).get("p").asDouble();
                jd.setPrice(price);

                String itemInfo = this.httpUtils.doGet(jd.getUrl());
                String title = Jsoup.parse(itemInfo).select("div.sku-name").text();
                jd.setTitle(title);
                jdMapper.save(jd);
            }

        }
    }
}
