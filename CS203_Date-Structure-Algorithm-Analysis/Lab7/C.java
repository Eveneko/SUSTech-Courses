package Lab7;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;
import java.util.StringTokenizer;

public class C {
	public static void main(String[] args) {
		InputStream inputStream = System.in;
		OutputStream outputStream = System.out;
		InputReader in = new InputReader(inputStream);
		PrintWriter out = new PrintWriter(outputStream);
		Task solver = new Task();
		solver.solve(in, out);
		out.close();
	}

	static class Task {

		public void solve(InputReader in, PrintWriter out) {
			int casenum = in.nextInt();
			for (int t = 1; t < casenum + 1; t++) {
				int n = in.nextInt();
				// build tree
//				TreeNode[] AVL = new TreeNode[n + 1];
//				for (int i = 1; i < AVL.length; i++) {
//					int index = in.nextInt();
//					AVL[i] = new TreeNode(index, 0, null, null, null);
//				}
//				boolean isBST = true;
//				for (int i = 1; i < n + 1; i++) {
//					int left = in.nextInt();
//					int right = in.nextInt();
//					if (AVL[i] != null) {
//						if (AVL[left] != null) {
//							AVL[i].setLeft(AVL[left]);
//							AVL[left].parent = AVL[i];
//						} else {
//							if (left == 0) {
//								AVL[i].setLeft(null);
//							} else {
//								isBST = false;
//							}
//						}
//					} else {
//						isBST = false;
//					}
//
//					if (AVL[i] != null) {
//						if (AVL[right] != null) {
//							AVL[i].setRight(AVL[left]);
//							AVL[right].parent = AVL[i];
//						} else {
//							if (right == 0) {
//								AVL[i].setRight(null);
//							} else {
//								isBST = false;
//							}
//						}
//					} else {
//						isBST = false;
//					}
//
//				}
//
//				if (!isBST) {
//					out.println("No");
//					continue;
//				}
//
//				// find root
//				TreeNode root = null;
//				for (int i = 1; i < AVL.length; i++) {
//					if (AVL[i].parent == null) {
//						root = AVL[i];
//						break;
//					}
//				}

				TreeNode[] nodes = new TreeNode[n + 1];
				for (int i = 1; i < nodes.length; i++)
					nodes[i] = new TreeNode(in.nextInt(), 0, null, null, null);
				boolean[] isNotRootNode = new boolean[n + 1];
				int l, r;
				for (int i = 1; i < nodes.length; i++) {
					l = in.nextInt();
					r = in.nextInt();
					isNotRootNode[l] = true;
					isNotRootNode[r] = true;
					nodes[i].left = l == 0 ? null : nodes[l];
					nodes[i].right = r == 0 ? null : nodes[r];
				}

				// find root
				TreeNode root = null;
				for (int i = 1; i < isNotRootNode.length; i++) {
					if (!isNotRootNode[i]) {
						root = nodes[i];
						break;
					}
				}

				// judge
				boolean isAVL = true;
				height(root);
				Queue<TreeNode> queue = new LinkedList<>();
				queue.offer(root);
				while (!queue.isEmpty()) {
					TreeNode node = queue.poll();
					if (node.left != null && node.right != null) {
						if (Math.abs(node.left.height - node.right.height) > 1) {
							isAVL = false;
							break;
						}
					}
					if (node.left == null && node.right != null && node.right.height > 1) {
						isAVL = false;
						break;
					}
					if (node.left != null && node.right == null && node.left.height > 1) {
						isAVL = false;
						break;
					}
					if (node.left != null) {
						queue.offer(node.left);
					}
					if (node.right != null) {
						queue.offer(node.right);
					}

				}
				if (isAVL && isBST(root, nodes)) {
					out.println("Yes");
				} else {
					out.println("No");
				}
			}
		}

		public void height(TreeNode node) {
			if (node != null) {
				height(node.left);
				height(node.right);
				if (node.left == null && node.right == null) {
					node.height = 1;
				}
				if (node.left != null && node.left.height != 0 && node.right == null) {
					node.height = node.left.height + 1;
				}
				if (node.right != null && node.right.height != 0 && node.left == null) {
					node.height = node.right.height + 1;
				}
				if (node.left != null && node.left.height != 0 && node.right != null && node.right.height != 0) {
					node.height = node.left.height > node.right.height ? node.left.height + 1 : node.right.height + 1;
				}
			}
		}

		public boolean isBST(TreeNode root, TreeNode[] BST) {
			if (root == null) {
				return true;
			}
			Stack<TreeNode> stack = new Stack<>();
			TreeNode pre = null;
			while (root != null || !stack.isEmpty()) {
				while (root != null) {
					stack.push(root);
					root = root.left;
				}
				root = stack.pop();
				if (pre != null && root.value <= pre.value) {
					return false;
				}
				pre = root;
				root = root.right;
			}
			return true;
		}

		public class TreeNode {
			int value;
			int height;
			TreeNode left;
			TreeNode right;
			TreeNode parent;

			public TreeNode(int value, int height, TreeNode left, TreeNode right, TreeNode parent) {
				this.value = value;
				this.height = height;
				this.left = left;
				this.right = right;
				this.parent = parent;
			}

			public void setLeft(TreeNode left) {
				this.left = left;
			}

			public void setRight(TreeNode right) {
				this.right = right;
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