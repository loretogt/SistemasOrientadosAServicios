package datos;


import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import javax.xml.bind.annotation.XmlAttribute;

public class Url {

	private URL url;
	private String rel;
	
	@XmlAttribute(name="href")
	public URL getUrl() {
		return url;
	}
	public void setUrl(URL url) {
		this.url = url;
	}
	
	@XmlAttribute
	public String getRel() {
		return rel;
	}
	public void setRel(String rel) {
		this.rel = rel;
	}
	public Url(String url, String rel) {
		try {
			this.url = new URL(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		this.rel = rel;
	}
	public Url(String uri) {
		try {
			this.url = new URL(uri);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}
	
	public Url() {

	}
}
