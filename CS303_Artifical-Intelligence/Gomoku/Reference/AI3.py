import numpy as np
import heapq
import math
import time
COLOR_BLACK = -1
COLOR_WHITE = 1
COLOR_NONE = 0
chessboard_len = 0
best_move = []
candidate_number = 5
DEPTH = 5


class Status:
    def __init__(self, chessboard: np.array, scope: list):
        self.chessboard = chessboard  # current_chessboard
        self.next_steps = []
        self.candidate = []
        self.scope = scope
        self.WIN = 0
        self.scores = {1: [0] * 72, -1: [0] * 72}
        self.allScore = {1: 0, -1: 0}
        self.ah = Ahocorasick()
        x = [*chess_pattern]
        for i in x:
            self.ah.addWord(i)
        self.ah.make()

    def append_next(self, pos):
        if self.chessboard[pos[0], pos[1]] == 0:
            self.next_steps.append([pos[0], pos[1]])

    def generate_next_step(self, position: list):
        if position[0] - 1 >= 0:
            self.append_next([position[0] - 1, position[1]])
        # if position[0] - 2 >= 0:
        #     self.append_next([position[0] - 2, position[1]])
        if position[0] + 1 < chessboard_len:
            self.append_next([position[0] + 1, position[1]])
        # if position[0] + 2 < chessboard_len:
        #     self.append_next([position[0] + 2, position[1]])
        if position[1] - 1 >= 0:
            self.append_next([position[0], position[1] - 1])
        # if position[1] - 2 >= 0:
        #     self.append_next([position[0], position[1] - 2])
        if position[1] + 1 < chessboard_len:
            self.append_next([position[0], position[1] + 1])
        # if position[1] + 2 < chessboard_len:
        #     self.append_next([position[0], position[1] + 2])
        if position[0] - 1 >= 0 and position[1] - 1 >= 0:
            self.append_next([position[0] - 1, position[1] - 1])
        # if position[0] - 2 >= 0 and position[1] - 2 >= 0:
        #     self.append_next([position[0] - 2, position[1] - 2])
        if position[0] + 1 < chessboard_len and position[1] - 1 >= 0:
            self.append_next([position[0] + 1, position[1] - 1])
        # if position[0] + 2 < chessboard_len and position[1] - 2 >= 0:
        #     self.append_next([position[0] + 2, position[1] - 2])
        if position[0] - 1 >= 0 and position[1] + 1 < chessboard_len:
            self.append_next([position[0] - 1, position[1] + 1])
        # if position[0] - 2 >= 0 and position[1] + 2 < chessboard_len:
        #     self.append_next([position[0] - 2, position[1] + 2])
        if position[0] + 1 < chessboard_len and position[1] + 1 < chessboard_len:
            self.append_next([position[0] + 1, position[1] + 1])
        # if position[0] + 2 < chessboard_len and position[1] + 2 < chessboard_len:
        #     self.append_next([position[0] + 2, position[1] + 2])

    def rem_repeat(self):
        next_steps = []
        for e in self.next_steps:
            if e not in next_steps:
                next_steps.append(e)
        self.next_steps = next_steps

    def add_candidates(self, color):
        self.candidate = []
        self.next_steps = []

        for x in range(self.scope[0], self.scope[1] + 1):
            for y in range(self.scope[2], self.scope[3] + 1):
                if self.chessboard[x, y] != 0:
                    self.generate_next_step([x, y])

        self.rem_repeat()

        for pos in self.next_steps:
            score = self.pos_score(pos)
            heapq.heappush(self.candidate, [score[color], 1, pos])
            heapq.heappush(self.candidate, [score[-color], -1, pos])

        self.candidate = heapq.nlargest(candidate_number, self.candidate)

    def record(self, res, color, chess_record):
        for chess_tuple in res:
            if chess_pattern[chess_tuple] == 'WIN5':
                self.WIN = color
            chess_record[color][chess_pattern[chess_tuple]] += 1

    def update_score(self, position):
        pos_x = position[0]
        pos_y = position[1]
        lines = {1: [''] * 4, -1: [''] * 4}

        for color in [1, -1]:
            map_pattern = {color: '1', -color: '2', 0: '0'}
            # vertical
            for x in range(chessboard_len):
                lines[color][0] += map_pattern[self.chessboard[x, pos_y]]
            # horizontal
            for y in range(chessboard_len):
                lines[color][1] += map_pattern[self.chessboard[pos_x, y]]
            #
            for x, y in zip(range(pos_x - min(pos_x, pos_y), chessboard_len),
                            range(pos_y - min(pos_x, pos_y), chessboard_len)):
                lines[color][2] += str(map_pattern[self.chessboard[x, y]])
            for x, y in zip(range(pos_x + min(pos_y, chessboard_len - 1 - pos_x), -1, -1),
                            range(pos_y - min(pos_y, chessboard_len - 1 - pos_x), chessboard_len)):
                lines[color][3] += str(map_pattern[self.chessboard[x, y]])

        line_score = {1: [0] * 4, -1: [0] * 4}
        for color, line in lines.items():
            for idx, val in enumerate(line):
                res = self.ah.search(val)
                for t in res:
                    line_score[color][idx] += pattern_score[chess_pattern[t]]

        a = pos_y
        b = chessboard_len + pos_x
        c = 2 * chessboard_len + (pos_y - pos_x + 10)
        d = 2 * chessboard_len + 21 + (pos_x + pos_y - 4)

        for color in [1, -1]:
            self.allScore[color] -= self.scores[color][a]
            self.allScore[color] -= self.scores[color][b]
            self.scores[color][a] = line_score[color][0]
            self.scores[color][b] = line_score[color][1]
            self.allScore[color] += self.scores[color][a]
            self.allScore[color] += self.scores[color][b]

        if -10 <= pos_y - pos_x <= 10:
            for color in [1, -1]:
                self.allScore[color] -= self.scores[color][c]
                self.scores[color][c] = line_score[color][2]
                self.allScore[color] += self.scores[color][c]

        if 4 <= pos_x + pos_y <= 24:
            for color in [1, -1]:
                self.allScore[color] -= self.scores[color][d]
                self.scores[color][d] = line_score[color][3]
                self.allScore[color] += self.scores[color][d]

    def evaluate_board_score(self):
        lines = {1: [''] * 72, -1: [''] * 72}

        for color in [1, -1]:
            map_pattern = {color: '1', -color: '2', 0: '0'}

            for x in range(chessboard_len):
                for y in range(chessboard_len):
                    if self.chessboard[x, y] != 0:
                        self.scope[0] = min(self.scope[0], x)
                        self.scope[1] = max(self.scope[1], x)
                        self.scope[2] = min(self.scope[2], y)
                        self.scope[3] = max(self.scope[3], y)

            # vertical
            for y in range(chessboard_len):
                for x in range(chessboard_len):
                    lines[color][y] += map_pattern[self.chessboard[x, y]]
            # horizontal
            for x in range(chessboard_len):
                for y in range(chessboard_len):
                    lines[color][x + chessboard_len] += map_pattern[self.chessboard[x, y]]
            #
            for i in range(-10, 11):
                idx = 2 * chessboard_len + 10 + i
                for y in range(chessboard_len):
                    x = y + i
                    if 0 <= y < chessboard_len and 0 <= x < chessboard_len:
                        lines[color][idx] += map_pattern[self.chessboard[x, y]]

            for i in range(4, 25):
                idx = 2 * chessboard_len + 21 + i - 4
                for x in range(chessboard_len):
                    y = i - x
                    if 0 <= y < chessboard_len and 0 <= x < chessboard_len:
                        lines[color][idx] += map_pattern[self.chessboard[x, y]]

        for color in [1, -1]:
            for i in range(72):
                res = self.ah.search(lines[color][i])
                for t in res:
                    self.scores[color][i] += pattern_score[chess_pattern[t]]

        for color in [1, -1]:
            for i in range(72):
                self.allScore[color] += self.scores[color][i]

    def update_new_scope(self, position):
        self.scope[0] = min(self.scope[0], position[0])
        self.scope[1] = max(self.scope[1], position[0])
        self.scope[2] = min(self.scope[2], position[1])
        self.scope[3] = max(self.scope[3], position[1])

    def pos_score(self, position):

        pos_x = position[0]
        pos_y = position[1]
        # horizontal
        chess_record = self.initialize_chess_record()
        # four direction has one chess_record
        for color in [1, -1]:
            self.chessboard[position[0], position[1]] = color
            map_pattern = {color: '1', -color: '2', 0: '0'}
            # horizontal
            text0 = ''
            for x in range(max(pos_x - 4, 0), min(pos_x + 5, chessboard_len)):
                text0 += map_pattern[self.chessboard[x, pos_y]]

            res = self.ah.search(text0)
            self.record(res, color, chess_record)
            # vertical
            text1 = ''
            for y in range(max(pos_y - 4, 0), min(pos_y + 5, chessboard_len)):
                text1 += map_pattern[self.chessboard[pos_x, y]]

            res = self.ah.search(text1)
            self.record(res, color, chess_record)
            # oblique left
            text2 = ''
            for i in range(-4, 5):
                if 0 <= pos_x + i < chessboard_len and 0 <= pos_y + i < chessboard_len:
                    text2 += map_pattern[self.chessboard[pos_x + i, pos_y + i]]
            res = self.ah.search(text2)
            self.record(res, color, chess_record)
            # Oblique right
            text3 = ''
            for i in range(-4, 5):
                if 0 <= pos_x + i < chessboard_len and 0 <= pos_y - i < chessboard_len:
                    text3 += map_pattern[self.chessboard[pos_x + i, pos_y - i]]
            res = self.ah.search(text3)
            self.record(res, color, chess_record)

            self.chessboard[position[0], position[1]] = 0

        score = self.compute_score(chess_record)
        return score

    @staticmethod
    def initialize_chess_record():
        chess_record = {1: {}, -1: {}}
        for key, val in pattern_score.items():
            for color in [1, -1]:
                chess_record[color][key] = 0
        return chess_record

    @staticmethod
    def compute_score(chess_record):
        score = {1: 0, -1: 0}
        for key, value in chess_record.items():
            if value['WIN5'] >= 1:
                score[key] += level_score['LevelOne']
            elif value['ALIVE4'] >= 1 or value['RUSH4'] >= 2 or (
                    value['RUSH4'] >= 1 and (value['ALIVE3'] >= 1 or value['TIAO3'])):
                score[key] += level_score['LevelTwo']
            elif value['ALIVE3'] + value['TIAO3'] >= 2:
                score[key] += level_score['LevelThree']
            elif (value['RUSH4'] >= 1 and value['SLEEP3'] >= 1) or (value['RUSH4'] >= 1 and value['TIAO3'] >= 1) or (
                    value['SLEEP3'] >= 1 and value['ALIVE2'] >= 4):
                score[key] += level_score['LevelFour']
            # elif value['RUSH4'] >= 1:
            #     score[key] += level_score['LevelFive']
            # elif value['TIAO3'] + value['SLEEP3'] >= 2:
            #     score[key] += level_score['LevelSix']
            # elif value['ALIVE2'] >= 2:
            #     score[key] += level_score['LevelSeven']
            # elif value['ALIVE3'] >= 1:
            #     score[key] += level_score['LevelEight']
            elif value['TIAO3'] + value['SLEEP3'] >= 2:
                score[key] += level_score['LevelFive']
            elif value['ALIVE2'] >= 2:
                score[key] += level_score['LevelSix']
            elif value['ALIVE3'] >= 1:
                score[key] += level_score['LevelSeven']
            elif value['RUSH4'] >= 1:
                score[key] += level_score['LevelEight']
            elif value['SLEEP3'] >= 1 or value['TIAO3'] >= 1:
                score[key] += level_score['LevelNine']
            elif value['ALIVE2'] + value['SLEEP2'] >= 2:
                score[key] += level_score['LevelTen']
            elif value['SLEEP2'] >= 2:
                score[key] += level_score['LevelEleven']
            else:
                continue
        return score


