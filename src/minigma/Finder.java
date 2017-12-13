package minigma;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Comparator;
import java.util.StringTokenizer;
import java.util.TreeSet;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Finder {
	//properties
	String google;
	String charset;
	String userAgent;
	TreeSet<PossibleSolution> TS;
	
	public Finder()
	{
		google = "http://www.google.com/search?q=";
		charset = "UTF-8";
		userAgent = "";//"ExampleBot 1.0 (+http://example.com/bot)";
		Comparator<PossibleSolution> comparator = new  WordComperator();
		TS = new TreeSet<PossibleSolution>(comparator);
	}
	
	public Object[] findSolutions(String clue, int length) throws Exception 
	{
		
		String search = clue;
		int answerLength = length;
		Elements links = null;
		
		try {
			links = Jsoup.connect(google + URLEncoder.encode(search, charset)).userAgent(userAgent).get().select(".g>.r>a");
		} catch (Exception e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		
		Elements combinedElements = new Elements();

		for (Element link : links) {
			String title = link.text();
			String url = link.absUrl("href"); // Google returns URLs in format "http://www.google.com/url?q=<url>&sa=U&ei=<someKey>".
			url = URLDecoder.decode(url.substring(url.indexOf('=') + 1, url.indexOf('&')), "UTF-8");

			if (!url.startsWith("http")) {
				continue; // Ads/news/etc.
			}


			System.out.println("URL: " + url);
			if(url.contains("wikipedia"))
				url = url.replace("wikipedia", "0wikipedia");

			try {
				Document document = Jsoup.connect(url).get();
				String html = document.html();
				Document doc = Jsoup.parse(html);
				Elements elements1 = doc.getElementsByTag("a");
				Elements elements2 = doc.getElementsByClass("word");
				Elements elements3 = doc.getElementsByTag("p");

				combinedElements.addAll(elements1);
				combinedElements.addAll(elements2);			
				combinedElements.addAll(elements3);
				
				System.out.println("Title: " + title);
			} catch (Exception e1) {
				
			}
		}
		String pageSummary = "";
		for(Element e : combinedElements)
		{				
			String temp = e.text();
			StringTokenizer sT = new StringTokenizer(temp, " ", false);

			while(sT.hasMoreTokens())
			{
				String text = sT.nextToken();
				text = text.toUpperCase();
				if(text.lastIndexOf('S') == text.length()-1 && text.length() == answerLength + 1)
					text = text.substring(0,text.length()-1);
				if(text.length() == answerLength - 1) {
					text = text + 'S';
				}
				text = text.replaceAll("-", "");
				text = text.replaceAll("\\.", "");
				text = text.replaceAll(",", "");
				text = text.replaceAll("'", "");
				//text.replaceAll("+", "");
				if(text.length() == answerLength && !text.contains("\"")&&!text.contains("+")&&!text.contains("[")&&
				!text.contains("(")&& !text.contains(")")&& !text.contains("?")&& !text.contains("!")&& !text.contains("+")
				&& !text.contains("*")&& !text.contains("'")&& !text.contains("/")&& !text.contains("#")&&!text.contains("]")
				&& text.compareTo("CLUES") != 0 && text.compareTo("CLUE") != 0&& text.compareTo("WORD") != 0&& text.compareTo("WORDS") != 0
				&& text.compareTo("TEXT") != 0&& text.compareTo("FULL") != 0&& text.compareTo("TIPS") != 0&& text.compareTo("FIND") != 0
				&& text.compareTo("2017") != 0&& text.compareTo("THIS") != 0&& text.compareTo("THAT") != 0&& text.compareTo("HOME") != 0
				&& text.compareTo("FROM") != 0&& text.compareTo("NEWS") != 0&& text.compareTo("FREE") != 0&& text.compareTo("BLOG") != 0
				&& text.compareTo("HELP") != 0&& text.compareTo("BACK") != 0&& text.compareTo("WHAT") != 0&& text.compareTo("THEY") != 0
				&& text.compareTo("DOWN") != 0&& text.compareTo("YORK") != 0&& text.compareTo("OTHER") != 0&& text.compareTo("GAMES") != 0)
				{
					pageSummary = pageSummary + "|" + text;
				}
			}
		}		

		StringTokenizer sT = new StringTokenizer(pageSummary, "|", false);

		while(sT.hasMoreTokens())
		{
			String temp = sT.nextToken();
			
			int count = StringUtils.countMatches(pageSummary, temp);
			if(count > 1)
			{
				PossibleSolution t = new PossibleSolution(temp);
				t.setCount( count);
				TS.add(t);
			}
			pageSummary = pageSummary.replaceAll(temp, "");
		}
		Object[] finalList = TS.toArray();
			
		System.out.print("done");
		return finalList;
	}
}

