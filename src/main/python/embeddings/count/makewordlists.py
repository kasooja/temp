from ioutils import load_pickle, write_pickle
from nltk.corpus import stopwords

FREQS = "/home/kat/Downloads/Anne/embeddings/span_period_years/5/time_period_freqs/{type}.pkl"
OUT = "/home/kat/Downloads/Anne/embeddings/span_period_years/5/word_lists/{type}-{cond}.pkl"
STOPWORDS = set(stopwords.words("english"))


def make_word_list(type):
    process_word = lambda word : word
    freqs = load_pickle(FREQS.format(type=type))
    word_lists = {}
    nstop_lists = {}

    print "Processing type: ", type
    for year, year_freqs in freqs.iteritems():
        word_lists[year] = [word for word in sorted(year_freqs, key = lambda val : -1*year_freqs[val]) if word != "" and word.isalnum()]
        nstop_lists[year] = [word for word in sorted(year_freqs, key = lambda val : -1*year_freqs[val]) if not process_word(word) in STOPWORDS and not word == "" and word.isalnum()]
        print "Finished year: ", year

    for time_period_dir in word_lists.keys():
        print str(time_period_dir) + " " + "words" + " " + str(len(word_lists.get(time_period_dir)))
        print str(time_period_dir) + " " + "nstop" + " " + str(len(nstop_lists.get(time_period_dir)))

    write_pickle(word_lists, OUT.format(type=type, cond="all"))
    write_pickle(nstop_lists, OUT.format(type=type, cond="nstop"))


if __name__ == '__main__':
    for type in ["word"]:
        make_word_list(type)