package vell.bibi.vview.util;

public class StringShiftUtil {
	
	public static String enShift(String str){
		str = str.replaceAll("&", "%26");
		str = str.replaceAll("#", "%23");
		str = str.replaceAll("\\?", "%3F");
		return str;
	}
	public static String deShift(String str){
		str = str.replaceAll("%26","&");
		str = str.replaceAll("%23","#");
		str = str.replaceAll("%3F","\\?");
		return str;
	}
	public static String wipeStr(String str){
		str = str.replaceAll("&", "");
		str = str.replaceAll("#", "");
		str = str.replaceAll("\\?", "");
		return str;
	}
	public static void main(String[] args) {
//		System.out.println(deShift("V%26View%23safd%3Fsss"));
	}

}
