import gensim
from gensim.models import word2vec

termsFile = "/home/kartik/PycharmProjects/Anne/resources/anne_query"
f = open(termsFile, 'r')
terms = f.readlines()

strList = []

fname = "/home/kartik/Dropbox/_anneKartik/embeddings/FBFastTextVectorsOnly/FBFastTextVectorsOnly/modelOne_65-75.vec"
model1 = word2vec.Word2Vec.load_word2vec_format(fname)

fname = "/home/kartik/Dropbox/_anneKartik/embeddings/FBFastTextVectorsOnly/FBFastTextVectorsOnly/modelTwo78-82.vec"
model2 = word2vec.Word2Vec.load_word2vec_format(fname)

fname = "/home/kartik/Dropbox/_anneKartik/embeddings/FBFastTextVectorsOnly/FBFastTextVectorsOnly/modelThree_83-87.vec"
model3 = word2vec.Word2Vec.load_word2vec_format(fname)

fname = "/home/kartik/Dropbox/_anneKartik/embeddings/FBFastTextVectorsOnly/FBFastTextVectorsOnly/modelFour_88-92.vec"
model4 = word2vec.Word2Vec.load_word2vec_format(fname)

fname = "/home/kartik/Dropbox/_anneKartik/embeddings/FBFastTextVectorsOnly/FBFastTextVectorsOnly/modelFive_93-97.vec"
model5 = word2vec.Word2Vec.load_word2vec_format(fname)

fname = "/home/kartik/Dropbox/_anneKartik/embeddings/FBFastTextVectorsOnly/FBFastTextVectorsOnly/modelSix_98-2002.vec"
model6 = word2vec.Word2Vec.load_word2vec_format(fname)

fname = "/home/kartik/Dropbox/_anneKartik/embeddings/FBFastTextVectorsOnly/FBFastTextVectorsOnly/modelSeven_2003-2006.vec"
model7 = word2vec.Word2Vec.load_word2vec_format(fname)

for term in terms:
    term = term.lower().strip()
    print(term)
    termStr = ""
    dirTextFile = open("/home/kartik/PycharmProjects/Anne/resources/anne/" + term + ".txt", "w")

    dirTextFile.write("65-75" + "\t" + str(model1.most_similar(term.strip(), topn=50)) + "\n")

    dirTextFile.write("78-82" + "\t" + str(model2.most_similar(term.strip(), topn=50)) + "\n")

    dirTextFile.write("83-87" + "\t" + str(model3.most_similar(term.strip(), topn=50)) + "\n")

    dirTextFile.write("88-92" + "\t" + str(model4.most_similar(term.strip(), topn=50)) + "\n")

    dirTextFile.write("93-97" + "\t" + str(model5.most_similar(term.strip(), topn=50)) + "\n")

    dirTextFile.write("98-2002" + "\t" + str(model6.most_similar(term.strip(), topn=50)) + "\n")

    dirTextFile.write("2003-2006" + "\t" + str(model7.most_similar(term.strip(), topn=50)) + "\n")

    dirTextFile.close()
