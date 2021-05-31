package Lab8;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Stack;

public class G {
	public static void main(String[] args) throws IOException {
		InputStream inputStream = System.in;
		OutputStream outputStream = System.out;
		Reader in = new Reader();
		PrintWriter out = new PrintWriter(outputStream);
		Task solver = new Task();
		solver.solve(in, out);
		out.close();
	}

	static class Task {

		public void solve(Reader in, PrintWriter out) throws IOException {
			for (int t = in.nextInt(); t > 0; t--) {
				int n = in.nextInt();
				int m = in.nextInt();
				ArrayList<Integer> tree[] = new ArrayList[n];
				for (int i = 0; i < tree.length; i++) {
					tree[i] = new ArrayList<>();
				}
				boolean[] notroot = new boolean[n];
				for (int i = 0; i < n - 1; i++) {
					int x = in.nextInt() - 1;
					int y = in.nextInt() - 1;
					tree[y].add(x);
					notroot[x] = true;
				}
				int root = -1;
				for (int j = 0; j < n; j++) {
					if (!notroot[j]) {
						root = j;
						break;
					}
				}
				int[][] time = new int[n][2];
				Stack<Integer> stack = new Stack<>();
				boolean[] shouldout = new boolean[n];
				int cnt = 0;
				stack.push(root);
				while (!stack.isEmpty()) {
					int nood = stack.peek();
					if (shouldout[nood]) {
						stack.pop();
						time[nood][1] = ++cnt;
					} else {
						time[nood][0] = ++cnt;
						for (int i = 0; i < tree[nood].size(); i++) {
							stack.push(tree[nood].get(i));
						}
						shouldout[nood] = true;
					}
				}
				for (int i = 0; i < m; i++) {
					int x = in.nextInt() - 1;
					int y = in.nextInt() - 1;
					if (time[y][0] <= time[x][0] && time[y][1] >= time[x][1]) {
						out.println("Yes");
					} else {
						out.println("No");
					}
				}

			}
		}
	}

	static class Reader {
		final private int BUFFER_SIZE = 1 << 16;
		private DataInputStream din;
		private byte[] buffer;
		private int bufferPointer, bytesRead;

		public Reader() {
			din = new DataInputStream(System.in);
			buffer = new byte[BUFFER_SIZE];
			bufferPointer = bytesRead = 0;
		}

		public Reader(String file_name) throws IOException {
			din = new DataInputStream(new FileInputStream(file_name));
			buffer = new byte[BUFFER_SIZE];
			bufferPointer = bytesRead = 0;
		}

		public String readLine() throws IOException {
			byte[] buf = new byte[64]; // line length
			int cnt = 0, c;
			while ((c = read()) != -1) {
				if (c == '\n')
					break;
				buf[cnt++] = (byte) c;
			}
			return new String(buf, 0, cnt);
		}

		public int nextInt() throws IOException {
			int ret = 0;
			byte c = read();
			while (c <= ' ')
				c = read();
			boolean neg = (c == '-');
			if (neg)
				c = read();
			do {
				ret = ret * 10 + c - '0';
			} while ((c = read()) >= '0' && c <= '9');

			if (neg)
				return -ret;
			return ret;
		}

		public long nextLong() throws IOException {
			long ret = 0;
			byte c = read();
			while (c <= ' ')
				c = read();
			boolean neg = (c == '-');
			if (neg)
				c = read();
			do {
				ret = ret * 10 + c - '0';
			} while ((c = read()) >= '0' && c <= '9');
			if (neg)
				return -ret;
			return ret;
		}

		public double nextDouble() throws IOException {
			double ret = 0, div = 1;
			byte c = read();
			while (c <= ' ')
				c = read();
			boolean neg = (c == '-');
			if (neg)
				c = read();

			do {
				ret = ret * 10 + c - '0';
			} while ((c = read()) >= '0' && c <= '9');

			if (c == '.') {
				while ((c = read()) >= '0' && c <= '9') {
					ret += (c - '0') / (div *= 10);
				}
			}

			if (neg)
				return -ret;
			return ret;
		}

		private void fillBuffer() throws IOException {
			bytesRead = din.read(buffer, bufferPointer = 0, BUFFER_SIZE);
			if (bytesRead == -1)
				buffer[0] = -1;
		}

		private byte read() throws IOException {
			if (bufferPointer == bytesRead)
				fillBuffer();
			return buffer[bufferPointer++];
		}

		public void close() throws IOException {
			if (din == null)
				return;
			din.close();
		}
	}
}
