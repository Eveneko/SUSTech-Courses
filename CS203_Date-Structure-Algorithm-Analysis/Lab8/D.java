package Lab8;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;

public class D {
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
				ArrayList<Integer> graph[] = new ArrayList[n];
				for (int i = 0; i < graph.length; i++) {
					graph[i] = new ArrayList<Integer>();
				}
				for (int i = 0; i < m; i++) {
					int node1 = in.nextInt() - 1;
					int node2 = in.nextInt() - 1;
					graph[node1].add(node2);
					graph[node2].add(node1);
				}
				int cnt = 0;
				for (int i = 0; i < graph.length; i++) {
					for (int j = i + 1; j < graph.length; j++) {
						boolean pass1 = false;
						for (int j1 = 0; j1 < graph[j].size(); j1++) {
							if (graph[j].get(j1) == i) {
								pass1 = true;
								break;
							}
						}
						if (!pass1) {
							continue;
						}

						for (int k = j + 1; k < graph.length; k++) {
							boolean pass2 = false;
							boolean pass3 = false;
							for (int k1 = 0; k1 < graph[k].size(); k1++) {
								if (graph[k].get(k1) == i) {
									pass2 = true;
									break;
								}
							}
							for (int k2 = 0; k2 < graph[k].size(); k2++) {
								if (graph[k].get(k2) == j) {
									pass3 = true;
									break;
								}
							}
							if (!pass2 || !pass3) {
								continue;
							}

							for (int l = k + 1; l < graph.length; l++) {
								boolean pass4 = false;
								boolean pass5 = false;
								boolean pass6 = false;
								for (int l1 = 0; l1 < graph[l].size(); l1++) {
									if (graph[l].get(l1) == i) {
										pass4 = true;
										break;
									}
								}
								for (int l2 = 0; l2 < graph[l].size(); l2++) {
									if (graph[l].get(l2) == j) {
										pass5 = true;
										break;
									}
								}
								for (int l3 = 0; l3 < graph[l].size(); l3++) {
									if (graph[l].get(l3) == k) {
										pass6 = true;
										break;
									}
								}
								if (pass4 && pass5 && pass6) {
									cnt++;
								}

							}
						}
					}
				}
				out.println(cnt);
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
