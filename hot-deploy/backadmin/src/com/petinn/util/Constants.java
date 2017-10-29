package com.petinn.util;

public final class Constants {

    private Constants() {  }

    // 缺省资源文件名称，位置${OFBIZ_HOME}/hot-deploy/bakmgr/config/backmanager.properties
    public static final String DEFAULT_RESOURCE_FILE = "backmanager.properties";

    /*
     * 微信请求URL列表
     */
    public static final String WECHAT_REQUEST_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid={0}&secret={1}";
    public static final String WECHAT_REQUEST_TICKET_URL = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token={0}&type={1}";
    public static final String WECHAT_DOWNLOAD_MEDIA = "https://api.weixin.qq.com/cgi-bin/media/get?access_token={0}&media_id={1}";
    public static final String WECHAT_CREATE_GROUP = "https://api.weixin.qq.com/cgi-bin/groups/create?access_token={0}";
    public static final String WECHAT_DELETE_GROUP = "https://api.weixin.qq.com/cgi-bin/groups/delete?access_token={0}";
    public static final String WECHAT_UPDATE_GROUP = "https://api.weixin.qq.com/cgi-bin/groups/update?access_token={0}";
    public static final String WECHAT_GET_GROUP = "https://api.weixin.qq.com/cgi-bin/groups/get?access_token={0}";
    public static final String WECHAT_GET_CUSTOMER = "https://api.weixin.qq.com/cgi-bin/user/get?access_token={0}";
    public static final String WECHAT_GET_NEXT_CUSTOMER = "https://api.weixin.qq.com/cgi-bin/user/get?access_token={0}&next_openid={1}";
    public static final String WECHAT_GET_CUSTOMER_INFO = "https://api.weixin.qq.com/cgi-bin/user/info?access_token={0}&openid={1}&lang=zh_CN"; //获取粉丝信息
    public static final String WECHAT_REMARK_CUSTOMER = "https://api.weixin.qq.com/cgi-bin/user/info/updateremark?access_token={0}";
    public static final String WECHAT_BATCH_GET_CUSTOMER_INFO = "https://api.weixin.qq.com/cgi-bin/user/info/batchget?access_token={0}";
    public static final String WECHAT_BATCH_UPDATE_GROUP = "https://api.weixin.qq.com/cgi-bin/groups/members/batchupdate?access_token={0}";
    public static final String WECHAT_CREATE_MENU = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token={0}";
    public static final String WECHAT_SEND_MSG = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token={0}";
    public static final String WECHAT_SEND_TEMP_MSG = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token={0}";
    //public static final String WECHAT_GET_STORE_CATEGORY = "https://api.weixin.qq.com/cgi-bin/api_getwxcategory?access_token={0}";
    public static final String WECHAT_GET_STORE_CATEGORY = "http://api.weixin.qq.com/cgi-bin/poi/getwxcategory?access_token={0}";
    public static final String WECHAT_CREATE_STORE = "https://api.weixin.qq.com/cgi-bin/poi/addpoi?access_token={0}";
    public static final String WECHAT_DELETE_STORE = "https://api.weixin.qq.com/cgi-bin/poi/delpoi?access_token={0}";
    public static final String WECHAT_GET_STORE_BASE_INFO_LIST = "https://api.weixin.qq.com/cgi-bin/poi/getpoilist?access_token={0}";
    public static final String WECHAT_SEND_MASS_MSG_BY_GROUP = "https://api.weixin.qq.com/cgi-bin/message/mass/sendall?access_token={0}";
    public static final String WECHAT_SEND_MASS_MSG_BY_OPENID = "https://api.weixin.qq.com/cgi-bin/message/mass/send?access_token={0}";
    public static final String WECHAT_UPLOAD_IMG_RESOURCE = "https://api.weixin.qq.com/cgi-bin/media/uploadimg?access_token={0}";
    public static final String WECHAT_SERVICE_GET_CHAT_RECORD = "https://api.weixin.qq.com/customservice/msgrecord/getrecord?access_token={0}";
    public static final String WECHAT_CUSTOMER_SERVICE_UPDATE = "https://api.weixin.qq.com/customservice/kfaccount/update?access_token={0}";
    public static final String WECHAT_CUSTOMER_SERVICE_ADD = "https://api.weixin.qq.com/customservice/kfaccount/add?access_token={0}";
    public static final String WECHAT_CREATE_CARD = "https://api.weixin.qq.com/card/create?access_token={0}";
    public static final String WECHAT_DELETE_CARD = "https://api.weixin.qq.com/card/delete?access_token={0}";
    public static final String WECHAT_CARD_IDS = "https://api.weixin.qq.com/card/batchget?access_token={0}";
    public static final String WECHAT_CARD_INFO = "https://api.weixin.qq.com/card/get?access_token={0}";
    public static final String WECHAT_CREATE_CARD_SHELF = "https://api.weixin.qq.com/card/landingpage/create?access_token={0}";
    public static final String WECHAT_CREATE_CARD_QR_CODE = "https://api.weixin.qq.com/card/qrcode/create?access_token={0}";
    public static final String WECHAT_SLIENT_AUTHORIZATION = "https://api.weixin.qq.com/sns/oauth2/access_token?appid={0}&secret={1}&code={2}&grant_type=authorization_code";
    public static final String WECHAT_CARD_GET_STATUS = "https://api.weixin.qq.com/card/code/get?access_token={0}";
    public static final String WECHAT_CARD_CONSUME = "https://api.weixin.qq.com/card/code/consume?access_token={0}";
    public static final String WECHAT_UPLOAD_FOREVER_IMG_RESOURCE = "https://api.weixin.qq.com/cgi-bin/material/add_material?access_token={0}";
    public static final String WECHAT_GET_NEWS = "https://api.weixin.qq.com/cgi-bin/material/get_material?access_token={0}";
    public static final String WECHAT_ADD_NEWS = "https://api.weixin.qq.com/cgi-bin/material/add_news?access_token={0}";
    public static final String WECHAT_SYNC_NEWS = "https://api.weixin.qq.com/cgi-bin/material/batchget_material?access_token={0}";
    public static final String WECHAT_UPDATE_NEWS = "https://api.weixin.qq.com/cgi-bin/material/update_news?access_token={0}";
    public static final String WECHAT_DELETE_NEWS = "https://api.weixin.qq.com/cgi-bin/material/del_material?access_token={0}";
    public static final String WECHAT_CARD_QR_CODE = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token={0}";
    public static final String WECHAT_GET_USER_INFO = "https://api.weixin.qq.com/sns/userinfo?access_token={0}&openid={1}&lang=zh_CN ";

