package Lab4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Stack;
import java.util.StringTokenizer;

/**
 * Built using CHelper plug-in Actual solution is at the top Author: Wavator
 */
public class G {
	public static void main(String[] args) {
		InputStream inputStream = System.in;// new FileInputStream("C:\\Users\\wavator\\Downloads\\test.in");
		OutputStream outputStream = System.out;
		InputReader in = new InputReader(inputStream);
		PrintWriter out = new PrintWriter(outputStream);
		Task solver = new Task();
		solver.solve(in, out);
		out.close();
	}

	static class Task {

		public void solve(InputReader in, PrintWriter out) {
			for (int t = in.nextInt(); t > 0; t--) {
				int operationtimes = in.nextInt();
				Stack<Long> stack = new Stack<Long>();
				Stack<Long> bigstack = new Stack<Long>();
				Stack<Long> smallstack = new Stack<Long>();

				for (int i = 0; i < operationtimes; i++) {
					String operation = in.next();
					// out.println("operation:" + operation + " ");
					if (operation.equals("push")) {
						long num = in.nextLong();
						// out.println("num:" + num + " ");
						stack.push(num);
						if (bigstack.isEmpty() || bigstack.peek() <= num) {
							bigstack.push(num);
						}
						if (smallstack.isEmpty() || smallstack.peek() >= num) {
							smallstack.push(num);
						}
						if (smallstack.peek() != num) {
							smallstack.push(smallstack.peek());
						}
						if (bigstack.peek() != num) {
							bigstack.push(bigstack.peek());
						}
					}
					if (operation.equals("pop")) {
						bigstack.pop();
						smallstack.pop();
						if (bigstack.isEmpty()) {
							out.println(0);
						} else {
							long big = bigstack.peek();
							long small = smallstack.peek();
							out.println(big - small);
						}
					}
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
