package net.doyouhike.app.wildbird.biz.model.bean;

import net.doyouhike.app.wildbird.biz.db.bean.DbWildBird;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SpeciesInfo implements Serializable {

    private static final long serialVersionUID = 1L;


    protected String speciesID = "0";
    protected String localName = ""; // 类型: string, 中文名，格式: "名字 拼音"
    protected String engName = ""; // 类型: string, 英语名
    protected String latinName = "";// 类型: string, 拉丁语名
    protected String ordo = "";//  类型: string,所属 目
    protected String familia = ""; // 类型: string, 所属 科
    protected String genus = "";// 类型: string, 所属 属


    private String share_url;// 类型: string, 分享地址
    private String audio_file;// 类型: string, 鸟种录音文件地址
    private String distribution_map; // 类型: string, 分布区域图地址
    private String b2w; // 类型: string, birds to Watch名录-- V=易危，E= 濒危，C=极危，DD=资料缺乏，NT=接近受危',
    private String cites; // 类型: string, 数字所指为附录等级,如CITES10
    private String rdb; // 类型: string, 中国濒危物种红皮书-- R=稀有，E=濒危，V=易危，I=不确定，X=国内灭绝',
    private String prot; // 类型: string, 列入中国重点保护名录-- P=列入二级保护，I=列入一级保护'
    private String description; // 类型: string, 鸟种描述
    private String color; // 类型: string, 颜色
    private String cry; // 类型: string, 叫声
    private String dist_range; // 类型: string, 分布范围
    private String dist_candition; // 类型: string, 分布状况
    private String species_record_count; // 类型: string, 审核通过的观鸟记录总数
    private String has_news = "0"; // 类型: string, 是否有新闻 0：无 1：有


    protected String shape = "";
    protected String colorList = "";
    protected String locateList = "";
    protected String behaviorList = "";
    protected String headList = "";
    protected String neckList = "";
    protected String bellyList = "";
    protected String waistList = "";
    protected String wingList = "";
    protected String tailList = "";
    protected String legList = "";
    protected String image = "";
    protected String author = "";
    protected String newlocate ="";//出现在
    private List<Image> images = new ArrayList<Image>();
    private List<Comment> comments = new ArrayList<Comment>();

    public SpeciesInfo() {
    }

    public SpeciesInfo(DbWildBird wildbird) {
        initData(wildbird);
    }

    public void addImage(Image image) {
        images.add(image);
    }

    public List<Image> getImages() {
        return images;
    }

    public void addComment(Comment comment) {
        comments.add(comment);
    }

    public List<Comment> getComments() {
        return comments;
    }

    public String getColorList() {
        return colorList;
    }

    public void setColorList(String colorList) {
        this.colorList = colorList;
    }

    public String getLocateList() {
        return locateList;
    }

    public void setLocateList(String locateList) {
        this.locateList = locateList;
    }

    public String getBehaviorList() {
        return behaviorList;
    }

    public void setBehaviorList(String behaviorList) {
        this.behaviorList = behaviorList;
    }

    public String getHeadList() {
        return headList;
    }

    public void setHeadList(String headList) {
        this.headList = headList;
    }

    public String getNeckList() {
        return neckList;
    }

    public void setNeckList(String neckList) {
        this.neckList = neckList;
    }

    public String getBellyList() {
        return bellyList;
    }

    public void setBellyList(String bellyList) {
        this.bellyList = bellyList;
    }

    public String getWaistList() {
        return waistList;
    }

    public void setWaistList(String waistList) {
        this.waistList = waistList;
    }

    public String getWingList() {
        return wingList;
    }

    public void setWingList(String wingList) {
        this.wingList = wingList;
    }

    public String getTailList() {
        return tailList;
    }

    public void setTailList(String tailList) {
        this.tailList = tailList;
    }

    public String getLegList() {
        return legList;
    }

    public void setLegList(String legList) {
        this.legList = legList;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public String getSpeciesID() {
        return speciesID;
    }

    public void setSpeciesID(String speciesID) {
        this.speciesID = speciesID;
    }

    public String getLocalName() {
        return localName;
    }

    public void setLocalName(String localName) {
        this.localName = localName;
    }

    public String getEngName() {
        return engName;
    }

    public void setEngName(String engName) {
        this.engName = engName;
    }

    public String getLatinName() {
        return latinName;
    }

    public void setLatinName(String latinName) {
        this.latinName = latinName;
    }

    public String getOrdo() {
        return ordo;
    }

    public void setOrdo(String ordo) {
        this.ordo = ordo;
    }

    public String getFamilia() {
        return familia;
    }

    public void setFamilia(String familia) {
        this.familia = familia;
    }

    public String getGenus() {
        return genus;
    }

    public void setGenus(String genus) {
        this.genus = genus;
    }

    public String getShape() {
        return shape;
    }

    public void setShape(String shape) {
        this.shape = shape;
    }


    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getShare_url() {
        return share_url;
    }

    public void setShare_url(String share_url) {
        this.share_url = share_url;
    }

    public String getAudio_file() {
        return audio_file;
    }

    public void setAudio_file(String audio_file) {
        this.audio_file = audio_file;
    }

    public String getDistribution_map() {
        return distribution_map;
    }

    public void setDistribution_map(String distribution_map) {
        this.distribution_map = distribution_map;
    }

    public String getB2w() {
        return b2w;
    }

    public void setB2w(String b2w) {
        this.b2w = b2w;
    }

    public String getCites() {
        return cites;
    }

    public void setCites(String cites) {
        this.cites = cites;
    }

    public String getRdb() {
        return rdb;
    }

    public void setRdb(String rdb) {
        this.rdb = rdb;
    }

    public String getProt() {
        return prot;
    }

    public void setProt(String prot) {
        this.prot = prot;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getCry() {
        return cry;
    }

    public void setCry(String cry) {
        this.cry = cry;
    }

    public String getDist_range() {
        return dist_range;
    }

    public void setDist_range(String dist_range) {
        this.dist_range = dist_range;
    }

    public String getDist_candition() {
        return dist_candition;
    }

    public void setDist_candition(String dist_candition) {
        this.dist_candition = dist_candition;
    }

    public String getSpecies_record_count() {
        return species_record_count;
    }

    public void setSpecies_record_count(String species_record_count) {
        this.species_record_count = species_record_count;
    }

    public String getNewlocate() {
        return newlocate;
    }

    public void setNewlocate(String newlocate) {
        this.newlocate = newlocate;
    }

    public String getHas_news() {
        return has_news;
    }

    public void setHas_news(String has_news) {
        this.has_news = has_news;
    }

    public static SpeciesInfo JsonToSpeciesInfo(JSONObject jsonObject) {
        SpeciesInfo speciesInfo = new SpeciesInfo();
        try {
            speciesInfo.setSpeciesID(jsonObject.getString("speciesID"));
            speciesInfo.setLocalName(jsonObject.getString("localName"));
            speciesInfo.setEngName(jsonObject.getString("engName"));
            speciesInfo.setLatinName(jsonObject.getString("ordo"));
            speciesInfo.setFamilia(jsonObject.getString("familia"));
            speciesInfo.setGenus(jsonObject.getString("genus"));
            // 还有图片和评论
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return speciesInfo;
    }


    private void initData(DbWildBird wildbird) {

        this.speciesID = wildbird.getSpeciesID();
        this.localName = wildbird.getLocalName();
        this.engName = wildbird.getEngName();
        this.latinName = wildbird.getLatinName();
        this.ordo = wildbird.getOrdo();
        this.familia = wildbird.getFamilia();
        this.genus = wildbird.getGenus();
        this.shape = wildbird.getShape();
        this.colorList = wildbird.getColor();
        this.locateList = wildbird.getLocateList();
        this.behaviorList = wildbird.getBehaviorList();
        this.headList = wildbird.getHeadList();
        this.neckList = wildbird.getNeckList();
        this.bellyList = wildbird.getBellyList();
        this.waistList = wildbird.getWaistList();
        this.wingList = wildbird.getWingList();
        this.tailList = wildbird.getTailList();
        this.legList = wildbird.getLegList();
        this.image = wildbird.getImage();
        this.author = wildbird.getAuthor();
        this.getImages().clear();

        for (Image image : wildbird.getDbImages()) {
            this.addImage(image);
        }
        this.getComments().clear();
        for (Comment comment : wildbird.getDbComments()) {
            this.addComment(comment);
        }

    }


}
