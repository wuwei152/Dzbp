package com.md.dzbp.ui.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.apkfuns.logutils.LogUtils;
import com.md.dzbp.Base.BaseActivity;
import com.md.dzbp.R;
import com.md.dzbp.constants.APIConfig;
import com.md.dzbp.constants.Constant;
import com.md.dzbp.data.CameraInfo;
import com.md.dzbp.data.SchoolAreaBean;
import com.md.dzbp.data.SchoolBean;
import com.md.dzbp.model.NetWorkRequest;
import com.md.dzbp.model.UIDataListener;
import com.md.dzbp.ui.view.MyProgressDialog;
import com.md.dzbp.ui.view.dropdownmenu.ArrayDropdownAdapter;
import com.md.dzbp.ui.view.dropdownmenu.DropdownMenu;
import com.md.dzbp.ui.view.dropdownmenu.MenuManager;
import com.md.dzbp.ui.view.dropdownmenu.OnDropdownItemClickListener;
import com.md.dzbp.utils.ACache;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 设置设备ID
 */
public class SettingActivity extends BaseActivity implements UIDataListener {

    @BindView(R.id.set_back)
    LinearLayout mBack;
    @BindView(R.id.set_school)
    DropdownMenu mSchool;
    @BindView(R.id.set_area)
    DropdownMenu mArea;
    @BindView(R.id.set_confirm)
    TextView mConfirm;
    @BindView(R.id.set_ip)
    EditText mIp;
    @BindView(R.id.set_port)
    EditText mport;
    @BindView(R.id.set_psw)
    EditText mPsw;
    @BindView(R.id.set_cameratype)
    RadioGroup cameratype;
    @BindView(R.id.set_devicetype)
    RadioGroup devicetype;
    private NetWorkRequest netWorkRequest;
    private Dialog dialog;
    private ArrayList<SchoolBean> schoolList;
    private ArrayList<SchoolAreaBean> schoolAreaList;
    private String schoolId;
    private String areaId;
    private ACache mAcache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayoutResID() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initUI() {
        mAcache = ACache.get(SettingActivity.this);
        dialog = MyProgressDialog.createLoadingDialog(this, "", this);
        netWorkRequest = new NetWorkRequest(this, this);

    }

    @Override
    protected void initData() {

        netWorkRequest.doGetRequest(0, Constant.getUrl(this, APIConfig.GET_SCHOOL), true, new HashMap());

        mSchool.setOnItemClickListener(new OnDropdownItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                schoolId = schoolList.get(i).getSchoolid();
                areaId = "";
                mArea.setTitle("请选择区域");
                Map map = new HashMap();
                map.put("schoolId", schoolList.get(i).getSchoolid());
                netWorkRequest.doGetRequest(1, Constant.getUrl(SettingActivity.this, APIConfig.GET_AREA), true, map);
            }
        });

        mArea.setOnItemClickListener(new OnDropdownItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                areaId = schoolAreaList.get(i).getId();
            }
        });
        MenuManager.group(mSchool, mArea);

//        initPermission();
    }

    @OnClick({R.id.set_back, R.id.set_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.set_back:
                finish();
                break;
            case R.id.set_confirm:
                if (TextUtils.isEmpty(schoolId)) {
                    showToast("请选择学校");
                    return;
                }
                if (TextUtils.isEmpty(areaId)) {
                    showToast("请选择区域");
                    return;
                }
                if (TextUtils.isEmpty(mIp.getText().toString())) {
                    showToast("请输入IP");
                    return;
                }
                if (TextUtils.isEmpty(mport.getText().toString())) {
                    showToast("请输入端口");
                    return;
                }
                if (TextUtils.isEmpty(mPsw.getText().toString())) {
                    showToast("请输入密码");
                    return;
                }

                ArrayList<CameraInfo> mCameraInfos = new ArrayList<>();
                mCameraInfos.add(new CameraInfo(mIp.getText().toString().trim(), mport.getText().toString().trim(), "admin", mPsw.getText().toString().trim()));//测试
                mAcache.put("CameraInfo", mCameraInfos);
                LogUtils.d(mCameraInfos.get(0));

                if (cameratype.getCheckedRadioButtonId() == R.id.set_type1) {
                    mAcache.put("CameraType", "1");
                } else {
                    mAcache.put("CameraType", "2");
                }

                if (devicetype.getCheckedRadioButtonId() == R.id.set_typed2) {
                    mAcache.put("DeviceType", "1");
                } else {
                    mAcache.put("DeviceType", "0");
                }


                Map map = new HashMap();
                map.put("schoolId", schoolId);
                map.put("schoolAreaId", areaId);
                map.put("sn", getMacAddress());
                netWorkRequest.doGetRequest(3, Constant.getUrl(SettingActivity.this, APIConfig.GET_DIVICE_ID), true, map);
                break;
        }
    }

