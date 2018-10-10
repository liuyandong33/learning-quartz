package build.dream.job.domains;

import build.dream.common.constants.Constants;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigInteger;
import java.util.Date;

@Table
@Entity
public class Branch {
    @Id
    @GeneratedValue
    private BigInteger id;
    private Date createTime;
    private BigInteger createUserId;
    private Date lastUpdateTime;
    private BigInteger lastUpdateUserId;
    private String lastUpdateRemark = Constants.VARCHAR_DEFAULT_VALUE;
    private Date deleteTime;
    private boolean deleted;
    /**
     * 商户id
     */
    private BigInteger tenantId;
    /**
     * 商户编码
     */
    private String tenantCode;
    /**
     * 门店编码
     */
    private String code;
    /**
     * 门店名称
     */
    private String name;
    /**
     * 门店类型，1-总部，2-直营店，3加盟店
     */
    private Integer type;
    /**
     * 门店状态，1-正常
     */
    private Integer status;
    /**
     * 省编码
     */
    private String provinceCode;
    /**
     * 省名称
     */
    private String provinceName;
    /**
     * 市编码
     */
    private String cityCode;
    /**
     * 市名称
     */
    private String cityName;
    /**
     * 区编码
     */
    private String districtCode;
    /**
     * 区名称
     */
    private String districtName;
    /**
     * 门店详细地址
     */
    private String address;
    /**
     * 经度
     */
    private String longitude;
    /**
     * 纬度
     */
    private String latitude;
    /**
     * 联系人
     */
    private String linkman;
    /**
     * 联系电话
     */
    private String contactPhone;
    /**
     * 饿了么账号类型，1-连锁账号，2-独立账号
     */
    private Integer elemeAccountType = Constants.ELEME_ACCOUNT_TYPE_CHAIN_ACCOUNT;
    /**
     * 饿了么门店id
     */
    private BigInteger shopId = Constants.BIGINT_DEFAULT_VALUE;
    /**
     * 微餐厅状态，1-正常，2-禁用
     */
    private Integer smartRestaurantStatus = Constants.SMART_RESTAURANT_STATUS_DISABLED;
    /**
     * 美团门店绑定的授权token
     */
    private String appAuthToken = Constants.VARCHAR_DEFAULT_VALUE;
    /**
     * 美团门店id
     */
    private String poiId = Constants.VARCHAR_DEFAULT_VALUE;
    /**
     * 美团门店名称
     */
    private String poiName = Constants.VARCHAR_DEFAULT_VALUE;

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public BigInteger getCreateUserId() {
        return createUserId;
    }

    public void setCreateUserId(BigInteger createUserId) {
        this.createUserId = createUserId;
    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public BigInteger getLastUpdateUserId() {
        return lastUpdateUserId;
    }

    public void setLastUpdateUserId(BigInteger lastUpdateUserId) {
        this.lastUpdateUserId = lastUpdateUserId;
    }

    public String getLastUpdateRemark() {
        return lastUpdateRemark;
    }

    public void setLastUpdateRemark(String lastUpdateRemark) {
        this.lastUpdateRemark = lastUpdateRemark;
    }

    public Date getDeleteTime() {
        return deleteTime;
    }

    public void setDeleteTime(Date deleteTime) {
        this.deleteTime = deleteTime;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }

    public BigInteger getTenantId() {
        return tenantId;
    }

    public void setTenantId(BigInteger tenantId) {
        this.tenantId = tenantId;
    }

    public String getTenantCode() {
        return tenantCode;
    }

    public void setTenantCode(String tenantCode) {
        this.tenantCode = tenantCode;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getProvinceCode() {
        return provinceCode;
    }

    public void setProvinceCode(String provinceCode) {
        this.provinceCode = provinceCode;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getDistrictCode() {
        return districtCode;
    }

    public void setDistrictCode(String districtCode) {
        this.districtCode = districtCode;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLinkman() {
        return linkman;
    }

    public void setLinkman(String linkman) {
        this.linkman = linkman;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public Integer getElemeAccountType() {
        return elemeAccountType;
    }

    public void setElemeAccountType(Integer elemeAccountType) {
        this.elemeAccountType = elemeAccountType;
    }

    public BigInteger getShopId() {
        return shopId;
    }

    public void setShopId(BigInteger shopId) {
        this.shopId = shopId;
    }

    public Integer getSmartRestaurantStatus() {
        return smartRestaurantStatus;
    }

    public void setSmartRestaurantStatus(Integer smartRestaurantStatus) {
        this.smartRestaurantStatus = smartRestaurantStatus;
    }

    public String getAppAuthToken() {
        return appAuthToken;
    }

    public void setAppAuthToken(String appAuthToken) {
        this.appAuthToken = appAuthToken;
    }

    public String getPoiId() {
        return poiId;
    }

    public void setPoiId(String poiId) {
        this.poiId = poiId;
    }

    public String getPoiName() {
        return poiName;
    }

    public void setPoiName(String poiName) {
        this.poiName = poiName;
    }
}
