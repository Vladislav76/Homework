package Main;

import Decoder.Base64;
import Decoder.SourceMapDecoder;

public class Main {

	public static void main(String[] args) {
		SourceMapDecoder myDecoder = new SourceMapDecoder();
		myDecoder.decodeSourceMap("resources/SourceMap");
	}
}