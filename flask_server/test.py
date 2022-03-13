# -*- coding: utf-8 -*-
import ast
import subprocess
import sys
import json

if __name__ == '__main__':

    result = subprocess.check_output([sys.executable, 'naver_crawling.py', "test", '1'], shell=True, encoding='utf-8')
    result = result.split("\n")

    print(len(result[len(result)-1]))

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