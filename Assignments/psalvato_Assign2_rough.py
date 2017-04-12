__author__ = 'paulsalvatore57'


#The code in this is not up to date, I have left it solely for the notes in case I want to see them at some other time.





#What I want to do is analyze tweets to determine which timezone is the happiest.

#I need to:
    #Analyze each timezone and create a happiness score.
        #Happiness score for each tweet is found by totalling the sentiments scores from key words.
        #Score for the tweet is the sum of the individual sentiment scores divided by the number of key words,
        #Tweets without keywords are ignored.
    #Happiness score is the total sum of the score of the tweets, divided by the number of tweets.


#Structure of tweets.txt:
    #[lat, long] value date time text
    #Value, date and time will not be used

#Structure of keywords.txt:
    #keyword, value
    #Value is on a scale of 1 (unhappy) - 10 (very happy)

#Timezones:

        #       p9   PACIFIC   p7   MOUNTAIN   p5   CENTRAL   p3   EASTERN   p1
        #       p10            p8              p6             p4             p2


        # p1  = (49.189787, -67.444574)
        # p2  = (24.660845, -67.444574)
        # p3  = (49.189787, -87.518395)
        # p4  = (24.660845, -87.518395)
        # p5  = (49.189787, -101.998892)
        # p6  = (24.660845, -101.998892)
        # p7  = (49.189787, -115.236428)
        # p8  = (24.660845, -115.236428)
        # p9  = (49.189787, -125.242264)
        # p10 = (24.660845, -125.242264)


#Parts to the program:
    #1) Takes input for text and tweets file (I will add this last)
    #2) Determine what time zone the tweet is in and sort it accordingly

        #For sorting I am thinking I can either have a list containing [[C, tweet], [M, tweet]...]
        #Such that it will be a list of lists, each giving the timezone and the tweet.
        #I don't need to edit the tweets at all because they will all be in lower case, and I intend in using:
                #if keyword in tweet: type of command, of course I'll need


        #What I can do is create a dictionary with each time zone as it's indices and the tweets in a list, this makes organization easier.


def findTimezone(coordinates):
    """
    Takes in a list of coordinates of the form: [latitude, longitude].
    Returns a string indicating which timezone these coordinates belong to.
    If the coordinates do not fall within any of the predefined timezones returns None.
    """
    if coordinates[0] > 49.189787 or coordinates[0] < 24.660845 or coordinates[1] > -67.444574 or coordinates[1] < -125.242264:
        return None
    elif -87.518395 < coordinates[1] <= -67.444574:
        return 'Eastern'
    elif -101.998892 < coordinates[1] <= 87.518395:
        return 'Central'
    elif -115.236428 < coordinates[1] <= -101.998892:
        return 'Mountain'
    else:
        return 'Pacific'


def readKeywords(file):
    """
    Reads a text file containing keywords of the form: 'word,value'
    Return a list of lists of the form: [[value, 'word'], [value, 'word'].. ]
    """
    formatedKeywords = []
    try:
        rawKeywords = open(file, 'r', encoding='utf-8')
        for line in rawKeywords:
            line = line.replace(',', ' ').lower().split()
            formatedKeywords += [[int(line[1]), line[0]],]
        return formatedKeywords
    except:
        return None


def readTweets(file):
    """
    Reads a text file containing tweets.
    Returns a dictionary with each timezone as an index assigned to a list of tweets corresponding to that timezone.
    Ignores any tweets that are not within the specified timezones.
    """
    try:
        sortedTweets = {'Eastern':[], 'Pacific':[], 'Mountain':[], 'Central':[]}
        rawTweets = open(file, 'r', encoding='utf-8')
        for line in rawTweets:
            timezone = findTimezone([float(line.split()[0][1:-1]), float(line.split()[1][0:-1])])
            if timezone == None:
                continue
            else:
                sortedTweets[timezone] += [line.lower(),]
        return sortedTweets
    except:
        return None


def hasKeyword(tweet, keywords):
    """
    Takes in a single tweet and the list of keywords and their associated values.
    Checks to see if a tweet contains any of the keywords, if so returns True, else returns False.
    """
    for word in keywords:
        if word[1] in tweet:
            return True
    return False


def findSentiment(tweets, keywords):
    """
    Takes in all of the tweets for a single timezone and a list containing the keywords and their associated values.
    Returns the average sentiment score for the timezone, ignoring tweets that do not contain any key words.
    """

    totalWithKeyWords = 0
    totalSentiment = 0

    for tweet in tweets:
        tweetSentiment = 0
        tweetKeyWords = 0

        if hasKeyword(tweet, keywords):
            totalWithKeyWords += 1
            for word in keywords:
                if word[1] in tweet:
                    tweetKeyWords += 1
                    tweetSentiment += word[0]
            totalSentiment += tweetSentiment/tweetKeyWords

    if totalWithKeyWords == 0:
        return 0
    else:
        return totalSentiment/totalWithKeyWords


def findHappiness():

    tweetsInput = input('What is the name of the tweets file?')
    sortedTweets = readTweets(tweetsInput)

    keysInput = input('What is the name of the keywords file?')
    keyWords = readKeywords(keysInput)

    # keyWords = readKeywords('keywords.txt'.txt')
    # sortedTweets = readTweets('tweetshorts.txt')

    if sortedTweets == None:
        print("Invalid Tweet File Input")
        return None
    if keyWords == None:
        print("Invalid Keywords File Input")
        return None

    numberPerTimezone = {}
    for timezone in sortedTweets:
        numberPerTimezone[timezone] = len(sortedTweets[timezone])

    sentiments = []
    for timezone in sortedTweets:
        sentiments += [(timezone, round(findSentiment(sortedTweets[timezone], keyWords), 2)),]

    # print (keyWords)

    for timezone in sentiments:
        print ('\n', '%30s' %('Sentiment score in ' + timezone[0] + ':'), timezone[1])
        print ('%31s' %('Number of tweets from ' + timezone[0] + ':'), numberPerTimezone[timezone[0]]),

    # for i in q:
    #     for h in q[i]:
    #         print (h)
    #     # if 'going' in q[i]:
    #     #     print (True)

findHappiness()



    #The values that I'm getting at this point in time:

#     Sentiment score in Eastern: 6.85
#  Number of tweets from Eastern: 1673
#
#     Sentiment score in Pacific: 6.45
#  Number of tweets from Pacific: 365
#
#    Sentiment score in Mountain: 6.61
# Number of tweets from Mountain: 101
#
#     Sentiment score in Central: 6.79
#  Number of tweets from Central: 746