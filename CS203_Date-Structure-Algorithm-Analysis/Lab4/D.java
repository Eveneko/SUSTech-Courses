package Lab4;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.StringTokenizer;

/**
 * Built using CHelper plug-in Actual solution is at the top Author: Wavator
 */
public class D {
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
				char[][] maze = new char[n][m];
				int Sx = 0, Sy = 0, Ex = 0, Ey = 0;
				for (int i = 0; i < n; i++) {
					String string = in.next();
					for (int j = 0; j < m; j++) {
						maze[i][j] = string.charAt(j);
						if (maze[i][j] == 'S') {
							Sx = j;
							Sy = i;
						}
						if (maze[i][j] == 'E') {
							Ex = j;
							Ey = i;
						}
					}
				}
				String num = in.next();
				int count = 0;
				for (int up = 0; up < 4; up++) {
					for (int down = 0; down < 4; down++) {
						for (int left = 0; left < 4; left++) {
							for (int right = 0; right < 4; right++) {
								if (up != down && up != left && up != right && down != left && down != right
										&& left != right) {
									int y = Sy;
									int x = Sx;
									int pointer = 0;
									while (pointer <= num.length() - 1) {
										// out.printf("up:%d down:%d left:%d right:%d\n", up, down, left, right);
										// out.printf("(x:%d,y:%d) %d\n", x, y, (int) num.charAt(pointer) - 48);
										if ((int) num.charAt(pointer) - 48 == up) {
											y--;
										} else if ((int) num.charAt(pointer) - 48 == down) {
											y++;
										} else if ((int) num.charAt(pointer) - 48 == left) {
											x--;
										} else if ((int) num.charAt(pointer) - 48 == right) {
											x++;
										}
										if (y < 0 || y >= n || x < 0 || x >= m || maze[y][x] == '#') {
											break;
										}
										if (x == Ex && y == Ey) {
											// out.printf("up:%d down:%d left:%d right:%d\n", up, down, left, right);
											count++;
											break;
										}
										pointer++;
									}
								}
							}
						}
					}
				}
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