    // 京东授权token地址
    public static final String JD_ACCESS_TOKEN_URL = "https://oauth.jd.com/oauth/token?grant_type=authorization_code&client_id={0}&redirect_uri={1}&code={2}&state={3}&client_secret={4}";
    // 京东刷新toekn地址
    public static final String JD_REFRESH_ACCESS_TOKEN_URL = "https://oauth.jd.com/oauth/token?grant_type=refresh_tokeN&client_secret={0}&client_id={1}&refresh_token={2}";

    // 请求天翼短信请求token
    public static final String REQUEST_SMS_TOOKEN_URL = "https://oauth.api.189.cn/emp/oauth2/v3/access_token";
    public static final String REQUEST_SMS_URL = "http://api.189.cn/v2/emp/templateSms/sendSms";
    public static final String REQUEST_SMS_STATUS_URL = "http://api.189.cn/v2/EMP/nsagSms/appnotify/querysmsstatus";

    public static final int SMS_CODE_EXPIRE_TIME = 2;// 短信的失效时间，以分钟为单位

    public static final String SMS_APP_ID = "SMS_APP_ID";
    public static final String SMS_APP_SECRET = "SMS_APP_SECRET";


    public static final String WECHAT_PAY_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
    public static final String WECHAT_QUERY_URL = "https://api.mch.weixin.qq.com/pay/orderquery";

    /*
     * 缓存关键字
     */
    public static final String WECHAT_KEY = "WECHAT_ACCOUNT";
    public static final String WECHAT_OPEN_KEY = "WECHAT_OPEN_ACCOUNT";
    public static final String WECHAT_ACCESSTOKEN = "WECHAT_ACCESSTOKEN";
    public static final String WECHAT_TICKET = "WECHAT_TICKET";
    public static final String DOMAIN_KEY = "CURRENT_DOMAIN";
    public static final String PHONE_SMS_KEY = "PHONE_SMS_KEY";
    public static final String PHONE_SMS_ACCESSTOKEN = "PHONE_SMS_ACCESSTOKEN";
    public static final String OAUTH_WECHAT_ACCESSTOKEN = "OAUTH_WECHAT_ACCESSTOKEN";
    public static final String JD_PRODUCT_STORE_ID = "JD_PRODUCT_STORE_ID";
    public static final String JD_PRODUCT_STORE_LIST = "JD_PRODUCT_STORE_LIST";
    public static final String TMALL_PRODUCT_STORE_ID = "TMALL_PRODUCT_STORE_ID";
    public static final String TMALL_PRODUCT_STORE_LIST = "TMALL_PRODUCT_STORE_LIST";
    /*
     * 本地上传路径
     */
    public static final String WECHAT_DIR = "/images/wechat/";
    public static final String SHOP_DIR = "/images/shop/";
}
