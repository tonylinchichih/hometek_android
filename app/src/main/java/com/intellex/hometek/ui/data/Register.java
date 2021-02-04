package com.intellex.hometek.ui.data;

import androidx.databinding.BaseObservable;
import androidx.databinding.ObservableField;

/**
 * Created by Hometek on 7/20/2017.
 *
 * <p>資料與ＵＩ欄位綁定
 */
public class Register extends BaseObservable {
    public final ObservableField<String> ip = new ObservableField<>();
    public final ObservableField<String> sipIp = new ObservableField<>();
    public final ObservableField<String> password = new ObservableField<>();
    public final ObservableField<String> phoneNumber = new ObservableField<>();

    public final ObservableField<String> address1 = new ObservableField<>();
    public final ObservableField<String> address2 = new ObservableField<>();
    public final ObservableField<String> address3 = new ObservableField<>();
    public final ObservableField<String> status = new ObservableField<>();
    public final ObservableField<String> fcmStatus = new ObservableField<>();

    // sip
    public final ObservableField<String> mainAccountDisplayName = new ObservableField<>();
    public final ObservableField<String> mainAccountAddress = new ObservableField<>();

    // version
    public final ObservableField<String> version = new ObservableField<>();
}
