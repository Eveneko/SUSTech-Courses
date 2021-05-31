package Lab9;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.Queue;

public class J {
	public static void main(String[] args) throws IOException {
		InputStream inputStream = System.in;
		OutputStream outputStream = System.out;
		Reader in = new Reader();
		PrintWriter out = new PrintWriter(outputStream);
		Task solver = new Task();
		solver.solve(in, out);
		out.close();
	}

	static int answer = 0;
	static int[] head;
	static node[] nodes;
	static int[] pre;

	static class Task {

		public void solve(Reader in, PrintWriter out) throws IOException {
			for (int t = in.nextInt(); t > 0; t--) {
				int n = in.nextInt();
				head = new int[n];
				for (int i = 0; i < head.length; i++) {
					head[i] = -1;
				}
				pre = new int[n];
				for (int i = 0; i < pre.length; i++) {
					pre[i] = i;
				}
				nodes = new node[n];
				for (int i = 0; i < nodes.length; i++) {
					nodes[i] = new node();
				}
				int cnt = 0;
				Queue<point> q = new LinkedList<>();
				for (int i = 0; i < n; i++) {
					int x = in.nextInt() - 1;
					String string = in.readLine();
					if (string.charAt(0) == 'w') {
						point st = new point(i, x);
						q.offer(st);
					} else {
						Add(x, i, cnt);
						int Fa = Find(x);
						int Fb = Find(i);
						if (Fa != Fb) {
							pre[Fa] = Fb;
						}
					}
				}
				int answer = 0;
				while (!q.isEmpty()) {
					point now = q.poll();
					int Fu = Find(now.u);
					int Fv = Find(now.v);
					if (Fu != Fv) {
						continue;
					}
					Dfs(now.v);
				}
				out.println(answer);
			}
		}

		public void Add(int a, int b, int cnt) {
			nodes[cnt].to = b;
			nodes[cnt].next = head[a];
			head[a] = cnt++;
		}

		public int Find(int x) {
			if (x == pre[x]) {
				return x;
			}
			return pre[x] = Find(pre[x]);
		}

		public void Dfs(int u) {
			answer++;
			for (int i = head[u]; i != -1; i = nodes[i].next) {
				Dfs(nodes[i].to);
			}
		}
	}

	static class node {
		int to, next;

		public node() {

		}

		public node(int to, int next) {
			this.to = to;
			this.next = next;
		}
	}

	static class point {
		int u, v;

		public point(int u, int v) {
			this.u = u;
			this.v = v;
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
