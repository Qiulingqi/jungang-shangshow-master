package com.shangshow.showlive.common.utils;

import android.content.Context;
import android.text.TextUtils;

import com.shangshow.showlive.R;

import java.util.regex.Pattern;

public class VerifyUtils {


    /**
     * 正则校验
     *
     * @param
     * @param str 需要校验的字符串
     * @return 验证通过返回true
     */
    public static boolean validation(String pattern, String str) {
        if (str == null)
            return false;
        return Pattern.compile(pattern).matcher(str).matches();
    }

    /**
     * 验证手机号码是否正确，手机号长度不足或未以1开头
     */
    public static boolean checkMobileNO(Context context, String mobileNo) {
        boolean flag = false;
        if (TextUtils.isEmpty(mobileNo)) {
            ToastUtils.show(context.getString(R.string.mobile_is_not_null));

            return flag;
        }
        try {
            flag = validation("^[1][3,4,5,7,8][0-9]{9}$", mobileNo);
        } catch (Exception e) {
            flag = false;
        }
        if (!flag) {
            ToastUtils.show(context.getString(R.string.mobile_is_wrong));
        }
        return flag;
    }

    /**
     * 验证密码：数字+大小写字母，6-15位
     */
    public static boolean checkPassword(Context context, String pwd) {
        boolean flag = false;
        if (TextUtils.isEmpty(pwd)) {
            ToastUtils.show(context.getString(R.string.password_is_not_null));
            return flag;
        }
        try {
            flag = validation("^[a-z0-9_-]{6,15}$", pwd);
        } catch (Exception e) {
            flag = false;
        }
        if (!flag) {
            ToastUtils.show(context.getString(R.string.password_is_wrong));
        }

        return flag;
    }

    /**
     * 检查两次输入的密码相同
     */
    public static boolean checkTwicePasswordIsEqual(Context context, String pwd1, String pwd2) {
        boolean flag = false;
        if (TextUtils.isEmpty(pwd1) || TextUtils.isEmpty(pwd2)) {
            ToastUtils.show(context.getString(R.string.password_is_not_null));
            return flag;
        }
        if (!pwd1.equals(pwd2)) {
            ToastUtils.show(context.getString(R.string.twice_password_is_not_equals));
            flag = false;
        } else {
            flag = true;
        }
        return flag;
    }

    /**
     * 验证短信验证码是否输入正确
     */
    public static boolean checkSmsCodeLength(Context context, String smsCode) {
        boolean flag = false;
        if (TextUtils.isEmpty(smsCode)) {
            ToastUtils.show(context.getString(R.string.smscode_is_not_null));
            return flag;
        }
        try {
            flag = validation("^[0-9_-]{4}$", smsCode);
        } catch (Exception e) {
            flag = false;
        }

        if (!flag) {
            ToastUtils.show(context.getString(R.string.smscode_length_is_wrong));
        }
        return flag;
    }


