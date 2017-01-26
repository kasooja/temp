import os
import embeddings.XMLParser as xmlp


path = "/home/kartik/PycharmProjects/Anne/resources/corpusDir/"
txtCorpusDirPath = "/home/kartik/PycharmProjects/Anne/resources/corpusDirPlainTxt/"


for corpusDir in os.listdir(path):
    if not corpusDir.startswith('.'):
        for file in os.listdir(path + corpusDir):
            dirPath = txtCorpusDirPath + corpusDir.title()
            if not os.path.exists(dirPath):
                os.makedirs(dirPath)
            if not file.startswith('.'):
                print(file)
                fileName = file.title().replace(".xml", "")
                textFile = open(dirPath + "/" + fileName + ".txt", "w")
                text = xmlp.extractText(path + corpusDir + "/" + file)
                textFile.write(text)
                textFile.close()

