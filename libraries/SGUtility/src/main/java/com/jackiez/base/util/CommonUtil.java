package com.jackiez.base.util;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 通用操作工具
 * Created by zsigui on 15-8-17.
 */
public final class CommonUtil {

    private static final String PRE_TAG = CommonUtil.class.getName();
    public static final String DEFAULT_SYS_CHARSET = "UTF-8";

    /**
     * 判断给定对象是否为空
     *
     * @param obj
     * @return
     */
    public static boolean isEmpty(Object obj) {
        boolean result = obj == null;
        if (!result) {
            if (obj instanceof CharSequence) {
                result = ((String) obj).isEmpty();
            } else if (obj instanceof Map) {
                result = ((Map) obj).isEmpty();
            } else if (obj instanceof List) {
                result = ((List) obj).isEmpty();
            } else if (obj instanceof Set) {
                result = ((Set) obj).isEmpty();
            }
        }
        return result;
    }

    /**
     * 将字节数组转换成十六进制字符串
     *
     * @param bs
     * @return
     */
    public static String bytesToHex(byte... bs) {
        if (isEmpty(bs)) {
            throw new IllegalArgumentException(PRE_TAG + ".bytesToHex : param bs(byte...) is null");
        }
        StringBuilder builder = new StringBuilder();
        for (byte b : bs) {
            int bt = b & 0xff;
            if (bt < 16) {
                builder.append(0);
            }
            builder.append(Integer.toHexString(bt));
        }
        return builder.toString();
    }

    /**
     * 将十六进制字符串转换成字节数组
     *
     * @param hex
     * @return
     */
    public static byte[] hexToBytes(String hex) {
        if (isEmpty(hex)) {
            throw new IllegalArgumentException(PRE_TAG + ".hexToBytes : param hex(String...) is null");
        }
        if (hex.length() % 2 == 1)
            hex += '0';
        byte[] result = new byte[hex.length() / 2];
        char[] cs = hex.toLowerCase(Locale.CHINA).toCharArray();
        for (int i = 0; i < result.length; i++) {
            int pos = i * 2;
            result[i] = (byte) (charToByte(cs[pos]) << 4 | charToByte(cs[pos + 1]));
        }
        return result;
    }

    /**
     * 将字节数组转为十六进制表示的MAC地址
     *
     * @param bs
     * @return
     */
    public static String byteToMacStr(byte[] bs) {
        if (isEmpty(bs)) {
            throw new IllegalArgumentException(PRE_TAG + ".byteToMacStr : param bs(byte[]) is null");
        }
        StringBuilder sb = new StringBuilder(bs.length);
        for (byte b : bs) {
            sb.append(":");
            sb.append(bytesToHex(b));
        }
        sb.deleteCharAt(0);
        return sb.toString();
    }

    /**
     * 将整形转换为点十六进制表示法的IP地址
     *
     * @param info
     * @return
     */
    public static String intToIpStr(int info) {
        return (info & 0xFF) + "." + (info >> 8 & 0xFF) + "." + (info >> 16 & 0xFF) + "." + (info >> 24 & 0xFF);
    }

    /**
     * 将字符转换为字节数
     *
     * @param c
     * @return
     */
    public static byte charToByte(char c) {
        return (byte) "0123456789abcdefg".indexOf(c);
    }

    /**
     * 将字节数组转换为指定格式编码的字符串
     */
    public static String bytesToStr(byte[] data, String charset) {
        String result = null;
        try {
            result = new String(data, charset);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 将字节数组转换为系统默认格式编码的字符串
     */
    public static String bytesToStr(byte[] data) {
        return bytesToStr(data, DEFAULT_SYS_CHARSET);
    }

    public static void copy(Object src, int srcPos, Object dest, int destPos, int length) {
        System.arraycopy(src, srcPos, dest, destPos, length);
    }

    /**
     * 将字节数组转换为非乱码字符串(会自动检测乱码并使用常用编码进行解码，故效率会比较低)
     */
    public static String bytesToNoMessyStr(byte[] data) {
        String result = null;
        String[] charsets = new String[]{"GB2312", "UTF-8", "BIG5", "GBK", "Unicode"};
        try {
            for (String charset : charsets) {
                String tmp = new String(data, charset);
                if (!isMessyCode(tmp)) {
                    result = tmp;
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 判断字符是否是中文
     */
    public static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return false;
    }

    /**
     * 判断字符串是否是乱码
     */
    public static boolean isMessyCode(String strName) {
        Pattern p = Pattern.compile("\\s*|t*|r*|n*");
        Matcher m = p.matcher(strName);
        String after = m.replaceAll("");
        String temp = after.replaceAll("\\p{P}", "");
        char[] ch = temp.trim().toCharArray();
        float chLength = ch.length;
        float count = 0;
        for (int i = 0; i < ch.length; i++) {
            char c = ch[i];
            if (!Character.isLetterOrDigit(c)) {
                if (!isChinese(c)) {
                    count = count + 1;
                }
            }
        }
        float result = count / chLength;
        if (result > 0.4) {
            return true;
        } else {
            return false;
        }

    }
}
