from flask import Flask, request

import re
from konlpy.tag import Okt
from tensorflow.keras.preprocessing.text import Tokenizer
from tensorflow.keras.preprocessing.sequence import pad_sequences
import tensorflow as tf
import os
import json

from .model import multiHeadAttention, TransformerBlock, TokenAndPositionEmbedding

app = Flask(__name__)

@app.route("/", methods=['POST'])
def runModel():

    content = request.get_json()['content']
    print(content)

    print(">>>runModel start ------------------------------------------")

    vocab_size = 30000  # 빈도수 상위 2만개의 단어만 사용
    max_len = 1000  # 문장의 최대 길이

    embedding_dim = 32  # 각 단어의 임베딩 벡터의 차원
    num_heads = 2  # 어텐션 헤드의 수
    dff = 32  # 포지션 와이즈 피드 포워드 신경망의 은닉층의 크기

    inputs = tf.keras.layers.Input(shape=(max_len,))
    embedding_layer = TokenAndPositionEmbedding(max_len, vocab_size, embedding_dim)
    x = embedding_layer(inputs)
    transformer_block = TransformerBlock(embedding_dim, num_heads, dff)
    x = transformer_block(x)
    x = tf.keras.layers.GlobalAveragePooling1D()(x)
    x = tf.keras.layers.Dropout(0.2)(x)
    x = tf.keras.layers.Dense(20, activation="relu")(x)
    x = tf.keras.layers.Dropout(0.2)(x)
    outputs = tf.keras.layers.Dense(2, activation="softmax")(x)
    my_model = tf.keras.Model(inputs=inputs, outputs=outputs)

    my_model.load_weights(os.path.join('./resource/dacon_try', 'tf_chkpoint.ckpt'))

    print(">>>runModel finish ------------------------------------------")

    okt = Okt()

    my_model.load_weights(os.path.join('./resource/dacon_try', 'tf_chkpoint.ckpt'))  # 모델 로드(폴더 경로를 넣어줘)

    s = content
    stopwords = ['의', '가', '이', '은', '들', '는', '좀', '잘', '걍', '과', '도', '를', '으로', '자', '에', '와', '한', '하다']
    s = re.sub(r'[^ㄱ-ㅎㅏ-ㅣ가-힣 ]', '', s)
    s = okt.morphs(s, stem=True)  # 토큰화
    s = [word for word in s if not word in stopwords]  # 불용어 제거: 리스트로 반환

    with open('./resource/wordIndex.json', 'r') as f:  # 저장한 워드인벡스 호출
        json_data = json.load(f)

    tokenizer = Tokenizer(num_words=28789)  # 피클데이터에서 가장 빈도가 높은 28789개의 단어만 선택하도록하는 Tokenizer 객체
    tokenizer.word_index = json_data
    s = tokenizer.texts_to_sequences([s])
    pad_new = pad_sequences(s, maxlen=1000)  # 패딩

    score = float(my_model.predict(pad_new)[0][0])  # 소프트맥스를 통해 출력된 확률값
    if (score > 0.5):
        return str(round(score * 100, 0))
    else:
        return str(round((1 - score) * 100, 0))

if __name__ == '__main__':
    app.run(debug=True)