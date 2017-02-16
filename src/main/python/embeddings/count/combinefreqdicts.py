from ioutils import load_pickle, write_pickle

DIR = "/home/kat/Downloads/Anne/embeddings/span_period_years/5/time_period_freqs/"

word = {}
span_period_years = 5
start_year = 1979
end_year = 2019
for year in range(start_year, end_year, span_period_years):
    word[year] = load_pickle(DIR + str(year) + "-word.pkl")

write_pickle(word, DIR + "word.pkl")
