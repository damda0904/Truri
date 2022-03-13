# -*- coding: utf-8 -*-
import ast
import subprocess
import sys
import json

from naver_crawling import naver_crawling


def subprocess_test():
    result = subprocess.check_output([sys.executable, 'naver_crawling.py', "test", '4'], shell=True, encoding='utf-8')
    print(result)

    result = result.split("\n")

    print(len(result))

    # items = []
    # for item in result:
    #     print(item)
    #     try :
    #         tmp = ast.literal_eval(item)
    #         print(type(tmp))
    #         items.append(tmp)
    #     except Exception as e :
    #         print(e)

    print("subprocess finished")

def naver_crawling_test():
    result = naver_crawling("test", 3)
    for item in result:
        print(item)

if __name__ == '__main__':
    naver_crawling_test()
    # subprocess_test()
