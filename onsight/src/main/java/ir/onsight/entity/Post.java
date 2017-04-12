package ir.onsight.entity;

import java.nio.file.Paths;
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
		IMAGE,
		OTHER;
	}

	private Integer id ;
	private User creator;
	private User confirmer;
	private Date createdTime;
	private Date releaseTime;
	private PostStatus status;
	private Boolean isEdited;
	private String editNote;
	private MediaType mediaType;
	private transient String mediaRelativePath;
	private String mediaWebUrl;
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
	private Integer year;
	private Integer size;
	private String projectStatusFa;
	private String projectStatusEn;
	private String descriptionFa;
	private String descriptionEn;
	private String keywordsFa;		//comma separated
	private String keywordsEn;

	public Post(Integer id, User creator, User confirmer, Date createdTime,
			Date releaseTime, PostStatus status, Boolean isEdited,
			String editNote, MediaType mediaType, String mediaRelativePath,
			String mediaWebUrl, Account account, String projectNameFa,
			String projectNameEn, String code, String programFa,
			String programEn, String locationFa, String locationEn,
			String architectFa, String architectEn, Integer year, Integer size,
			String projectStatusFa, String projectStatusEn,
			String descriptionFa, String descriptionEn, String keywordsFa,
			String keywordsEn)
	{
		this.id = id;
		this.creator = creator;
		this.confirmer = confirmer;
		this.createdTime = createdTime;
		this.releaseTime = releaseTime;
		this.status = status;
		this.isEdited = isEdited;
		this.editNote = editNote;
		this.mediaType = mediaType;
		this.mediaRelativePath = mediaRelativePath;
		this.mediaWebUrl = mediaWebUrl;
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

	public Post(User creator, MediaType mediaType, String mediaRelativePath,
			Account account, String projectNameFa, String projectNameEn,
			String code, String programFa, String programEn, String locationFa,
			String locationEn, String architectFa, String architectEn,
			Integer year, Integer size, String projectStatusFa, String projectStatusEn,
			String descriptionFa, String descriptionEn, String keywordsFa,
			String keywordsEn) {
		this(null, creator, null, new Date(), null, PostStatus.UNCONFIRMED, false, null,
			mediaType, mediaRelativePath, null, account, projectNameFa, projectNameEn,
			code, programFa, programEn, locationFa, locationEn, architectFa, architectEn,
			year, size, projectStatusFa, projectStatusEn, descriptionFa, descriptionEn,
			keywordsFa, keywordsEn);
	}

	public void updateFields(User creator, User confirmer, Date createdTime,
			Date releaseTime, PostStatus status, Boolean isEdited,
			String editNote, MediaType mediaType, String mediaRelativePath,
			String mediaWebUrl, Account account, String projectNameFa,
			String projectNameEn, String code, String programFa,
			String programEn, String locationFa, String locationEn,
			String architectFa, String architectEn, Integer year, Integer size,
			String projectStatusFa, String projectStatusEn,
			String descriptionFa, String descriptionEn, String keywordsFa,
			String keywordsEn)
	{
		this.creator = creator != null ? creator : this.creator;
		this.confirmer = confirmer != null ? confirmer : this.confirmer;
		this.createdTime = createdTime != null ? createdTime : this.createdTime;
		this.releaseTime = releaseTime != null ? releaseTime : this.releaseTime;
		this.status = status != null ? status : this.status;
		this.isEdited = isEdited != null ? isEdited : this.isEdited;
		this.editNote = editNote != null ? editNote : this.editNote;
		this.mediaType = mediaType != null ? mediaType : this.mediaType;
		this.mediaRelativePath = mediaRelativePath != null ? mediaRelativePath : this.mediaRelativePath;
		this.mediaWebUrl = mediaWebUrl != null ? mediaWebUrl : this.mediaWebUrl;
		this.account = account != null ? account : this.account;
		this.projectNameFa = projectNameFa != null ? projectNameFa : this.projectNameFa;
		this.projectNameEn = projectNameEn != null ? projectNameEn : this.projectNameEn;
		this.code = code != null ? code : this.code;
		this.programFa = programFa != null ? programFa : this.programFa;
		this.programEn = programEn != null ? programEn : this.programEn;
		this.locationFa = locationFa != null ? locationFa : this.locationFa;
		this.locationEn = locationEn != null ? locationEn : this.locationEn;
		this.architectFa = architectFa != null ? architectFa : this.architectFa;
		this.architectEn = architectEn != null ? architectEn : this.architectEn;
		this.year = year != null ? year : this.year;
		this.size = size != null ? size : this.size;
		this.projectStatusFa = projectStatusFa != null ? projectStatusFa : this.projectStatusFa;
		this.projectStatusEn = projectStatusEn != null ? projectStatusEn : this.projectStatusEn;
		this.descriptionFa = descriptionFa != null ? descriptionFa : this.descriptionFa;
		this.descriptionEn = descriptionEn != null ? descriptionEn : this.descriptionEn;
		this.keywordsFa = keywordsFa != null ? keywordsFa : this.keywordsFa;
		this.keywordsEn = keywordsEn != null ? keywordsEn : this.keywordsEn;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
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

	public Boolean isEdited() {
		return isEdited;
	}

	public void setEdited(Boolean isEdited) {
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

	public String getMediaRelativePath() {
		return mediaRelativePath;
	}

	public void setMediaRelativePath(String mediaRelativePath) {
		this.mediaRelativePath = mediaRelativePath;
	}

	public void setMediaRelativePathFromFileName(String mediaFileName) {
		if(this.id != null)
			this.mediaRelativePath = Paths.get(this.account.getUsername(),Integer.toString(this.id) + "_" + mediaFileName).toString();
	}

	public String getMediaWebUrl() {
		return mediaWebUrl;
	}

	public void setMediaWebUrl(String mediaWebUrl) {
		this.mediaWebUrl = mediaWebUrl;
	}

	public void setMediaWebUrlFromBase(String mediaBaseUrl) {
		this.mediaWebUrl = Paths.get(mediaBaseUrl,this.mediaRelativePath).toString();
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

	public Integer getYear() {
		return year;
	}

	public void setYear(Integer year) {
		this.year = year;
	}

	public Integer getSize() {
		return size;
	}

	public void setSize(Integer size) {
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