    /**
     * 验证用户昵称
     */
    public static boolean checkNickName(Context context, String nickName) {
        boolean flag = false;
        if (TextUtils.isEmpty(nickName)) {
            ToastUtils.show(context.getString(R.string.nickname_is_not_null));
            return flag;
        }
        try {
            flag = validation("^[a-zA-Z0-9\\u4e00-\\u9fa5]{1,12}$", nickName);
        } catch (Exception e) {
            flag = false;
        }

        if (!flag) {
            ToastUtils.show(context.getString(R.string.nickname_is_wrong));
        }
        return flag;
    }


//    /**
//     * 验证中文是否正确，若不正确返回错误提示,若正确则返回 CORRECT 验证规则：text不合法，或者长度过长，过短
//     */
//    public static String checkChinese(String text, int minLength, int maxLength) {
//        if (null == text || "".equals(text)) {
//            return NULL;
//        }
////        text = text.replace(" ","");
//        text = text.trim();
//        boolean flag = false;
//        String checkResult;
//        try {
//            Pattern p = Pattern
//                    .compile("^([一-龥]*)$");
//            Matcher m = p.matcher(text);
//            flag = m.matches();
//        } catch (Exception e) {
//            LogUtil.e(TAG, e.getMessage());
//            flag = false;
//        }
//
//        if (flag) {
//            int length = text.length();
//            if (length < minLength)
//                checkResult = CHINESE_SHORT;
//            else if (length > maxLength)
//                checkResult = CHINESE_LONG;
//            else
//                checkResult = CORRECT;
//        } else {
//            checkResult = CHINESE_ERROR;
//        }
//
//        return checkResult;
//    }
//
//    /**
//     * 验证中文+英文是否正确，若不正确返回错误提示,若正确则返回 CORRECT 验证规则：text不合法，或者长度过长，过短
//     */
//    public static String checkChineseAndEnglish(String text, int minLength, int maxLength) {
//        if (null == text || "".equals(text)) {
//            return NULL;
//        }
//        text = text.replace(" ", "");
//        boolean flag = false;
//        String checkResult;
//        try {
//            Pattern p = Pattern
//                    .compile("^([a-zA-Z一-龥]*)$");  //|([一-龥]
//            Matcher m = p.matcher(text);
//            flag = m.matches();
//        } catch (Exception e) {
//            LogUtil.e(TAG, e.getMessage());
//            flag = false;
//        }
//
//        if (flag) {
//            int length = text.length();
//            if (length < minLength)
//                checkResult = CHINESEANDENGLISH_SHORT;
//            else if (length > maxLength)
//                checkResult = CHINESEANDENGLISH_LONG;
//            else
//                checkResult = CORRECT;
//        } else {
//            checkResult = CHINESEANDENGLISH_ERROR;
//        }
//
//        return checkResult;
//    }
//
//    /**
//     * 验证邮箱是否正确，若不正确返回错误提示,若正确则返回 CORRECT 验证规则：正确邮箱格式
//     */
//    public static String checkEmail(String email) {
//        if (null == email || "".equals(email)) {
//            return NULL;
//        }
//        boolean flag = false;
//        String checkResult;
//        try {
//            Pattern p = Pattern
//                    .compile("^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\\.[a-zA-Z0-9_-]{2,3}){1,2})$");
//            Matcher m = p.matcher(email);
//            flag = m.matches();
//        } catch (Exception e) {
//            flag = false;
//        }
//
//        if (flag) {
//            checkResult = CORRECT;
//        } else {
//            checkResult = EMAIL_ERROR;
//        }
//        return checkResult;
//    }
//
//
//    /**
//     * 验证单位为年的年龄是否正确，若不正确返回错误提示,若正确则返回 CORRECT
//     */
//    public static String checkYearAge(String age, int minAge, int maxAge) {
//        if (null == age || "".equals(age)) {
//            return NULL;
//        }
//        boolean flag = false;
//        try {
//            Pattern p = Pattern
//                    .compile("^([1-9]+[0-9]{0,2})$");
//            Matcher m = p.matcher(age);
//            flag = m.matches();
//        } catch (Exception e) {
//            flag = false;
//        }
//        if (flag) {
//            int intValue = new Integer(age).intValue();
//            if (intValue < minAge)
//                return AGE_YOUNG;
//            else if (intValue > maxAge)
//                return AGE_OLD;
//            else
//                return CORRECT;
//        } else {
//            return AGE_ERROR;
//        }
//    }
//
//
//    /**
//     * 验证单位为月的年龄是否正确，若不正确返回错误提示,若正确则返回 CORRECT
//     */
//    public static String checkMonthAge(String age, int minAge, int maxAge) {
//        if (null == age || "".equals(age)) {
//            return NULL;
//        }
//        boolean flag = false;
//        try {
//            Pattern p = Pattern
//                    .compile("^([1-9]+[0-9]{0,2})$");
//            Matcher m = p.matcher(age);
//            flag = m.matches();
//        } catch (Exception e) {
//            flag = false;
//        }
//        if (flag) {
//            int intValue = new Integer(age).intValue();
//            if (intValue < minAge)
//                return AGE_YOUNG;
//            else if (intValue > maxAge)
//                return AGE_OLD;
//            else
//                return CORRECT;
//        } else {
//            return AGE_ERROR;
//        }
//    }
//
//    /**
//     * 验证输入文本长度，若不正确返回错误提示,若正确则返回 CORRECT 字符数大于等于min，小于等于max
//     */
//    public static String checkTextLenth(String text, int minLength, int maxLength) {
//        if (null == text || "".equals(text)) {
//            return NULL;
//        }
//        int length = text.length();
//        if (length < minLength)
//            return TEXT_SHORT;
//        else if (length > maxLength)
//            return TEXT_LONG;
//        else
//            return CORRECT;
//    }
//
//    /**
//     * 验证输入整数数字大小，若不正确返回错误提示,若正确则返回 CORRECT 整数大于等于min，小于等于max
//     */
//    public static String checkIntLenth(String num, int min, int max) {
//        if (null == num || "".equals(num)) {
//            return NULL;
//        }
//        boolean flag = false;
//        String checkResult;
//        try {
//            Pattern p = Pattern
//                    .compile("^[0-9]*$");
//            Matcher m = p.matcher(num);
//            flag = m.matches();
//        } catch (Exception e) {
//            flag = false;
//        }
//
//        if (flag) {
//            int intValue = new Integer(num);
//            if (intValue < min)
//                checkResult = NUM_MIN;
//            else if (intValue > max)
//                checkResult = NUM_MAX;
//            else
//                checkResult = CORRECT;
//        } else {
//            checkResult = NUM_ERROR;
//        }
//
//        return checkResult;
//    }
//
//    /**
//     * 验证输入的金额，若不正确返回错误提示，若正确返回 CORRECT
//     */
//    public static String checkAmount(String amount) {
//        if (null == amount || "".equals(amount)) {
//            return NULL;
//        }
//        boolean flag = false;
//        String checkResult;
//        try {
//            Pattern p = Pattern
//                    .compile("^(([0-9]+)|([0-9]+.[0-9]{1,2}))$");
//            Matcher m = p.matcher(amount);
//            flag = m.matches();
//        } catch (Exception e) {
//            flag = false;
//        }
//        if (flag) {
//            checkResult = CORRECT;
//        } else {
//            checkResult = AMOUNT_ERROR;
//        }
//        return checkResult;
//    }
}