class AI(object):
    def __init__(self, chessboard_size: int, color: int, time_out):
        global chessboard_len
        chessboard_len = chessboard_size
        # if color == 1:
        #     global DEPTH
        #     DEPTH = 4
        self.startTime = time.time()
        self.game_tree = Game_Tree()
        self.chessboard_size = chessboard_size
        self.color = color
        self.time_out = time_out
        self.candidate_list = []
        self.candidate_last_score = -math.inf
        self.history_status = Status(np.zeros((15, 15)), [chessboard_len, 0, chessboard_len, 0])

    def go(self, chessboard: np.ndarray):
        now = time.time()
        if now - self.startTime > 150:
            global DEPTH
            DEPTH = 3
        self.candidate_list.clear()
        if len(np.where(self.history_status.chessboard != chessboard)[0]) == 0:
            self.candidate_list.append([7, 7])
            self.history_status.chessboard[7, 7] = -1
            self.history_status.update_score([7, 7])
            self.history_status.update_new_scope([7, 7])
        elif len(np.where(self.history_status.chessboard != chessboard)[0]) == 1:
            if chessboard[7, 7] == 0:
                pos = np.where(self.history_status.chessboard != chessboard)
                opponent_pos = [pos[0][0], pos[1][0]]
                self.history_status.chessboard[opponent_pos[0], opponent_pos[1]] = -self.color
                self.history_status.update_score(opponent_pos)
                self.history_status.update_new_scope(opponent_pos)
                self.candidate_list.append([7, 7])
                self.history_status.chessboard[7, 7] = self.color
                self.history_status.update_score([7, 7])
                self.history_status.update_new_scope([7, 7])
            else:
                if self.color == -1 and chessboard[6, 6] == 0:
                    self.candidate_list.append([6, 6])
                    pos = np.where(self.history_status.chessboard != chessboard)
                    opponent_pos = [pos[0][0], pos[1][0]]
                    self.history_status.chessboard[opponent_pos[0], opponent_pos[1]] = -self.color
                    self.history_status.update_score(opponent_pos)
                    self.history_status.update_new_scope(opponent_pos)
                    self.history_status.chessboard[6, 6] = self.color
                    self.history_status.update_score([6, 6])
                    self.history_status.update_new_scope([6, 6])
                elif self.color == -1 and chessboard[6, 8] == 0:
                    self.candidate_list.append([6, 8])
                    pos = np.where(self.history_status.chessboard != chessboard)
                    opponent_pos = [pos[0][0], pos[1][0]]
                    self.history_status.chessboard[opponent_pos[0], opponent_pos[1]] = -self.color
                    self.history_status.update_score(opponent_pos)
                    self.history_status.update_new_scope(opponent_pos)
                    self.history_status.chessboard[6, 8] = self.color
                    self.history_status.update_score([6, 8])
                    self.history_status.update_new_scope([6, 8])
                elif self.color == -1 and chessboard[8, 6] == 0:
                    self.candidate_list.append([8, 6])
                    pos = np.where(self.history_status.chessboard != chessboard)
                    opponent_pos = [pos[0][0], pos[1][0]]
                    self.history_status.chessboard[opponent_pos[0], opponent_pos[1]] = -self.color
                    self.history_status.update_score(opponent_pos)
                    self.history_status.update_new_scope(opponent_pos)
                    self.history_status.chessboard[8, 6] = self.color
                    self.history_status.update_score([8, 6])
                    self.history_status.update_new_scope([8, 6])
                elif self.color == -1 and chessboard[8, 8] == 0:
                    self.candidate_list.append([8, 8])
                    pos = np.where(self.history_status.chessboard != chessboard)
                    opponent_pos = [pos[0][0], pos[1][0]]
                    self.history_status.chessboard[opponent_pos[0], opponent_pos[1]] = -self.color
                    self.history_status.update_score(opponent_pos)
                    self.history_status.update_new_scope(opponent_pos)
                    self.candidate_list.append([8, 8])
                    self.history_status.chessboard[8, 8] = self.color
                    self.history_status.update_score([8, 8])
                    self.history_status.update_new_scope([8, 8])
                else:
                    pos = np.where(self.history_status.chessboard != chessboard)
                    opponent_pos = [pos[0][0], pos[1][0]]
                    self.history_status.chessboard[opponent_pos[0], opponent_pos[1]] = -self.color
                    self.history_status.update_score(opponent_pos)
                    self.history_status.update_new_scope(opponent_pos)
                    self.game_tree.minimax(self.history_status, DEPTH, -math.inf, math.inf, self.candidate_list, self.color)
                    # print()
                    self.history_status.chessboard[self.candidate_list[-1][0], self.candidate_list[-1][1]] = self.color
                    self.history_status.update_score(self.candidate_list[-1])
                    self.history_status.update_new_scope(self.candidate_list[-1])

        else:
            self.history_status.chessboard = chessboard
            self.history_status.evaluate_board_score()
            self.game_tree.minimax(self.history_status, DEPTH, -math.inf, math.inf, self.candidate_list, self.color)


