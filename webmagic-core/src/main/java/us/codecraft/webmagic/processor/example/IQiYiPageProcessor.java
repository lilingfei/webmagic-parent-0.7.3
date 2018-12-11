package us.codecraft.webmagic.processor.example;

import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.pipeline.JsonFilePipeline;
import us.codecraft.webmagic.processor.PageProcessor;

public class IQiYiPageProcessor implements PageProcessor{

	private Site site = Site.me().setDomain("list.iqiyi.com").setSleepTime(5).setRetryTimes(4)
	.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/66.0.3359.139 Safari/537.36");
	@Override
	public void process(Page page) {
		// TODO Auto-generated method stub
		System.out.println("------------------------"+page.getUrl()+"//////////////////////////.......");
		if(page.getUrl().regex("http://www.iqiyi.com/.*.html#vfrm=.*").match()){
			//剧名
			page.putField("title", page.getHtml().xpath("//div[@class='info-intro']/h1/a[1]/@title"));
			//类型
			page.putField("type", page.getHtml().xpath("//p[@class='episodeIntro-area']/a"));
			//剧集链接
			page.putField("link", page.getHtml().links().regex("http://www.iqiyi.com/v_19rr.*.html").all());
		}
		if(page.getResultItems().get("title") == null){
		    page.setSkip(true);		
		    }
		if(page.getUrl().regex("http://list.iqiyi.com/www/2/-----------2018--4-1-1-iqiyi--.html").match()){
			//列表链接
			page.addTargetRequests(page.getHtml().xpath("//div[@class='site-piclist_pic']/a/@href").all());
			//分页  http://list.iqiyi.com/www/2/-------------4-2-1-iqiyi--.html
			page.addTargetRequests(page.getHtml().xpath("//div[@class='mod-page']/a/@href").all());
		}
	}

	@Override
	public Site getSite() {
	
		return site;
	}
	
	public static void main(String[] args) {	
		Spider.create(new IQiYiPageProcessor()).addUrl("http://list.iqiyi.com/www/2/-----------2018--4-1-1-iqiyi--.html")
		.addPipeline(new JsonFilePipeline("/data/webmagic"))
		.thread(3).run();
	}

}
