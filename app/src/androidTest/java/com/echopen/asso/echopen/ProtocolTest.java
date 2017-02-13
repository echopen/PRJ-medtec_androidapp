package com.echopen.asso.echopen;

import android.content.res.AssetManager;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.SmallTest;

import com.echopen.asso.echopen.model.Data.BitmapDisplayer;
import com.echopen.asso.echopen.model.Data.BitmapDisplayerFactory;
import com.echopen.asso.echopen.ui.MainActionController;
import com.echopen.asso.echopen.ui.RenderingContextController;

import org.junit.Before;

import java.io.FileNotFoundException;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Test that the right route are held when the user chooses the protocol
 * Then checks the ScanConverter
 */
@SmallTest
public class ProtocolTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private MainActivity mainActivity;

    public ProtocolTest() {
        super(MainActivity.class);
    }

    @Before
    protected void setUp() throws Exception {
        super.setUp();
        mainActivity = getActivity();
    }

    /**
     * Clicking on local checkbox triggers ScanConversion on the data stored in a local CSV file
     */
    public void testFileIsFetchedFromLocal() throws Throwable {
        boolean thrown = false;
        try {
            AssetManager assetManager = mainActivity.getResources().getAssets();
            assetManager.open("data/raw_data/data_phantom.csv");
        } catch (FileNotFoundException e) {
            thrown = true;
            e.printStackTrace();
        }
        assertFalse(thrown);
    }

    public void testFetchData() throws Exception {
        boolean thrown = false;
        BitmapDisplayerFactory bitmapDisplayerFactory = mock(BitmapDisplayerFactory.class);
        BitmapDisplayer bitmapDisplayer= mock(BitmapDisplayer.class);

        doReturn(bitmapDisplayer).when(bitmapDisplayerFactory).populateBitmap(
                any(MainActivity.class), any(MainActionController.class), any(RenderingContextController.class),
                anyString(), anyInt());
        try {
            mainActivity.fetchData(bitmapDisplayerFactory);
        } catch (Exception e) {
            thrown = true;
            e.printStackTrace();
        }
        assertFalse(thrown);
        verify(bitmapDisplayerFactory,times(1)).populateBitmap(any(MainActivity.class), any(MainActionController.class), any(RenderingContextController.class),
                anyString(), anyInt());
    }
}
