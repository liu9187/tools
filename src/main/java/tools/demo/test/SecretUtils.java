package tools.demo.test;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;

/**
 * 3DES加密解密工具类
 */
public class SecretUtils {
    //定义加密算法有DES ,DESede（3DES）,BlowFish
    private static final String Algorithm = "DESede";
    private static final String PASSWORD_CRYPT_KEY = "zhihuijiangnanjiamijiemi";
    /**
     * 转换成十六进制字节数组
     *
     * @param key
     * @return
     */
    public static byte[] hex(String key) {
        String f = DigestUtils.md5Hex( key );
        byte[] bkeys = new String( f ).getBytes();
        byte[] enk = new byte[24];
        for (int i = 0; i < 24; i++) {
            enk[i] = bkeys[i];
        }
        return enk;
    }


    /**
     * 加密
     *
     * @param key
     * @param srcStr
     * @return
     */
    public static String encode3Des(String key, String srcStr) {
        //字符串转化为十六进制字节数组
        byte[] keybyte = hex( key );
        byte[] src = srcStr.getBytes();
        try {
            //生成秘钥
            SecretKey desKey = new SecretKeySpec( keybyte, Algorithm );
            //加密
            Cipher cipher= Cipher.getInstance(Algorithm);
            cipher.init(Cipher.ENCRYPT_MODE,desKey  );
          String pwd=  Base64.encodeBase64String( cipher.doFinal( src ) );
            return  pwd;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        }
        return null;
    }
    /**
     * 3DES解密
     * @param key 加密密钥，长度为24字节
     * @param desStr 解密后的字符串
     * @return
     *
     * lee on 2017-08-09 10:52:54
     */
    public static String decode3Des(String key, String desStr){
        Base64 base64 = new Base64();
        byte[] keybyte = hex(key);
        byte[] src = base64.decode(desStr);

        try {
            //生成密钥
            SecretKey deskey = new SecretKeySpec(keybyte, Algorithm);
            //解密
            Cipher c1 = Cipher.getInstance(Algorithm);
            c1.init(Cipher.DECRYPT_MODE, deskey);
            String pwd = new String(c1.doFinal(src));
//            return c1.doFinal(src);
            return pwd;
        } catch (java.security.NoSuchAlgorithmException e1) {
            // TODO: handle exception
            e1.printStackTrace();
        }catch(javax.crypto.NoSuchPaddingException e2){
            e2.printStackTrace();
        }catch(java.lang.Exception e3){
            e3.printStackTrace();
        }
        return null;
    }


    public static void main(String[] args) throws IOException {

       //
        FileInputStream fin=new FileInputStream( "config/application.properties" );
        Properties properties=new Properties(  );
        properties.load( fin );
         String password=   properties.getProperty( "db.password" );
         if (null==password||"".equals( password )){
             System.out.println("password failed");
         }else {
             String encode=  SecretUtils.encode3Des( PASSWORD_CRYPT_KEY,password);
             System.out.println("【加密之前】"+encode);
             String decode=   SecretUtils.decode3Des( PASSWORD_CRYPT_KEY,encode );
             System.out.println("【解密之后】"+decode);
         }

    }
}
