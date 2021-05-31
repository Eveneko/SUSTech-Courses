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

public class Cplus {
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

		public static int[] kmpnext(String Pattern) {
			int slen = Pattern.length();
			int[] next = new int[slen];
			next[0] = -1;
			int suf = 0;
			int pre = -1;
			while (suf < slen - 1) {
				if (pre == -1 || Pattern.charAt(suf) == Pattern.charAt(pre)) {
					next[suf + 1] = pre + 1;
					suf++;
					pre++;
				} else {
					pre = next[pre];
				}
			}
			return next;
		}

		public static int kmpcmp(String Text, String Pattern, int[] next) {
			char[] t = Text.toCharArray();
			char[] p = Pattern.toCharArray();
			int count = 0;
			int i = 0;
			int j = 0;
			while (i < Text.length() && j < Pattern.length()) {
				if (j == -1 || t[i] == p[j]) {
					i++;
					j++;
					if (j == Pattern.length()) {
						count++;
						i--;
						j = next[j - 1];
					}
				} else {
					j = next[j];
				}
			}
			return count;
		}

		public void solve(InputReader in, PrintWriter out) {
			for (int t = in.nextInt(); t > 0; t--) {
				int n = in.nextInt();
				String Text = in.next();
				int m = in.nextInt();
				String Pattern = in.next();
				if (n < m) {
					out.println(0);
					continue;
				}
				int[] next = kmpnext(Pattern);
				int count = kmpcmp(Text, Pattern, next);
				out.println(count);
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