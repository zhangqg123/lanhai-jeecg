package org;

import com.tls.tls_sigature.tls_sigature;
import com.tls.tls_sigature.tls_sigature.GenTLSSignatureResult;

public class TestUserSig {
	public static void main(String[] args) {
		int sdkAppID=1400217925;
		String userID="zhangqg123";
    	String privStr ="-----BEGIN PRIVATE KEY-----\n"+
        "MIGHAgEAMBMGByqGSM49AgEGCCqGSM49AwEHBG0wawIBAQQgVHY4d+pzimZJntM/\n"+
        "9kEMI+2JwHvwuEIlGrRbeSIlXMShRANCAASDH3axnHfwbs2GnndA07pQb6Wse7km\n"+
        "jr24IrEJ4KXvEwJqMMNdRuyXRayeZyHTTB+CHW6YBaLW1bArobIxpMmX\n"+
        "-----END PRIVATE KEY-----";
		GenTLSSignatureResult result = tls_sigature.genSig(sdkAppID, userID, privStr);
		System.out.println("userSig---:"+result.urlSig);
	}

}
