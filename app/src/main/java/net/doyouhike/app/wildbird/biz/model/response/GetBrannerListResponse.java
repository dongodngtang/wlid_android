package net.doyouhike.app.wildbird.biz.model.response;

import net.doyouhike.app.wildbird.biz.model.bean.Banner;

import java.util.List;

/**
 * 功能：广告列表
 *
 * @author：曾江 日期：16-4-18.
 */
public class GetBrannerListResponse {
   private List<Banner> banners;

    public List<Banner> getBanners() {
        return banners;
    }

    public void setBanners(List<Banner> banners) {
        this.banners = banners;
    }
}
