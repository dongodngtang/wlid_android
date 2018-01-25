package net.doyouhike.app.wildbird.biz.dao.net;

/**
 * 功能：
 *
 * @author：曾江 日期：16-4-15.
 */
public enum ErrState {
    NO_CONNECT,
    SERVICE_ERR,
    DATA_ERR,//数据出错,可能是token 错误,可能是传参错误,具体结合错误代码
    TIMEOUT,
    PARSE_ERR,//解析出错
    OTHER_ERR//
}
