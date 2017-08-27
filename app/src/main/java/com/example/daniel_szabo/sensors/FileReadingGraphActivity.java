package com.example.daniel_szabo.sensors;

import com.example.daniel_szabo.sensors.parcelable.ParcelableSample;
import com.example.daniel_szabo.sensors.util.LargeDataTransferUtil;
import com.example.daniel_szabo.sensors.util.ToastUtil;

import java.io.IOException;
import java.util.List;

public class FileReadingGraphActivity extends GraphActivity {
    public static final String RECORDED_DATA_FILE_NAME_INTENT_KEY = "recordedDataFileNameIntetnKey";

    @Override
    protected List<ParcelableSample> loadRawData() {
        return loadDataFromFile();
    }

    private List<ParcelableSample> loadDataFromFile() {
        try {
            return LargeDataTransferUtil.readDataFromTempFile(getApplicationContext(), RECORDED_DATA_FILE_NAME_INTENT_KEY);
        } catch (IOException e) {
            ToastUtil.toastShortMessage(getApplicationContext(), "Could NOT load data!");
        }
        return null;
    }
}
