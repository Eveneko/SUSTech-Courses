import numpy as np
from graphics import *

GRID_WIDTH = 20
COLUMN = 15
ROW = 15

list1 = []  # black
list2 = []  # white
list3 = []  # all
chessboard = np.zeros((15, 15))

list_all = []  # 整个棋盘的


def game_win(list):
    for m in range(COLUMN):
        for n in range(ROW):

            if n < ROW - 4 and (m, n) in list and (m, n + 1) in list and (m, n + 2) in list and (
                    m, n + 3) in list and (m, n + 4) in list:
                return True
            elif m < ROW - 4 and (m, n) in list and (m + 1, n) in list and (m + 2, n) in list and (
                    m + 3, n) in list and (m + 4, n) in list:
                return True
            elif m < ROW - 4 and n < ROW - 4 and (m, n) in list and (m + 1, n + 1) in list and (
                    m + 2, n + 2) in list and (m + 3, n + 3) in list and (m + 4, n + 4) in list:
                return True
            elif m < ROW - 4 and n > 3 and (m, n) in list and (m + 1, n - 1) in list and (
                    m + 2, n - 2) in list and (m + 3, n - 3) in list and (m + 4, n - 4) in list:
                return True
    return False


def gobangwin():
    win = GraphWin("this is a gobang game", 320, 320)  
    win.setBackground("pink")
    i1 = 20

    while i1 < 321:
        l = Line(Point(i1, 20), Point(i1, 300))
        l.draw(win)
        i1 = i1 + 20

    i2 = 20

    while i2 < 321:
        l = Line(Point(20, i2), Point(300, i2))
        l.draw(win)
        i2 = i2 + 20
    return win

def main():
    win = gobangwin()
    for i in range(COLUMN + 1):
        for j in range(ROW + 1):
            list_all.append((i, j))

    change = 0
    g = 0

    while g == 0:

        if change % 2 == 1:
            p2 = win.getMouse()

            if not ((round((p2.getX()) / GRID_WIDTH), round((p2.getY()) / GRID_WIDTH)) in list3):

                a2 = round((p2.getX()) / GRID_WIDTH - 1)
                b2 = round((p2.getY()) / GRID_WIDTH - 1)
                print("HU pos: " + str([a2, b2]))

                chessboard[a2, b2] = 1
                list1.append((a2, b2))
                list3.append((a2, b2))

                piece = Circle(Point(GRID_WIDTH * (a2 + 1), GRID_WIDTH * (b2 + 1)), 8)
                piece.setFill('white')
                piece.draw(win)
                if game_win(list1):
                    message = Text(Point(100, 100), "white win.")
                    message.draw(win)
                    g = 1

                change = change + 1

        else:
            p2 = win.getMouse()

            if not ((round((p2.getX()) / GRID_WIDTH), round((p2.getY()) / GRID_WIDTH)) in list3):

                a2 = round((p2.getX()) / GRID_WIDTH - 1)
                b2 = round((p2.getY()) / GRID_WIDTH - 1)
                print("HU pos: " + str([a2, b2]))

                chessboard[a2, b2] = 1
                list2.append((a2, b2))
                list3.append((a2, b2))

                piece = Circle(Point(GRID_WIDTH * (a2 + 1), GRID_WIDTH * (b2 + 1)), 8)
                piece.setFill('black')
                piece.draw(win)
                if game_win(list2):
                    message = Text(Point(100, 100), "black win.")
                    message.draw(win)
                    g = 1

                change = change + 1

    message = Text(Point(100, 120), "Click anywhere to quit.")
    message.draw(win)
    win.getMouse()
    win.close()


main()
