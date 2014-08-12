package com.asad.thestore;

public class Models {
	private String name;
    private String image;
	private String uuid;
	private String tags;
	private String version;
	
	public Models(){
		
	}
	public Models(String name, String image, String uuid, String tags, String version){
		super();
		this.name = name;
		this.image = image;
		this.uuid = uuid;
		this.tags = tags;
		this.version = version;
		
	}
	
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	
	public String getTags() {
		return tags;
	}
	public void setTags(String tags) {
		this.tags = tags;
	}
	
	
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	
	

}