chess_pattern = {'0111110': 'WIN5',
                 '111110': 'WIN5',
                 '011111': 'WIN5',
                 '111112': 'WIN5',
                 '211111': 'WIN5',
                 '11111': 'WIN5',
                 '011110': 'ALIVE4',
                 '01111': 'RUSH4',
                 '11110': 'RUSH4',
                 '11101': 'RUSH4',
                 '10111': 'RUSH4',
                 '11011': 'RUSH4',
                 '01110': 'ALIVE3',
                 '010110': 'TIAO3',
                 '011010': 'TIAO3',
                 '11100': 'SLEEP3',
                 '00111': 'SLEEP3',
                 '11010': 'SLEEP3',
                 '01011': 'SLEEP3',
                 '10110': 'SLEEP3',
                 '01101': 'SLEEP3',
                 '10011': 'SLEEP3',
                 '11001': 'SLEEP3',
                 '10101': 'SLEEP3',
                 '0110010': 'SLEEP3',
                 '0100110': 'SLEEP3',
                 '001100': 'ALIVE2',
                 '01010': 'ALIVE2',
                 '010010': 'ALIVE2',
                 '11000': 'SLEEP2',
                 '00011': 'SLEEP2',
                 '10100': 'SLEEP2',
                 '00101': 'SLEEP2',
                 '01001': 'SLEEP2',
                 '10010': 'SLEEP2',
                 '10001': 'SLEEP2',
                 }

