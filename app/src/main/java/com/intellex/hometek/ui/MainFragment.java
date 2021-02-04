package com.intellex.hometek.ui;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import com.google.firebase.iid.FirebaseInstanceId;
import com.intellex.hometek.sip.R;

/** 主頁ＵＩ元件控制 */
public class MainFragment extends Fragment {
    private static final String TAG = "MainFragment";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private MainFragment.OnFragmentInteractionListener mListener;

    public MainFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of this fragment using the provided
     * parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
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
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Log.d(TAG, "FirebaseInstanceId=" + FirebaseInstanceId.getInstance().getToken());
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        /*
        mSecurity = (ToggleButton) view.findViewById(R.id.security);
        mSecurityBtn = (ImageView) view.findViewById(R.id.security_btn);
        mSecurity.setChecked(DeviceStatusManager.getInstance().getStatus(SECURITY, 2));
        mSecurityBtn.setImageResource(DeviceStatusManager.getInstance().getStatus(SECURITY, 2)?
        R.drawable.off : R.drawable.on);
        mSecurityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("hometek", "onclick");
                ConfigEhomeStatusCtl configEhome = new ConfigEhomeStatusCtl();
                configEhome.start(getContext(),
                        SECURITY,1, new ConfigEhomeStatusCtl.CommandStatusListener(){
                            @Override
                            public void onSuccess() {
                                getStatus();
                            }

                            @Override
                            public void onFailure(String msg) {
                                if (mListener != null) {
                                    Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });
        //mSecurityBtn.setOnCheckedChangeListener(this);
        */
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
        //   void onSecurityCheckedChange(boolean checked);
    }

    @Override
    public void onResume() {
        super.onResume();
        //       getDio();
        //       getStatus();
    }
    /*
        private void getDio() {
            if (!UserInfo.getInstance(MainFragment.this.getActivity()).isRegister()) {
                return;
            }
            DeviceStatusManager.getInstance().getDio(this.getActivity(), new DeviceStatusManager.DeviceStatusListener() {
                @Override
                public void onSuccess(String status) {
                    getStatus();
                }

                @Override
                public void onFailure(String msg) {
                    if (mListener != null)
                        Toast.makeText(MainFragment.this.getActivity(), msg, Toast.LENGTH_LONG).show();
                }
            });
        }

        private void getStatus() {
            if (!UserInfo.getInstance(MainFragment.this.getActivity()).isRegister()) {
                return;
            }
            DeviceStatusManager.getInstance().getStatusAsString(MainFragment.this.getActivity(),new DeviceStatusManager.DeviceStatusListener() {
                @Override
                public void onSuccess(String status) {
                    mSecurity.setChecked(DeviceStatusManager.getInstance().getStatus(SECURITY, 2));
                    mSecurityBtn.setImageResource(DeviceStatusManager.getInstance().getStatus(SECURITY, 2)?
                            R.drawable.off : R.drawable.on);
                }

                @Override
                public void onFailure(String msg) {
                    if (mListener != null)
                        Toast.makeText(MainFragment.this.getActivity(), msg, Toast.LENGTH_LONG).show();
                }
            });
        }
    */
}
