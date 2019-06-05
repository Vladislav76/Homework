package decoder;

import java.util.ArrayList;

public class Base64 {
	private static final String letters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static final String others = "0123456789+/";
	private static final ArrayList<Character> indexTable = new ArrayList<>(64);

	static {
		for(int i = 0; i < letters.length(); i++) {
			indexTable.add(letters.charAt(i));
		}
		String littleLetters = letters.toLowerCase();
		for(int i = 0; i < littleLetters.length(); i++) {
			indexTable.add(littleLetters.charAt(i));
		}
		for(int i = 0; i < others.length(); i++) {
			indexTable.add(others.charAt(i));
		}
	}

	public static String decodeCharacter(char c) {
		int x = indexTable.indexOf(Character.valueOf(c));
		StringBuilder sb = new StringBuilder(Integer.toBinaryString(x));
		while (sb.length() < 6) {
			sb.insert(0, '0');
		}
		sb.setLength(6);
		return sb.toString();
	}

	public static String[] decodeString(String s) {
		String[] array = new String[s.length()];
		for(int i = 0; i < s.length(); i++) {
			array[i] = decodeCharacter(s.charAt(i));
		}
		return array;
	}
}