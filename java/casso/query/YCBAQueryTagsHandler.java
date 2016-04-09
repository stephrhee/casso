package casso.query;

import android.content.Context;
import android.util.Log;

import casso.http.YCBARequestHandler;
import casso.model.Artwork;
import casso.model.Tag;
import casso.util.XmlUtil;

import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class YCBAQueryTagsHandler implements YCBARequestHandler.Callback {

    public final Integer[] mCurrentPiecesIdArray = {109, 113, 116, 120, 123, 124, 125, 4984, 137, 4910, 4934, 4985, 150, 153, 4986, 166, 167, 168, 169, 173, 174, 4987, 184, 4988, 186, 192, 196, 202, 213, 215, 234, 237, 238, 246, 254, 260, 262, 265, 4936, 273, 278, 283, 285, 292, 298, 306, 307, 308, 311, 4989, 313, 4994, 319, 320, 321, 329, 337, 338, 345, 358, 365, 366, 375, 381, 382, 384, 4997, 4998, 390, 23, 4999, 1448, 1449, 1450, 1451, 1464, 1466, 1468, 1470, 1471, 1472, 1473, 1475, 1476, 1478, 1480, 396, 5000, 5001, 4937, 399, 400, 5002, 5004, 407, 408, 414, 415, 5005, 5006, 421, 5008, 5010, 5011, 423, 5012, 425, 5013, 429, 430, 446, 448, 449, 450, 459, 461, 470, 1483, 471, 474, 477, 479, 484, 5017, 5018, 45, 493, 495, 497, 498, 1485, 504, 1486, 1487, 512, 516, 518, 521, 523, 524, 530, 550, 561, 562, 5023, 563, 566, 567, 568, 5024, 570, 574, 581, 588, 590, 591, 592, 594, 596, 597, 599, 602, 603, 604, 607, 608, 609, 621, 627, 5025, 635, 636, 639, 640, 644, 647, 653, 655, 5026, 658, 659, 666, 667, 668, 676, 679, 683, 690, 703, 706, 707, 715, 719, 720, 727, 732, 101, 748, 5027, 755, 767, 773, 786, 789, 798, 5029, 5030, 810, 811, 816, 824, 825, 830, 834, 5033, 841, 5034, 5036, 865, 866, 870, 872, 873, 874, 885, 888, 889, 893, 896, 898, 900, 904, 906, 907, 910, 920, 923, 924, 928, 929, 933, 939, 941, 944, 4940, 947, 948, 951, 952, 957, 958, 980, 982, 987, 1000, 1001, 1002, 1003, 1004, 1006, 1007, 1008, 1016, 1017, 1018, 63, 1029, 1035, 1040, 1047, 5038, 1072, 1081, 5009, 1082, 5039, 1090, 1095, 1097, 1100, 1113, 68, 1119, 1121, 1122, 1123, 1124, 1126, 5040, 5035, 5041, 1155, 1156, 5042, 1179, 1182, 1183, 1188, 5043, 5044, 5045, 5046, 5047, 1208, 5048, 1211, 1212, 1492, 1225, 1227, 5051, 5052, 1230, 1234, 1245, 1247, 1248, 1252, 1257, 1263, 1266, 1274, 1279, 1281, 1287, 1288, 1289, 1290, 5059, 1371, 1372, 1294, 1297, 1299, 1301, 1302, 1305, 1306, 1511, 1346, 1377, 6257, 1517, 1405, 1406, 1408, 1409, 1415, 1513, 1351, 5064, 1352, 1540, 1541, 1548, 1551, 1551, 1619, 1620, 5726, 6153, 7218, 1570, 10595, 49608, 40720, 40724, 21168, 41226, 21169, 38548, 21173, 42740, 40716, 38723, 38734, 41296, 42329, 42330, 38518, 42114, 42750, 41301, 38536, 38538, 38542, 38716, 38739, 41988, 39110, 39154, 39157, 39173, 39181, 39186, 39187, 39190, 39191, 39192, 39193, 39195, 39273, 39705, 39706, 6127, 51957, 52212, 53420, 53751, 54059, 54060, 55898, 54269, 54270, 54430, 59175, 58556, 60732, 60741, 1584, 61493, 60922, 54265, 54266, 54267, 62228, 62364, 62367, 63916, 63981, 64065, 54284, 54286, 54288, 62280, 66182, 67295, 67297, 67546, 54268, 54306, 68747, 67320, 71519, 67891};
    public final HashMap<String, Integer> mTagToFrequencyHashMap = new HashMap<>();
    public final int mCurrentPiecesCount = mCurrentPiecesIdArray.length;
    public int mFetchedCount = 0;

    private YCBARequestHandler mYCBARequestHandler;
    private Context mContext;

    public YCBAQueryTagsHandler(Context context) {
        mContext = context;
    }

    public void execute() {
        for (int objectId : mCurrentPiecesIdArray) {
            mYCBARequestHandler = new YCBARequestHandler(
                    mContext,
                    Integer.toString(objectId),
                    this);
            mYCBARequestHandler.execute();
        }
    }

    private void updateTagCount(Artwork artwork) {
        List<Tag> tags = artwork.mTags;
        if (tags != null && tags.size() > 0) {
            for (Tag tag : tags) {
                Integer count = mTagToFrequencyHashMap.get(tag.mName);
                count = (count == null) ? 1 : count + 1;
                Tag newTag = new Tag.Builder().fromOld(tag).setCount(count).build();
                mTagToFrequencyHashMap.put(newTag.mName, count);
            }
        }
    }

    private void updateFetchedCount() {
        mFetchedCount += 1;
        if (mFetchedCount == mCurrentPiecesCount) {
            List<String> tags = new ArrayList<>();
            tags.addAll(mTagToFrequencyHashMap.keySet());
            for (String tag : tags) {
                Log.d("YCBAQueryTagsHandler", tag + " " + mTagToFrequencyHashMap.get(tag));
            }
        }
    }

    @Override
    public void onYCBAResponseFetched(YCBARequestHandler.Result result) {
        String xml = result.mString;
        InputStream stream = new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8));
        try {
            Artwork.Builder builder = new Artwork.Builder();
            XmlUtil.parse(stream, builder);
            Artwork artwork = builder.build();
            updateTagCount(artwork);
        } catch (XmlPullParserException | IOException e) {
            Log.d("YCBAQueryTagsHandler", e.toString());
        }
        updateFetchedCount();
    }

    @Override
    public void onYCBARequestFailed() {
        updateFetchedCount();
    }

}
