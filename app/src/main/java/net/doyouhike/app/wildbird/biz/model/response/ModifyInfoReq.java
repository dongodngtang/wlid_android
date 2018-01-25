package net.doyouhike.app.wildbird.biz.model.response;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.gson.annotations.Expose;

import net.doyouhike.app.wildbird.biz.model.base.BasePostRequest;

/**
 * 修改个人信息
 * Created by zengjiang on 16/6/13.
 */
public class ModifyInfoReq extends BasePostRequest{
    @Expose
    private int city_id; // 类型: int, 用户所在的城市id
    @Expose
    private String insitution; // 类型: string, 所属机构
    @Expose
    private String camera; // 类型: string, 相机 多个用英文逗号隔开
    @Expose
    private String telescope; // 类型: string, 望远镜 多个用英文逗号隔开


    public int getCity_id() {
        return city_id;
    }

    public void setCity_id(int city_id) {
        this.city_id = city_id;
    }

    public String getInsitution() {
        return insitution;
    }

    public void setInsitution(String insitution) {
        this.insitution = insitution;
    }

    public String getCamera() {
        return camera;
    }

    public String[] getArrayCamera() {

        if (TextUtils.isEmpty(camera)) {
            return new String[]{};
        }

        return camera.split(",");

    }

    public void setCamera(String[] cameras) {
        this.camera = getArrayToString(cameras);
    }

    public void setCamera(String camera) {
        this.camera = camera;
    }


    public String getTelescope() {
        return telescope;
    }

    public String[] getArrayTelescope() {
        if (TextUtils.isEmpty(telescope)) {
            return new String[]{};
        }

        return telescope.split(",");
    }

    public void setTelescope(String[] telescopes) {
        this.telescope = getArrayToString(telescopes);
    }

    public void setTelescope(String telescope) {
        this.telescope = telescope;
    }


    @NonNull
    private String getArrayToString(String[] cameras) {
        String strCamera = "";

        if (null != cameras) {

            StringBuilder stringBuilder = new StringBuilder("");
            for (String camera : cameras) {
                stringBuilder.append(camera).append(',');
            }

            strCamera = stringBuilder.toString();

        }


        if (!TextUtils.isEmpty(strCamera)) {
            strCamera = strCamera.substring(0, strCamera.length() - 1);
        }
        return strCamera;
    }
}
