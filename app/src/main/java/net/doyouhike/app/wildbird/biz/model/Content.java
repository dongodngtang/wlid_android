package net.doyouhike.app.wildbird.biz.model;

import android.os.Environment;

import net.doyouhike.app.wildbird.BuildConfig;

/**
 * Created by zaitu on 15-11-9.
 */
public class Content {


    //发布测试环境
    public static final String WB_URL = BuildConfig.DEBUG ?
//            "http://dev.wbird.zaitu.cn" :
            "http://wbird.test.zaitu.cn" :
//            "http://bird-api.zaitu.cn" :
            "http://bird-api.zaitu.cn";


    //发布测试环境
    public static final String WEB_MOBILE_URL = BuildConfig.DEBUG ?
            "http://www.test.doyouhike.net" :
            "http://www.doyouhike.net";

    public static final String NET_HEADER_TOKEN = "X-ZAITU-TOKEN";
    public static final String NET_RESP_RET = "ret";
    public static final String NET_RESP_LOG = "log";
    public static final String NET_RESP_CODE = "code";
    public static final String NET_RESP_DATA = "data";


    /**
     * 路径
     */
    public static final String FILE_PATH_PARENT = Environment.getExternalStorageDirectory() + "/doyouhike/wildbird/";
    public static final String FILE_PATH_PARENT_PREVIOUS = Environment.getExternalStorageDirectory() + "/wildbird/";
    public static final String FILE_PATH_PARENT_AVATAR = FILE_PATH_PARENT + "avatar/";

    public static final String FILE_PATH_PARENT_OFFLINE_DATA = FILE_PATH_PARENT + "offline_package.zip";
    public static final String FILE_PATH_PARENT_CACHE = FILE_PATH_PARENT + "cathe/";

    /**
     * SharePreferences
     **/
    public static final String SP_TOKEN = "token";
    public static final String SP_USER_NAME = "userName";
    public static final String SP_USER_ID = "userId";
    public static final String SP_AVATAR = "avatar";
    public static final String SP_NEED_UPDATE = "needUpdate";
    public static final String SP_DOWN_URL = "downUrl";//离线数据包下载地址
    /**
     * 已经复制了本地数据库到数据库文件夹的标志
     * todo 更新本地数据库包时,将后面数字升级,会将原来的数据库删除,并将raw里的wildbird文件替换原来数据库文件
     */
    public static final String SP_COPY_DB_SYMBOL = "hasDatabaseSymbol4";//
    public static final String SP_DATABASE_VERSION = "dataVer";//本地数据版本
    public static final String DATABASE_VERSION_CODE = "5.7";//本地数据版本


    /**
     * SharePreferences
     * #end
     */


    /**
     * 数据库相关
     * #start
     **/
    public static final String DB_NAME_WILDBIRD = "wildbird";
    public static final String DB_NAME_DRAFT = "draft";

    /**
     * 数据库相关
     * #end
     */


    public static String EmptyStr = "";

    /**
     * START ACTIVITY REQUEST CODE
     **/
    public static final int REQ_CODE_LOGIN = 204;

