package net.doyouhike.app.wildbird.biz.dao.base;


import net.doyouhike.app.wildbird.biz.model.base.BaseResponse;

/**
 * Created by zaitu on 15-11-18.
 */
public interface IResponseProcess<T extends BaseResponse>{
    T getResponse(String strJson);
    void setExtraTag(Object tag);
    Object getExtraTag();
}
