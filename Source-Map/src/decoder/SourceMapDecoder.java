package decoder;

import java.util.Scanner;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Collections;
import java.util.ArrayList;

public class SourceMapDecoder {
	private Scanner in;
	private ArrayList<int[]> mappings;
	private ArrayList<String> names;
	private ArrayList<String> sources;

	private void init() {
		mappings = new ArrayList<>();
		names = new ArrayList<>();
		sources = new ArrayList<>();
	}

	public void loadSourceMap(String fileName) {
		try {
			in = new Scanner(new FileReader(fileName));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}	

	private void closeSourceMap() {
		if (in != null) {
			in.close();
		}
	}

	public void decodeSourceMap(String fileName) {
		init();
		loadSourceMap(fileName);
		while (in.hasNext()) {
			if (in.hasNext("\"mappings\":")) {
				in.next();
				decodeMappings(in.next());
			}
			else if (in.hasNext("\"names\":")) {
				in.next();
				decodeNames(in.next());
			}
			else if (in.hasNext("\"sources\":")) {
				in.next();
				decodeSources(in.next());
			}
			else {
				in.next();
			}
		}
		closeSourceMap();
		restoreSource();
	}

	private void restoreSource() {
		try {
			String rootDirectory = "product/";
			PrintWriter[] out = new PrintWriter[sources.size()];
			for(int i = 0; i < out.length; i++) {
				out[i] = new PrintWriter(rootDirectory + sources.get(i));
			}

			int currentLine = 0;
			int currentColumn = 0;
			for(int[] word : mappings) {
				int k = word[1];

				if (k < 0 || k >= out.length) {
					System.out.println("Incorrect format in \"mappings\" (indexInputFile)");
					System.exit(-1);
				}

				while (currentLine < word[2]) {
					out[k].println();
					currentLine++;
					currentColumn = 0;
				}
				while (currentColumn < word[3]) {
					out[k].print(" ");
					currentColumn++;
				}
				out[k].print(names.get(word[4]));
				currentColumn += names.get(word[4]).length();
			}

			for(int i = 0; i < out.length; i++) {
				out[i].close();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}	
	}

	private void decodeSources(String string) {
		sources.addAll(Arrays.asList(string.split("(\\[\"|\",\"|\"\\],)", 0)));
		sources.removeAll(Collections.singleton(""));
	}

	private void decodeNames(String string) {
		names.addAll(Arrays.asList(string.split("(\\[\"|\",\"|\"\\],)", 0)));
		names.removeAll(Collections.singleton(""));
	}

	private void decodeMappings(String string) {
		ArrayList<String> list = new ArrayList<>(Arrays.asList(string.split("[;\"]", 0)));
		list.removeAll(Collections.singleton(""));
		String[] lines = list.toArray(new String[list.size()]);

		int[] offsets = {0, 0, 0, 0, 0};

		for(int i = 0; i < lines.length; i++) {
			String[] words = lines[i].split(",");
			
			for(int j = 0; j < words.length; j++) {
				String[] codeBytes = Base64.decodeString(words[j]);
				int[] data = decodeCodeBytes(codeBytes);

				if (data.length != 5) {
					System.out.println("Incorrect format in \"mappings\" (format of word)");
					System.exit(-1);
				}

				for(int k = 0; k < 5; k++) {
					offsets[k] += data[k];
				}
				mappings.add(offsets.clone());
			}	
			offsets[3] = 0;
		}	

		mappings.sort((a, b) -> 
			(a[2] < b[2] ? -1 : a[2] == b[2] ? a[3] < b[3] ? -1 : a[3] == b[3] ? 0 : 1 : 1));
	}

	private static int[] decodeCodeBytes(String[] codeBytes) {
		int[] data = new int[codeBytes.length];
		int i = 0;
		boolean isSingleWord = true;
		int sign = 0;
		int k = 0;
		StringBuilder sb = new StringBuilder();
		while (i < codeBytes.length) {
			String string = codeBytes[i];
			boolean isEndWord = false;
			char continuation = string.charAt(0);

			if (isSingleWord) {
				if (continuation == '0') {
					sign = getSign(string.charAt(5));
					sb.insert(0, string.substring(1, 5));
					isEndWord = true;
				}
				else {
					isSingleWord = false;
					sign = getSign(string.charAt(5));
					sb.insert(0, string.substring(1, 5));
				}
			}
			else {
				if (continuation == '0') {
					isSingleWord = true;
					isEndWord = true;
					sb.insert(0, string.substring(1, 6));
				}
				else {
					sb.insert(0, string.substring(1, 6));
				}
			}
			if (isEndWord) {
				data[k++] = getValue(sb.toString()) * sign;
				sb.setLength(0);
			}
			i++;
		}
		if (k < data.length) {
			data = Arrays.copyOf(data, k);
		}
		return data;
	}

	private static int getSign(char c) {
		return (c == '0') ? 1 : (c == '1') ? -1 : 0;
	}

	private static int getValue(String string) {
		return Integer.valueOf(string, 2);
	}
}