package cn.lanyj.services.models;

import javax.persistence.Entity;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity
@Table(name = "shared_files")
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE, region = "fileCache")
public class SharedFile extends BaseModel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6782817073932882279L;

	private String path;
	private String originalName;
	private int expireDay;
	
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getOriginalName() {
		return originalName;
	}

	public void setOriginalName(String originalName) {
		this.originalName = originalName;
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
}
