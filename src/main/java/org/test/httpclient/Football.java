package org.test.httpclient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Football
{

	/**进入复仇，查找我的历史里面名次小于该名次的战斗*/
	private static final int SPECIAL_RANK_FIGHT_MAX = 300;
	/**当自己名次大于这个的时候进入复仇*/
	private static final int ENTER_SPECIAL_FIGHT_RANK = 5000;
	public static final String COOKIE = "vjuids=-36eaccbd9.13fd8b62a15.0.e1a838ba; ALLYESID4=00130714092738258523247; usertrack=ezq0JFI63VFkTm4qJbtmAg==; NTES_REPLY_NICKNAME=zhouyuzhy%40126.com%7Czhouyuzhy126%7C-4396741636047328574%7C6037906236%7Chttp%3A%2F%2Fmimg.126.net%2Fp%2Fbutter%2F1008031648%2Fimg%2Fface_big.gif%7C%7C1%7C2; pgv_pvi=6712780800; _ntes_nuid=58f255d303d2b7d12cb143a7c655d9d5; _ntes_nnid=58f255d303d2b7d12cb143a7c655d9d5,1402832271270; locOfHebeiSite=others; ancmt=1; NTES_PASSPORT=MEocq2YEG96TbD4r4MYFtlwBH5mbsF1vHA9DzRXXsqEUN5kdd6D43FndxD8MOikwF8lX6vI9vnWiNiOb_z3fFnesAUdCSpov1gUDyz81w44oz76wH_mFfZm3X; ANTICSRF=41d85ef5005c2c6000718e29fe1a1e59; NTES_SESS=eUHWNZiV5WmZwbSgnYVyB8FeRfLL2npJxSM96DsxWcUV0LnqqVyPCkgqNyQWaIn6kQMAVTY_Tg2I9Ugbuviq0.M0uX1EQE34bj1FyD_SOCZC7EUBvtBl4gN2FlxvSrDzYn9LSAPnx6VqJ9qKa3B3enbtgpangnOTfxiu0Xyw8K4O4; P_INFO=zhouyuzhy@126.com|1404396162|1|other|11&15|zhj&1404396139&exchange#zhj&330100#10#0#0|151496&0|caipiao&epay|zhouyuzhy@126.com; S_INFO=1404579011|1|#0&80#|zhouyuzhy@126.com#zhoushaoyuzsy#huangjqi@126.com#qsampt@yeah.net; ne_analysis_trace_id=1404638743738; vinfo_n_f_l_n3=1ed5cd37e9f3ce62.1.12.1394935693000.1404400019890.1404638746647; s_n_f_l_n3=1ed5cd37e9f3ce621404638354231; pgr_n_f_l_n3=1ed5cd37e9f3ce6214046387437428820; nteslogger_exit_time=1404638746657; JSESSIONID=FAE680404CEE3C766E33C06C5571C8BF.app-1-8010; Futsal_SID=f686e6a8-ff68-4ee1-855a-d102eead5814; sv_id=52; AJSTAT_ok_pages=109; AJSTAT_ok_times=12; vjlast=1373730384.1404638354.11; _ga=GA1.2.80161961.1403615257";

	/**
	 * @param args中文
	 * @throws IOException 
	 * @throws HttpException 
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) throws HttpException, IOException, InterruptedException
	{
		while (true)
		{
			try
			{
				int t = tili();
				if (t <= 0)
				{
					System.err.println("no energy");
					continue;
				}
				String sb = httpRequest("http://a.wc.163.com/game/ranking?showHis=true&_="+System.currentTimeMillis());
				analysisHTML(sb);
				System.out.println("挑战完成");
			}
			catch (IllegalStateException e)
			{
				System.err.println(e.getMessage());
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			finally
			{
				Thread.sleep(60 * 1000);
			}
		}
	}

	private static Integer tili() throws HttpException, IOException
	{
		Document doc = Jsoup.parse(httpRequest("http://a.wc.163.com/history?showHis=true&_="+System.currentTimeMillis()));
		for (Element ele : doc.getElementsByClass("lifes-txt"))
		{
			return NumberUtils.createInteger(String.valueOf((ele.text().charAt(ele.text().length() - 1))));
		}
		return -1;
	}

	/**
	 * @param method
	 */
	private static void httpHeads(HttpMethod method)
	{
		method.setRequestHeader("Accept", "text/html, */*; q=0.01");
		method.setRequestHeader(
				"User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/29.0.1547.66 Safari/537.36 LBBROWSER");
		method.setRequestHeader("Referer", "http://a.wc.163.com/main");
		method.setRequestHeader("Accept-Encoding", "gzip,deflate,sdch");
		method.setRequestHeader("Accept-Language", "zh-CN,zh;q=0.8");
		method.setRequestHeader("Cookie", COOKIE);
		method.setRequestHeader("X-Requested-With", "XMLHttpRequest");
	}

	/**
	 * 01 05 13 26 28 32
	 *	09 15
	 * @param html
	 * @throws IOException 
	 * @throws HttpException 
	 */
	public static void analysisHTML(String html) throws HttpException, IOException
	{
		Document doc = Jsoup.parse(html);
		//获取100名内的战斗url
		String url = call(doc);
		//开始比赛
		httpRequest(url);
	}

	/**
	 * @param url
	 * @throws IOException
	 * @throws HttpException
	 */
	private static String httpRequest(String url) throws IOException, HttpException
	{
		HttpMethod method = new GetMethod(url);
		httpHeads(method);
		HttpClient client = new HttpClient();
		int status = client.executeMethod(method);
		if (status == 200)
		{
			InputStream is = method.getResponseBodyAsStream();
			GZIPInputStream gzipIs = new GZIPInputStream(is);
			BufferedReader br = new BufferedReader(new InputStreamReader(gzipIs));
			StringBuffer sb = new StringBuffer();
			String line = null;
			while ((line = br.readLine()) != null)
			{
				sb.append(line).append("\n");
			}
			is.close();
			method.releaseConnection();
			String result = sb.toString();
			if (StringUtils.contains(result, "用户未登录"))
			{
				throw new IllegalStateException("先抓个cookie过来设置吧,http://2014.163.com/card");
			}
			return result;
		}
		return "";
	}

	private static String call(Document doc) throws HttpException, IOException
	{
		Long myRank = getMyRank();
		if (myRank != null && myRank < 100)
		{
			throw new IllegalStateException("都在100名了，没必要打了！！！等一分钟再检测");
		}
		if (myRank != null && myRank >= ENTER_SPECIAL_FIGHT_RANK)
		{
			return specialFight(myRank);
		}
		String href = "";
		long minRank = Long.MAX_VALUE;
		for (Element div : doc.getElementsByAttributeValue("class", "user-team-rank"))
		{
			if (!StringUtils.equals(div.tagName(), "div"))
			{
				continue;
			}
			Long rank = NumberUtils.createLong(div.text());
			if (rank == null)
			{
				continue;
			}
			if (rank > minRank)
			{
				continue;
			}
			if (rank < 100)
			{
				System.out.println("准备挑战:" + rank);
				return div.nextElementSibling().attr("href").replace("#", "http://a.wc.163.com/game");
			}

			href = div.nextElementSibling().attr("href").replace("#", "http://a.wc.163.com/game");
		}
		System.out.println("准备挑战:" + minRank);
		return href;
	}

	/**
	 * 被比赛历史里面的人复仇打到后面的名次了
	 * @return
	 * @throws IOException 
	 * @throws HttpException 
	 */
	private static String specialFight(long rank) throws HttpException, IOException
	{
		System.err.println("注意被复仇打到" + rank + "名了，尽量手工去完成");
		int page = 1;
		while (page < 10)
		{
			String historyPage = httpRequest("http://a.wc.163.com/history?showHis=true&_="+System.currentTimeMillis()+"&page=" + page);
			String url = handleHistoryAndFight(historyPage, rank);
			if (StringUtils.isNotBlank(url))
			{
				return url;
			}
			page++;
		}
		return null;
	}

	private static String handleHistoryAndFight(String historyPage, long myRank) throws HttpException, IOException
	{
		Document doc = Jsoup.parse(historyPage);
		Elements eles = doc.getElementsByClass("user-team-name");
		for (Element ele : eles)
		{
			String url = getHistoryRank(ele, myRank);
			if (StringUtils.isNotBlank(url))
			{
				return url;
			}
		}
		return null;
	}

	private static String getHistoryRank(Element ele, long myRank) throws HttpException, IOException
	{
		String url = ele.child(0).attr("href").replace("#", "http://a.wc.163.com");
		url +="&_="+System.currentTimeMillis();
		String historyTeamPage = httpRequest(url);
		Document doc = Jsoup.parse(historyTeamPage);
		Elements eles = doc.getElementsByClass("battle-history");
		Long rank = null;
		for (Element e : eles)
		{
			rank = NumberUtils.createLong(e.child(3).getElementsByTag("strong").get(0).text());
		}
		if (rank != null && rank < myRank && rank < SPECIAL_RANK_FIGHT_MAX && rank > 0)
		{
			System.out.println("准备复仇" + rank + "名次");
			return getFightUrl(doc);
		}
		return null;
	}

	private static String getFightUrl(Document doc)
	{
		Elements battleElements = doc.getElementsByClass("battle-btns");
		String url = battleElements.get(0).child(0).attr("href").replace("#", "http://a.wc.163.com");
		return url;
	}

	private static Long getMyRank() throws HttpException, IOException
	{
		String url = "http://a.wc.163.com/main";
		String r = httpRequest(url);
		Document doc = Jsoup.parse(r);
		Elements eles = doc.getElementsByClass("myteam");
		for (Element ele : eles)
		{
			return NumberUtils.createLong((ele.child(0).child(1).child(0).text()));
		}
		return null;
	}

}
