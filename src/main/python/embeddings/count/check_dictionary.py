from ioutils import load_pickle, write_pickle
import random

OUT = "/home/kat/Downloads/Anne/embeddings_lat/span_period_years/5/info/word-dict.pkl"

word_dic = load_pickle(OUT)
print "size of word dict: " + str(word_dic.__sizeof__())


i = 0
for word in word_dic.keys():
    if i<100:
        if bool(random.getrandbits(1)):
            print word, " ", word_dic.get(word)
    else:
        break
    i= i+1