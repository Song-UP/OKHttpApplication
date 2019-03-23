package com.example.rxjava_retrofitapp.http;

import android.content.Context;
import android.content.Intent;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

public class SSLSocketHelper {

    /**
     * 通过所有证书
     * @return
     */
    public static SSLSocketFactory getSSLSocketFactory(){
        try {
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null,getTrustManager(),new SecureRandom());
            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            return sslSocketFactory;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 自制证书
     * @param assentFile
     * @return
     */
    public static SSLSocketFactory getSSLSocketFactory(Context context, String assentFile)  {
        SSLSocketFactory sslSocketFactory = null;
        try {
            //证书工厂
            KeyStore keyStore = KeyStore.getInstance("");
            keyStore.load(null);

            //证书路径
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            String certificateAlias = Integer.toString(0);
            keyStore.setCertificateEntry(certificateAlias,
                    certificateFactory.generateCertificate(context.getAssets().open(assentFile)));//打开证书

            SSLContext sslContext = SSLContext.getInstance("SSL");
            //需要自定义的验证证书，要通过构造方法获取TrustManagerFactory获取了
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);
            sslContext.init(null,trustManagerFactory.getTrustManagers(), new SecureRandom());

            sslSocketFactory = sslContext.getSocketFactory();//得到sslSocketFactory

        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }

        return sslSocketFactory;

    }

    private static TrustManager[] getTrustManager(){
        //证书验证的方式
        TrustManager[] trustManagers = new TrustManager[]{
                new X509TrustManager() {
                    @Override
                    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    }
                    @Override
                    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    }
                    @Override
                    public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[0];
                    }
                }

        };
        return trustManagers;
    }

    /**
     * 通过所有域名
     * @return
     */
    public static HostnameVerifier getHostnameVerifier(){
        //域名验证
        HostnameVerifier hostnameVerifier = new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                //通过所有的域名
                return true;
            }
        };
        return hostnameVerifier;
    }

    /**
     * 只运行指定域名
     * @param real_name
     * @return
     */
    public static HostnameVerifier getHostnameVerfier(final String real_name){
        HostnameVerifier hostnameVerifier = new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                //校验域名是否正确
                return HttpsURLConnection.getDefaultHostnameVerifier().verify(real_name,   session);
            }
        };
        return hostnameVerifier;

    }



}
