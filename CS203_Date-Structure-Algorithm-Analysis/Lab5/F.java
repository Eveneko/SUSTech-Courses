package Lab5;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.StringTokenizer;

public class F {
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

		public static boolean kmpcmp(String Text, String Pattern, int[] next) {
			char[] t = Text.toCharArray();
			char[] p = Pattern.toCharArray();
			int i = 0;
			int j = 0;
			while (i < Text.length() && j < Pattern.length()) {
				if (j == -1 || t[i] == p[j]) {
					i++;
					j++;
					if (j == Pattern.length()) {
						return true;
					}
				} else {
					j = next[j];
				}
			}
			return false;
		}

		public void solve(InputReader in, PrintWriter out) {
			for (int t = in.nextInt(); t > 0; t--) {
				int len = in.nextInt();
				String[] strlist = new String[len];
				ArrayList reslist = new ArrayList();
				String min = "";
				for (int i = 0; i < strlist.length; i++) {
					strlist[i] = in.next();
					if (min.length() == 0) {
						min = strlist[i];
					}
					if (strlist[i].length() < min.length()) {
						min = strlist[i];
					}
				}
				int minlen = min.length();
				for (int i = minlen; i > 0; i--) {
					for (int j = 0; j < minlen - i + 1; j++) {
						String sub = min.substring(j, j + i);
						// out.println(sub);
						int[] next = kmpnext(sub);
						boolean LCS = true;
						for (int k = 0; k < strlist.length; k++) {
							if (!kmpcmp(strlist[k], sub, next)) {
								LCS = false;
								// out.println(" NO");
								break;
							}
						}
						if (LCS) {
							// out.println(" YES");
							reslist.add(sub);
						}
					}

					if (!reslist.isEmpty()) {
						Collections.sort(reslist);
						out.println(reslist.get(0));
						break;
					}
				}

				if (reslist.isEmpty()) {
					out.println("Hong");
				}

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