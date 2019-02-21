package com.echopen.asso.echopen.probe_communication;

import android.content.Context;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.echopen.asso.echopen.echography_image_streaming.EchographyImageStreamingService;
import com.echopen.asso.echopen.echography_image_streaming.modes.EchographyImageStreamingTCPMode;
import com.echopen.asso.echopen.probe_communication.notifications.ProbeCommunicationWifiNotification;
import com.echopen.asso.echopen.probe_communication.notifications.ProbeCommunicationWifiNotificationState;
import com.echopen.asso.echopen.utils.Constants;
import com.thanosfisherman.wifiutils.WifiUtils;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by lecoucl on 15/04/17.
 */
public class ProbeCommunicationService {

    private static final String TAG = ProbeCommunicationService.class.getSimpleName();

    private Context mApplicationContext;
    private EchographyImageStreamingService mEchographyImageStreamingService;

    private static final String PROBE_AP_SSID = "demoEcho";
    private static final String PROBE_AP_PASSWORD = "demoEcho";

    private static final int PROBE_AP_CONNECTION_TIME = 1000; // in milliseconds

    public ProbeCommunicationService(Context iApplicationContext, EchographyImageStreamingService iEchographyImageStreamingService) {
        mApplicationContext = iApplicationContext;
        mEchographyImageStreamingService = iEchographyImageStreamingService;
    }

    public void connect(){
        WifiUtils.withContext(mApplicationContext).enableWifi(this::checkResult);

        WifiUtils.withContext(mApplicationContext)
                .connectWith(PROBE_AP_SSID, PROBE_AP_PASSWORD)
                .onConnectionResult(this::checkConnectionResult).start();
    }

    private void checkResult(boolean isSuccess){
        if (isSuccess) {
            EventBus.getDefault().post(new ProbeCommunicationWifiNotification(ProbeCommunicationWifiNotificationState.WIFI_ENABLED));
        }
        else{
            EventBus.getDefault().post(new ProbeCommunicationWifiNotification(ProbeCommunicationWifiNotificationState.WIFI_ENABLED_ERROR));
        }
    }

    private void checkConnectionResult(boolean isSuccess){
        if(isSuccess) {
            try {
                Thread.sleep(PROBE_AP_CONNECTION_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            EventBus.getDefault().post(new ProbeCommunicationWifiNotification(ProbeCommunicationWifiNotificationState.WIFI_CONNECTED));
            mEchographyImageStreamingService.connect(new EchographyImageStreamingTCPMode(Constants.Http.REDPITAYA_IP, Constants.Http.REDPITAYA_PORT));
        }
        else{
            EventBus.getDefault().post(new ProbeCommunicationWifiNotification(ProbeCommunicationWifiNotificationState.WIFI_CONNECTED_ERROR));
        }

    }

}
