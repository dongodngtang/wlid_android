package net.doyouhike.app.wildbird.biz.model.bean;

/**
 * 版本更新响应
 * Created by zengjiang on 16/6/14.
 */
public class Version {
    private double version;// "5.0",//最新版本号
    private String message;// "IOS 检测到APP新版本，请进入更新.", //更新提示
    private String url;// "http://a.app.qq.com/o/simple.jsp?pkgname=net.doyouhike.app.wildbird",//升级地址
    private int force;// 0  是否强制升级 0 选择升级 1 强制升级


    public double getVersion() {
        return version;
    }

    public void setVersion(double version) {
        this.version = version;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getForce() {
        return force;
    }

    public void setForce(int force) {
        this.force = force;
    }
}
