package Lab9;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;

public class E {
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
				Node courses[] = new Node[n];
				for (int i = 0; i < n; i++) {
					courses[i] = new Node(new ArrayList<Integer>(), new ArrayList<Integer>());
				}
				int[] indegree = new int[n];
				for (int i = 0; i < n; i++) {
					indegree[i] = 0;
				}
				for (int i = 0; i < m; i++) {
					int u = in.nextInt() - 1;
					int v = in.nextInt() - 1;
					courses[u].next.add(v);
					courses[v].pre.add(u);
					indegree[v]++;
				}
				ArrayList<Integer> list = new ArrayList<Integer>();
				for (int i = 0; i < indegree.length; i++) {
					if (indegree[i] == 0) {
						list.add(i);
					}
				}
				if (list.isEmpty()) {
					out.println("impossible");
					break;
				}
				int num = 0;
				while (num != n) {
					Collections.sort(list);
					int course = list.get(0);
					list.remove(0);
					num++;
					if (num != n) {
						out.print(course + 1 + " ");
					} else {
						out.println(course + 1);
					}
					for (int i = 0; i < courses[course].next.size(); i++) {
						if (--indegree[courses[course].next.get(i)] == 0) {
							list.add(courses[course].next.get(i));
						}
					}
				}
			}
		}
	}

	static class Node {
		ArrayList<Integer> pre;
		ArrayList<Integer> next;

		public Node(ArrayList<Integer> pre, ArrayList<Integer> next) {
			this.pre = pre;
			this.next = next;
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
