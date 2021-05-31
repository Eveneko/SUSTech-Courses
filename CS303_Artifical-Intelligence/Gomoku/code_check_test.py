import sys
import time
from code_check import CodeCheck


def main():
    code_checker = CodeCheck("Gomoku_stable.py", 15)
    # code_checker = CodeCheck("Gomoku5.py", 15)
    starttime = time.time()
    if not code_checker.check_code():
        print(code_checker.errormsg)
    else:
        print('pass')
    print(time.time()-starttime, end='s\n')


if __name__ == '__main__':
    main()
