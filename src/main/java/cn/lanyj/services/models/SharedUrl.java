package cn.lanyj.services.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "shared_urls")
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "urlCache")
public class SharedUrl extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2429571822969861661L;
	
	@Column(name="_url", nullable=false, length=1024)
	private String url;
	
	@Column(name="_description", length=1024)
	private String description;
	
	private int expireDay;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	

	public int getExpireDay() {
		return expireDay;
	}

	public void setExpireDay(int expireDay) {
		if(expireDay < 1) {
			expireDay = 1;
		}
		if(expireDay >= 365) {
			expireDay = 365;
		}
		this.expireDay = expireDay;
	}

	@PrePersist
    public void prePersist() {
		super.prePersist();
		setExpireDay(expireDay);
    }

    @PreUpdate
    public void preUpdate() {
    	super.preUpdate();
		setExpireDay(expireDay);
    }

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
