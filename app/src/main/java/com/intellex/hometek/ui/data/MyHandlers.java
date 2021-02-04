package com.intellex.hometek.ui.data;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RadioGroup;
import com.intellex.hometek.UserInfo;
import org.linphone.LinphoneContext;
import org.linphone.LinphoneManager;
import org.linphone.core.AVPFMode;
import org.linphone.core.AccountCreator;
import org.linphone.core.Address;
import org.linphone.core.Core;
import org.linphone.core.Factory;
import org.linphone.core.ProxyConfig;
import org.linphone.core.TransportType;
import org.linphone.core.VideoActivationPolicy;
import org.linphone.dialer.DialerActivity;
import org.linphone.settings.LinphonePreferences;

/**
 * Created by Hometek on 7/21/2017.
 */
public class MyHandlers {

    private static final String TAG = "MyHandlers";
    Register register;
    Context context;
    UserInfo userInfo;
    RegisterListener listener;

    public interface RegisterListener {
        void onSuccess();

        void onFailure(String msg);
    }

    public MyHandlers(Register register, Context context, RegisterListener listener) {
        this.register = register;
        this.context = context;
        this.listener = listener;
        userInfo = UserInfo.getInstance(context);
    }

    public void onClickSip(View view) {
        Intent intent = new Intent();
        intent.setClass(this.context, DialerActivity.class);
        this.context.startActivity(intent);
    }

    private static AccountCreator getAccountCreator() {
        return LinphoneManager.getInstance().getAccountCreator();
    }

    public void onClickCreateAccount(View view) {
        createAccount("999999999", "Hometek", "999999999", "192.168.10.40", "61.220.35.159", true);
        videoActivationPolicy();
    }

    public void onClickCreateAccount2(View view) {
        createAccount(
                "12345", "linphoneOrg", "12345", "test.linphone.org", "test.linphone.org", true);
        videoActivationPolicy();
    }

    public static void registerSip(String name, String proxyAddress) {
        createAccount(name, "Hometek", name, "192.168.10.40", proxyAddress, true);
        videoActivationPolicy();
    }

    private static void videoActivationPolicy() {
        Core core = LinphoneManager.getCore();
        if (core != null) {
            VideoActivationPolicy vap = core.getVideoActivationPolicy();
            vap.setAutomaticallyInitiate(true);
            vap.setAutomaticallyAccept(true);
            core.setVideoActivationPolicy(vap);
        }
    }

    private static void createAccount(
            String name,
            String displayName,
            String password,
            String domain,
            String proxyAddress,
            boolean setAsDefault) {
        Core core = LinphoneManager.getCore();
        if (core == null) return;

        ProxyConfig[] proxyConfigs = core.getProxyConfigList();
        for (ProxyConfig proxyConfig : proxyConfigs) {
            if (!TextUtils.isEmpty(proxyConfig.getIdentityAddress().getDisplayName())) {
                if (proxyConfig.getIdentityAddress().getDisplayName().equals(displayName)) {
                    core.removeProxyConfig(proxyConfig);
                }
            }
        }

        AccountCreator accountCreator = getAccountCreator();
        accountCreator.setUsername(name);
        accountCreator.setDomain(domain);
        accountCreator.setPassword(password);
        accountCreator.setDisplayName(displayName);
        accountCreator.setTransport(TransportType.Tcp);
        accountCreator.setAsDefault(setAsDefault);
        ProxyConfig proxyConfig = accountCreator.createProxyConfig();
        proxyConfig.edit();
        proxyConfig.setAvpfMode(AVPFMode.Enabled);
        proxyConfig.setAvpfRrInterval(1);
        Address proxy =
                Factory.instance().createAddress("<sip:" + proxyAddress + ";transport=tcp>");
        if (proxy != null) {
            proxyConfig.setServerAddr(proxy.asString());
            proxyConfig.setRoute(proxy.asString());
        }
        proxyConfig.done();
        LinphonePreferences.instance().setServiceNotificationVisibility(true);
        LinphonePreferences.instance().setDebugEnabled(true);
        LinphoneContext.instance().getNotificationManager().startForeground();
        core.setIncTimeout(60);
    }

    public void deleteAllProxyConfigs(View view) {
        Core core = LinphoneManager.getCore();
        if (core != null) {
            ProxyConfig[] proxyConfigs = core.getProxyConfigList();
            for (ProxyConfig proxyConfig : proxyConfigs) {
                core.removeProxyConfig(proxyConfig);
            }
        }
    }

    public static void enableProxyConfig(String displayName, boolean enable) {
        Log.d(TAG, "enableProxyConfig:" + displayName + ", " + enable);
        Core core = LinphoneManager.getCore();
        if (core == null) return;

        ProxyConfig[] proxyConfigs = core.getProxyConfigList();
        for (ProxyConfig proxyConfig : proxyConfigs) {
            if (!TextUtils.isEmpty(proxyConfig.getIdentityAddress().getDisplayName())) {
                if (proxyConfig.getIdentityAddress().getDisplayName().equals(displayName)) {
                    Log.d(
                            TAG,
                            "proxyConfig.getIdentityAddress():" + proxyConfig.getIdentityAddress());
                    proxyConfig.edit();
                    proxyConfig.enableRegister(enable);
                    proxyConfig.done();
                }
            }
        }
    }

    public static boolean getSipRegisterEnabled(String displayName) {
        Core core = LinphoneManager.getCore();
        if (core == null) return false;

        ProxyConfig[] proxyConfigs = core.getProxyConfigList();
        for (ProxyConfig proxyConfig : proxyConfigs) {
            if (!TextUtils.isEmpty(proxyConfig.getIdentityAddress().getDisplayName())) {
                if (proxyConfig.getIdentityAddress().getDisplayName().equals(displayName)) {
                    return proxyConfig.registerEnabled();
                }
            }
        }
        return false;
    }

    public void onClickRegister(View view) {
        deleteAllProxyConfigs(null);
        if (!(register.address1.get().matches("-?\\d+")
                && register.address2.get().matches("-?\\d+")
                && register.address3.get().matches("-?\\d+"))) {
            // todo: show err log
            return;
        }

        userInfo.setSipIp(register.sipIp.get());
        userInfo.setAddress1(register.address1.get());
        userInfo.setAddress2(register.address2.get());
        userInfo.setAddress3(register.address3.get());
        userInfo.save();

        final StringBuilder sipAddr = new StringBuilder();
        String addr1 = String.format("%04d", Integer.parseInt(register.address1.get()));
        String addr2 = String.format("%02d", Integer.parseInt(register.address2.get()));
        String addr3 = String.format("%02d", Integer.parseInt(register.address3.get()));
        sipAddr.append(addr1).append(addr2).append(addr3).append(9);
        MyHandlers.registerSip(sipAddr.toString(), register.sipIp.get());
    }

    public void onTypeChanged(RadioGroup radioGroup, int id) {}
}
