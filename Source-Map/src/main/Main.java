package main;

import decoder.SourceMapDecoder;

public class Main {

	public static void main(String[] args) {
		SourceMapDecoder myDecoder = new SourceMapDecoder();
		myDecoder.decodeSourceMap("resources/SourceMap");
	}
}