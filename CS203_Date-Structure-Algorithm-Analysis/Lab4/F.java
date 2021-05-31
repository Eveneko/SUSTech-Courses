package Lab4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.StringTokenizer;

/**
 * Built using CHelper plug-in Actual solution is at the top Author: Wavator
 */
public class F {
	static int MOD = 1000000007;

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
				int m = in.nextInt();
				long[][][] matrixlist = new long[n + 25][m][m];
				// 读入矩阵
				for (int i = 0; i < n; i++) {
					for (int j = 0; j < m; j++) {
						for (int k = 0; k < m; k++) {
							matrixlist[i][j][k] = in.nextInt();
						}
					}
				}
				String input = in.next();
				Stack<String> test = polish(input);
				Stack<String> outstack = new Stack<String>();
				while (!test.isEmpty()) {
					outstack.push(test.pop());
					// out.print(outstack.peek() + ' ');
				}
				Stack<String> tempstack = new Stack();
				int i = 0;
				while (!outstack.isEmpty()) {
					String temp = outstack.pop();
					if (!isOperator(temp)) {
						tempstack.push(temp);
					} else {
						int num2 = Integer.parseInt(tempstack.peek());
						long[][] matrix2 = matrixlist[Integer.parseInt(tempstack.pop()) - 1];
						int num1 = Integer.parseInt(tempstack.peek());
						long[][] matrix1 = matrixlist[Integer.parseInt(tempstack.pop()) - 1];
						// out.printf("%d%s%d\n", num1, temp, num2);
						switch (temp) {
						case "+":
							matrixlist[n + i] = plus(matrix1, matrix2);
							tempstack.push("" + (n + i + 1));
							i++;
							break;

						case "-":
							matrixlist[n + i] = sub(matrix2, matrix1);
							tempstack.push("" + (n + i + 1));
							i++;
							break;

						case "*":
							matrixlist[n + i] = mutip(matrix1, matrix2);
							tempstack.push("" + (n + i + 1));
							i++;
							break;
						}

					}
				}
				// for (int j = 0; j < n + i; j++) {
				// out.printf("(%d)\n", j + 1);
				// for (int k = 0; k < m; k++) {
				// for (int k2 = 0; k2 < m; k2++) {
				// out.print(matrixlist[j][k][k2] + " ");
				// }
				// out.println();
				// }
				// out.println();
				// }

				// int[][] outmatrix = new int[m][m];
				// for (int j = 0; j < m; j++) {
				// for (int k = 0; k < m; k++) {
				// outmatrix[j][k] = (int) matrixlist[n + i - 1][j][k];
				// }
				// }
				//
				// for (int j = 0; j < m; j++) {
				// for (int k = 0; k < m; k++) {
				// out.print(outmatrix[j][k]);
				// if (k == m - 1) {
				// if (j != m - 1) {
				// out.println("");
				// }
				// } else {
				// out.print(" ");
				// }
				// }
				// }

				for (int j = 0; j < m; j++) {
					for (int k = 0; k < m; k++) {

						out.print((MOD - matrixlist[n + i - 1][j][k]) % MOD);
						if (k == m - 1) {
							if (j != m - 1) {
								out.println("");
							}
						} else {
							out.print(" ");
						}
					}
				}

			}
		}

		public static long[][] mutip(long[][] matrix1, long[][] matrix2) {
			long[][] newmatrix = new long[matrix1.length][matrix1.length];
			for (int i = 0; i < newmatrix.length; i++) {
				for (int j = 0; j < newmatrix.length; j++) {
					for (int k = 0; k < newmatrix.length; k++) {
						newmatrix[i][j] = (newmatrix[i][j] + ((matrix1[i][k] * matrix2[k][j]) % MOD)) % MOD;
						// newmatrix[i][j] += ((matrix1[i][k] * matrix2[k][j]));
					}

				}
			}
			return newmatrix;
		}

		public static long[][] plus(long[][] matrix1, long[][] matrix2) {
			long[][] newmatrix = new long[matrix1.length][matrix1.length];
			for (int i = 0; i < newmatrix.length; i++) {
				for (int j = 0; j < newmatrix.length; j++) {
					newmatrix[i][j] = ((matrix1[i][j] + matrix2[i][j]) % MOD);
					// newmatrix[i][j] = ((matrix1[i][j] + matrix2[i][j]));
				}
			}
			return newmatrix;
		}

		public static long[][] sub(long[][] matrix1, long[][] matrix2) {
			long[][] newmatrix = new long[matrix1.length][matrix1.length];
			for (int i = 0; i < newmatrix.length; i++) {
				for (int j = 0; j < newmatrix.length; j++) {
					newmatrix[i][j] = ((matrix1[i][j] - matrix2[i][j] + MOD) % MOD);
					// newmatrix[i][j] = ((matrix1[i][j] - matrix2[i][j]));
				}
			}
			return newmatrix;
		}

		public static Stack polish(String input) {
			// 输出的栈
			Stack<String> outstack = new Stack();
			// 符号的栈
			Stack<String> opStack = new Stack();

			// 符号优先级
			Map<String, Integer> opMap = new HashMap();
			opMap.put("(", 0);
			opMap.put("+", 1);
			opMap.put("-", 1);
			opMap.put("*", 2);

			// 处理字符串
			for (int i = 0; i < input.length();) {
				String temp = "";
				char temp1 = input.charAt(i);
				if (isOperator(temp1) || temp1 == '(' || temp1 == ')') {
					temp = temp + temp1;
					i++;
				} else {
					while (!isOperator(temp1) && temp1 != '(' && temp1 != ')') {
						temp = temp + temp1;
						i++;
						if (i < input.length()) {
							temp1 = input.charAt(i);
						} else {
							break;
						}
					}
				}
				if ("(".equals(temp)) {
					opStack.push(temp);
				} else if (isOperator(temp)) {
					if (opStack.isEmpty()) {
						opStack.push(temp);
					} else if (!opStack.isEmpty() && opMap.get(temp) >= opMap.get(opStack.peek())) {
						opStack.push(temp);
					} else if (!opStack.isEmpty() && opMap.get(temp) < opMap.get(opStack.peek())) {
						while (opMap.get(temp) < opMap.get(opStack.peek())) {
							outstack.push(opStack.pop());
							if (opStack.isEmpty()
									|| (!opStack.isEmpty() && opMap.get(temp) >= opMap.get(opStack.peek()))) {
								opStack.push(temp);
								break;
							}
						}
					}
				} else if (")".equals(temp)) {
					for (int j = 0; j < opStack.size(); j++) {
						String c = opStack.pop();
						if (c.equals("("))
							break;
						else
							outstack.push(c);
					}
				} else {
					outstack.push(temp);
				}
			}
			int times = opStack.size();
			for (int i = 0; i < times; i++) {
				if (opStack.peek().equals("(")) {
					opStack.pop();
				} else {
					outstack.push(opStack.pop());
				}
			}
			return outstack;
		}

		public static boolean isOperator(String charAt) {
			if (charAt.equals("+") || charAt.equals("-") || charAt.equals("*"))
				return true;
			return false;
		}

		public static boolean isOperator(char charAt) {
			if (charAt == '+' || charAt == '-' || charAt == '*')
				return true;
			return false;
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