    /**
     * 6
     * 检查数据包更新
     * API接口地址: /bird/checkDataUpdate
     * 请求方式：GET
     */
    public static String REQ_CHECK_DATAUPDATE = "/bird/checkDataUpdate";
    /**
     * 7
     * tvTip.setText("网络不佳，点击刷新。");
     * 查询鸟种详情
     * API接口地址: /bird/getDetails
     * 请求方式: GET
     * GET { speciesID: xxx // 类型: int }
     */
    public static String REQ_GET_DETAILS = "/bird/getDetails";
    /**
     * 8
     * 获取更多评论
     * API接口地址: /bird/getComments
     * 请求方式: GET
     */
    public static String REQ_getComments = "/bird/getComments";
    /**
     * 9
     * 获取注册验证码接口
     * API接口地址：/user/getRegVcode
     * 请求方式：GET
     */
    public static String REQ_getRegVcode = "/user/getRegVcode";
    /**
     * 10
     * 注册接口
     * 为手机号phoneNumber注册
     * API接口地址：/user/reg
     * 请求方式：POST
     */
    public static String REQ_reg = "/user/reg";
    /**
     * 11
     * 登录接口
     * 接口地址：user/login
     * 请求方式：POST
     */
    public static String REQ_login = "/user/login";
    /**
     * 12
     * 获取忘记密码验证码接口
     * API接口地址：/user/getPwdVcode
     * 请求方式：POST
     */
    public static String REQ_getPwdVcode = "/user/getPwdVcode";
    /**
     * 13
     * 忘记密码
     * 接口地址：user/forgetPwd
     * 提交方式：POST
     */
    public static String REQ_forgetPwd = "/user/forgetPwd";
    /**
     * 14
     * 对评论点赞接口
     * API接口地址: /bird/likeComment
     * 提交方式: POST
     */
    public static String REQ_likeComment = "/bird/likeComment";
    /**
     * 15
     * 对鸟种增加评论
     * API地址: /bird/addComment
     * 提交方式: POST
     */
    public static String REQ_addComment = "/bird/addComment";
    /**
     * 16
     * 增加观鸟记录
     * API接口地址: /bird/publishRecord
     * 提交方式: POST
     */
    public static String REQ_publishRecord = "/bird/publishRecord";
    /**
     * 17
     * 新的增加观鸟记录
     * API接口地址: /bird/publishBirdRecord
     * 提交方式: POST
     */
    public static String REQ_publishBirdRecord = "/bird/publishBirdRecord";
    /**
     * 18
     * 新的修改观鸟记录
     * API接口地址: /bird/modifyBirdRecord
     * 提交方式: POST
     */
    public static String REQ_modifyBirdRecord = "/bird/modifyBirdRecord";
    /**
     * 19
     * 修改观鸟记录
     * API接口地址: /bird/modifyRecord
     * 提交方式: POST
     */
    public static String REQ_modifyRecord = "/bird/modifyRecord";
    /**
     * 20
     * 删除观鸟记录
     * API接口地址: /bird/deleteRecord
     * 提交方式: POST
     */
    public static String REQ_deleteRecord = "/bird/deleteRecord";
    /**
     * 21
     * 获取我的记录
     * API接口地址: /bird/getMyRecords
     * 提交方式: GET
     */
    public static String REQ_getMyRecords = "/bird/getMyRecords";
    /**
     * 22
     * 获取记录详情
     * API接口地址: /bird/getRecordDetail
     * 提交方式: GET
     */
    public static String REQ_getRecordDetail = "/bird/getRecordDetail";
    /**
     * 23
     * 野鸟记录统计查询
     * API接口地址: /bird/getRecordStats
     * 提交方式: GET
     */
    public static String REQ_getRecordStats = "/bird/getRecordStats";
    /**
     * 24
     * 意见反馈
     * API接口地址: /bird/feedback
     * 提交方式: POST
     */
    public static String REQ_feedback = "/bird/feedback";
    /**
     * 25
     * 个人记录统计接口
     * API接口地址: /user/getUserRecStats
     * 提交方式: GET
     */
    public static String REQ_getUserRecStats = "/user/getUserRecStats";
    /**
     * 26
     * 更改头像接口
     * API地址: /user/modify_avatar
     */
    public static String REQ_modify_avatar = "/user/modify_avatar";
    /**
     * 27
     * 更改头像接口
     * API地址: /user/upload_avatar
     */
    public static String REQ_upload_avatar = "/user/upload_avatar";
    /**
     * 27
     * 上传图片
     * API地址: /bird/uploadImage
     */
    public static String REQ_uploadImage = "/bird/uploadImage";
    /**
     * 30
     * <p/>
     * 获取鸟种相关新闻信息
     * API接口地址: /news/getList
     * 请求方式：GET species_id
     */
    public static String REQ_NEWS_GET_LIST = "/news/getList";


    /**
     * 31
     * <p/>
     * 根据用户id获取个人信息
     * API接口地址: /user/personInfo
     * 请求方式：GET
     */
    public static String REQ_USER_PERSON_INFO = "/user/personInfo";
    /**
     * 32
     * <p/>
     * 根据用户id获取个人观鸟记录
     * <p/>
     * API接口地址: /record/personRecords
     * <p/>
     * 请求方式：GET
     */
    public static String REQ_RECORD_PERSON_RECORD = "/record/personRecords";
    /**
     * 33
     * <p/>
     * 评论个人观鸟记录
     * <p/>
     * API接口地址: /record/addComment
     * <p/>
     * 请求方式：post
     * <p/>
     * POST {
     * record_id: 'x', // 类型: int, 观鸟记录id
     * content: 'xxx', // 类型: string, 评论的内容
     * }
     */
    public static String REQ_RECORD_ADD_COMMENT = "/record/addComment";
    /**
     * 34
     * 点赞个人观鸟记录
     * API接口地址: /record/addZan
     * 请求方式：post
     * POST {
     * record_id: 'x', // 类型: int, 观鸟记录id
     * }
     */
    public static String REQ_RECORD_ADD_ZAN = "/record/addZan";
    /**
     * 35
     * <p/>
     * 修改个人信息
     * <p/>
     * API接口地址: /user/modifyInfo
     * 请求方式：post
     */
    public static String REQ_RECORD_MODIFY_INFO = "/user/modifyInfo";