//    public synchronized String getMacid() {
//        WifiManager wm = (WifiManager) getSystemService(Context.WIFI_SERVICE);
//        String WLANMAC = wm.getConnectionInfo().getMacAddress();
//        return WLANMAC;
//    }

    @Override
    public void loadDataFinish(int code, Object data) {
        if (code == 0) {
            if (data != null) {
                schoolList = JSON.parseObject(data.toString(), new TypeReference<ArrayList<SchoolBean>>() {
                });
                if (schoolList != null) {
                    List<String> mList = new ArrayList<>();
                    for (SchoolBean b : schoolList) {
                        mList.add(b.getSchoolname());
                    }
                    mSchool.setAdapter(new ArrayDropdownAdapter(this, R.layout.dropdown_item, mList));
                }
            }
        } else if (code == 1) {
            if (data != null) {
                schoolAreaList = JSON.parseObject(data.toString(), new TypeReference<ArrayList<SchoolAreaBean>>() {
                });
                if (schoolAreaList != null && schoolAreaList.size() > 0) {
                    List<String> mList = new ArrayList<>();
                    for (SchoolAreaBean b : schoolAreaList) {
                        mList.add(b.getAddress());
                    }
                    mArea.setAdapter(new ArrayDropdownAdapter(this, R.layout.dropdown_item, mList));
                } else {
                    mArea.setTitle("暂无数据");
                }
            }
        } else if (code == 3) {
            LogUtils.d(data.toString());
            if (data != null) {

                JSONObject object = JSONObject.parseObject(((JSONObject) data).toJSONString());
                String deviceId = object.getString("deviceId");
                String qrcode = object.getString("qrcode");

                if (!TextUtils.isEmpty(deviceId)) {
                    mAcache.put("DeviceId", deviceId);
                    if (TextUtils.isEmpty(qrcode)) {
                        qrcode = "  ";
                    }
                    mAcache.put("qrcode", qrcode);
                    startActivity(new Intent(this, MainActivity.class));
                    finish();
                }
            }
        }
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showDialog() {
        if (dialog != null) {
            dialog.show();
        }
    }

    @Override
    public void dismissDialog() {
        if (dialog != null && !isFinishing()) {
            dialog.dismiss();
        }
    }

    @Override
    public void onError(int errorCode, String errorMessage) {

    }

    @Override
    public void cancelRequest() {
        netWorkRequest.CancelPost();
    }


    private String getMacAddress() {
        String strMacAddr = null;
        try {
            InetAddress ip = getLocalInetAddress();

            byte[] b = NetworkInterface.getByInetAddress(ip)
                    .getHardwareAddress();
            StringBuffer buffer = new StringBuffer();
            for (int i = 0; i < b.length; i++) {
                if (i != 0) {
                    buffer.append(':');
                }

                String str = Integer.toHexString(b[i] & 0xFF);
                buffer.append(str.length() == 1 ? 0 + str : str);
            }
            strMacAddr = buffer.toString().toUpperCase();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return strMacAddr;
    }

    /**
     * 获取移动设备本地IP
     *
     * @return
     */
    protected static InetAddress getLocalInetAddress() {
        InetAddress ip = null;
        try {
            //列举
            Enumeration en_netInterface = NetworkInterface.getNetworkInterfaces();
            while (en_netInterface.hasMoreElements()) {//是否还有元素
                NetworkInterface ni = (NetworkInterface) en_netInterface.nextElement();//得到下一个元素
                Enumeration en_ip = ni.getInetAddresses();//得到一个ip地址的列举
                while (en_ip.hasMoreElements()) {
                    ip = (InetAddress) en_ip.nextElement();
                    if (!ip.isLoopbackAddress() && ip.getHostAddress().indexOf(":") == -1)
                        break;
                    else
                        ip = null;
                }

                if (ip != null) {
                    break;
                }
            }
        } catch (SocketException e) {

            e.printStackTrace();
        }
        return ip;
    }


//    private void initPermission() {
//        XXPermissions.with(SettingActivity.this)
//                .request(new OnPermission() {
//
//                    @Override
//                    public void hasPermission(List<String> granted, boolean isAll) {
//                        if (!isAll) {
////                            Toast.makeText(SettingActivity.this, "获取部分权限成功，但部分权限未正常授予",Toast.LENGTH_LONG).show();
//                        }
//                    }
//
//                    @Override
//                    public void noPermission(List<String> denied, boolean quick) {
//
//                    }
//                });
//    }


}
