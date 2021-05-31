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
public class E {
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
				int n = in.nextInt();
				Stack<Integer> cards = new Stack<>();
				int[] nums = new int[n];
				for (int i = 0; i < n; i++) {
					nums[i] = in.nextInt();
				}
				for (int i = n - 1; i >= 0; i--) {
					cards.push(nums[i]);
				}
				boolean[] inslot = new boolean[n + 1];
				int pointer = 1;
				Stack<Integer> slot = new Stack<>();

				while (!cards.isEmpty()) {
					if (slot.isEmpty()) {
						slot.push(cards.pop());
						inslot[slot.peek()] = true;
						// while (pointer <= n && inslot[pointer]) {
						// pointer++;
						// }
					} else {
						if (slot.peek() == pointer) {
							out.print(slot.pop() + " ");
							pointer++;
							while (pointer <= n && inslot[pointer]) {
								pointer++;
							}
							while (!cards.isEmpty() && !slot.isEmpty()
									&& pointer >= slot.peek() & pointer >= cards.peek()) {
								if (cards.peek() >= slot.peek()) {
									int num = slot.peek();
									out.print(slot.pop() + " ");
									if (pointer == num) {
										pointer++;
									}
								} else {
									slot.push(cards.pop());
									inslot[slot.peek()] = true;
								}

								while (pointer <= n && inslot[pointer]) {
									pointer++;
								}
							}
							while (!cards.isEmpty() && pointer >= cards.peek()) {
								slot.push(cards.pop());
								inslot[slot.peek()] = true;
								// while (pointer <= n && inslot[pointer]) {
								// pointer++;
								// }
							}
							while (!slot.isEmpty() && pointer >= slot.peek()) {
								int num = slot.peek();
								out.print(slot.pop() + " ");
								if (pointer == num) {
									pointer++;
								}
								while (pointer <= n && inslot[pointer]) {
									pointer++;
								}
							}
						} else {
							slot.push(cards.pop());
							inslot[slot.peek()] = true;
						}
					}
				}
				while (!slot.isEmpty()) {
					out.print(slot.pop() + " ");
				}
				out.println();
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