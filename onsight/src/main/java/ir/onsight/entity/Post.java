package ir.onsight.entity;

import java.util.Date;


public class Post {
	public enum PostStatus{
		UNCONFIRMED,
		CONFIRMED,
		REJECTED,
		POSTED,
		DELETED;
	}
	
	public enum MediaType{
		VIDEO,
		IMAGE;
	}
	
	private int id ;
	private User creator;
	private User confirmer;
	private Date createdTime;
	private Date releaseTime;
	private PostStatus status;
	private boolean isEdited;
	private String editNote;
	private MediaType mediaType;
	private transient String mediaPath;
	//pure info of post
	private Account account;
	private String projectNameFa;
	private String projectNameEn;
	private String code;
	private String programFa;
	private String programEn;
	private String locationFa;
	private String locationEn;
	private String architectFa;
	private String architectEn;
	private int year;
	private int size;
	private String projectStatusFa;
	private String projectStatusEn;
	private String descriptionFa;
	private String descriptionEn;
	private String keywordsFa;		//comma separated 
	private String keywordsEn;
	
	public Post(User creator, MediaType mediaType, String mediaPath,
			Account account, String projectNameFa, String projectNameEn,
			String code, String programFa, String programEn, String locationFa,
			String locationEn, String architectFa, String architectEn,
			int year, int size, String projectStatusFa, String projectStatusEn,
			String descriptionFa, String descriptionEn, String keywordsFa,
			String keywordsEn) {
		this.creator = creator;
		this.createdTime =new Date();
		this.status = PostStatus.UNCONFIRMED;
		this.isEdited = false;
		this.mediaType = mediaType;
		this.mediaPath = mediaPath;
		this.account = account;
		this.projectNameFa = projectNameFa;
		this.projectNameEn = projectNameEn;
		this.code = code;
		this.programFa = programFa;
		this.programEn = programEn;
		this.locationFa = locationFa;
		this.locationEn = locationEn;
		this.architectFa = architectFa;
		this.architectEn = architectEn;
		this.year = year;
		this.size = size;
		this.projectStatusFa = projectStatusFa;
		this.projectStatusEn = projectStatusEn;
		this.descriptionFa = descriptionFa;
		this.descriptionEn = descriptionEn;
		this.keywordsFa = keywordsFa;
		this.keywordsEn = keywordsEn;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public User getCreator() {
		return creator;
	}

	public void setCreator(User creator) {
		this.creator = creator;
	}

	public User getConfirmer() {
		return confirmer;
	}

	public void setConfirmer(User confirmer) {
		this.confirmer = confirmer;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Date getReleaseTime() {
		return releaseTime;
	}

	public void setReleaseTime(Date releaseTime) {
		this.releaseTime = releaseTime;
	}

	public PostStatus getStatus() {
		return status;
	}

	public void setStatus(PostStatus status) {
		this.status = status;
	}

	public boolean isEdited() {
		return isEdited;
	}

	public void setEdited(boolean isEdited) {
		this.isEdited = isEdited;
	}

	public String getEditNote() {
		return editNote;
	}

	public void setEditNote(String editNote) {
		this.editNote = editNote;
	}

	public MediaType getMediaType() {
		return mediaType;
	}

	public void setMediaType(MediaType mediaType) {
		this.mediaType = mediaType;
	}

	public String getMediaPath() {
		return mediaPath;
	}

	public void setMediaPath(String mediaPath) {
		this.mediaPath = mediaPath;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public String getProjectNameFa() {
		return projectNameFa;
	}

	public void setProjectNameFa(String projectNameFa) {
		this.projectNameFa = projectNameFa;
	}

	public String getProjectNameEn() {
		return projectNameEn;
	}

	public void setProjectNameEn(String projectNameEn) {
		this.projectNameEn = projectNameEn;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getProgramFa() {
		return programFa;
	}

	public void setProgramFa(String programFa) {
		this.programFa = programFa;
	}

	public String getProgramEn() {
		return programEn;
	}

	public void setProgramEn(String programEn) {
		this.programEn = programEn;
	}

	public String getLocationFa() {
		return locationFa;
	}

	public void setLocationFa(String locationFa) {
		this.locationFa = locationFa;
	}

	public String getLocationEn() {
		return locationEn;
	}

	public void setLocationEn(String locationEn) {
		this.locationEn = locationEn;
	}

	public String getArchitectFa() {
		return architectFa;
	}

	public void setArchitectFa(String architectFa) {
		this.architectFa = architectFa;
	}

	public String getArchitectEn() {
		return architectEn;
	}

	public void setArchitectEn(String architectEn) {
		this.architectEn = architectEn;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public String getProjectStatusFa() {
		return projectStatusFa;
	}

	public void setProjectStatusFa(String projectStatusFa) {
		this.projectStatusFa = projectStatusFa;
	}

	public String getProjectStatusEn() {
		return projectStatusEn;
	}

	public void setProjectStatusEn(String projectStatusEn) {
		this.projectStatusEn = projectStatusEn;
	}

	public String getDescriptionFa() {
		return descriptionFa;
	}

	public void setDescriptionFa(String descriptionFa) {
		this.descriptionFa = descriptionFa;
	}

	public String getDescriptionEn() {
		return descriptionEn;
	}

	public void setDescriptionEn(String descriptionEn) {
		this.descriptionEn = descriptionEn;
	}

	public String getKeywordsFa() {
		return keywordsFa;
	}

	public void setKeywordsFa(String keywordsFa) {
		this.keywordsFa = keywordsFa;
	}

	public String getKeywordsEn() {
		return keywordsEn;
	}

	public void setKeywordsEn(String keywordsEn) {
		this.keywordsEn = keywordsEn;
	}	
	
}
