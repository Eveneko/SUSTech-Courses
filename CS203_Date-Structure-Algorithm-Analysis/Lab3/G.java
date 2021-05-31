package Lab3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
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
			int times = in.nextInt();
			// 数组拟链，双向链表，map排序
			for (int i = 0; i < times; i++) {
				int len = in.nextInt();
				// 生成三条数组模拟链表。list存放的是原始数据
				int[] list = new int[len];
				// pre存放排好序后，某数字前一位的数组下标
				int[] pre = new int[len];
				// next存放排好序后，某数字后一位的数组下标
				int[] next = new int[len];
				// mid倒序输出存放
				int[] mid = new int[(len + 1) / 2];

				// Hashmap排序，key是数字在list的下标，即第几个来的，value是该数字
				HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
				// 把数据读入map和list
				for (int j = 0; j < len; j++) {
					int num = in.nextInt();
					map.put(j, num);
					list[j] = num;
				}
				// 排序
				LinkedHashMap<Integer, Integer> Map2 = new LinkedHashMap<Integer, Integer>();
				map.entrySet().stream().sorted(Map.Entry.comparingByValue())
						.forEachOrdered(z -> Map2.put(z.getKey(), z.getValue()));
				Iterator<Map.Entry<Integer, Integer>> it = Map2.entrySet().iterator();

				int headpointer = 0;
				// 补充pre和next的数据
				int[] key = new int[len];
				int[] value = new int[len];
				for (int j = 0; j < len; j++) {
					Map.Entry<Integer, Integer> entry = it.next();
					key[j] = entry.getKey();
					value[j] = entry.getValue();
				}
				for (int j = 0; j < len; j++) {
					if (j == 0) {
						pre[key[j + 1]] = key[j];
						next[key[j]] = key[j + 1];
						pre[key[j]] = -1;
					}
					if (j == len - 1) {
						next[key[j - 1]] = key[j];
						next[key[j]] = -2;
						pre[key[j]] = key[j - 1];
					}
					if (j != 0 & j != len - 1) {
						next[key[j - 1]] = key[j];
						pre[key[j + 1]] = key[j];
						next[key[j]] = key[j + 1];
						pre[key[j]] = key[j - 1];
					}
				}

				// 找中位数
				int pointer = key[0];
				int come = len / 2;
				for (int j = 0; j < come; j++) {
					pointer = next[pointer];
				}
				mid[0] = list[pointer];
				mid[len / 2] = list[0];
				for (int j = 1; j < come; j++) {
					if ((list[len - 1] - list[pointer]) > 0 & (list[len - 2] - list[pointer]) > 0) {
						pointer = pre[pointer];
					}
					if ((list[len - 1] - list[pointer]) < 0 & (list[len - 2] - list[pointer]) < 0) {
						pointer = next[pointer];
					}
					if ((list[len - 1] - list[pointer]) == 0 & (list[len - 2] - list[pointer]) > 0) {
						pointer = pre[pointer];
					}
					if ((list[len - 1] - list[pointer]) == 0 & (list[len - 2] - list[pointer]) < 0) {
						pointer = next[pointer];
					}
					if ((list[len - 1] - list[pointer]) > 0 & (list[len - 2] - list[pointer]) == 0) {
						pointer = pre[pointer];
					}
					if ((list[len - 1] - list[pointer]) < 0 & (list[len - 2] - list[pointer]) == 0) {
						pointer = next[pointer];
					}
					if (next[len - 1] != -2 & pre[len - 1] != -1) {
						pre[next[len - 1]] = pre[len - 1];
						next[pre[len - 1]] = next[len - 1];
					}
					if (next[len - 1] == -2 & pre[len - 1] != -1) {
						next[pre[len - 1]] = next[len - 1];
					}
					if (next[len - 1] != -2 & pre[len - 1] == -1) {
						pre[next[len - 1]] = pre[len - 1];
					}

					if (next[len - 2] != -2 & pre[len - 2] != -1) {
						pre[next[len - 2]] = pre[len - 2];
						next[pre[len - 2]] = next[len - 2];
					}
					if (next[len - 2] == -2 & pre[len - 2] != -1) {
						next[pre[len - 2]] = next[len - 2];
					}
					if (next[len - 2] != -2 & pre[len - 2] == -1) {
						pre[next[len - 2]] = pre[len - 2];
					}

					len -= 2;
					mid[j] = list[pointer];
				}
				for (int j = 0; j < mid.length; j++) {
					out.print(mid[mid.length - 1 - j] + " ");
				}
				out.println("");
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