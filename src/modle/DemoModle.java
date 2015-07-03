package modle;

import java.io.Serializable;
import java.util.List;


@SuppressWarnings("serial")
public class DemoModle implements Serializable{
	
	private int image;
	
	private String name;
	
	private String url;
	
	private String info;
	
	private String date;
	
	private List childs;
	
	
	private int postion;
	
	

	public DemoModle() {
		super();
	}

	public DemoModle(int image, String name) {
		super();
		this.image = image;
		this.name = name;
	}
	public DemoModle(int image, String name,String url) {
		super();
		this.image = image;
		this.name = name;
		this.url = url;
	}

	public DemoModle(int image, String name, String url, int postion) {
		super();
		this.image = image;
		this.name = name;
		this.url = url;
		this.postion = postion;
	}

	public int getImage() {
		return image;
	}

	public void setImage(int image) {
		this.image = image;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public List getChilds() {
		return childs;
	}

	public void setChilds(List childs) {
		this.childs = childs;
	}

	public int getPostion() {
		return postion;
	}

	public void setPostion(int postion) {
		this.postion = postion;
	}

	

}
