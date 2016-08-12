package com.chengsi.weightcalc.utils.search;

import android.util.Log;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;

/**
 * Created by Hook on 2016/2/28.
 */
public class PinyinTransformation {

    private final HanyuPinyinOutputFormat format;

    public PinyinTransformation() {
        format = new HanyuPinyinOutputFormat();
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        format.setVCharType(HanyuPinyinVCharType.WITH_V);
    }

    public ShipName toPinyin(String str) {
        ShipName shipname = new ShipName();

        shipname.setName(str);
        String longname = "";
        String shortname = "";
        for (int i = 0; i < str.length(); i++) {
            try {
                String[] pinyin = PinyinHelper.toHanyuPinyinStringArray(str.charAt(i), format);
                longname = longname + pinyin[0];
                shortname = shortname + pinyin[0].charAt(0);
            } catch (Exception e) {
                shipname.setLongname("");
                shipname.setShortname("");
            }
        }
        shipname.setLongname(longname);
        shipname.setShortname(shortname);
        Log.i("TAG", shipname.toString());
        return shipname;
    }

}
