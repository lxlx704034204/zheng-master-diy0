package com.zheng.common.util;

import com.alibaba.druid.pool.DruidDataSource;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.sql.*;

/**
 * AES加解密工具类
 * Created by shuzheng on 2017/2/5.
 */
public class AESUtil {

    private static final String encodeRules = "zheng";//这个不能改！

    /**
     * 加密
     * 1.构造密钥生成器
     * 2.根据ecnodeRules规则初始化密钥生成器
     * 3.产生密钥
     * 4.创建和初始化密码器
     * 5.内容加密
     * 6.返回字符串
     */
    public static String AESEncode(String content) {
        try {
            //1.构造密钥生成器，指定为AES算法,不区分大小写
            KeyGenerator keygen = KeyGenerator.getInstance("AES");
            //2.根据ecnodeRules规则初始化密钥生成器
            //生成一个128位的随机源,根据传入的字节数组
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            random.setSeed(encodeRules.getBytes());
            keygen.init(128, random);
            //3.产生原始对称密钥
            SecretKey original_key = keygen.generateKey();
            //4.获得原始对称密钥的字节数组
            byte[] raw = original_key.getEncoded();
            //5.根据字节数组生成AES密钥
            SecretKey key = new SecretKeySpec(raw, "AES");
            //6.根据指定算法AES自成密码器
            Cipher cipher = Cipher.getInstance("AES");
            //7.初始化密码器，第一个参数为加密(Encrypt_mode)或者解密解密(Decrypt_mode)操作，第二个参数为使用的KEY
            cipher.init(Cipher.ENCRYPT_MODE, key);
            //8.获取加密内容的字节数组(这里要设置为utf-8)不然内容中如果有中文和英文混合中文就会解密为乱码
            byte[] byte_encode = content.getBytes("utf-8");
            //9.根据密码器的初始化方式--加密：将数据加密
            byte[] byte_AES = cipher.doFinal(byte_encode);
            //10.将加密后的数据转换为字符串
            //这里用Base64Encoder中会找不到包
            //解决办法：
            //在项目的Build path中先移除JRE System Library，再添加库JRE System Library，重新编译后就一切正常了。
            String AES_encode = new String(new BASE64Encoder().encode(byte_AES));
            //11.将字符串返回
            return AES_encode;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //如果有错就返加nulll
        return null;
    }

    /**
     * 解密
     * 解密过程：
     * 1.同加密1-4步
     * 2.将加密后的字符串反纺成byte[]数组
     * 3.将加密内容解密
     */
    public static String AESDecode(String content) {
        try {
            //1.构造密钥生成器，指定为AES算法,不区分大小写
            KeyGenerator keygen = KeyGenerator.getInstance("AES");
            //2.根据ecnodeRules规则初始化密钥生成器
            //生成一个128位的随机源,根据传入的字节数组
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            random.setSeed(encodeRules.getBytes());
            keygen.init(128, random);
            //3.产生原始对称密钥
            SecretKey original_key = keygen.generateKey();
            //4.获得原始对称密钥的字节数组
            byte[] raw = original_key.getEncoded();
            //5.根据字节数组生成AES密钥
            SecretKey key = new SecretKeySpec(raw, "AES");
            //6.根据指定算法AES自成密码器
            Cipher cipher = Cipher.getInstance("AES");
            //7.初始化密码器，第一个参数为加密(Encrypt_mode)或者解密(Decrypt_mode)操作，第二个参数为使用的KEY
            cipher.init(Cipher.DECRYPT_MODE, key);
            //8.将加密并编码后的内容解码成字节数组
            byte[] byte_content = new BASE64Decoder().decodeBuffer(content);
            /*
             * 解密
             */
            byte[] byte_decode = cipher.doFinal(byte_content);
            String AES_decode = new String(byte_decode, "utf-8");
            return AES_decode;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            throw new RuntimeException("兄弟，配置文件中的密码需要使用AES加密，请使用com.zheng.common.util.AESUtil工具类修改这些值！");
            //e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        }
        //如果有错就返加nulll
        return null;
    }

    public static void main(String[] args) {
//        String[] keys = {
//                "", "123456"
//        };
//        System.out.println("key | AESEncode | AESDecode");
//        for (String key : keys) {
//            System.out.print(key + " | ");
//            String encryptString = AESEncode(key);
//            System.out.print(encryptString + " | ");
//            String decryptString = AESDecode(encryptString);
//            System.out.println(decryptString);
//        }


        System.out.print("-test-21-:"+ MybatisGeneratorUtil.class.getResource("/").getPath() );        //







//        testConnectDB();

//       注意："zheng"加密后为"xUHISEO23dP+VSxTjekCyQ=="
//          "liangxin"加密后为"iIZhAeuyc/+QfYppQBG0Ww=="
//             "mysql"加密后为"p98wSLPTg1FFEMrGwtBVOA=="
//        String mysqlPassWord = "liangxin";
//        String encode = AESEncode(mysqlPassWord);
//        String dncode = AESDecode(encode);
//        System.out.print("-test-对个人数据库密码进行加密-:"+ encode +"\n");  //"p98wSLPTg1FFEMrGwtBVOA=="
//        System.out.print("-test-对加密后的mysql密码 解密-:"+ dncode);        //"mysql"

    }





    public static void testConnectDB(){
        System.out.println( "-test-0-: ");
        Connection cc=null;
        PreparedStatement statement =null;
        ResultSet rs =null;
        try{
            cc= getConnection();
//            cc= getConnection2();
            if(!cc.isClosed()) System.out.println( "-test-1-: Succeeded connecting to the Database!");
            statement = cc.prepareStatement("select * from upms_user  where user_id=?");
            statement.setString(1, "1");//"select * from users where name=?"
            rs = statement.executeQuery();
            while(rs.next()) {
                System.out.println("-test-2-'upms_user'表id=1的name为: "+ rs.getString("username")+"");
            }
        }catch(SQLException e){
            System.out.println("-test-3-: "+ e.toString());
        }finally {
            System.out.println("-test-4-: ");
            try{
                if(rs!=null) rs.close();
                if(statement!=null) statement.close();
                if(cc!=null) cc.close();
            }catch(SQLException e){
                e.printStackTrace();
            }
        }
    }

    public static Connection getConnection(){
        try{
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn= DriverManager.getConnection(
                    "jdbc:mysql://dbserver:3306/zheng",
                    "root",
                    "mysql");//获取连接对象
            return conn;
        }catch(ClassNotFoundException e){
            e.printStackTrace();
            return null;
        }catch(SQLException e){
            e.printStackTrace();
            return null;
        }
    }

    public static Connection getConnection2(){
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        dataSource.setUsername("root");
        dataSource.setPassword("zheng");
        dataSource.setUrl("jdbc:mysql://dbserver:3306/zheng");//127.0.0.1
        dataSource.setInitialSize(1); dataSource.setMinIdle(1);
        dataSource.setMaxActive(20); // 启用监控统计功能  dataSource.setFilters("stat");// for mysql  dataSource.setPoolPreparedStatements(false);

        try{
//            Class.forName(driver);
//            Connection conn= DriverManager.getConnection(url,name,pwd);//获取连接对象

            Connection conn = dataSource.getConnection();
            return conn;
        }catch(SQLException e){
            e.printStackTrace();
            return null;
        }
    }

}
