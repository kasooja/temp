import os
import embeddings.XMLParser as xmlp


path = "/home/kartik/PycharmProjects/Anne/resources/corpusDir/"
fileCorpusPath = "/home/kartik/PycharmProjects/Anne/resources/corpusInFiles/"

for corpusDir in os.listdir(path):
    if not corpusDir.startswith('.'):
        dirText = []
        dirTextFile = open(fileCorpusPath + corpusDir + ".txt", "w")
        for file in os.listdir(path + corpusDir):
            if not file.startswith('.'):
                print(file)
                text = xmlp.extractText(path + corpusDir + "/" + file)
                dirText.append(text + "\n")
                dirTextFile.write(text)
        dirTextFile.close()

