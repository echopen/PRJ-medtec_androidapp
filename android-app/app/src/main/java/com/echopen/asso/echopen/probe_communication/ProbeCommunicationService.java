package com.echopen.asso.echopen.probe_communication;

import android.content.Context;
import android.os.AsyncTask;

import com.echopen.asso.echopen.echography_image_streaming.EchographyImageStreamingService;
import com.echopen.asso.echopen.probe_communication.commands.PatternType;
import com.echopen.asso.echopen.probe_communication.commands.RequestForStatusCommand;
import com.echopen.asso.echopen.probe_communication.commands.RequestForTestPatternCommand;
import com.echopen.asso.echopen.probe_communication.notifications.ProbeCommunicationSocketStateNotification;
import com.echopen.asso.echopen.probe_communication.notifications.ProbeCommunicationWifiNotification;
import com.echopen.asso.echopen.probe_communication.notifications.SocketState;
import com.echopen.asso.echopen.probe_communication.notifications.WifiState;
import com.thanosfisherman.wifiutils.WifiUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class ProbeCommunicationService {

    private static final String TAG = ProbeCommunicationService.class.getSimpleName();

    private Context mApplicationContext;
    private EchographyImageStreamingService mEchographyImageStreamingService;
    private TCPCommandChannel mTcpCommandChannel;
    private UDPImageStreamChannel mUdpImageStreamChannel;
    private CommandManager mCommandManager;

    private static final String PROBE_AP_SSID = "demoEcho";
    private static final String PROBE_AP_PASSWORD = "demoEcho";

    private static final int PROBE_AP_CONNECTION_TIME = 1000; // in milliseconds

    public ProbeCommunicationService(Context iApplicationContext, EchographyImageStreamingService iEchographyImageStreamingService, TCPCommandChannel iTcpCommandChannel, UDPImageStreamChannel iUdpImageStreamChannel, CommandManager iCommandManager) {
        mApplicationContext = iApplicationContext;
        mEchographyImageStreamingService = iEchographyImageStreamingService;
        mCommandManager = iCommandManager;
        mTcpCommandChannel = iTcpCommandChannel;
        mUdpImageStreamChannel = iUdpImageStreamChannel;

        EventBus.getDefault().register(this);
    }

    public void connect(){
        WifiUtils.withContext(mApplicationContext).enableWifi(this::checkResult);

        WifiUtils.withContext(mApplicationContext)
                .connectWith(PROBE_AP_SSID, PROBE_AP_PASSWORD)
                .onConnectionResult(this::checkConnectionResult).start();
    }

    /**
     * @brief check result of wifi enabling and update application state
     *
     * @param isSuccess wifi enabled
     */
    private void checkResult(boolean isSuccess){
        if (isSuccess) {
            EventBus.getDefault().post(new ProbeCommunicationWifiNotification(WifiState.WIFI_ENABLED));
        }
        else{
            EventBus.getDefault().post(new ProbeCommunicationWifiNotification(WifiState.WIFI_ENABLED_ERROR));
        }
    }

    /**
     * @brief check connection result following attempt to connect to an acces point
     *
     * @param isSuccess connection to Acces Point succeed
     */

    private void checkConnectionResult(boolean isSuccess){
        if(isSuccess) {
            try {
                Thread.sleep(PROBE_AP_CONNECTION_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            EventBus.getDefault().post(new ProbeCommunicationWifiNotification(WifiState.WIFI_CONNECTED));

            // start communication sockets
            mTcpCommandChannel.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            mUdpImageStreamChannel.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            //mEchographyImageStreamingService.connect(new EchographyImageStreamingTCPMode(Constants.Http.REDPITAYA_IP, Constants.Http.REDPITAYA_PORT));
        }
        else{
            EventBus.getDefault().post(new ProbeCommunicationWifiNotification(WifiState.WIFI_CONNECTED_ERROR));
        }

    }


    @Subscribe(threadMode = ThreadMode.BACKGROUND)
    public void onReply(ProbeCommunicationSocketStateNotification iSocketNotification) {
        // initialize probe configuration when command socket is connected
        if(iSocketNotification.getSocketState() == SocketState.CONNECTED && iSocketNotification.getSocketName().equals("TCP")){
            initProbeCommunication();
        }
    }

    /**
     * @brief initialize probe configuration
     */
    private void initProbeCommunication(){
        mCommandManager.sendRequest(new RequestForStatusCommand());
        mCommandManager.sendRequest(
                new RequestForTestPatternCommand(PatternType.PAT_GRAY,
                                    1000000,
                                    100,
                                    5000,
                                    800,
                                    8));

    }

}
