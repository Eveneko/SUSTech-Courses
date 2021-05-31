package Lab5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.StringTokenizer;

public class E {
	public static void main(String[] args) {
		InputStream inputStream = System.in;
		OutputStream outputStream = System.out;
		InputReader in = new InputReader(inputStream);
		PrintWriter out = new PrintWriter(outputStream);
		Task solver = new Task();
		solver.solve(in, out);
		out.close();
	}

	static class Task {

		public static int[] kmpnextplus(String Pattern) {
			int slen = Pattern.length();
			int[] next = new int[slen];
			next[0] = -1;
			int suf = 0;
			int pre = -1;
			while (suf < slen - 1) {
				if (pre == -1 || Pattern.charAt(suf) == Pattern.charAt(pre)) {
					next[++suf] = ++pre;
				} else {
					pre = next[pre];
				}
			}
			return next;
		}

		public static int kmpcmp(String Text, String Pattern, int[] next) {
			char[] s = Text.toCharArray();
			char[] p = Pattern.toCharArray();
			int suf = 0;
			int pre = 0;
			while (suf < Text.length() && pre < Pattern.length()) {
				if (pre == -1 || s[suf] == p[pre]) {
					suf++;
					pre++;
				} else {
					pre = next[pre];
				}
			}
			return pre;
		}

		public void solve(InputReader in, PrintWriter out) {
			for (int t = in.nextInt(); t > 0; t--) {
				int len1 = in.nextInt();
				int len2 = in.nextInt();
				String str1 = in.next();
				String str2 = in.next();
				int[] next = kmpnextplus(str1);
				int len = kmpcmp(str2, str1, next);
				String substr = str1.substring(0, len);
				out.println(len + " " + substr);
			}
		}
	}

	static class InputReader {
		public BufferedReader reader;
		public StringTokenizer tokenizer;

		public InputReader(InputStream stream) {
			reader = new BufferedReader(new InputStreamReader(stream), 32768);
			tokenizer = null;
		}

		public String next() {
			while (tokenizer == null || !tokenizer.hasMoreTokens()) {
				try {
					tokenizer = new StringTokenizer(reader.readLine());
				} catch (IOException e) {
					throw new RuntimeException(e);
				}
			}
			return tokenizer.nextToken();
		}

		public int nextInt() {
			return Integer.parseInt(next());
		}

		public long nextLong() {
			return Long.parseLong(next());
		}

		public double nextDouble() {
			return Double.parseDouble(next());
		}

		public char[] nextCharArray() {
			return next().toCharArray();
		}

		// public boolean hasNext() {
		// try {
		// return reader.ready();
		// } catch(IOException e) {
		// throw new RuntimeException(e);
		// }
		// }
		public boolean hasNext() {
			try {
				String string = reader.readLine();
				if (string == null) {
					return false;
				}
				tokenizer = new StringTokenizer(string);
				return tokenizer.hasMoreTokens();
			} catch (IOException e) {
				return false;
			}
		}

		public BigInteger nextBigInteger() {
			return new BigInteger(next());
		}

		public BigDecimal nextBigDecinal() {
			return new BigDecimal(next());
		}
	}
}