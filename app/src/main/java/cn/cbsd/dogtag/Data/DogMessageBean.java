package cn.cbsd.dogtag.Data;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;
import java.util.List;

import org.greenrobot.greendao.annotation.Generated;

@Entity
public class DogMessageBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id(autoincrement = true)
    private Long id;

    private String personName;

    private String personId;

    private String address;

    private String dogName;

    private String dogType;

    private String dogTagNum;

    private String dogTagQRCode;

    private String startDate;

    private String stopDate;

    @Convert(converter = MyBitmapListConvert.class, columnType = String.class)
    private List<String> bitmaps;

    @Generated(hash = 743125490)
    public DogMessageBean(Long id, String personName, String personId,
            String address, String dogName, String dogType, String dogTagNum,
            String dogTagQRCode, String startDate, String stopDate,
            List<String> bitmaps) {
        this.id = id;
        this.personName = personName;
        this.personId = personId;
        this.address = address;
        this.dogName = dogName;
        this.dogType = dogType;
        this.dogTagNum = dogTagNum;
        this.dogTagQRCode = dogTagQRCode;
        this.startDate = startDate;
        this.stopDate = stopDate;
        this.bitmaps = bitmaps;
    }

    @Generated(hash = 1899971196)
    public DogMessageBean() {
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

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDogName() {
        return this.dogName;
    }

    public void setDogName(String dogName) {
        this.dogName = dogName;
    }

    public String getDogType() {
        return this.dogType;
    }

    public void setDogType(String dogType) {
        this.dogType = dogType;
    }

    public String getDogTagNum() {
        return this.dogTagNum;
    }

    public void setDogTagNum(String dogTagNum) {
        this.dogTagNum = dogTagNum;
    }

    public String getDogTagQRCode() {
        return this.dogTagQRCode;
    }

    public void setDogTagQRCode(String dogTagQRCode) {
        this.dogTagQRCode = dogTagQRCode;
    }

    public String getStartDate() {
        return this.startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getStopDate() {
        return this.stopDate;
    }

    public void setStopDate(String stopDate) {
        this.stopDate = stopDate;
    }

    public List<String> getBitmaps() {
        return this.bitmaps;
    }

    public void setBitmaps(List<String> bitmaps) {
        this.bitmaps = bitmaps;
    }

 

}
