package Bouns;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.StringTokenizer;

public class C {
	static long count = 0;

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

		public void solve(InputReader in, PrintWriter out) throws Exception {
			for (int t = in.nextInt(); t > 0; t--) {
				count = 0;
				int len = in.nextInt();
				int[] list = new int[len];
				for (int i = 0; i < len; i++) {
					list[i] = in.nextInt();
				}
				sort(list, 0, list.length - 1);
				out.println(count);
			}
		}

		public static void sort(int[] a, int low, int high) {
			int mid = (low + high) / 2;
			if (low < high) {
				sort(a, low, mid);
				sort(a, mid + 1, high);
				merge(a, low, mid, high);
			}
		}

		public static void merge(int[] a, int low, int mid, int high) {
			int[] temp = new int[high - low + 1];
			int i = low;
			int j = mid + 1;
			int k = 0;

			for (int q = low, w = mid + 1; q <= mid; q++) {
				while (w <= high && a[q] > a[w]) {
					w++;
				}
				count += w - (mid + 1);
			}

			while (i <= mid && j <= high) {
				if (a[i] < a[j]) {
					temp[k++] = a[i++];
				} else {
					temp[k++] = a[j++];
				}
			}
			while (i <= mid) {
				temp[k++] = a[i++];
			}
			while (j <= high) {
				temp[k++] = a[j++];
			}
			for (int x = 0; x < temp.length; x++) {
				a[x + low] = temp[x];
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