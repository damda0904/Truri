import pandas as pd
import numpy as np
import re
import urllib.request
from tqdm import tqdm
from tensorflow.keras.preprocessing.text import Tokenizer
from tensorflow.keras.preprocessing.sequence import pad_sequences
from konlpy.tag import Okt
from tensorflow.keras.models import load_model
import json

def runModel(content) :
    okt = Okt()

    result = []
    for s in content:
        with open('../resource/wordIndex.json', 'r') as f:  # 저장한 워드인벡스 호출
            json_data = json.load(f)

        tokenizer = Tokenizer(num_words=28789)  # 피클데이터에서 가장 빈도가 높은 28789개의 단어만 선택하도록하는 Tokenizer 객체
        tokenizer.word_index = json_data
        s = tokenizer.texts_to_sequences([s])
        pad_new = pad_sequences(s, maxlen=1000)  # 패딩

        model = load_model("../resource/best_model.h5")  # 모델불러오기
        score = float(model.predict(pad_new))
        if (score > 0.5):
            result.append(round(score * 100, 0))
        else:
            result.append(round((1 - score) * 100, 0))

    return result