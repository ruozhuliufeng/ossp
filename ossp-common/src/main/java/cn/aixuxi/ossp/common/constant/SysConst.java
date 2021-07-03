package cn.aixuxi.ossp.common.constant;

/**
 * 功能描述: 系统常量定义
 * @author : ruozhuliufeng
 * @date : 2021/6/23 20:49
 */
public interface SysConst {
    /**
     * 验证码
     */
    public final static String CAPTCHA_KEY = "captcha";
    /**
     * 默认头像
     */
    public final static String DEFULT_AVATAR = "https://image-1300566513.cos.ap-guangzhou.myqcloud.com/upload/images/5a9f48118166308daba8b6da7e466aab.jpg";
    /**
     * 默认密码
     */
    public final static String DEFAULT_PASSWORD = "123456";
    /**
     * 正常状态
     */
    public static final Integer STATUS_NORMAL = 0;

    /**
     * 禁用状态
     */
    public static final Integer STATUS_DISABLE = -1;

    /**
     * 删除标志
     */
    public static final Integer DEL_FLAG_1 = 1;

    /**
     * 未删除
     */
    public static final Integer DEL_FLAG_0 = 0;

    /**
     * 系统日志类型： 登录
     */
    public static final int LOG_TYPE_1 = 1;

    /**
     * 系统日志类型： 操作
     */
    public static final int LOG_TYPE_2 = 2;

    /**
     * 系统日志类型： 数据
     */
    public static final int LOG_TYPE_3 = 3;


    /**
     * 系统日志类型： 推送
     */
    public static final int LOG_TYPE_4 = 4;


    /**
     * 系统日志类型： 回传
     */
    public static final int LOG_TYPE_5 = 5;

    /**
     * 操作日志类型： 查询
     */
    public static final int OPERATE_TYPE_1 = 1;

    /**
     * 操作日志类型： 添加
     */
    public static final int OPERATE_TYPE_2 = 2;

    /**
     * 操作日志类型： 更新
     */
    public static final int OPERATE_TYPE_3 = 3;

    /**
     * 操作日志类型： 删除
     */
    public static final int OPERATE_TYPE_4 = 4;

    /**
     * 操作日志类型： 倒入
     */
    public static final int OPERATE_TYPE_5 = 5;

    /**
     * 操作日志类型： 导出
     */
    public static final int OPERATE_TYPE_6 = 6;


    //=======================================网络相关===============================================

    /**
     * {@code 500 Server Error} (HTTP/1.0 - RFC 1945)
     */
    public static final Integer SC_INTERNAL_SERVER_ERROR_500 = 500;
    /**
     * {@code 200 OK} (HTTP/1.0 - RFC 1945)
     */
    public static final Integer SC_OK_200 = 200;

    public static final Integer SC_OK_202 = 202;

    /**
     * 访问权限认证未通过 510
     */
    public static final Integer SC_JEECG_NO_AUTHZ = 510;

    /**
     * 登录用户Token令牌缓存KEY前缀
     */
    public static final String PREFIX_USER_TOKEN = "prefix_user_token_";
    /**
     * Token缓存时间：3600秒即一小时
     */
    public static final int PREFIX_WINNING_EXPIRE_TIME = 3600 * 1;
    /**
     * Token缓存时间：3600秒即一小时
     */
    public static final int TOKEN_EXPIRE_TIME = 3600 * 8;

    /**
     * 发布状态（0未发布，1已发布，2已撤销）
     */
    public static final String NO_SEND = "0";
    public static final String HAS_SEND = "1";
    public static final String HAS_CANCLE = "2";

    /**
     * 状态(0无效1有效)
     */
    public static final String STATUS_0 = "0";
    public static final String STATUS_1 = "1";


    /**
     * 是否配置菜单的数据权限 1是0否
     */
    public static final Integer RULE_FLAG_0 = 0;
    public static final Integer RULE_FLAG_1 = 1;


    /**
     * 游标常量
     */
    public static final int CURSOR = -10;

    /**
     * 身份验证参数
     */
    String ACCESS_TOKEN = "access_token";
}
