package com.jeecg.lanhai.roomservice.webrtc.common;

public class Config {

    /**
     * 需要开通 实时音视频 服务
     * 参考 源码目录下《后台配置部署.md》中的云服务开通部分
     * 有介绍appid 和 accType的获取方法。以及私钥文件的下载方法。
     */
    public class iLive {
        public final static long sdkAppID = 1400220472;

        public final static String accountType = "0";

        /**
         * 派发userSig 和 privateMapKey 采用非对称加密算法RSA，用私钥生成签名。privateKey就是用于生成签名的私钥，私钥文件可以在实时音视频控制台获取
         * 配置privateKey
         * 将private_key文件的内容按下面的方式填写到 privateKey字段。
         */
        public final static String privateKey = "-----BEGIN PRIVATE KEY-----\n" +
                "MIGHAgEAMBMGByqGSM49AgEGCCqGSM49AwEHBG0wawIBAQQg+Y7G7TZOxd7hFQc7\n" +
                "cJ4jYZY3Or1FQwLt9luPIaZBfIyhRANCAAS8T41WN7dieiH/4iBx51r8qsa/yg+k\n" +
                "rydo6x+E8vgZPA2U4ysZlJy1cHRhN3CikcED3gQ1zp/XfHPrC3EgRnsN\n" +
                "-----END PRIVATE KEY-----";
    }


    /**
     * webrtc房间相关参数
     */
    public class WebRTCRoom {
        // 房间容量上限
        public final static int maxMembers = 4;

        // 心跳超时 单位秒
        public final static int heartBeatTimeout = 20;
    }

}
