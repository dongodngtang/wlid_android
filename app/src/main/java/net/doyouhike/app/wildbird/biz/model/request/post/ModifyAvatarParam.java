package net.doyouhike.app.wildbird.biz.model.request.post;

import com.google.gson.annotations.Expose;

import net.doyouhike.app.wildbird.biz.model.base.BasePostRequest;

/**
 * Created by zaitu on 15-12-4.
 */
public class ModifyAvatarParam extends BasePostRequest {

    /**
     * name : xxx.jpg
     * content :  // name 头像图片的文件名, content 头像图片的内容的 base64 编码字符串
     */

    @Expose
    private AvatarEntity avatar;

    public void setAvatar(AvatarEntity avatar) {
        this.avatar = avatar;
    }

    public AvatarEntity getAvatar() {
        return avatar;
    }

    public static class AvatarEntity {

        @Expose
        private String name;
        @Expose
        private String content;

        public void setName(String name) {
            this.name = name;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getName() {
            return name;
        }

        public String getContent() {
            return content;
        }
    }
}
