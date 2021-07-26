package cn.aixuxi.ossp.auth.client.util;

import cn.aixuxi.ossp.common.utils.RsaUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.security.interfaces.RSAPublicKey;
import java.util.stream.Collectors;

/**
 * Jwt工具类
 * @author ruozhuliufeng
 * @date 2021-07-26
 */
public class JwtUtils {
    private static final String PUBKEY_START = "-----BEGIN PUBLIC KEY-----";
    private static final String PUBKEY_END = "-----END PUBLIC KEY-----";

    public static RSAPublicKey getPubKeyObj(){
        Resource res = new ClassPathResource("");
        try(BufferedReader br = new BufferedReader(new InputStreamReader(res.getInputStream()))){
            String pubKey = br.lines().collect(Collectors.joining("\n"));
            pubKey = pubKey.substring(PUBKEY_START.length(),pubKey.indexOf(PUBKEY_END));
            return RsaUtils.getPublicKey(pubKey);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
