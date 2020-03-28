package cn.cbsd.dogtag.Data;


import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.util.List;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class DogViolationBean {

    @Id(autoincrement = true)
    private Long id;

    private String personName;

    private String personId;

    private String dogName;

    private String dealStatus;

    private String violation_message;

    private String dogTag;

    private String dealContent;

    private String date;

    @Convert(converter = MyBitmapListConvert .class,columnType =String.class )
    private List<String> bitmaps;

    @Generated(hash = 787558675)
    public DogViolationBean(Long id, String personName, String personId,
            String dogName, String dealStatus, String violation_message,
            String dogTag, String dealContent, String date, List<String> bitmaps) {
        this.id = id;
        this.personName = personName;
        this.personId = personId;
        this.dogName = dogName;
        this.dealStatus = dealStatus;
        this.violation_message = violation_message;
        this.dogTag = dogTag;
        this.dealContent = dealContent;
        this.date = date;
        this.bitmaps = bitmaps;
    }

    @Generated(hash = 1548190387)
    public DogViolationBean() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPersonName() {
        return this.personName;
    }

    public void setPersonName(String personName) {
        this.personName = personName;
    }

    public String getPersonId() {
        return this.personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getDogName() {
        return this.dogName;
    }

    public void setDogName(String dogName) {
        this.dogName = dogName;
    }

    public String getDealStatus() {
        return this.dealStatus;
    }

    public void setDealStatus(String dealStatus) {
        this.dealStatus = dealStatus;
    }

    public String getViolation_message() {
        return this.violation_message;
    }

    public void setViolation_message(String violation_message) {
        this.violation_message = violation_message;
    }

    public String getDogTag() {
        return this.dogTag;
    }

    public void setDogTag(String dogTag) {
        this.dogTag = dogTag;
    }

    public String getDealContent() {
        return this.dealContent;
    }

    public void setDealContent(String dealContent) {
        this.dealContent = dealContent;
    }

    public List<String> getBitmaps() {
        return this.bitmaps;
    }

    public void setBitmaps(List<String> bitmaps) {
        this.bitmaps = bitmaps;
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }


}
