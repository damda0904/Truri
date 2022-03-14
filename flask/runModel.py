import re
from konlpy.tag import Okt
from tensorflow.keras.preprocessing.text import Tokenizer
from tensorflow.keras.preprocessing.sequence import pad_sequences
import tensorflow as tf
import os
import json
import jpype
import asyncio

class multiHeadAttention(tf.keras.layers.Layer):
    def __init__(self, embedding_dim, num_heads=8):
        super(multiHeadAttention, self).__init__()
        self.embedding_dim = embedding_dim # d_model
        self.num_heads = num_heads

        assert embedding_dim % self.num_heads == 0

        self.projection_dim = embedding_dim // num_heads
        self.query_dense = tf.keras.layers.Dense(embedding_dim)
        self.key_dense = tf.keras.layers.Dense(embedding_dim)
        self.value_dense = tf.keras.layers.Dense(embedding_dim)
        self.dense = tf.keras.layers.Dense(embedding_dim)

    def scaled_dot_product_attention(self, query, key, value):
        matmul_qk = tf.matmul(query, key, transpose_b=True)
        depth = tf.cast(tf.shape(key)[-1], tf.float32)
        logits = matmul_qk / tf.math.sqrt(depth)
        attention_weights = tf.nn.softmax(logits, axis=-1)
        output = tf.matmul(attention_weights, value)
        return output, attention_weights

    def split_heads(self, x, batch_size):
        x = tf.reshape(x, (batch_size, -1, self.num_heads, self.projection_dim))
        return tf.transpose(x, perm=[0, 2, 1, 3])

    def call(self, inputs):
        # x.shape = [batch_size, seq_len, embedding_dim]
        batch_size = tf.shape(inputs)[0]

        # (batch_size, seq_len, embedding_dim)
        query = self.query_dense(inputs)
        key = self.key_dense(inputs)
        value = self.value_dense(inputs)

        # (batch_size, num_heads, seq_len, projection_dim)
        query = self.split_heads(query, batch_size)
        key = self.split_heads(key, batch_size)
        value = self.split_heads(value, batch_size)

        scaled_attention, _ = self.scaled_dot_product_attention(query, key, value)
        # (batch_size, seq_len, num_heads, projection_dim)
        scaled_attention = tf.transpose(scaled_attention, perm=[0, 2, 1, 3])

        # (batch_size, seq_len, embedding_dim)
        concat_attention = tf.reshape(scaled_attention, (batch_size, -1, self.embedding_dim))
        outputs = self.dense(concat_attention)
        return outputs

class TransformerBlock(tf.keras.layers.Layer):
    def __init__(self, embedding_dim, num_heads, dff, rate=0.1):
        super(TransformerBlock, self).__init__()
        self.att = multiHeadAttention(embedding_dim, num_heads)
        self.ffn = tf.keras.Sequential(
            [tf.keras.layers.Dense(dff, activation="relu"),
             tf.keras.layers.Dense(embedding_dim), ]
        )
        self.layernorm1 = tf.keras.layers.LayerNormalization(epsilon=1e-6)
        self.layernorm2 = tf.keras.layers.LayerNormalization(epsilon=1e-6)
        self.dropout1 = tf.keras.layers.Dropout(rate)
        self.dropout2 = tf.keras.layers.Dropout(rate)

    def call(self, inputs, training):
        attn_output = self.att(inputs)  # 첫번째 서브층 : 멀티 헤드 어텐션
        attn_output = self.dropout1(attn_output, training=training)
        out1 = self.layernorm1(inputs + attn_output)  # Add & Norm
        ffn_output = self.ffn(out1)  # 두번째 서브층 : 포지션 와이즈 피드 포워드 신경망
        ffn_output = self.dropout2(ffn_output, training=training)
        return self.layernorm2(out1 + ffn_output)  # Add & Norm

class TokenAndPositionEmbedding(tf.keras.layers.Layer):
    def __init__(self, max_len, vocab_size, embedding_dim):
        super(TokenAndPositionEmbedding, self).__init__()
        self.token_emb = tf.keras.layers.Embedding(vocab_size, embedding_dim)
        self.pos_emb = tf.keras.layers.Embedding(max_len, embedding_dim)

    def call(self, x):
        max_len = tf.shape(x)[-1]
        positions = tf.range(start=0, limit=max_len, delta=1)
        positions = self.pos_emb(positions)
        x = self.token_emb(x)
        return x + positions



def runModel(content) :

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
    my_model.load_weights(os.path.join('./resource/dacon_try', 'tf_chkpoint.ckpt'))  # 모델 로드(폴더 경로를 넣어줘)
    with open('./resource/wordIndex.json', 'r') as f:  # 저장한 워드인벡스 호출
        json_data = json.load(f)
    tokenizer = Tokenizer(num_words=28789)  # 피클데이터에서 가장 빈도가 높은 28789개의 단어만 선택하도록하는 Tokenizer 객체
    tokenizer.word_index = json_data

    okt = Okt()

    #스레드 처리
    loop = asyncio.new_event_loop()
    asyncio.set_event_loop(loop)

    tasks = []
    for c in content :
        tasks.append(bridge(loop, okt, tokenizer, my_model, c))

    return loop.run_until_complete(asyncio.gather(*tasks))


async def bridge(loop, okt, tokenizer, my_model, content) :
    return await loop.run_in_executor(None, do_morphs, okt, tokenizer, my_model, content)


def do_morphs(okt, tokenizer, my_model, s):
    jpype.attachThreadToJVM()
    stopwords = ['의','가','이','은','들','는','좀','잘','걍','과','도','를','으로','자','에','와','한','하다']
    s = re.sub(r'[^ㄱ-ㅎㅏ-ㅣ가-힣 ]','', s)
    s = okt.morphs(s, stem=True) # 토큰화
    s = [word for word in s if not word in stopwords] # 불용어 제거: 리스트로 반환
    s = tokenizer.texts_to_sequences([s])
    pad_new = pad_sequences(s, maxlen = 1000) #패딩
    score = float(my_model.predict(pad_new)[0][1]) #소프트맥스를 통해 출력된 확률값

    return round(score * 100)

    # if (score > 0.5):
    #     return round(score * 100, 0)
    # else:
    #     return round((1 - score) * 100, 0)