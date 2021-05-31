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

public class D {
	public static void main(String[] args) throws Exception {
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
			int[] next = new int[Pattern.length()];
			next[0] = 0;
			for (int i = 1, j = 0; i < Pattern.length(); i++) {
				while (j > 0 && Pattern.charAt(j) != Pattern.charAt(i)) {
					j = next[j - 1];
				}
				if (Pattern.charAt(i) == Pattern.charAt(j)) {
					j++;
				}
				next[i] = j;
			}
			return next;
		}

		public void solve(InputReader in, PrintWriter out) throws Exception {
			for (int t = in.nextInt(); t > 0; t--) {
				int len = in.nextInt();
				String str = in.next();
				if (len < 3) {
					out.println(0);
					continue;
				}
				int[] next = kmpnext(str);
				int sublen = next[next.length - 1];
				String res = str.substring(0, sublen);
				String mid = "0";
				if (sublen <= len - sublen) {
					mid = str.substring(sublen, len - sublen);
				}
				int len1 = len;
				String str1 = str;
				while (sublen > len / 3 || !mid.contains(res)) {
					str1 = res;
					len1 = str1.length();
					next = kmpnext(str1);
					// out.println(Arrays.toString(next));
					sublen = next[next.length - 1];
					res = str1.substring(0, sublen);

					if (sublen <= len - sublen) {
						mid = str.substring(sublen, len - sublen);
					}
				}

				out.println(sublen);
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