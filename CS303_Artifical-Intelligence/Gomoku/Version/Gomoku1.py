import numpy as np
import random

random.seed(0)

# Chess
COLOR_BLACK = -1
COLOR_WHITE = 1
COLOR_NONE = 0

# Magic Number
BOARD_WIDTH = 15
HASH_ITEM_INDEX_MASK = 65535
RADIO = 1.2
DEPTH = 2

# patern score
score = {
    '11111': 50000,
    '011110': 4320,
    '011100': 720,
    '001110': 720,
    '011010': 720,
    '010110': 720,
    '11110': 720,
    '01111': 720,
    '11011': 720,
    '10111': 720,
    '11101': 720,
    '001100': 120,
    '001010': 120,
    '010100': 120,
    '000100': 20,
    '001000': 20
}

list1 = []  # me
list2 = []  # enemy
list3 = []  # all
list_blank = []  # blank


# don't change the class name
class AI(object):
    # chessboard_size, color, time_out passed from agent
    def __init__(self, chessboard_size, color, time_out):
        self.chessboard_size = chessboard_size
        # You are white or black
        self.color = color
        # the max time you should use, your algorithm's run time must not exceed the time limit.
        self.time_out = time_out
        # You need add your decision into your candidate_list. System will get the end of your candidate_list as your decision .
        self.candidate_list = []

    # judge judge/loss
    @staticmethod
    def judge(color, chessboard):
        chess = np.where(chessboard == color)
        chess = list(zip(chess[0], chess[1]))
        for x in chess:
            if (x[1] < BOARD_WIDTH - 4) and ((x[0], x[1]+1) in chess) and ((x[0], x[1]+2) in chess) and ((x[0], x[1]+3) in chess) and ((x[0], x[1]+4) in chess):
                return True
            if (x[0] < BOARD_WIDTH - 4) and ((x[0]+1, x[1]) in chess) and ((x[0]+2, x[1]) in chess) and ((x[0]+3, x[1]) in chess) and ((x[0]+4, x[1]) in chess):
                return True
            if (x[0] < BOARD_WIDTH - 4) and (x[1] < BOARD_WIDTH - 4) and ((x[0]+1, x[1]+1) in chess) and ((x[0]+2, x[1]+2) in chess) and ((x[0]+3, x[1]+3) in chess) and ((x[0]+4, x[1]+4) in chess):
                return True
            if (x[0] < BOARD_WIDTH - 4) and (x[1] > 3) and ((x[0]+1, x[1]-1) in chess) and ((x[0]+2, x[1]-2) in chess) and ((x[0]+3, x[1]-3) in chess) and ((x[0]+4, x[1]-4) in chess):
                return True
        return False

    @staticmethod
    def cal_score(x, y, my_list, enemy_list):
        res = 0
        all_shape = []
        global score
        global BOARD_WIDTH
        for dx, dy in [[0, 1], [1, 0], [1, 1], [1, -1]]:
            temp_shape = []
            temp_score = 0
            for offset in range(-5, 1):
                shape = ''
                for i in range(0, 6):
                    if (x + (i + offset) * dx < 0) | (x + (i + offset) * dx >= BOARD_WIDTH) | (y + (i + offset) * dy < 0) | (y + (i + offset) * dy >= BOARD_WIDTH):
                        shape = shape + '3'
                    elif (x + (i + offset) * dx, y + (i + offset) * dy) in enemy_list:
                        shape = shape + '2'
                    elif (x + (i + offset) * dx, y + (i + offset) * dy) in my_list:
                        shape = shape + '1'
                    else:
                        shape = shape + '0'
                for key in score:
                    if not (shape.find(key) == -1):
                        if score[key] > temp_score:
                            temp_score = score[key]
                            temp_shape = [[x + offset * dx, y + offset * dy], [x + (5 + offset) * dx, y + (5 + offset) * dy]]
            sameshape = 0
            for item in all_shape:
                if (not temp_shape) or (not item):
                    break
                if (temp_shape[0][0] == item[0][0]) and (temp_shape[0][1] == item[0][1]) and (temp_shape[1][0] == item[1][0]) and (temp_shape[1][1] == item[1][1]):
                    sameshape = 1
                    break
            if sameshape == 0:
                res += temp_score
                all_shape.append(temp_shape)
        return res

    # calculate the score
    def evaluation(self, is_me, my_list, enemy_list):
        my_score = 0
        for x in my_list:
            my_score = self.cal_score(x[0], x[1], my_list, enemy_list)
        enemy_score = 0
        for x in enemy_list:
            enemy_score = self.cal_score(x[0], x[1], enemy_list, my_list)
        total_score = my_score - enemy_score * RADIO
        return total_score

    @staticmethod
    def blank_order(blank_list):
        last_chess = list_blank[-1]
        for i in [-2, 2, 0, -1, 1]:
            for j in [-2, 2, 0, -1, 1]:
                if (i == 0) and (j == 0):
                    continue
                if (last_chess[0] + i, last_chess[1] + j) in blank_list:
                    blank_list.remove((last_chess[0] + i, last_chess[1] + j))
                    blank_list.insert(0, (last_chess[0] + i, last_chess[1] + j))

    @staticmethod
    def friend(x, chessboard):
        for i in range(-2, 3):
            for j in range(-2, 3):
                if i == 0 and j == 0:
                    continue
                if (x[0] + i >= 0) and (x[0] + i < BOARD_WIDTH) and (x[1] + j >= 0) and (x[1] + j < BOARD_WIDTH):
                    if not (chessboard[x[0] + i][x[1] + j] == 0):
                        return True
        return False

    # min-max, α-β
    def minmax(self, is_me, color, depth, alpha, beta, chessboard):
        global list1, list2, list3
        if is_me:
            my_list = list1
            enemy_list = list2
        else:
            my_list = list2
            enemy_list = list1

        # if self.judge(color, chessboard) or self.judge(-color, chessboard) or (depth == 0):
        #     return self.evaluation(is_me, my_list, enemy_list)
        if depth == 0:
            return self.evaluation(is_me, my_list, enemy_list)

        chess = np.where(chessboard == COLOR_NONE)
        blank_list = list(zip(chess[0], chess[1]))
        sorted_blank = []
        for x in blank_list:
            if is_me:
                my_list.append(x)
            else:
                enemy_list.append(x)
            val = self.evaluation(is_me, my_list, enemy_list)
            sorted_blank.append([x, val])
            if is_me:
                my_list.remove(x)
            else:
                enemy_list.remove(x)
        sorted_blank.sort(key=lambda blank: (blank[1]), reverse=True)

        for idx, x in enumerate(sorted_blank):
            if idx >= 20:
                break
            if is_me:
                list1.append(x[0])
            else:
                list2.append(x[0])
            # sorted_blank.append(x)
            chessboard[x[0][0]][x[0][1]] = color
            value = -(self.minmax(not is_me, -color, depth-1, -beta, -alpha, chessboard))
            if is_me:
                list1.remove(x[0])
            else:
                list2.remove(x[0])
            # sorted_blank.remove(x)
            chessboard[x[0][0]][x[0][1]] = 0

            if value > alpha:
                if depth == DEPTH:
                    self.candidate_list.append(x[0])
                    print(self.candidate_list)
                if value >= beta:
                    return beta
                alpha = value
        return alpha

    def first_step(self, chessboard):
        if self.color == COLOR_BLACK:
            emptyboard = np.zeros((BOARD_WIDTH, BOARD_WIDTH), dtype=np.int)
            if (chessboard == emptyboard).all():
                self.candidate_list.append([BOARD_WIDTH//2, BOARD_WIDTH//2])
                return False
        return True

    # The input is current chessboard.
    def go(self, chessboard):
        # Clear candidate_list
        self.candidate_list.clear()
        # ==================================================================
        global BOARD_WIDTH
        BOARD_WIDTH = self.chessboard_size
        global list1, list2, list3
        chess = np.where(chessboard == self.color)
        list1 = list(zip(chess[0], chess[1]))
        chess = np.where(chessboard == -self.color)
        list2 = list(zip(chess[0], chess[1]))
        list3 = list1 + list2
        if self.first_step(chessboard):
            self.minmax(True, self.color, DEPTH, -999999999, 999999999, chessboard)
        
        # ==============Find new pos========================================
        # Make sure that the position of your decision in chess board is empty.
        # If not, return error.
        # assert chessboard[new_pos[0], new_pos[1]] == COLOR_NONE
        # Add your decision into candidate_list, Records the chess board
