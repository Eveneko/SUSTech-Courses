package Lab9;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;

public class B_prim {
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

				// create graph
				Vnode[] AdjList = new Vnode[n];
				for (int i = 0; i < n; i++) {
					AdjList[i] = new Vnode(i, null);
				}
				for (int i = 0; i < m; i++) {
					int u = in.nextInt() - 1;
					int v = in.nextInt() - 1;
					int w = in.nextInt();
					Enode temp = new Enode();
					temp.adjvex = v;
					temp.weight = w;
					temp.next = AdjList[u].firstarc;
					AdjList[u].firstarc = temp;
					temp = new Enode();
					temp.adjvex = u;
					temp.weight = w;
					temp.next = AdjList[v].firstarc;
					AdjList[v].firstarc = temp;
				}

				// Prim Algorithm
				int result = 0;
				int delmin = 0;
				boolean[] visited = new boolean[n];
				int[] edgeTo = new int[n];
				int[] distTo = new int[n];
				for (int i = 0; i < n; i++) {
					distTo[i] = Integer.MAX_VALUE;
				}
				for (int i = 0; i < n; i++) {
					int mindst = Integer.MAX_VALUE;
					visited[delmin] = true;
					if (delmin == 0) {
						edgeTo[0] = 0;
						distTo[0] = 0;
					}
					Enode p = AdjList[delmin].firstarc;
					while (p != null) {
						if (visited[p.adjvex]) {
							p = p.next;
							continue;
						}
						if (p.weight < distTo[p.adjvex]) {
							edgeTo[p.adjvex] = delmin;
							distTo[p.adjvex] = p.weight;
						}
						p = p.next;
					}
					for (int j = 0; j < n; j++) {
						if (!visited[j] && mindst > distTo[j]) {
							delmin = j;
							mindst = distTo[j];
						}
					}
				}
				for (int i = 0; i < n; i++) {
					result += distTo[i];
				}
				out.println(result);
			}
		}
	}

	static class Enode {
		int adjvex; // 索引
		int weight; // 权重
		Enode next; // 链表下一位

		public Enode() {

		}

		public Enode(int adjvex, int weight, Enode next) {
			this.adjvex = adjvex;
			this.weight = weight;
			this.next = next;
		}
	}

	static class Vnode {
		int vertex; // 值
		Enode firstarc; // 指向第一个中间节点

		public Vnode(int vertex, Enode firstarc) {
			this.vertex = vertex;
			this.firstarc = firstarc;
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
