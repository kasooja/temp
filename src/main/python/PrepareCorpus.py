import os
import re
import shutil


def tryint(s):
    try:
        return int(s)
    except ValueError:
        return s

def alphanum_key(s):
    return [tryint(c) for c in re.split('([0-9]+)', s)]

path = "/home/kartik/PycharmProjects/Anne/resources/xml"

startYear = "65"
endYear = "2006"
fileList = []
listMaxSize = 5
corpusDir = "/home/kartik/PycharmProjects/Anne/resources/corpusDir"
miniCorpusCounter = 1

for dataDir in sorted(os.listdir(path), key=alphanum_key):
    if dataDir == "2006":
        print("debug")

    fileList.append(path+dataDir)

    if len(fileList) >= listMaxSize or dataDir == endYear:
        print("******")
        miniCorpusDir = corpusDir + str(miniCorpusCounter)
        os.makedirs(miniCorpusDir, exist_ok=True)
        for fileDir in fileList:
            print(fileDir)
            for file in os.listdir(fileDir):
                shutil.copy(fileDir + "/" + file, miniCorpusDir)
        fileList = []
        miniCorpusCounter += 1







