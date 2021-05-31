package Lab4;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Stack;
import java.util.StringTokenizer;

class InputReader {
	public BufferedReader br;
	public StringTokenizer tokenizer;

	public InputReader(InputStream stream) throws FileNotFoundException {
		br = new BufferedReader(new InputStreamReader(stream), 327680);
		tokenizer = null;
	}

	public boolean hasNext() {
		while (tokenizer == null || !tokenizer.hasMoreElements()) {
			try {
				tokenizer = new StringTokenizer(br.readLine());
			} catch (Exception e) {
				return false;
			}
		}
		return true;
	}

	public String next() {
		while (tokenizer == null || !tokenizer.hasMoreTokens()) {
			try {
				tokenizer = new StringTokenizer(br.readLine());
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		return tokenizer.nextToken();
	}

	public int nextInt() {
		try {
			int c = br.read();
			while (c <= 32) {
				c = br.read();
			}
			boolean negative = false;
			if (c == '-') {
				negative = true;
				c = br.read();
			}
			int x = 0;
			while (c > 32) {
				x = x * 10 + c - '0';
				c = br.read();
			}
			return negative ? -x : x;
		} catch (IOException e) {
			return -1;
		}
	}

	public long nextLong() {
		try {
			int c = br.read();
			while (c <= 32) {
				c = br.read();
			}
			boolean negative = false;
			if (c == '-') {
				negative = true;
				c = br.read();
			}
			long x = 0;
			while (c > 32) {
				x = x * 10 + c - '0';
				c = br.read();
			}
			return negative ? -x : x;
		} catch (IOException e) {
			return -1;
		}
	}

}

public class A {
	static PrintWriter out;
	static InputReader in;

	public static void main(String args[]) throws IOException {
		out = new PrintWriter(System.out);
		in = new InputReader(System.in);

		for (int t = in.nextInt(); t > 0; t--) {
			// push lanran in the satck
			Stack<Character> stack = new Stack<>();
			String lanran = "lanran";
			for (int i = 0; i < lanran.length(); i++) {
				stack.push(lanran.charAt(lanran.length() - 1 - i));
			}
			// compare with lanran and other string
			String string = in.next();
			for (int i = 0; i < string.length(); i++) {
				if (!stack.empty() && stack.peek().equals(string.charAt(i))) {
					stack.pop();
				}
				if (stack.empty()) {
					out.println("Yes");
					break;
				}
				if (i == string.length() - 1) {
					out.println("No");
				}
			}

		}
		out.close();
	}

}