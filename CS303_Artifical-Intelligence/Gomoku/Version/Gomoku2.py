import numpy as np
import random

random.seed(0)

# Chess
COLOR_BLACK = -1
COLOR_WHITE = 1
COLOR_NONE = 0

# 棋形对应的分数
shape = {
    'h5': 20000,
    'h4': 2000,
    't4': 450,
    'h3': 450,
    'c4': 300,
    't22': 300,
    't3': 300,
    'tc4': 250,
    'h2': 100,
    'c3': 50,
    'tc3': 50,
    'c2': 20
}


class AI(object):
    def __init__(self, chessboard_size, color, time_out):
        self.chessboard_size = chessboard_size
        self.color = color
        self.time_out = time_out
        self.chessboard = None
        self.candidate_list = []
        self.DEPTH = 3
        self.RADIO = 1.2
        self.HASH_INDEX_MASK = 0xffff
        self.MAX_SCORE = -100000000
        self.sortedNum = 5
        self.empty = np.zeros((self.chessboard_size, self.chessboard_size), dtype=np.int)
        self.myScore = np.zeros((self.chessboard_size, self.chessboard_size), dtype=np.int)
        self.enemyScore = np.zeros((self.chessboard_size, self.chessboard_size), dtype=np.int)
        self.totalScore = np.zeros((self.chessboard_size, self.chessboard_size), dtype=np.int)
        self.level = np.zeros((self.chessboard_size, self.chessboard_size), dtype=np.int)

    def go(self, chessboard):
        self.MAX_SCORE = -100000000
        self.candidate_list.clear()
        self.chessboard = chessboard
        self.empty = np.zeros((self.chessboard_size, self.chessboard_size), dtype=np.int)
        self.myScore = np.zeros((self.chessboard_size, self.chessboard_size), dtype=np.int)
        self.enemyScore = np.zeros((self.chessboard_size, self.chessboard_size), dtype=np.int)
        self.totalScore = np.zeros((self.chessboard_size, self.chessboard_size), dtype=np.int)
        self.level = np.zeros((self.chessboard_size, self.chessboard_size), dtype=np.int)
        for i in range(self.chessboard_size):
            for j in range(self.chessboard_size):
                tmp = self.chessboard_size // 2 - \
                      max(abs(i - self.chessboard_size // 2), abs(j - self.chessboard_size // 2))
                self.level[i][j] = tmp
        if self.is_empty():
            self.candidate_list.append([self.chessboard_size // 2, self.chessboard_size // 2])
        else:
            idx = np.where(chessboard == COLOR_NONE)
            idx = list(zip(idx[0], idx[1]))
            if len(idx)<10:
                pos_idx = random.randint(0, len(idx)-1)
                new_pos = idx[pos_idx]
                self.candidate_list.append(new_pos)
            else:
                self.minimax(self.color, self.DEPTH)
        print(self.candidate_list)

    def minimax(self, color, depth, alpha=-100000000, beta=100000000):
        if depth <= 0:
            return self.evaluation_global()
        blanklist = self.next_step()
        if color == self.color:
            mytmpscore = 0
            enemytmpscore = 0
            step = []
            for i in blanklist:
                x = i[0]
                y = i[1]
                if mytmpscore < self.myScore[x][y]:
                    mytmpscore = self.myScore[x][y]
                    step = [x, y]
                if enemytmpscore < self.enemyScore[x][y]:
                    enemytmpscore = self.enemyScore[x][y]
            if mytmpscore >= enemytmpscore and mytmpscore >= shape['h5']:
                alpha = shape['h5']
                if depth == self.DEPTH:
                    self.MAX_SCORE = shape['h5']
                    self.candidate_list.append(step)
                return alpha
            elif enemytmpscore >= mytmpscore and enemytmpscore >= shape['h5']:
                for i in blanklist:
                    x = i[0]
                    y = i[1]
                    if enemytmpscore == self.enemyScore[x][y]:
                        self.chessboard[x][y] = color
                        self.update_socre(x, y)
                        score = self.minimax(-color, depth - 1, alpha, beta)
                        if depth == self.DEPTH and score > self.MAX_SCORE:
                            self.candidate_list.append([x, y])
                            self.MAX_SCORE = score
                        self.chessboard[x][y] = COLOR_NONE
                        self.update_socre(x, y)
                        if score > alpha:
                            alpha = score
                        if score >= beta:
                            return alpha
                return alpha
            for i in blanklist:
                x = i[0]
                y = i[1]
                self.chessboard[x][y] = color
                self.update_socre(x, y)
                score = self.minimax(-color, depth - 1, alpha, beta)
                if depth == self.DEPTH and score > self.MAX_SCORE:
                    self.candidate_list.append([x, y])
                    self.MAX_SCORE = score
                self.chessboard[x][y] = COLOR_NONE
                self.update_socre(x, y)
                if score > alpha:
                    alpha = score
                if score >= beta:
                    return alpha
            return alpha
        else:
            mytmpscore = 0
            enemytmpscore = 0
            for i in blanklist:
                x = i[0]
                y = i[1]
                if mytmpscore < self.myScore[x][y]:
                    mytmpscore = self.myScore[x][y]
                    step = [x, y]
                if enemytmpscore < self.enemyScore[x][y]:
                    enemytmpscore = self.enemyScore[x][y]
            if mytmpscore <= enemytmpscore and enemytmpscore >= shape['h5']:
                beta = -shape['h5']
                return beta
            elif mytmpscore >= enemytmpscore and mytmpscore >= shape['h5']:
                for i in blanklist:
                    x = i[0]
                    y = i[1]
                    if mytmpscore == self.myScore[x][y]:
                        self.chessboard[x][y] = color
                        self.update_socre(x, y)
                        score = self.minimax(-color, depth - 1, alpha, beta)
                        self.chessboard[x][y] = COLOR_NONE
                        self.update_socre(x, y)
                        if score < beta:
                            beta = score
                        if alpha >= beta:
                            return beta
                return beta
            for i in blanklist:
                x = i[0]
                y = i[1]
                self.chessboard[x][y] = color
                self.update_socre(x, y)
                score = self.minimax(-color, depth - 1, alpha, beta)
                self.chessboard[x][y] = COLOR_NONE
                self.update_socre(x, y)
                if score < beta:
                    beta = score
                if alpha >= beta:
                    return beta
            return beta

    def is_empty(self):
        for i in range(self.chessboard_size):
            for j in range(self.chessboard_size):
                if self.chessboard[i][j] != COLOR_NONE:
                    return False
        return True

    def evaluation_point(self, x, y, color):
        score = 0
        shapeCnt = {
            'h5': 0,
            'h4': 0,
            't4': 0,
            'h3': 0,
            'c4': 0,
            't22': 0,
            't3': 0,
            'tc4': 0,
            'h2': 0,
            'c3': 0,
            'tc3': 0,
            'c2': 0
        }
        self.evaluation_dir(x, y, color, shapeCnt)
        cnt4 = shapeCnt['c4'] + shapeCnt['tc4'] + shapeCnt['t4'] + shapeCnt['t22']
        cnt3 = shapeCnt['h3'] + shapeCnt['t3']
        if shapeCnt['h5'] > 0:
            return shape['h5']
        elif shapeCnt['h4'] > 0:
            return shape['h4'] + 100
        elif cnt4 >= 2:
            return shape['h4'] + 100
        elif cnt4 and cnt3:
            return shape['h4'] + 100
        elif cnt3 >= 2:
            return shape['h4'] + 0
        else:
            score += shape['h5'] * shapeCnt['h5'] + shape['h4'] * shapeCnt['h4'] + shape['t4'] * shapeCnt['t4'] \
                     + shape['h3'] * shapeCnt['h3'] + shape['c4'] * shapeCnt['c4'] + shape['t22'] * shapeCnt['t22'] \
                     + shape['t3'] * shapeCnt['t3'] + shape['tc4'] * shapeCnt['tc4'] + shape['h2'] * shapeCnt['h2'] \
                     + shape['c3'] * shapeCnt['c3'] + shape['tc3'] * shapeCnt['tc3'] + shape['c2'] * shapeCnt['c2']
        return score

    def evaluation_global(self):
        score = 0
        for i in range(self.chessboard_size):
            for j in range(self.chessboard_size):
                if self.chessboard[i][j] == COLOR_NONE:
                    score += self.myScore[i][j] - self.enemyScore[i][j] * self.RADIO
        return score

    def evaluation_dir(self, x, y, color, shapeCnt):
        # ——
        line = np.zeros(self.chessboard_size)
        for i in range(self.chessboard_size):
            line[i] = self.chessboard[x][i]
        self.evaluation_line(self.chessboard_size, y, color, line, shapeCnt)
        # |
        line = np.zeros(self.chessboard_size)
        for i in range(self.chessboard_size):
            line[i] = self.chessboard[i][y]
        self.evaluation_line(self.chessboard_size, x, color, line, shapeCnt)
        # /
        line = np.zeros(self.chessboard_size)
        i0 = j0 = 0
        if x > y:
            j0 = x - y
        else:
            i0 = y - x
        k = 0
        for k in range(self.chessboard_size):
            if (i0 + k > self.chessboard_size - 1) or (j0 + k > self.chessboard_size - 1):
                break
            line[k] = self.chessboard[j0 + k][i0 + k]
        for l in range(k, self.chessboard_size):
            line[l] = 2
        self.evaluation_line(k, y - i0, color, line, shapeCnt)
        # \
        line = np.zeros(self.chessboard_size)
        if self.chessboard_size - 1 - x < y:
            i0 = x + y - (self.chessboard_size - 1)
            j0 = self.chessboard_size - 1
        else:
            i0 = 0
            j0 = x + y
        k = 0
        for k in range(self.chessboard_size):
            if (i0 + k > self.chessboard_size - 1) or (j0 - k < 0):
                break
            line[k] = self.chessboard[j0 - k][i0 + k]
        for l in range(k, self.chessboard_size):
            line[l] = 2
        self.evaluation_line(k, y - i0, color, line, shapeCnt)

    def evaluation_line(self, size, pos, color, line, shapeCnt):
        if size < 5:
            return
        leftchess = np.zeros(2)
        leftblank = np.zeros(2)
        rightchess = np.zeros(2)
        rightblank = np.zeros(2)
        leftpointer = pos - 1
        for i in range(2):
            while leftpointer >= 0 and line[leftpointer] == color:
                leftpointer -= 1
                leftchess[i] += 1
            while leftpointer >= 0 and line[leftpointer] == COLOR_NONE:
                leftpointer -= 1
                leftblank[i] += 1
        rightpointer = pos + 1
        for i in range(2):
            while rightpointer < self.chessboard_size and line[rightpointer] == color:
                rightpointer += 1
                rightchess[i] += 1
            while rightpointer < self.chessboard_size and line[rightpointer] == COLOR_NONE:
                rightpointer += 1
                rightblank[i] += 1
        cnt = 1 + leftchess[0] + rightchess[0]
        if cnt >= 5:
            shapeCnt['h5'] += 1
            return
        elif cnt == 4:
            if leftblank[0] > 0 and rightblank[0] > 0:
                shapeCnt['h4'] += 1
                return
            if leftblank[0] > 0 or leftblank[0] > 0:
                shapeCnt['c4'] += 1
                return
        elif cnt == 3:
            if (leftblank[0] > 0 and leftchess[1] > 0 and leftblank[1] > 0 and rightblank[1] > 0) \
                    or (rightblank[0] > 0 and rightchess[1] > 0 and rightblank[1] > 0 and leftblank[1] > 0):
                shapeCnt['t4'] += 1
                return
            if (leftblank[0] > 0 and leftchess[1] >= 1) or (rightblank[0] > 0 and rightchess[1] >= 1):
                shapeCnt['tc4'] += 1
                return
            if leftblank[0] > 0 and rightblank[0] > 0 and leftblank[0] + rightblank[0] >= 3:
                shapeCnt['h3'] += 1
                return
            if leftblank[0] > 0 or rightblank[0] > 0:
                shapeCnt['c3'] += 1
                return
        elif cnt == 2:
            if (leftblank[0] == 1 and leftchess[1] >= 2) or (rightblank[0] == 1 and rightchess[1] >= 2):
                shapeCnt['t22'] += 1
                return
            if (leftblank[0] == 1 and leftchess[0] == 1 and rightblank[0] == 1 and leftchess[1] >= 1) \
                    or (rightblank[0] == 1 and rightchess[0] == 1 and leftblank[0] == 1 and rightchess[1] >= 1):
                shapeCnt['t3'] += 1
                return
            if (leftblank[0] == 1 and leftchess[1] == 1 and rightblank[0] + leftblank[1] >= 1) or \
                    (rightblank[0] == 1 and rightchess[1] == 1 and rightblank[1] + leftblank[0] >= 1):
                shapeCnt['c3'] += 1
                return
            if leftblank[0] > 0 and rightblank[0] > 0 and (leftblank[0] + rightblank[0] >= 4):
                shapeCnt['h2'] += 1
                return
            if (leftblank[0] > 0 and leftchess[1] >= 1) or (rightblank[0] > 0 and rightchess[1] >= 1):
                shapeCnt['tc3'] += 1
                return
            if leftblank[0] >= 3 or rightblank[0] >= 3:
                shapeCnt['c2'] += 1
                return
        elif cnt == 1:
            if (leftblank[0] > 0 and rightblank[0] == 1 and rightchess[1] >= 3 and rightblank[1] > 0) \
                    or (rightblank[0] > 0 and leftblank[0] == 1 and leftchess[1] >= 3 and leftblank[1] > 0):
                shapeCnt['t4'] += 1
                return
            if (leftblank[0] > 0 and rightblank[0] == 1 and rightchess[1] >= 2 and rightblank[1] > 0) \
                    or (rightblank[0] > 0 and leftblank[0] == 1 and leftchess[1] >= 2 and leftblank[1] > 0):
                shapeCnt['t3'] += 1
                return
            if (rightblank[0] > 0 and leftblank[0] == 1 and leftchess[1] == 3) \
                    or (leftblank[0] > 0 and rightblank[0] == 1 and rightchess[1] == 3):
                shapeCnt['tc4'] += 1
                return
            if (rightblank[0] > 0 and leftblank[0] == 1 and leftchess[1] == 2) \
                    or (leftblank[0] > 0 and rightblank[0] == 1 and rightchess[1] == 2):
                shapeCnt['tc3'] += 1
                return
            if (rightblank[0] == 1 and rightchess[1] == 2 and leftblank[0] + rightblank[1] >= 1) or \
                    (leftblank[0] == 1 and leftchess[1] == 2 and rightblank[0] + leftblank[1] >= 1):
                shapeCnt['c3'] += 1
                return
            if (leftblank[0] == 1 and leftchess[1] == 1 and rightblank[0] + leftblank[1] >= 3 and
                rightblank[0] >= 1 and leftblank[1] >= 1) or (rightblank[0] == 1 and rightchess[1] == 1
                                                              and leftblank[0] + rightblank[1] >= 3
                                                              and leftblank[0] >= 1 and rightblank[1] >= 1):
                shapeCnt['c2'] += 1
                return
            if (leftblank[0] == 2 and leftchess[1] == 1 and rightblank[0] >= 1 and leftblank[1] >= 1) or \
                    (rightblank[0] == 2 and rightchess[1] == 1 and leftblank[0] >= 1 and rightblank[1] >= 1):
                shapeCnt['c2'] += 1
                return

    def next_step(self):
        blanklist = []
        sortedlist = []
        if self.totalScore.all() == self.empty.all():
            for i in range(self.chessboard_size):
                for j in range(self.chessboard_size):
                    if self.chessboard[i][j] == COLOR_NONE:
                        myPointVal = self.evaluation_point(i, j, self.color)
                        self.myScore[i][j] = myPointVal
                        enemyPointVal = self.evaluation_point(i, j, -self.color)
                        self.enemyScore[i][j] = enemyPointVal
                        self.totalScore[i][j] = myPointVal + enemyPointVal
                        sortedlist.append((self.totalScore[i][j] + self.level[i][j], i, j))
        else:
            for i in range(self.chessboard_size):
                for j in range(self.chessboard_size):
                    if self.chessboard[i][j] == COLOR_NONE:
                        sortedlist.append((self.totalScore[i][j] + self.level[i][j], i, j))
        sortedlist.sort(reverse=True)
        cnt = 0
        for step in sortedlist:
            if cnt >= self.sortedNum:
                break
            blanklist.append([step[1], step[2]])
            cnt += 1
        return blanklist

    def update_socre(self, x, y):
        dirc = [
            [0, 1], [1, 1], [1, 0], [1, -1],
            [0, -1], [-1, -1], [-1, 0], [-1, 1]
        ]
        for i in range(len(dirc)):
            x0 = x + dirc[i][0]
            y0 = y + dirc[i][1]
            cnt = 0
            while 0 <= x0 < self.chessboard_size and 0 <= y0 < self.chessboard_size and cnt < 5:
                if self.chessboard[x0][y0] == COLOR_NONE:
                    myPointVal = self.evaluation_point(x0, y0, self.color)
                    self.myScore[x0][y0] = myPointVal
                    enemyPointVal = self.evaluation_point(x0, y0, -self.color)
                    self.enemyScore[x0][y0] = enemyPointVal
                    self.totalScore[x0][y0] = myPointVal + enemyPointVal
                    cnt += 1
                x0 = x0 + dirc[i][0]
                y0 = y0 + dirc[i][1]