    /**
     * 36
     * <p/>
     * 获取个人观鸟记录排行榜(年榜和总榜)
     * API接口地址: /rank/personList
     * <p/>
     * 请求方式：GET
     * GET {
     * skip: 'x', // 类型: int, 从第x条开始取
     * count: 'y', // 类型: int, 共取y条数据
     * type: 'z', // 类型: int, 0年榜 1 总榜
     * }
     */
    public static String REQ_RANK_PERSONLIST = "/rank/personList";
    /**
     * 37
     * <p/>
     * 获取自己的观鸟记录排名（年榜和总榜）
     * <p/>
     * API接口地址: /rank/personRank
     * <p/>
     * 请求方式：GET
     * <p/>
     * GET {
     * user_id: 'z', // 类型: int,用户id
     * type: 'z', // 类型: int, 0年榜 1 总榜
     * }
     */
    public static String REQ_PERSON_RANK = "/rank/personRank";
    /**
     * 38
     * <p/>
     * 获取地区排行榜(年榜和总榜)
     * API接口地址: /rank/areaList
     * <p/>
     * 请求方式：GET
     * GET {
     * skip: 'x', // 类型: int, 从第x条开始取
     * count: 'y', // 类型: int, 共取y条数据
     * type: 'z', // 类型: int, 0年榜 1 总榜
     * }
     */
    public static String REQ_RNK_AREA_LIST = "/rank/areaList";
    /**
     * 39
     * <p/>
     * <p/>
     * 根据省份id获取排名（年榜和总榜）
     * <p/>
     * API接口地址: /rank/areaRank
     * <p/>
     * 请求方式：GET
     * <p/>
     * GET {
     * province_id: 'x', // 类型: int, 省份id
     * type: 'z', // 类型: int, 0年榜 1 总榜
     * }
     */
    public static String REQ_RNK_AREA_RANK = "/rank/areaRank";
    /**
     * 40
     * <p/>
     * <p/>
     * 获取广告列表
     * <p/>
     * API接口地址: /banner/getList
     * 请求方式：GET
     * <p/>
     * GET {
     * }
     */
    public static String REQ_BANNER_GETLIST = "/banner/getList";

    /**
     * 41
     * <p/>
     * <p/>
     * 获取鸟种记录评论列表
     * <p/>
     * API接口地址: /record/commentList
     * <p/>
     * 请求方式：GET
     */
    public static String REQ_GET_COMMENT_LIST = "/record/commentList";
    /**
     * 42
     * <p/>
     * 获取鸟种记录列表
     * API接口地址: /record/getSpeciesRecords
     * 请求方式：GET
     * GET {
     * species_id: 'x', // 类型: int, 鸟种id
     * date: x, // 类型: string,格式“2016-3-15” 取指定日之后十天的数据
     * }
     */
    public static String REQ_RECORD_GET_SPECIES_RECORDS = "/record/getSpeciesRecords";
    /**
     * 43
     * <p/>
     * 获取他人观鸟记录（新版结构）
     * <p/>
     * API接口地址: /record/get_user_species_record
     * 请求方式：GET
     */
    public static String REQ_RECORD_GET_USER_SPECIES_RECORDS = "/record/get_user_species_record";
    /**
     * 44
     * <p/>
     * 获取自己的观鸟记录（新版结构）
     * <p/>
     * API接口地址: /record/get_my_species_record
     * 请求方式：GET
     */
    public static String REQ_RECORD_GET_MY_SPECIES_RECORDS = "/record/get_my_species_record";
    /**
     * 45
     * 检测是否有更新的鸟种
     * <p/>
     * API接口地址: /bird/get_update_bird
     * 请求方式：GET
     * GET {
     * species_id: 'x', // 类型: int, 客户端最新的鸟种id
     * }
     */
    public static String REQ_GET_UPDATE_BIRD = "/bird/get_update_bird";
    /**
     *
     * 46
     *
     * 版本检测
     *
     * API接口地址: /version/check
     * 请求方式：GET
     */
    public static String REQ_VERSION_CHECK = "/version/check";


    /**
     * 发送邮箱修改密码
     * 域名不一致
     * 接口地址：/mobile/user/account/reset_password
     * 提交方式：GET
     */
    public static String REQ_RESET_PW_EMAIL = "/mobile/user/account/reset_password";


}
