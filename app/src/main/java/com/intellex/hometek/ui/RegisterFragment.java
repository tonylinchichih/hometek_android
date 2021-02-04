package com.intellex.hometek.ui;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.databinding.DataBindingUtil;
import com.intellex.hometek.MainActivity;
import com.intellex.hometek.UserInfo;
import com.intellex.hometek.sip.BuildConfig;
import com.intellex.hometek.sip.R;
import com.intellex.hometek.sip.databinding.FragmentRegisterBinding;
import com.intellex.hometek.ui.comm.TextWatcherAdapter;
import com.intellex.hometek.ui.data.MyHandlers;
import com.intellex.hometek.ui.data.Register;
import org.linphone.LinphoneManager;
import org.linphone.core.Core;
import org.linphone.core.CoreListenerStub;
import org.linphone.core.ProxyConfig;
import org.linphone.core.RegistrationState;
import org.linphone.settings.widget.SettingListenerBase;
import org.linphone.settings.widget.SwitchSetting;

/** 註冊頁面 */
public class RegisterFragment extends MyBaseFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private UserInfo mUserInfo;
    private OnFragmentInteractionListener mListener;

    private CoreListenerStub mLinphoneCoreListener;

    private ImageView status;

    private SwitchSetting mDisable;

    public RegisterFragment() {
        // Required empty public constructor
    }

    @Override
    public void update(boolean skip) {}

    /**
     * Use this factory method to create a new instance of this fragment using the provided
     * parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegisterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegisterFragment newInstance(String param1, String param2) {
        RegisterFragment fragment = new RegisterFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        mUserInfo = UserInfo.getInstance(getContext());
    }

    Register register;
    MyRegisterListener myRegisterListener;

    private class MyRegisterListener implements MyHandlers.RegisterListener {
        @Override
        public void onSuccess() {
            register.status.set(getString(R.string.register_ok));
            registerFirebase();
        }

        @Override
        public void onFailure(String msg) {
            register.status.set(getString(R.string.register_failure));
            // registerFirebase();
        }
    }

    private void registerFirebase() {}

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final FragmentRegisterBinding binding =
                DataBindingUtil.inflate(inflater, R.layout.fragment_register, container, false);
        TextWatcherAdapter watcher =
                new TextWatcherAdapter() {
                    @Override
                    public void afterTextChanged(Editable s) {
                        // loginInfo.validate(getResources());
                        if (s == binding.ip.getEditableText()) {
                            register.ip.set(s.toString());
                        } else if (s == binding.address1.getEditableText()) {
                            register.address1.set(s.toString());
                        } else if (s == binding.address2.getEditableText()) {
                            register.address2.set(s.toString());
                        } else if (s == binding.address3.getEditableText()) {
                            register.address3.set(s.toString());
                        } else if (s == binding.phoneNumber.getEditableText()) {
                            register.phoneNumber.set(s.toString());
                        } else if (s == binding.password.getEditableText()) {
                            register.password.set(s.toString());
                        } else if (s == binding.sipIp.getEditableText()) {
                            register.sipIp.set(s.toString());
                        }
                    }
                };

        register = new Register();
        binding.setRegister(register);
        register.address1.set(mUserInfo.getAddress1());
        register.address2.set(mUserInfo.getAddress2());
        register.address3.set(mUserInfo.getAddress3());
        register.version.set("v" + BuildConfig.VERSION_NAME);
        binding.sipIp.addTextChangedListener(watcher);
        binding.address1.addTextChangedListener(watcher);
        binding.address2.addTextChangedListener(watcher);
        binding.address3.addTextChangedListener(watcher);

        // sip
        register.sipIp.set(mUserInfo.getSipIp());
        if (BuildConfig.DEBUG) {
            binding.linphoneBtn.setVisibility(View.VISIBLE);
        } else {
            binding.linphoneBtn.setVisibility(View.GONE);
        }

        myRegisterListener = new MyRegisterListener();
        MyHandlers handlers = new MyHandlers(register, getContext(), myRegisterListener);
        binding.setHandlers(handlers);

        mLinphoneCoreListener =
                new CoreListenerStub() {
                    @Override
                    public void onRegistrationStateChanged(
                            final Core core,
                            final ProxyConfig proxy,
                            final RegistrationState state,
                            String smessage) {
                        if (core.getProxyConfigList() == null) {
                            showNoAccountConfigured();
                            return;
                        }

                        if ((core.getDefaultProxyConfig() != null
                                        && core.getDefaultProxyConfig().equals(proxy))
                                || core.getDefaultProxyConfig() == null) {

                            if (core.getDefaultProxyConfig() == null) {
                                showNoAccountConfigured();
                            } else {
                                status.setImageResource(getStatusIconResource(state));
                                register.mainAccountDisplayName.set(
                                        proxy.getIdentityAddress().getDisplayName());
                                register.mainAccountAddress.set(
                                        proxy.getIdentityAddress().asStringUriOnly());
                            }
                            updateSipSwitch();
                            //
                            // mStatusText.setText(getStatusIconText(state));
                        }
                    }
                };
        View root = binding.getRoot();

        mDisable = root.findViewById(R.id.pref_disable_account);
        mDisable.setTitle(getString(R.string.sip_enable));
        updateSipSwitch();
        status = root.findViewById(R.id.main_account_status);
        return root;
    }

    private void updateSipSwitch() {
        mDisable.setListener(null);
        mDisable.setChecked(MyHandlers.getSipRegisterEnabled("Hometek"));
        mDisable.setListener(
                new SettingListenerBase() {
                    @Override
                    public void onBoolValueChanged(boolean newValue) {
                        MyHandlers.enableProxyConfig("Hometek", newValue);
                    }
                });
    }

    private void showNoAccountConfigured() {
        register.mainAccountDisplayName.set(getString(R.string.no_account));
        register.mainAccountAddress.set("");
        status.setImageResource(R.drawable.led_disconnected);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        // Log.d("hometek", "push onresume");
        ((MainActivity) getActivity()).setActionBarTitle(R.string.setting);

        //        recyclerView.scrollToPosition(mlist.size() - 1);

        Core core = LinphoneManager.getCore();
        if (core != null) {
            core.addListener(mLinphoneCoreListener);
            ProxyConfig lpc = core.getDefaultProxyConfig();
            if (lpc != null) {
                mLinphoneCoreListener.onRegistrationStateChanged(core, lpc, lpc.getState(), null);
            } else {
                showNoAccountConfigured();
            }
        } else {
            //            mStatusText.setVisibility(View.VISIBLE);
        }

        //        displayMainAccount();
    }

    @Override
    public void onStop() {
        super.onStop();
        ((MainActivity) getActivity()).clearActionBarTitle();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(
                    context.toString() + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this fragment to allow an
     * interaction in this fragment to be communicated to the activity and potentially other
     * fragments contained in that activity.
     *
     * <p>See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html" >Communicating
     * with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private int getStatusIconResource(RegistrationState state) {
        try {
            if (state == RegistrationState.Ok) {
                return R.drawable.led_connected;
            } else if (state == RegistrationState.Progress) {
                return R.drawable.led_inprogress;
            } else if (state == RegistrationState.Failed) {
                return R.drawable.led_error;
            } else {
                return R.drawable.led_disconnected;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return R.drawable.led_disconnected;
    }
}
