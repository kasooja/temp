import regex
import string
from stemming.porter import stem
from nltk.stem import WordNetLemmatizer
from nltk.corpus import stopwords

NUM = regex.compile("^\d*[-\./,]*\d+$")
PUNCT = set(string.punctuation)
PUNCT.add('--')
AFFIX = set(["n't", "'s", "'d", "'t"]) 

REGEX_TOKENIZER_1 = "(\\.\\.\\.+|[\\p{Po}\\p{Ps}\\p{Pe}\\p{Pi}\\p{Pf}\u2013\u2014\u2015&&[^'\\.]]|(?<!(\\.|\\.\\p{L}))\\.(?=[\\p{Z}\\p{Pf}\\p{Pe}]|\\Z)|(?<!\\p{L})'(?!\\p{L}))"
REGEX_TOKENIZER_2 = "\\p{C}|^\\p{Z}+|\\p{Z}+$"
REGEX_SPLITTER = "\\p{Z}+"

wordnet_lemmatizer = WordNetLemmatizer()
stops = set(stopwords.words("english"))


def process_line(line):
    tokens = line.split()

    if len(tokens) <=0:
        return None
    words = []
    for word in tokens:
        word = clean_word_permissive(word)
        if word is not None:
            words.append(lemmatize(word))

    return words


def clean_word_permissive(word):
    if word == "@" or word =="<p>":
        return None
    elif word in PUNCT:
        return None
    elif word in AFFIX:
        return None
    elif len(word) < 2:
        return None
    elif stops.__contains__(word):
        return None
    else:
        word = word.strip().strip("*").lower()
        if NUM.match(word):
            word = "<NUM>"
    return word


def stem_it(word):
    return stem(word)


def lemmatize(word):
    return wordnet_lemmatizer.lemmatize(word)

print(process_line("I am going to the market ."))

print stem_it("translations")
print stem_it("translating")
print stem_it("translation")
print stem_it("technologies")
print "**************"
print lemmatize("translations")
print lemmatize("translating")
print lemmatize("translation")
print lemmatize("technologies")
print lemmatize("svm")
print lemmatize("svms")