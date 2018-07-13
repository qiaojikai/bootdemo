package com.example.common.utils.string;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.net.URLCodec;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringEscapeUtils;


public class StringUtil {
	public static final byte[] BOM = { -17, -69, -65 };

	public static final char[] HexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	public static final Pattern PTitle = Pattern.compile("<title>(.+?)</title>", 34);

	public static Pattern patternHtmlTag = Pattern.compile("<[^<>]+>", 32);

	public static final Pattern PLetterOrDigit = Pattern.compile("^\\w*$", 34);

	public static final Pattern PLetter = Pattern.compile("^[A-Za-z]*$", 34);

	public static final Pattern PDigit = Pattern.compile("^\\d*$", 34);

	private static Pattern chinesePattern = Pattern.compile("[^一-龥]+", 34);

	private static Pattern idPattern = Pattern.compile("[\\w\\s\\_\\.\\,]*", 34);

	public static byte[] md5(String src) {
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			byte[] md = md5.digest(src.getBytes());
			return md;
		} catch (Exception e) {
		}
		return null;
	}

	public static byte[] md5(byte[] src) {
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			byte[] md = md5.digest(src);
			return md;
		} catch (Exception e) {
		}
		return null;
	}

	public static String md5Hex(String src) {
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			byte[] md = md5.digest(src.getBytes());
			return hexEncode(md);
		} catch (Exception e) {
		}
		return null;
	}

	public static String md5Base64(String str) {
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			return base64Encode(md5.digest(str.getBytes()));
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String md5Base64FromHex(String md5str) {
		char[] cs = md5str.toCharArray();
		byte[] bs = new byte[16];
		for (int i = 0; i < bs.length; i++) {
			char c1 = cs[(i * 2)];
			char c2 = cs[(i * 2 + 1)];
			byte m1 = 0;
			byte m2 = 0;
			for (byte k = 0; k < 16; k = (byte) (k + 1)) {
				if (HexDigits[k] == c1) {
					m1 = k;
				}
				if (HexDigits[k] == c2) {
					m2 = k;
				}
			}
			bs[i] = (byte) (m1 << 4 | 0 + m2);
		}

		String newstr = base64Encode(bs);
		return newstr;
	}

	public static String md5HexFromBase64(String base64str) {
		return hexEncode(base64Decode(base64str));
	}

	public static String hexEncode(byte[] bs) {
		return new String(new Hex().encode(bs));
	}

	public static byte[] hexDecode(String str) {
		try {
			if (str.endsWith("\n")) {
				str = str.substring(0, str.length() - 1);
			}
			char[] cs = str.toCharArray();
			return Hex.decodeHex(cs);
		} catch (DecoderException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String byteToBin(byte[] bs) {
		char[] cs = new char[bs.length * 9];
		for (int i = 0; i < bs.length; i++) {
			byte b = bs[i];
			int j = i * 9;
			cs[j] = ((b >>> 7 & 0x1) == 1 ? 49 : '0');
			cs[(j + 1)] = ((b >>> 6 & 0x1) == 1 ? 49 : '0');
			cs[(j + 2)] = ((b >>> 5 & 0x1) == 1 ? 49 : '0');
			cs[(j + 3)] = ((b >>> 4 & 0x1) == 1 ? 49 : '0');
			cs[(j + 4)] = ((b >>> 3 & 0x1) == 1 ? 49 : '0');
			cs[(j + 5)] = ((b >>> 2 & 0x1) == 1 ? 49 : '0');
			cs[(j + 6)] = ((b >>> 1 & 0x1) == 1 ? 49 : '0');
			cs[(j + 7)] = ((b & 0x1) == 1 ? 49 : '0');
			cs[(j + 8)] = ',';
		}
		return new String(cs);
	}

	public static String byteArrayToHexString(byte[] b) {
		StringBuffer resultSb = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			resultSb.append(byteToHexString(b[i]));
			resultSb.append(" ");
		}
		return resultSb.toString();
	}

	private static String byteToHexString(byte b) {
		int n = b;
		if (n < 0) {
			n += 256;
		}
		int d1 = n / 16;
		int d2 = n % 16;
		return String.valueOf(HexDigits[d1] + HexDigits[d2]);
	}

	public static boolean isUTF8(byte[] bs) {
		if (hexEncode(ArrayUtils.subarray(bs, 0, 3)).equals("efbbbf")) {
			return true;
		}
		int lLen = bs.length;
		for (int i = 0; i < lLen;) {
			byte b = bs[(i++)];
			if (b >= 0) {
				continue;
			}
			if ((b < -64) || (b > -3)) {
				return false;
			}
			int c = b > -32 ? 2 : b > -16 ? 3 : b > -8 ? 4 : b > -4 ? 5 : 1;
			if (i + c > lLen) {
				return false;
			}
			for (int j = 0; j < c; i++) {
				if (bs[i] >= -64)
					return false;
				j++;
			}

		}
		return true;
	}

	/**
	 * base64Decode String转换成byte[]
	 * 
	 * @param encodeStr
	 * @return
	 */
	public static byte[] base64Decode(String decodeStr) {
		byte[] b = decodeStr.getBytes();
		Base64 base64 = new Base64();
		b = base64.decodeBase64(b);
		return b;
	}

	/**
	 * base64Encode byte[]转换成String
	 * 
	 * @param b
	 * @return
	 */
	public static String base64Encode(byte[] b) {
		Base64 base64 = new Base64();
		b = base64.encode(b);
		String s = new String(b);
		return s;
	}

	public static String javaEncode(String txt) {
		if ((txt == null) || (txt.length() == 0)) {
			return txt;
		}
		txt = replaceEx(txt, "\\", "\\\\");
		txt = replaceEx(txt, "\r\n", "\n");
		txt = replaceEx(txt, "\r", "\\r");
		txt = replaceEx(txt, "\n", "\\n");
		txt = replaceEx(txt, "\"", "\\\"");
		txt = replaceEx(txt, "'", "\\'");
		return txt;
	}

	public static String javaDecode(String txt) {
		if ((txt == null) || (txt.length() == 0)) {
			return txt;
		}
		txt = replaceEx(txt, "\\\\", "\\");
		txt = replaceEx(txt, "\\n", "\n");
		txt = replaceEx(txt, "\\r", "\r");
		txt = replaceEx(txt, "\\\"", "\"");
		txt = replaceEx(txt, "\\'", "'");
		return txt;
	}

	public static String[] splitEx(String str, String spilter) {
		if (str == null) {
			return null;
		}
		if ((spilter == null) || (spilter.equals("")) || (str.length() < spilter.length())) {
			String[] t = { str };
			return t;
		}
		ArrayList al = new ArrayList();
		char[] cs = str.toCharArray();
		char[] ss = spilter.toCharArray();
		int length = spilter.length();
		int lastIndex = 0;
		for (int i = 0; i <= str.length() - length;) {
			boolean notSuit = false;
			for (int j = 0; j < length; j++) {
				if (cs[(i + j)] != ss[j]) {
					notSuit = true;
					break;
				}
			}
			if (!notSuit) {
				al.add(str.substring(lastIndex, i));
				i += length;
				lastIndex = i;
			} else {
				i++;
			}
		}
		if (lastIndex <= str.length()) {
			al.add(str.substring(lastIndex, str.length()));
		}
		String[] t = new String[al.size()];
		for (int i = 0; i < al.size(); i++) {
			t[i] = ((String) al.get(i));
		}
		return t;
	}

	public static String replaceEx(String str, String subStr, String reStr) {
		if (str == null) {
			return null;
		}
		if ((subStr == null) || (subStr.equals("")) || (subStr.length() > str.length()) || (reStr == null)) {
			return str;
		}
		StringBuffer sb = new StringBuffer();
		int lastIndex = 0;
		while (true) {
			int index = str.indexOf(subStr, lastIndex);
			if (index < 0) {
				break;
			}
			sb.append(str.substring(lastIndex, index));
			sb.append(reStr);

			lastIndex = index + subStr.length();
		}
		sb.append(str.substring(lastIndex));
		return sb.toString();
	}

	public static String replaceAllIgnoreCase(String source, String oldstring, String newstring) {
		Pattern p = Pattern.compile(oldstring, 34);
		Matcher m = p.matcher(source);
		return m.replaceAll(newstring);
	}

	/**
	 * UTF-8格式urlEncode
	 * 
	 * @param str
	 * @return
	 */
	public static String urlEncode(String str) {
		return urlEncode(str, "UTF-8");
	}

	/**
	 * UTF-8格式urlDecode
	 * 
	 * @param str
	 * @return
	 */
	public static String urlDecode(String str) {
		return urlDecode(str, "UTF-8");
	}

	public static String urlEncode(String str, String charset) {
		try {
			return new URLCodec().encode(str, charset);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String urlDecode(String str, String charset) {
		try {
			return new URLCodec().decode(str, charset);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String htmlEncode(String txt) {
		return StringEscapeUtils.escapeHtml3(txt);
	}

	public static String htmlDecode(String txt) {
		txt = replaceEx(txt, "&#8226;", "·");
		return StringEscapeUtils.unescapeHtml3(txt);
	}

	public static String quotEncode(String txt) {
		if ((txt == null) || (txt.length() == 0)) {
			return txt;
		}
		txt = replaceEx(txt, "&", "&amp;");
		txt = replaceEx(txt, "\"", "&quot;");
		return txt;
	}

	public static String quotDecode(String txt) {
		if ((txt == null) || (txt.length() == 0)) {
			return txt;
		}
		txt = replaceEx(txt, "&quot;", "\"");
		txt = replaceEx(txt, "&amp;", "&");
		return txt;
	}

	public static String escape(String src) {
		StringBuffer sb = new StringBuffer();
		sb.ensureCapacity(src.length() * 6);
		for (int i = 0; i < src.length(); i++) {
			char j = src.charAt(i);
			if ((Character.isDigit(j)) || (Character.isLowerCase(j)) || (Character.isUpperCase(j))) {
				sb.append(j);
			} else if (j < 'ā') {
				sb.append("%");
				if (j < '\020') {
					sb.append("0");
				}
				sb.append(Integer.toString(j, 16));
			} else {
				sb.append("%u");
				sb.append(Integer.toString(j, 16));
			}
		}
		return sb.toString();
	}

	public static String unescape(String src) {
		StringBuffer sb = new StringBuffer();
		sb.ensureCapacity(src.length());
		int lastPos = 0;
		int pos = 0;

		while (lastPos < src.length()) {
			pos = src.indexOf("%", lastPos);
			if (pos == lastPos) {
				if (src.charAt(pos + 1) == 'u') {
					char ch = (char) Integer.parseInt(src.substring(pos + 2, pos + 6), 16);
					sb.append(ch);
					lastPos = pos + 6;
				} else {
					char ch = (char) Integer.parseInt(src.substring(pos + 1, pos + 3), 16);
					sb.append(ch);
					lastPos = pos + 3;
				}
			} else if (pos == -1) {
				sb.append(src.substring(lastPos));
				lastPos = src.length();
			} else {
				sb.append(src.substring(lastPos, pos));
				lastPos = pos;
			}
		}

		return sb.toString();
	}

	public static String leftPad(String srcString, char c, int length) {
		if (srcString == null) {
			srcString = "";
		}
		int tLen = srcString.length();

		if (tLen >= length)
			return srcString;
		int iMax = length - tLen;
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < iMax; i++) {
			sb.append(c);
		}
		sb.append(srcString);
		return sb.toString();
	}

	public static String subString(String src, int length) {
		if (src == null) {
			return null;
		}
		int i = src.length();
		if (i > length) {
			return src.substring(0, length);
		}
		return src;
	}

	public static String subStringEx(String src, int length) {
		length *= 2;
		if (src == null) {
			return null;
		}
		int k = lengthEx(src);
		if (k > length) {
			int m = 0;
			boolean unixFlag = false;
			String osname = System.getProperty("os.name").toLowerCase();
			if ((osname.indexOf("sunos") > 0) || (osname.indexOf("solaris") > 0) || (osname.indexOf("aix") > 0))
				unixFlag = true;
			try {
				byte[] b = src.getBytes("Unicode");
				for (int i = 2; i < b.length; i += 2) {
					byte flag = b[(i + 1)];
					if (unixFlag) {
						flag = b[i];
					}
					if (flag == 0)
						m++;
					else {
						m += 2;
					}
					if (m > length)
						return src.substring(0, (i - 2) / 2);
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				throw new RuntimeException("执行方法getBytes(\"Unicode\")时出错！");
			}
		}
		return src;
	}

	public static int lengthEx(String src) {
		int length = 0;
		boolean unixFlag = false;
		String osname = System.getProperty("os.name").toLowerCase();
		if ((osname.indexOf("sunos") > 0) || (osname.indexOf("solaris") > 0) || (osname.indexOf("aix") > 0))
			unixFlag = true;
		try {
			byte[] b = src.getBytes("Unicode");
			for (int i = 2; i < b.length; i += 2) {
				byte flag = b[(i + 1)];
				if (unixFlag) {
					flag = b[i];
				}
				if (flag == 0)
					length++;
				else
					length += 2;
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throw new RuntimeException("执行方法getBytes(\"Unicode\")时出错！");
		}
		return length;
	}

	public static String rightPad(String srcString, char c, int length) {
		if (srcString == null) {
			srcString = "";
		}
		int tLen = srcString.length();

		if (tLen >= length)
			return srcString;
		int iMax = length - tLen;
		StringBuffer sb = new StringBuffer();
		sb.append(srcString);
		for (int i = 0; i < iMax; i++) {
			sb.append(c);
		}
		return sb.toString();
	}

	public static String rightTrim(String src) {
		if (src != null) {
			char[] chars = src.toCharArray();
			for (int i = chars.length - 1; i > 0; i--) {
				if ((chars[i] != ' ') && (chars[i] != '\t')) {
					return new String(ArrayUtils.subarray(chars, 0, i + 1));
				}
			}
		}
		return src;
	}

	public static String toSBC(String input) {
		char[] c = input.toCharArray();
		for (int i = 0; i < c.length; i++)
			if (c[i] == ' ') {
				c[i] = '　';
			} else {
				if (((c[i] > '@') && (c[i] < '[')) || ((c[i] > '`') && (c[i] < '{'))) {
					continue;
				}
				if (c[i] < '')
					c[i] = (char) (c[i] + 65248);
			}
		return new String(c);
	}

	public static String toNSBC(String input) {
		char[] c = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == ' ') {
				c[i] = '　';
			} else if (c[i] < '')
				c[i] = (char) (c[i] + 65248);
		}
		return new String(c);
	}

	public static String toDBC(String input) {
		char[] c = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == '　') {
				c[i] = ' ';
			} else if ((c[i] > 65280) && (c[i] < 65375))
				c[i] = (char) (c[i] - 65248);
		}
		return new String(c);
	}

	public static String getHtmlTitle(String html) {
		Matcher m = PTitle.matcher(html);
		if (m.find()) {
			return m.group(1).trim();
		}
		return null;
	}

	public static String clearHtmlTag(String html) {
		String text = patternHtmlTag.matcher(html).replaceAll("");
		if (isEmpty(text)) {
			return "";
		}
		text = htmlDecode(html);
		return text.replaceAll("[\\s　]{2,}", " ");
	}

	public static String capitalize(String str) {
		int strLen;
		if ((str == null) || ((strLen = str.length()) == 0))
			return str;
		// int strLen;
		return strLen + Character.toTitleCase(str.charAt(0)) + str.substring(1);
	}

	/**
	 * 判断String为空
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str) {
		if (str != null && !str.equals("")) {
			return false;
		}
		return true;
	}

	/**
	 * 判断String不为空
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNotEmpty(String str) {
		return !isEmpty(str);
	}

	public static final String noNull(String string, String defaultString) {
		return isEmpty(string) ? defaultString : string;
	}

	public static final String noNull(String string) {
		return noNull(string, "");
	}

	public static String join(Object[] arr) {
		return join(arr, ",");
	}

	public static String join(Object[][] arr) {
		return join(arr, "\n", ",");
	}

	public static String join(Object[] arr, String spliter) {
		if (arr == null) {
			return null;
		}
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < arr.length; i++) {
			if (i != 0) {
				sb.append(spliter);
			}
			sb.append(arr[i]);
		}
		return sb.toString();
	}

	public static String join(Object[][] arr, String spliter1, String spliter2) {
		if (arr == null) {
			return null;
		}
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < arr.length; i++) {
			if (i != 0) {
				sb.append(spliter2);
			}
			sb.append(join(arr[i], spliter2));
		}
		return sb.toString();
	}

	public static String join(List list) {
		return join(list, ",");
	}

	public static String join(List list, String spliter) {
		if (list == null) {
			return null;
		}
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < list.size(); i++) {
			if (i != 0) {
				sb.append(spliter);
			}
			sb.append(list.get(i));
		}
		return sb.toString();
	}

	public static int count(String str, String findStr) {
		int lastIndex = 0;
		int length = findStr.length();
		int count = 0;
		int start = 0;
		while ((start = str.indexOf(findStr, lastIndex)) >= 0) {
			lastIndex = start + length;
			count++;
		}
		return count;
	}

	public static boolean isLetterOrDigit(String str) {
		return PLetterOrDigit.matcher(str).find();
	}

	public static boolean isLetter(String str) {
		return PLetter.matcher(str).find();
	}

	public static boolean isDigit(String str) {
		if (isEmpty(str)) {
			return false;
		}
		return PDigit.matcher(str).find();
	}

	public static boolean containsChinese(String str) {
		return !chinesePattern.matcher(str).matches();
	}

	public static boolean checkID(String str) {
		if (isEmpty(str)) {
			return true;
		}

		return idPattern.matcher(str).matches();
	}

	public static Map splitToMapx(String str, String entrySpliter, String keySpliter) {
		Map map = new HashMap();
		String[] arr = splitEx(str, entrySpliter);
		for (int i = 0; i < arr.length; i++) {
			String[] arr2 = splitEx(arr[i], keySpliter);
			String key = arr2[0];
			if (isEmpty(key)) {
				continue;
			}
			key = key.trim();
			String value = null;
			if (arr2.length > 1) {
				value = arr2[1];
			}
			map.put(key, value);
		}
		return map;
	}

	public static String getURLExtName(String url) {
		if (isEmpty(url)) {
			return null;
		}
		int index1 = url.indexOf('?');
		if (index1 == -1) {
			index1 = url.length();
		}
		int index2 = url.lastIndexOf('.', index1);
		if (index2 == -1) {
			return null;
		}
		int index3 = url.indexOf('/', 8);
		if (index3 == -1) {
			return null;
		}
		String ext = url.substring(index2 + 1, index1);
		if (ext.matches("[^\\/\\\\]*")) {
			return ext;
		}
		return null;
	}

	public static String getURLFileName(String url) {
		if (isEmpty(url)) {
			return null;
		}
		int index1 = url.indexOf('?');
		if (index1 == -1) {
			index1 = url.length();
		}
		int index2 = url.lastIndexOf('/', index1);
		if ((index2 == -1) || (index2 < 8)) {
			return null;
		}
		String ext = url.substring(index2 + 1, index1);
		return ext;
	}

	public static String clearForXML(String str) {
		char[] cs = str.toCharArray();
		char[] ncs = new char[cs.length];
		int j = 0;
		for (int i = 0; i < cs.length; i++) {
			if (cs[i] > 65533)
				continue;
			if (cs[i] < ' ')
				if (((cs[i] != '\t' ? 1 : 0) & (cs[i] != '\n' ? 1 : 0) & (cs[i] != '\r' ? 1 : 0)) != 0) {
					continue;
				}
			ncs[(j++)] = cs[i];
		}
		ncs = ArrayUtils.subarray(ncs, 0, j);
		return new String(ncs);
	}

	/**
	 * 初始化当前日期
	 * 
	 * @return String类型 格式yyyy-MM-dd HH:mm:ss
	 */
	public static String getsysDATEyyyyMMddHHmmss() {
		Date date = new Date();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String str = df.format(date);
		return str;
	}

	/**
	 * 初始化当前日期延迟2秒
	 * 
	 * @return String类型 格式yyyy-MM-dd HH:mm:ss
	 */
	public static String getsysDATEyyyyMMddHHmmssaddTwoSecond() {
		Date date = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.SECOND, 2);
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String str = df.format(calendar.getTime());
		return str;
	}

	/**
	 * 获取当前日期
	 * 
	 * @return String类型 yyyy-MM-dd格式
	 */
	public static String getsysDATEyyyyMMdd() {
		Date date = new Date();
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String str = df.format(date);
		return str;
	}

	/**
	 * Date 转String 返回yyyy-MM-dd HH:mm:ss
	 * 
	 * @param date
	 * @return
	 */
	public static String convertDATEyyyyMMddHHmmss(Date date) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String str = df.format(date);
		return str;
	}

	/**
	 * Date 转String 返回yyyy-MM-dd格式
	 * 
	 * @param date
	 * @return
	 */
	public static String convertDATEyyyyMMdd(Date date) {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String str = df.format(date);
		return str;
	}

	/**
	 * 转换日期string类型
	 * 
	 * @param date
	 * @return 返回yyyy-MM-dd HH:mm:ss
	 */
	public static String convertDATEyyyyMMddHHmmss(String date) {
		if (StringUtil.isEmpty(date)) {
			return "";
		}
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d = null;
		try {
			d = df.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return "";
		}
		String str = convertDATEyyyyMMddHHmmss(d);
		return str;
	}

	/**
	 * 转换日期string类型
	 * 
	 * @param date
	 * @return 返回yyyy-MM-dd
	 */
	public static String convertDATEyyyyMMdd(String date) {
		if (StringUtil.isEmpty(date)) {
			return "";
		}
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date d = null;
		try {
			d = df.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return "";
		}
		String str = convertDATEyyyyMMddHHmmss(d);
		return str;
	}

	/**
	 * String日期转换为Date类型日期
	 * 
	 * @param date
	 * @return 返回yyyy-MM-dd HH:mm:ss格式
	 */
	public static Date convertStringToDateForyyyyMMddHHmmss(String date) {
		if (StringUtil.isEmpty(date)) {
			return null;
		}
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date d = null;
		try {
			d = df.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
		String str = convertDATEyyyyMMddHHmmss(d);
		return d;
	}

	/**
	 * String日期转Date
	 * 
	 * @param date
	 * @return 返回yyyy-MM-dd格式
	 */
	public static Date convertStringToDateForyyyyMMdd(String date) {
		if (StringUtil.isEmpty(date)) {
			return null;
		}
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date d = null;
		try {
			d = df.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
		String str = convertDATEyyyyMMddHHmmss(d);
		return d;
	}

	/**
	 * String转换成int
	 * 
	 * @param str
	 * @return
	 */
	public static int stringtoint(String str) {
		try {
			int strint = Integer.parseInt(str);
			return strint;
		} catch (Exception e) {
			e.printStackTrace();
			return 0000;
		}
	}

	/**
	 * 生成UUID ，随机生成
	 */
	public static String getRandomUUID() {
		return UUID.randomUUID().toString().replace("-", "").toLowerCase();
	}

	/**
	 * 需要生成实例，生成UUID变化较小
	 * 
	 * @return
	 */
	public String getNewUUID() {
		StringBuffer sUUID = new StringBuffer();
		Date date = new Date();
		Random rnd = new Random();

		int[] xorFactor = new int[4];
		xorFactor[0] = 0;
		xorFactor[1] = (int) (date.getTime() >>> 32);
		xorFactor[2] = (int) (date.getTime() & 0xFFFFFFFF);
		xorFactor[3] = hashCode();

		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 2; j++) {
				int value = (int) (65536.0D * Math.random());
				if (j == 1) {
					xorFactor[i] >>>= 16;
				}
				value = xorFactor[i] & 0xFFFF ^ value;
				String sHexValue = Integer.toHexString(value);
				sHexValue = "0000".substring(0, 4 - sHexValue.length()) + sHexValue;
				sUUID.append(sHexValue);
			}
		}
		rnd.nextInt();
		return sUUID.toString();
	}

	/**
	 * 将String[]转换为map用于查找
	 */
	public static Map getTorFByStringShuZu(String[] sshuzu) {
		Map map = new HashMap();
		for (String s : sshuzu) {
			map.put(s, "Y");
		}
		return map;
	}


	/**
	 * 三级联动导出到excel里 兼容性设置 判断String字符串第一位是否为数字，如果为数字，则在字符串前加“_”
	 * 
	 * @param value
	 * @return
	 */
	public static String StringFirstisValidInt(String value) {
		boolean flag = true;
		try {
			Integer.parseInt(value.substring(0, 1));
		} catch (NumberFormatException e) {
			flag = false;
		}
		if (flag) {
			return "_" + value;
		} else {
			return value;
		}
	}

	/**
	 * 判断一个字符串是否都为数字
	 * 
	 * @param strNum
	 * @return
	 */
	public static boolean isDigitMatcher(String strNum) {
		Pattern pattern = Pattern.compile("[0-9]{1,}");
		Matcher matcher = pattern.matcher((CharSequence) strNum);
		return matcher.matches();
	}

	/**
	 * String 字符串截取数字
	 * 
	 * @param content
	 * @return
	 */
	public static String getNumbers(String content) {
		Pattern pattern = Pattern.compile("\\d+");
		Matcher matcher = pattern.matcher(content);
		while (matcher.find()) {
			return matcher.group(0);
		}
		return "";
	}

	/**
	 * String 字符串截取非数字
	 * 
	 * @param content
	 * @return
	 */
	public static String splitNotNumber(String content) {
		Pattern pattern = Pattern.compile("\\D+");
		Matcher matcher = pattern.matcher(content);
		while (matcher.find()) {
			return matcher.group(0);
		}
		return "";
	}

	/**
	 * Object转换为String,如果为空则返回“”
	 * 
	 * @param object
	 * @return
	 */
	public static String ObjectToString(Object object) {
		if (null != object) {
			return object.toString();
		} else {
			return "";
		}
	}

	/**
	 * Object转换为Integer,如果为空则返回0
	 * 
	 * @param object
	 * @return
	 */
	public static Integer ObjectToInt(Object object) {
		if (null != object) {
			return Integer.parseInt(object.toString());
		} else {
			return 0;
		}
	}

}
