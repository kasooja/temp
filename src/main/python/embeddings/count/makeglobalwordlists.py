from ioutils import load_pickle, write_pickle
from nltk.corpus import stopwords

#FREQS = "/dfs/scratch0/COHA/cooccurs/{type}/avg_freqs.pkl", use average frequency?
FREQS = "/home/kat/Downloads/Anne/embeddings/span_period_years/5/full_freqs/{type}.pkl"
OUT = "/home/kat/Downloads/Anne/embeddings/span_period_years/5/word_lists/full-{type}-{cond}.pkl"

STOPWORDS = set(stopwords.words("english"))


def make_word_list(type):
    process_word = lambda word : word
    freqs = load_pickle(FREQS.format(type=type))

    print "Processing type: ", type
    word_lists = [word for word in sorted(freqs, key = lambda val : -1*freqs[val]) if word != "" and word.isalnum()]
    nstop_lists = [word for word in sorted(freqs, key = lambda val : -1*freqs[val]) if not process_word(word) in STOPWORDS if word != "" and word.isalnum()]

    print "Overall words" + " " + str(len(word_lists))
    print "Overall nstop" + " " + str(len(nstop_lists))

    write_pickle(word_lists, OUT.format(type=type, cond="all"))
    write_pickle(nstop_lists, OUT.format(type=type, cond="nstop"))


if __name__ == '__main__':
    for type in ["word"]:
        make_word_list(type)