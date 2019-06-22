package com.jeecg.lanhai.roomservice.liveroom.common;

public class Config {

    /**
     * 需要开通云直播服务
     * 参考指引 @https://cloud.tencent.com/document/product/454/7953#1.-.E8.A7.86.E9.A2.91.E7.9B.B4.E6.92.AD.EF.BC.88lvb.EF.BC.89
     * 有介绍bizid 和 pushSecretKey的获取方法。
     */
    public class Live {
        /**
         * 云直播 APP_ID =  和 APIKEY 主要用于腾讯云直播common cgi请求。appid 用于表示您是哪个客户，APIKey参与了请求签名sign的生成。
         * 后台用他们来校验common cgi调用的合法性
         */
        public final static int APP_ID = 1252709773;

        /**
         * 云直播 APP_BIZID = 和pushSecretKey 主要用于推流地址的生成，填写错误，会导致推流地址不合法，推流请求被腾讯云直播服务器拒绝
         */
        public final static int APP_BIZID = 52027;

        /**
         * 云直播 推流防盗链key = 和 APP_BIZID 主要用于推流地址的生成，填写错误，会导致推流地址不合法，推流请求被腾讯云直播服务器拒绝
         */
        public final static String PUSH_SECRET_KEY = "681c40644af4d9ceda64e0ed2fc6a21e";

        /**
         * 云直播 API鉴权key = 和appID 主要用于common cgi请求。appid 用于表示您是哪个客户，APIKey参与了请求签名sign的生成。
         * 后台用他们来校验common cgi调用的合法性。
         */
        public final static String APIKEY = "681c40644af4d9ceda64e0ed2fc6a21e";

        // 云直播 推流有效期单位秒 默认7天
        public final static int validTime = 3600 * 24 * 7;
        // 云直播 播放域名。https://console.cloud.tencent.com/live/domainmanage
        public final static String PLAYHOST = "play.zqgzht.com";
        // 云直播 推流域名。https://console.cloud.tencent.com/live/domainmanage
        public final static String PUSHHOST = "push.zqgzht.com";
    }

    /**
     * 需要开通云通信服务
     * 参考指引 @https://cloud.tencent.com/document/product/454/7953#3.-.E4.BA.91.E9.80.9A.E8.AE.AF.E6.9C.8D.E5.8A.A1.EF.BC.88im.EF.BC.89
     * 有介绍appid 和 accType的获取方法。以及私钥文件的下载方法。
     */
    public class IM {
        /**
         * 云通信 IM_SDKAPPID = IM_ACCOUNTTYPE 和 PRIVATEKEY 是云通信独立模式下，为您的独立账号 identifer，
         * 派发访问云通信服务的userSig票据的重要信息，填写错误会导致IM登录失败，IM功能不可用
         */
        public final static long IM_SDKAPPID = 1400216054;

        /**
         * 云通信 账号集成类型 IM_ACCOUNTTYPE = IM_SDKAPPID 和 PRIVATEKEY 是云通信独立模式下，为您的独立账户identifer，
         * 派发访问云通信服务的userSig票据的重要信息，填写错误会导致IM登录失败，IM功能不可用
         */
        public final static String IM_ACCOUNTTYPE = "0";

        // 云通信 管理员账号
        public final static String ADMINISTRATOR = "admin";

        /**
         * 云通信 派发usersig 采用非对称加密算法RSA，用私钥生成签名。PRIVATEKEY就是用于生成签名的私钥，私钥文件可以在互动直播控制台获取
         * 配置privateKey
         * 将private_key文件的内容按下面的方式填写到 PRIVATEKEY。
         */
        public final static String PRIVATEKEY = "-----BEGIN PRIVATE KEY-----\n"+
                "MIGHAgEAMBMGByqGSM49AgEGCCqGSM49AwEHBG0wawIBAQQgJ+OyTFCaHDWCEPoO\n"+
                "OV+Nr3je0JTkgglI4C5ozai1sbuhRANCAASSzqkhZ/NHmzYBfncRZjm3Ki2Sa1IY\n"+
                "O4IrSc1RZJT+0svuer4XaFf7CGbsPouOdY1dxr8JGaOczR5RPRxj4vHt\n"+
                "-----END PRIVATE KEY-----";
        /**
         * 云通信 验证usersig 所用的公钥
         */
        public final static String PUBLICKEY = "-----BEGIN PUBLIC KEY-----\n" +
                "MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAEks6pIWfzR5s2AX53EWY5tyotkmtS\n" +
                "GDuCK0nNUWSU/tLL7nq+F2hX+whm7D6LjnWNXca/CRmjnM0eUT0cY+Lx7Q==\n" +
                "-----END PUBLIC KEY-----\n";
    }


    /**
     * 多人音视频房间相关参数
     */
    public class MultiRoom {
        // 房间容量上限
        public final static int maxMembers = 4;

        // 心跳超时 单位秒
        public final static int heartBeatTimeout = 20;

        // 空闲房间超时 房间创建后一直没有人进入，超过给定时间将会被后台回收，单位秒
        public final static int maxIdleDuration = 30;
    }

    /**
     * 双人音视频房间相关参数
     */
    public class DoubleRoom {
        // 心跳超时 单位秒
        public final static int heartBeatTimeout = 20;

        // 空闲房间超时 房间创建后一直没有人进入，超过给定时间将会被后台回收，单位秒
        public final static int maxIdleDuration = 30;
    }

    /**
     * 直播连麦房间相关参数
     */
    public class LiveRoom {
        // 房间容量上限
        public final static int maxMembers = 4;

        // 心跳超时 单位秒
        public final static int heartBeatTimeout = 20;

        // 空闲房间超时 房间创建后一直没有人进入，超过给定时间将会被后台回收，单位秒
        public final static int maxIdleDuration = 30;

        // 最大观众列表长度
        public final static int maxAudiencesLen = 30;
    }

    /**
     * 创建者退出的时候是否需要删除房间
     * 默认false。表示房间所有成员是对等的，第一个进房的人退出并不会销毁房间，只有房间没人的时候才会销毁房间。
     * 此配置项只针对双人和多人实时音视频
     */
    public final static boolean isCreatorDestroyRoom = false;
}
