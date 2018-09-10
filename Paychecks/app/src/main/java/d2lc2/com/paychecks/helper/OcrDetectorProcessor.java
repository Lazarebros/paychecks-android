package d2lc2.com.paychecks.helper;

import android.util.Log;
import android.util.SparseArray;

import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.text.Text;
import com.google.android.gms.vision.text.TextBlock;

import java.util.ArrayList;
import java.util.List;

public class OcrDetectorProcessor implements Detector.Processor<TextBlock> {

    List<String> lines = new ArrayList<>();

    @Override
    public void release() {
    }

    @Override
    public void receiveDetections(Detector.Detections<TextBlock> detections) {
        lines.clear();
        SparseArray<TextBlock> items = detections.getDetectedItems();
        for (int i = 0; i < items.size(); ++i) {
            TextBlock item = items.valueAt(i);
            lines.add(item.getValue());
        }
    }

    public List<String> getLines() {
        return lines;
    }

}
