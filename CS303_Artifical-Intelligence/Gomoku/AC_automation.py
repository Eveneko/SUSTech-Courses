# AC自动机
class Node(object):
    def __init__(self):
        self.next = {}  # 相当于指针，指向树节点的下一层节点
        self.fail = None  # 失配指针，这个是AC自动机的关键
        self.isWord = False  # 标记，用来判断是否是一个标签的结尾
        self.word = ""  # 用来储存标签


class Automata(object):
    def __init__(self):
        self.root = Node()

    def add(self, word):
        temp_root = self.root
        for char in word:
            temp_root = temp_root.next.setdefault(char, Node())
        temp_root.isWord = True

    def make_fail(self):
        temp_que = [self.root]
        while len(temp_que) != 0:
            temp = temp_que.pop()
            p = None
            for key, value in temp.next.items():
                if temp == self.root:
                    temp.next[key].fail = self.root
                else:
                    p = temp.fail
                    while p is not None:
                        if key in p.next:
                            temp.next[key].fail = p.next[key]
                            break
                        p = p.fail
                    if p is None:
                        temp.next[key].fail = self.root
                temp_que.append(temp.next[key])

    def search(self, content):
        result = []
        goal = []
        start_word_index = 0
        for currentposition in range(len(content)):
            p = self.root
            word = content[currentposition]
            end_word_index = currentposition
            while word in p.next:
                if p == self.root:
                    start_word_index = currentposition
                p = p.next[word]
                if p.isWord:
                    result.append((start_word_index, end_word_index))
                if p.next and end_word_index + 1 < len(content):
                    end_word_index += 1
                    word = content[end_word_index]
                else:
                    break
                while word not in p.next and p != self.root:
                    p = p.fail
                    start_word_index += 1
                if p == self.root:
                    break
        for res in result:
            pattern = content[res[0]: res[1] + 1]
            if pattern not in goal:
                goal.append(pattern)
        return goal
