import regex
import string

NUM = regex.compile("^\d*[-\./,]*\d+$")
PUNCT = set(string.punctuation)
PUNCT.add('--')
AFFIX = set(["n't", "'s", "'d", "'t"]) 

REGEX_TOKENIZER_1 = "(\\.\\.\\.+|[\\p{Po}\\p{Ps}\\p{Pe}\\p{Pi}\\p{Pf}\u2013\u2014\u2015&&[^'\\.]]|(?<!(\\.|\\.\\p{L}))\\.(?=[\\p{Z}\\p{Pf}\\p{Pe}]|\\Z)|(?<!\\p{L})'(?!\\p{L}))"
REGEX_TOKENIZER_2 = "\\p{C}|^\\p{Z}+|\\p{Z}+$"
REGEX_SPLITTER = "\\p{Z}+"


def process_line(line):
    tokens = line.split()

    if len(tokens) <=0:
        return None
    words = []
    for word in tokens:
        word = clean_word_permissive(word)
        if word is not None:
            words.append(word)

    return words


def clean_word_permissive(word):
    if word == "@" or word =="<p>":
        return None
    elif word in PUNCT:
        return None
    elif word in AFFIX:
        return None
    else:
        word = word.strip().strip("*").lower()
        if NUM.match(word):
            word = "<NUM>"
    return word


