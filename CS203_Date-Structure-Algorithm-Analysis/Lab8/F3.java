package Lab8;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class F3 {
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
				Node[] nodes = new Node[n + 1];
				for (int i = 0; i < n + 1; i++) {
					nodes[i] = new Node(i, 0, 0, 0, new ArrayList<Integer>(), new ArrayList<Integer>());
				}
				for (int i = 0; i < m; i++) {
					int x = in.nextInt();
					int y = in.nextInt();
					int z = in.nextInt();
					nodes[y].parent.add(x);
					nodes[y].weight.add(z);
					nodes[x].outdegree++;
					nodes[y].indegree++;

				}
				Queue<Integer> queue1 = new LinkedList<>();
				Queue<Integer> queue = new LinkedList<>();
				for (int i = 1; i < n + 1; i++) {
					if (nodes[i].outdegree == 0) {
						queue.offer(i);
					}
					if (nodes[i].indegree == 0) {
						queue1.offer(i);
					}
				}
				while (!queue1.isEmpty()) {
					int node = queue1.poll();
					nodes[node].parent.add(0);
					nodes[node].weight.add(0);
					nodes[node].indegree++;
					nodes[0].outdegree++;
				}
				while (!queue.isEmpty()) {
					Node node = nodes[queue.poll()];
					for (int i = 0; i < node.parent.size(); i++) {
						if (nodes[node.parent.get(i)].dist < node.dist + node.weight.get(i)) {
							nodes[node.parent.get(i)].dist = node.dist + node.weight.get(i);
						}
						nodes[node.parent.get(i)].outdegree--;
						if (nodes[node.parent.get(i)].outdegree == 0) {
							queue.offer(nodes[node.parent.get(i)].value);
						}
					}
				}
				out.println(nodes[0].dist);
			}
		}
	}

	static class Node {
		int value;
		long dist;
		int indegree;
		int outdegree;
		ArrayList<Integer> weight;
		ArrayList<Integer> parent;

		public Node(int value, long dist, int indegree, int outdegree, ArrayList<Integer> weight,
				ArrayList<Integer> parent) {
			this.value = value;
			this.dist = dist;
			this.indegree = indegree;
			this.outdegree = outdegree;
			this.weight = weight;
			this.parent = parent;
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
