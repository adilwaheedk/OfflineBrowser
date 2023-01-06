package com.khanstech.offlinebrowser.others;

import java.util.ArrayList;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import android.util.Log;
import com.khanstech.offlinebrowser.database.Link;

public class HtmlParser {

	public static List<Link> getLinksFromURL(String title, String url) {
		try {
			Document doc = Jsoup.connect(url)
					.userAgent(System.getProperty("http.agent")).get();
			List<Link> list = new ArrayList<Link>();
			Elements links = doc.select("a[href]");
			for (int i = 0; i < links.size(); i++) {
				String correctlink = links.get(i).attr("href").trim()
						.toLowerCase();
				if (correctlink.startsWith("mailto:")
						|| correctlink.startsWith("#")
						|| correctlink.contains("javascript:;"))
					continue;
				else {
					if (correctlink.startsWith("/")) {
						correctlink = correctlink.substring(1);
						correctlink = url + correctlink;
					} else if (correctlink.startsWith("?")
							|| correctlink.endsWith(".htm")
							|| correctlink.endsWith(".html"))
						correctlink = url + correctlink;
					list.add(new Link(title, correctlink));
				}
			}
			return list;
		} catch (Exception e) {
			Log.e("error", "HtmlParser " + e.getMessage());
		}
		return null;
	}

	public static void getAllImagesFromURL(String url) {
		try {
			Document doc = Jsoup.connect(url).get();
			Elements images = doc.select("img[src~=(?i)\\.(png|jpe?g|gif)]");
			for (Element image : images) {
				System.out.println("\nsrc : " + image.attr("src"));
				System.out.println("height : " + image.attr("height"));
				System.out.println("width : " + image.attr("width"));
				System.out.println("alt : " + image.attr("alt"));
			}
		} catch (Exception e) {
			Log.e("error", "HtmlParser " + e.getMessage());
		}
	}

	public static String getFavIcon(String url) {
		Document doc = Jsoup.parse(url);
		String fav = "";
		Element element = doc.head().select("link[href~=.*\\.(ico|png)]")
				.first();
		if (element == null) {
			element = doc.head().select("meta[itemprop=image]").first();
			if (element != null) {
				fav = element.attr("content");
			}
		} else {
			fav = element.attr("href");
		}
		return fav;
	}
}
