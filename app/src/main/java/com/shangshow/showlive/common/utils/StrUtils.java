package com.shangshow.showlive.common.utils;

import android.text.TextUtils;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.regex.Pattern;

public class StrUtils {

    /**
     * 判断 字符串是否是空的
     *
     * @param string
     * @return true 空字符串，false 不为空
     */
    public static boolean StringIsEmpty(String string) {
        if (string == null || ("").equals(string.trim())) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 得到字符串的非空指针
     *
     * @param str 要转换的字符串
     * @return
     */
    public static String getString(String str) {
        if (str == null || str.equals("null") || str.equals("")) {
            return "";
        } else {
            return str.trim();
        }
    }

    /**
     * 判断是否是数字
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        if (str == null || "".equals(str.trim())) {
            return false;
        } else {
            Pattern pattern = Pattern.compile("[0-9]*");
            return pattern.matcher(str.trim()).matches();
        }
    }

    /**
     * 取整（四舍五入）
     *
     * @param num 待转换的价格
     * @return 转换后的价格
     */
    public static String formatInt(String num) {
        int xx = Math.round(Float.parseFloat(num));
        return xx + "";
    }

    /**
     * 价格转换
     *
     * @param price 待转换的价格
     * @return 转换后的价格
     */
    public static String formatPrice(long price) {
        DecimalFormat myformat = new DecimalFormat();
        if (price % 100 == 0) {
            myformat.applyPattern("##0");
        } else {
            myformat.applyPattern("##0.00");
        }
        return myformat.format(price / 100.0);
    }

    /**
     * 支付显示金额公用方法
     *
     * @param price
     * @return
     */
    public static String formatPaymentPrice(long price) {
        DecimalFormat myformat = new DecimalFormat();
        myformat.applyPattern("##0.00");
        return myformat.format(price / 100.0);
    }

    /**
     * 将金额格式化为100,000,000.00
     *
     * @param price
     * @return
     */
    public static String formatShowCash(long price) {
        DecimalFormat myformat = new DecimalFormat();
        myformat.applyPattern("##,##0.00");
        return myformat.format(price / 100.0);
    }

    private static DecimalFormat dfs = null;

    public static DecimalFormat getCashformat(String pattern) {
        if (dfs == null) {
            dfs = new DecimalFormat();
        }
        dfs.setRoundingMode(RoundingMode.FLOOR);
        dfs.applyPattern(pattern);
        return dfs;
    }

    /**
     * 距离单位换算
     *
     * @param distance
     * @return
     */
    public static String formatDistanceUnit(float distance) {
        String result = "";
        if (distance > 1000) {
            DecimalFormat fnum = new DecimalFormat("##0.0");
            result = fnum.format(distance / 1000.0) + "公里";

        } else {
            DecimalFormat fnum = new DecimalFormat("##0");
            result = fnum.format(distance) + "米";
        }

        return result;
    }

//    /**
//     * 将价格转换为保留2位小数
//     *
//     * @param price 待转换的价格
//     * @return 转换后的价格
//     */
//    public static String formatCashPrice(long price) {
//        //金额取整
//        return formatInt(price / 100 + "");
//    }

    public static String getPeriod(long times) {
        String period = null;
        switch ((int) times) {
            case 1:
                period = "一　　期";
                break;
            case 2:
                period = "二　　期";
                break;
            case 3:
                period = "三　　期";
                break;
            case 4:
                period = "四　　期";
                break;
            case 5:
                period = "五　　期";
                break;
            case 6:
                period = "六　　期";
                break;
            case 7:
                period = "七　　期";
                break;
            case 8:
                period = "八　　期";
                break;
            case 9:
                period = "九　　期";
                break;
            case 10:
                period = "十　　期";
                break;
            case 11:
                period = "十  一  期";
                break;
            case 12:
                period = "十  二  期";
                break;

            default:
                break;
        }
        return period;
    }

    public static String getBankTailNo(String bankCardNo) {
        if (!StringIsEmpty(bankCardNo) && bankCardNo.length() >= 4) {
            return "尾号" + bankCardNo.substring(bankCardNo.length() - 4, bankCardNo.length());
        } else {
            return "";
        }
    }

    public static String getPhoneNumber(String phoneNum) {
        if (phoneNum.length() == 11) {
            String front = phoneNum.substring(0, 3);
            String behind = phoneNum.substring(7);
            return front + "****" + behind;
        }
        return "";

    }

    public static String combinationBankCardNo(String p6Code, String l4Code) {
        if (!TextUtils.isEmpty(p6Code) && !TextUtils.isEmpty(l4Code)) {
            return p6Code + " ****** " + l4Code;
        } else {
            return "";
        }
    }
}