level_score = {'LevelOne': 100000000,
               'LevelTwo': 1000000,
               'LevelThree': 10000,
               'LevelFour': 8000,
               'LevelFive': 5000,
               'LevelSix': 3000,
               'LevelSeven': 400,
               'LevelEight': 90,
               'LevelNine': 50,
               'LevelTen': 10,
               'LevelEleven': 9,
               'LevelTwelve': 5,
               'LevelThirteen': 2,
               'LevelFourteen': 1}
pattern_score = {'WIN5': 10000000,
                 'ALIVE4': 300000,
                 'RUSH4': 50000,
                 'ALIVE3': 30000,
                 'TIAO3': 25050,
                 'SLEEP3': 18000,
                 'ALIVE2': 17000,
                 'SLEEP2': 1000
                 }


class Node(object):
    def __init__(self):
        self.next = {}
        self.fail = None
        self.isWord = False


class Ahocorasick(object):
    def __init__(self):
        self.__root = Node()

    def addWord(self, word):
        tmp = self.__root
        for char in word:
            tmp = tmp.next.setdefault(char, Node())
        tmp.isWord = True

    def make(self):

        tmpQueue = []
        tmpQueue.append(self.__root)
        while len(tmpQueue) > 0:
            temp = tmpQueue.pop()
            p = None
            for k, v in temp.next.items():
                if temp == self.__root:
                    temp.next[k].fail = self.__root
                else:
                    p = temp.fail
                    while p is not None:
                        if k in p.next:
                            temp.next[k].fail = p.next[k]
                            break
                        p = p.fail
                    if p is None:
                        temp.next[k].fail = self.__root
                tmpQueue.append(temp.next[k])

    def search(self, content):
        result = []
        final = []
        startWordIndex = 0
        for currentPosition in range(len(content)):
            word = content[currentPosition]
            endWordIndex = currentPosition
            p = self.__root
            while word in p.next:
                if p == self.__root:
                    startWordIndex = currentPosition
                p = p.next[word]
                if p.isWord:
                    result.append((startWordIndex, endWordIndex))
                if p.next and endWordIndex + 1 < len(content):
                    endWordIndex += 1
                    word = content[endWordIndex]
                else:
                    break
                while (word not in p.next) and (p != self.__root):
                    p = p.fail
                    startWordIndex += 1
                if p == self.__root:
                    break
        for res in result:
            message = content[res[0]: res[1] + 1]
            if message not in final:
                final.append(message)
        return final

    def replace(self, content):

        replacepos = self.search(content)
        result = content
        for posindex in replacepos:
            result = result[0:posindex[0]] + (posindex[1] - posindex[0] + 1) * '*' + content[posindex[1] + 1:]
        return result


scoreboard = np.zeros((15, 15))


class Game_Tree(object):

    def minimax(self, status: Status, depth, alpha, beta, candidate, color):

        if status.allScore[color] >= 10000000:
            return 100000000
        if status.allScore[-color] >= 10000000:
            return -100000000

        if depth == 0:
            score = status.allScore[color] - status.allScore[-color]
            return score

        status.add_candidates(color)

        if depth == DEPTH and len(status.candidate) < 10:
            candidate.append(status.candidate[0][2])

        for pos in status.candidate:
            pos = pos[2]
            status.chessboard[pos[0], pos[1]] = color
            old_scope = status.scope
            status.update_new_scope(pos)
            status.update_score(pos)

            score = - self.minimax(status, depth - 1, -beta, -alpha, candidate, -color)
            # if depth == DEPTH:
            #     print("[pos:" + str(pos) + str(score) + "]")

            status.chessboard[pos[0], pos[1]] = 0
            status.scope = old_scope
            status.update_score(pos)

            if score > alpha:
                alpha = score
                if depth == DEPTH:
                    # print(pos)
                    candidate.append(pos)
            if alpha >= beta:
                return beta

        return alpha