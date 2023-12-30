import json
import random
import numpy as np
from PIL import Image
from shutil import copyfile
import os

USERS_NUM = 10000
ARTICLES_NUM = 10000
READS_NUM = 1000000

uid_region = {}
aid_lang = {}


# Beijing:60%   Hong Kong:40%
# en:20%    zh:80%
# 20 depts
# 3 roles
# 50 tags
# 0~99 credits

def gen_an_user (i):
    timeBegin = 1506328859000
    user = {}
    user["timestamp"] = str(timeBegin + i)
    user["id"] = 'u'+str(i)
    user["uid"] = str(i)
    user["name"] = "user%d" % i
    user["gender"] = "male" if random.random() > 0.33 else "female"
    user["email"] = "email%d" % i
    user["phone"] = "phone%d" % i
    user["dept"]  = "dept%d" % int(random.random() * 20)
    user["grade"] = "grade%d" % int(random.random() * 4 + 1)
    user["language"] = "en" if random.random() > 0.8 else "zh"
    user["region"] = "Beijing" if random.random() > 0.4 else "Hong Kong"
    user["role"] = "role%d" % int(random.random() * 3)
    user["preferTags"] = "tags%d" % int(random.random() * 50)
    user["obtainedCredits"] = str(int(random.random() * 100))

    uid_region[user["uid"]] = user["region"]
    return "(" +  \
            "\"" + user["timestamp"] + "\", " + \
            "\"" + user["id"] + "\", " + \
            "\"" + user["uid"] + "\", " + \
            "\"" + user["name"] + "\", " + \
            "\"" + user["gender"] + "\", " + \
            "\"" + user["email"] + "\", " + \
            "\"" + user["phone"] + "\", " + \
            "\"" + user["dept"] + "\", " + \
            "\"" + user["grade"] + "\", " + \
            "\"" + user["language"] + "\", " + \
            "\"" + user["region"] + "\", " + \
            "\"" + user["role"] + "\", " + \
            "\"" + user["preferTags"] + "\", " + \
            "\"" + user["obtainedCredits"] + "\")";

# science:45%   technology:55%
# en:50%    zh:50%
# 50 tags
# 2000 authors
aid_category = {}
def gen_an_article (i):
    timeBegin = 1506000000000
    article = {}
    article["id"] = 'a'+str(i)
    article["timestamp"] = str(timeBegin + i)
    article["aid"] = str(i)
    article["title"] = "title%d" % i
    article["category"] = "science" if random.random() > 0.55 else "technology"
    article["abstract"] = "abstract of article %d" % i
    article["articleTags"] = "tags%d" % int(random.random() * 50)
    article["authors"]  = "author%d" % int(random.random() * 2000)
    article["language"] = "en" if random.random() > 0.5 else "zh"

    # create text
    article["text"] = "text_a"+str(i)+'.txt'
    path = './articles/article'+str(i)
    if not os.path.exists(path):
        os.makedirs(path) 

    categories = ['business', 'entertainment', 'sport', 'tech']
    random_category = categories[random.randint(0,3)]
    files = os.listdir('./bbc_news_texts/' + random_category +'/')
    aid_category[str(i)] = random_category
    size = len(files)
    random_news = files[random.randint(0,size-1)]
    copyfile('bbc_news_texts/' + random_category +'/' +random_news, path+"/text_a"+str(i)+'.txt')

    # create images
    image_num = random.randint(1,3)
    image_str = ""
    for j in range(image_num):
        image_str+= 'image_a'+str(i)+'_'+str(j)+'.jpg,'
    article["image"] = image_str

    for j in range(image_num):
        copyfile('./image/' + str(random.randint(0,599))+'.jpg',path+'/image_a'+str(i)+'_'+str(j)+'.jpg')

    # create video
    if random.random() < 0.05:
        #has one video
        article["video"] = "video_a"+str(i)+'_video.flv'
        if random.random()<0.5:
            copyfile('./video/video1.flv',path+"/video_a"+str(i)+'_video.flv')
        else:
            copyfile('./video/video2.flv',path+"/video_a"+str(i)+'_video.flv')
    else:
        article["video"] = ""

    aid_lang[article["aid"]] = article["language"]
    return "(" +  \
            "\"" + article["timestamp"] + "\", " + \
            "\"" + article["id"] + "\", " + \
            "\"" + article["aid"] + "\", " + \
            "\"" + article["title"] + "\", " + \
            "\"" + article["category"] + "\", " + \
            "\"" + article["abstract"] + "\", " + \
            "\"" + article["articleTags"] + "\", " + \
            "\"" + article["authors"] + "\", " + \
            "\"" + article["language"] + "\", " + \
            "\"" + article["text"] + "\", " + \
            "\"" + article["image"] + "\", " + \
            "\"" + article["video"] + "\")";

time_within_day = 1516332287000 - 1000 * 60 * 60 * 24
time_within_week = 1516332287000 - 1000 * 60 * 60 * 24 * 7
time_within_month = 1516332287000 - 1000 * 60 * 60 * 24 * 30
month_rank = {}
week_rank = {}
day_rank = {}
def calc_popular_index(readNum, commentNum, agreeNum, shareNum):
    return readNum / 100 * 0.4 + commentNum * 0.3 + agreeNum * 0.2 + shareNum * 0.1

# This function is called for each Read entry generated
def calc_popular_rank(read_entry):
    # If the read entry is not within a month, return
    if read_entry['timestamp'] > str(time_within_month):
        score = calc_popular_index(int(read_entry['readTimeLength']), int(read_entry['agreeOrNot']), int(read_entry['commentOrNot']), int(read_entry['shareOrNot']))
        if month_rank.get(read_entry['aid']) is None:
            month_rank[read_entry['aid']] = score
        else:
            month_rank[read_entry['aid']] += score
        if read_entry['timestamp'] > str(time_within_week):
            if week_rank.get(read_entry['aid']) is None:
                week_rank[read_entry['aid']] = score
            else:
                week_rank[read_entry['aid']] += score
            if read_entry['timestamp'] > str(time_within_day):
                if day_rank.get(read_entry['aid']) is None:
                    day_rank[read_entry['aid']] = score
                else:
                    day_rank[read_entry['aid']] += score
    else:
        return
# user in Beijing read/agree/comment/share an english article with the probability 0.6/0.2/0.2/0.1
# user in Hong Kong read/agree/comment/share an Chinese article with the probability 0.8/0.2/0.2/0.1
p = {}
p["Beijing"+"en"] = [0.6,0.2,0.2,0.1]
p["Beijing"+"zh"] = [1,0.3,0.3,0.2]
p["Hong Kong"+"en"] = [1,0.3,0.3,0.2]
p["Hong Kong"+"zh"] = [0.8,0.2,0.2,0.1]
def gen_an_read (i):
    timeBegin = 1506332297000
    read = {}
    read["timestamp"] = str(timeBegin + i*10000)
    read["id"] = 'r'+str(i)
    read["uid"] = str(int(random.random() * USERS_NUM))
    read["aid"] = str(int(random.random() * ARTICLES_NUM))
    
    region = uid_region[read["uid"]]
    lang = aid_lang[read["aid"]]
    ps = p[region + lang]

    if (random.random() > ps[0]):
        # read["readOrNot"] = "0";
        return gen_an_read (i)
    else:
        # read["readOrNot"] = "1"
        read["readTimeLength"] = str(int(random.random() * 100))
        # read["readSequence"] = str(int(random.random() * 4))
        read["agreeOrNot"] = "1" if random.random() < ps[1] else "0"
        read["commentOrNot"] = "1" if random.random() < ps[2] else "0"
        read["shareOrNot"] = "1" if random.random() < ps[3] else "0"
        read["commentDetail"] = "comments to this article: (" + read["uid"] + "," + read["aid"] + ")" 
    update_be_read(read)
    calc_popular_rank(read)
    return "(" +  \
            "\"" + read["timestamp"] + "\", " + \
            "\"" + read["id"] + "\", " + \
            "\"" + read["uid"] + "\", " + \
            "\"" + read["aid"] + "\", " + \
            "\"" + read["readTimeLength"] + "\", " + \
            "\"" + read["agreeOrNot"] + "\", " + \
            "\"" + read["commentOrNot"] + "\", " + \
            "\"" + read["shareOrNot"] + "\", " + \
            "\"" + read["commentDetail"] + "\")"



with open("user_beijing.sql", "w+") as f_beijing, open("user_hongkong.sql", "w+") as f_hongkong:
    f_beijing.write("DROP TABLE IF EXISTS `user`;\n")
    f_beijing.write("CREATE TABLE `user` (\n" + \
            "  `timestamp` char(14) DEFAULT NULL,\n" + \
            "  `id` char(5) DEFAULT NULL,\n" + \
            "  `uid` char(5) DEFAULT NULL,\n" + \
            "  `name` char(9) DEFAULT NULL,\n" +  \
            "  `gender` char(7) DEFAULT NULL,\n" +  \
            "  `email` char(10) DEFAULT NULL,\n" +  \
            "  `phone` char(10) DEFAULT NULL,\n" +  \
            "  `dept` char(9) DEFAULT NULL,\n" +  \
            "  `grade` char(7) DEFAULT NULL,\n" +  \
            "  `language` char(3) DEFAULT NULL,\n" +  \
            "  `region` char(10) DEFAULT NULL,\n" +  \
            "  `role` char(6) DEFAULT NULL,\n" +  \
            "  `preferTags` char(7) DEFAULT NULL,\n" +  \
            "  `obtainedCredits` char(3) DEFAULT NULL\n) ENGINE=InnoDB DEFAULT CHARSET=utf8;\n\n")
    f_beijing.write("LOCK TABLES `user` WRITE;\n")
    f_beijing.write("INSERT INTO `user` VALUES\n")
    f_hongkong.write("DROP TABLE IF EXISTS `user`;\n")
    f_hongkong.write("CREATE TABLE `user` (\n" + \
        "  `timestamp` char(14) DEFAULT NULL,\n" + \
        "  `id` char(5) DEFAULT NULL,\n" + \
        "  `uid` char(5) DEFAULT NULL,\n" + \
        "  `name` char(9) DEFAULT NULL,\n" +  \
        "  `gender` char(7) DEFAULT NULL,\n" +  \
        "  `email` char(10) DEFAULT NULL,\n" +  \
        "  `phone` char(10) DEFAULT NULL,\n" +  \
        "  `dept` char(9) DEFAULT NULL,\n" +  \
        "  `grade` char(7) DEFAULT NULL,\n" +  \
        "  `language` char(3) DEFAULT NULL,\n" +  \
        "  `region` char(10) DEFAULT NULL,\n" +  \
        "  `role` char(6) DEFAULT NULL,\n" +  \
        "  `preferTags` char(7) DEFAULT NULL,\n" +  \
        "  `obtainedCredits` char(3) DEFAULT NULL\n) ENGINE=InnoDB DEFAULT CHARSET=utf8;\n\n")
    f_hongkong.write("LOCK TABLES `user` WRITE;\n")
    f_hongkong.write("INSERT INTO `user` VALUES\n")
    
    last_user_beijing = None
    last_user_hongkong = None

    for i in range(USERS_NUM):
        user = gen_an_user(i)
        if uid_region[str(i)] == "Beijing":
            if last_user_beijing is not None:
                f_beijing.write("  " + last_user_beijing + ",\n")
            last_user_beijing = user
        else:
            if last_user_hongkong is not None:
                f_hongkong.write("  " + last_user_hongkong + ",\n")
            last_user_hongkong = user

    # Write the last user for each file
    if last_user_beijing is not None:
        f_beijing.write("  " + last_user_beijing + ";\n")
    if last_user_hongkong is not None:
        f_hongkong.write("  " + last_user_hongkong + ";\n")

    f_beijing.write("UNLOCK TABLES;\n\n\n")
    f_hongkong.write("UNLOCK TABLES;\n\n\n")

with open("article_1.sql", "w+") as f_1, open("article_2.sql", "w+") as f_2:
    f_1.write("DROP TABLE IF EXISTS `article`;\n")
    f_1.write("CREATE TABLE `article` (\n" + \
            "  `timestamp` char(14) DEFAULT NULL,\n" + \
            "  `id` char(7) DEFAULT NULL,\n" + \
            "  `aid` char(7) DEFAULT NULL,\n" + \
            "  `title` char(15) DEFAULT NULL,\n" +  \
            "  `category` char(11) DEFAULT NULL,\n" +  \
            "  `abstract` char(30) DEFAULT NULL,\n" +  \
            "  `articleTags` char(14) DEFAULT NULL,\n" +  \
            "  `authors` char(13) DEFAULT NULL,\n" +  \
            "  `language` char(3) DEFAULT NULL,\n" +  \
            "  `text` char(31) DEFAULT NULL,\n" +  \
            "  `image` char(32) DEFAULT NULL,\n" +  \
            "  `video` char(32) DEFAULT NULL\n) ENGINE=InnoDB DEFAULT CHARSET=utf8;\n\n")
    f_1.write("LOCK TABLES `article` WRITE;\n")
    f_1.write("INSERT INTO `article` VALUES\n")
    
    f_2.write("DROP TABLE IF EXISTS `article`;\n")
    f_2.write("CREATE TABLE `article` (\n" + \
            "  `timestamp` char(14) DEFAULT NULL,\n" + \
            "  `id` char(7) DEFAULT NULL,\n" + \
            "  `aid` char(7) DEFAULT NULL,\n" + \
            "  `title` char(15) DEFAULT NULL,\n" +  \
            "  `category` char(11) DEFAULT NULL,\n" +  \
            "  `abstract` char(30) DEFAULT NULL,\n" +  \
            "  `articleTags` char(14) DEFAULT NULL,\n" +  \
            "  `authors` char(13) DEFAULT NULL,\n" +  \
            "  `language` char(3) DEFAULT NULL,\n" +  \
            "  `text` char(31) DEFAULT NULL,\n" +  \
            "  `image` char(32) DEFAULT NULL,\n" +  \
            "  `video` char(32) DEFAULT NULL\n) ENGINE=InnoDB DEFAULT CHARSET=utf8;\n\n")
    f_2.write("LOCK TABLES `article` WRITE;\n")
    f_2.write("INSERT INTO `article` VALUES\n")
    article_one_last = None
    article_two_last = None
    for i in range (ARTICLES_NUM):
        if (aid_category[str(i)] == 'tech'):
            if article_two_last is not None:
                f_2.write("  " + article_two_last + ",\n")
            article_two_last = gen_an_article(i)
        else:
            if article_one_last is not None:
                f_1.write("  " + article_one_last + ",\n")
            article_one_last = gen_an_article(i)

    # Write the last article for each file
    if article_one_last is not None:
        f_1.write("  " + article_one_last + ";\n")
    if article_two_last is not None:
        f_2.write("  " + article_two_last + ";\n")
    f_1.write("UNLOCK TABLES;\n\n\n")
    f_2.write("UNLOCK TABLES;\n\n\n")

# aid : Be-Read
be_read_dict = {}
# This function is called for each Read entry generated
def update_be_read(read_entry):
    aid = read_entry['aid']
    uid = read_entry['uid']

    if aid not in be_read_dict:
        be_read_dict[aid] = {
            'id': "br" + aid,
            'timestamp': read_entry['timestamp'],
            'aid': aid,
            'readNum': 0,
            'readUidList': set(),
            'commentNum': 0,
            'commentUidList': set(),
            'agreeNum': 0,
            'agreeUidList': set(),
            'shareNum': 0,
            'shareUidList': set()
        }
    be_read_entry = be_read_dict[aid]
    be_read_entry['readNum'] += 1
    be_read_entry['readUidList'].add(uid)
    if read_entry['commentOrNot'] == '1':
        be_read_entry['commentNum'] += 1
        be_read_entry['commentUidList'].add(uid)
    if read_entry['agreeOrNot'] == '1':
        be_read_entry['agreeNum'] += 1
        be_read_entry['agreeUidList'].add(uid)
    if read_entry['shareOrNot'] == '1':
        be_read_entry['shareNum'] += 1
        be_read_entry['shareUidList'].add(uid)
    be_read_dict[aid] = be_read_entry      

with open("user_read_1.sql", "w+") as f_1, open("user_read_2.sql", "w+") as f_2:
    f_1.write("DROP TABLE IF EXISTS `user_read`;\n")
    f_1.write("CREATE TABLE `user_read` (\n" + \
            "  `timestamp` char(14) DEFAULT NULL,\n" + \
            "  `id` char(7) DEFAULT NULL,\n" + \
            "  `uid` char(5) DEFAULT NULL,\n" + \
            "  `aid` char(7) DEFAULT NULL,\n" + \
            "  `readTimeLength` char(3) DEFAULT NULL,\n" +  \
            "  `agreeOrNot` char(2) DEFAULT NULL,\n" +  \
            "  `commentOrNot` char(2) DEFAULT NULL,\n" +  \
            "  `shareOrNot` char(2) DEFAULT NULL,\n" +  \
            "  `commentDetail` char(100) DEFAULT NULL\n) ENGINE=InnoDB DEFAULT CHARSET=utf8;\n\n")
    f_2.write("DROP TABLE IF EXISTS `user_read`;\n")
    f_2.write("CREATE TABLE `user_read` (\n" + \
            "  `timestamp` char(14) DEFAULT NULL,\n" + \
            "  `id` char(7) DEFAULT NULL,\n" + \
            "  `uid` char(5) DEFAULT NULL,\n" + \
            "  `aid` char(7) DEFAULT NULL,\n" + \
            "  `readTimeLength` char(3) DEFAULT NULL,\n" +  \
            "  `agreeOrNot` char(2) DEFAULT NULL,\n" +  \
            "  `commentOrNot` char(2) DEFAULT NULL,\n" +  \
            "  `shareOrNot` char(2) DEFAULT NULL,\n" +  \
            "  `commentDetail` char(100) DEFAULT NULL\n) ENGINE=InnoDB DEFAULT CHARSET=utf8;\n\n")
    f_1.write("LOCK TABLES `user_read` WRITE;\n")
    f_1.write("INSERT INTO `user_read` VALUES\n")
    f_2.write("LOCK TABLES `user_read` WRITE;\n")
    f_2.write("INSERT INTO `user_read` VALUES\n")
    read_one_last = None
    read_two_last = None

    for i in range (READS_NUM):
        read_entry = gen_an_read(i)
        if (uid_region[str(i)] == 'Beijing'):
            if read_one_last is not None:
                f_1.write("  " + read_one_last + ",\n")
            read_one_last = read_entry
        else:
            if read_two_last is not None:
                f_2.write("  " + read_two_last + ",\n")
            read_two_last = read_entry
    if read_one_last is not None:
        f_1.write("  " + read_one_last + ";\n")
    if read_two_last is not None:
        f_2.write("  " + read_two_last + ";\n")
    f_1.write("UNLOCK TABLES;\n\n\n")
    f_2.write("UNLOCK TABLES;\n\n\n")

# After all Read entries are processed, write Be-Read data to SQL
with open("be_read_1.sql", "w+") as f_1, open("be_read_2.sql", "w+") as f_2:
    f_1.write("DROP TABLE IF EXISTS `be_read`;\n")
    f_1.write("CREATE TABLE `be_read` (\n" + \
            "  `timestamp` char(14) DEFAULT NULL,\n" + \
            "  `id` char(7) DEFAULT NULL,\n" + \
            "  `aid` char(7) DEFAULT NULL,\n" + \
            "  `readNum` char(5) DEFAULT NULL,\n" + \
            "  `readUidList` varchar(1000) DEFAULT NULL,\n" + \
            "  `commentNum` char(5) DEFAULT NULL,\n" + \
            "  `commentUidList` varchar(1000) DEFAULT NULL,\n" + \
            "  `agreeNum` char(5) DEFAULT NULL,\n" + \
            "  `agreeUidList` varchar(1000) DEFAULT NULL,\n" + \
            "  `shareNum` char(5) DEFAULT NULL,\n" + \
            "  `shareUidList` varchar(1000) DEFAULT NULL\n) ENGINE=InnoDB DEFAULT CHARSET=utf8;\n\n")
    f_2.write("DROP TABLE IF EXISTS `be_read`;\n")
    f_2.write("CREATE TABLE `be_read` (\n" + \
            "  `timestamp` char(14) DEFAULT NULL,\n" + \
            "  `id` char(7) DEFAULT NULL,\n" + \
            "  `aid` char(7) DEFAULT NULL,\n" + \
            "  `readNum` char(5) DEFAULT NULL,\n" + \
            "  `readUidList` varchar(1000) DEFAULT NULL,\n" + \
            "  `commentNum` char(5) DEFAULT NULL,\n" + \
            "  `commentUidList` varchar(1000) DEFAULT NULL,\n" + \
            "  `agreeNum` char(5) DEFAULT NULL,\n" + \
            "  `agreeUidList` varchar(1000) DEFAULT NULL,\n" + \
            "  `shareNum` char(5) DEFAULT NULL,\n" + \
            "  `shareUidList` varchar(1000) DEFAULT NULL\n) ENGINE=InnoDB DEFAULT CHARSET=utf8;\n\n")
    f_1.write("LOCK TABLES `be_read` WRITE;\n")
    f_1.write("INSERT INTO `be_read` VALUES\n")
    f_2.write("LOCK TABLES `be_read` WRITE;\n")
    f_2.write("INSERT INTO `be_read` VALUES\n")
    be_read_items = list(be_read_dict.items())
    be_read_one_last = None
    be_read_two_last = None
    for i in range(len(be_read_items) - 1):
        be_read_entry = be_read_items[i][1]
        if aid_category[be_read_entry['aid']] == 'tech':
            if be_read_two_last is not None:
                f_2.write("  (" + \
                "\"" + be_read_two_last['timestamp'] + "\", " + \
                "\"" + be_read_two_last['id'] + "\", " + \
                "\"" + be_read_two_last['aid'] + "\", " + \
                "\"" + str(be_read_two_last['readNum']) + "\", " + \
                "\"" + ','.join(be_read_two_last['readUidList']) + "\", " + \
                "\"" + str(be_read_two_last['commentNum']) + "\", " + \
                "\"" + ','.join(be_read_two_last['commentUidList']) + "\", " + \
                "\"" + str(be_read_two_last['agreeNum']) + "\", " + \
                "\"" + ','.join(be_read_two_last['agreeUidList']) + "\", " + \
                "\"" + str(be_read_two_last['shareNum']) + "\", " + \
                "\"" + ','.join(be_read_two_last['shareUidList']) + "\"),\n")
            be_read_two_last = be_read_entry
        else:
            if be_read_one_last is not None:
                f_1.write("  (" + \
                "\"" + be_read_one_last['timestamp'] + "\", " + \
                "\"" + be_read_one_last['id'] + "\", " + \
                "\"" + be_read_one_last['aid'] + "\", " + \
                "\"" + str(be_read_one_last['readNum']) + "\", " + \
                "\"" + ','.join(be_read_one_last['readUidList']) + "\", " + \
                "\"" + str(be_read_one_last['commentNum']) + "\", " + \
                "\"" + ','.join(be_read_one_last['commentUidList']) + "\", " + \
                "\"" + str(be_read_one_last['agreeNum']) + "\", " + \
                "\"" + ','.join(be_read_one_last['agreeUidList']) + "\", " + \
                "\"" + str(be_read_one_last['shareNum']) + "\", " + \
                "\"" + ','.join(be_read_one_last['shareUidList']) + "\"),\n")
            be_read_one_last = be_read_entry
    # Write the last entry for each file
    if be_read_one_last is not None:
        f_1.write("  (" + \
                "\"" + be_read_one_last['timestamp'] + "\", " + \
                "\"" + be_read_one_last['id'] + "\", " + \
                "\"" + be_read_one_last['aid'] + "\", " + \
                "\"" + str(be_read_one_last['readNum']) + "\", " + \
                "\"" + ','.join(be_read_one_last['readUidList']) + "\", " + \
                "\"" + str(be_read_one_last['commentNum']) + "\", " + \
                "\"" + ','.join(be_read_one_last['commentUidList']) + "\", " + \
                "\"" + str(be_read_one_last['agreeNum']) + "\", " + \
                "\"" + ','.join(be_read_one_last['agreeUidList']) + "\", " + \
                "\"" + str(be_read_one_last['shareNum']) + "\", " + \
                "\"" + ','.join(be_read_one_last['shareUidList']) + "\");\n")
    if be_read_two_last is not None:
            f_2.write("  (" + \
                "\"" + be_read_two_last['timestamp'] + "\", " + \
                "\"" + be_read_two_last['id'] + "\", " + \
                "\"" + be_read_two_last['aid'] + "\", " + \
                "\"" + str(be_read_two_last['readNum']) + "\", " + \
                "\"" + ','.join(be_read_two_last['readUidList']) + "\", " + \
                "\"" + str(be_read_two_last['commentNum']) + "\", " + \
                "\"" + ','.join(be_read_two_last['commentUidList']) + "\", " + \
                "\"" + str(be_read_two_last['agreeNum']) + "\", " + \
                "\"" + ','.join(be_read_two_last['agreeUidList']) + "\", " + \
                "\"" + str(be_read_two_last['shareNum']) + "\", " + \
                "\"" + ','.join(be_read_two_last['shareUidList']) + "\");\n")
    f_1.write("UNLOCK TABLES;\n\n\n")
    f_2.write("UNLOCK TABLES;\n\n\n")
# Function to get top 5 articles from a rank dictionary
def get_top_5_articles(rank_dict):
    return sorted(rank_dict.items(), key=lambda x: x[1], reverse=True)[:5]
def get_top_5_article_ids(top_5):
    top_5_ids = [aid for aid, _ in top_5]
    return '{' + ', '.join(top_5_ids) + '}'

# Get top 5 articles for each granularity
top_5_month = get_top_5_articles(month_rank)
top_5_week = get_top_5_articles(week_rank)
top_5_day = get_top_5_articles(day_rank)

# Also write the popular rank data to SQL
with open("popular_rank_1.sql", "w+") as f_1, open("popular_rank_2.sql", "w+") as f_2:
    f_1.write("DROP TABLE IF EXISTS `popular_rank`;\n")
    f_1.write("CREATE TABLE `popular_rank` (\n" + \
            "  `timestamp` char(14) DEFAULT NULL,\n" + \
            "  `id` char(7) DEFAULT NULL,\n" + \
            "  `temporalGranularity` char(8) DEFAULT NULL,\n" + \
            "  `articleAidList` varchar(1000) DEFAULT NULL\n) ENGINE=InnoDB DEFAULT CHARSET=utf8;\n\n")
    f_1.write("LOCK TABLES `popular_rank` WRITE;\n")
    f_1.write("INSERT INTO `popular_rank` VALUES\n")
    # Write top 5 daily rank
    print(top_5_day)
    f_1.write("  (" + \
            "\"" + str(time_within_day) + "\", " + \
            "\"" + str(1) + "\", " + \
            "\"" + "day" + "\", " + \
            "\"" + get_top_5_article_ids(top_5_day) + "\");\n")
    f_1.write("UNLOCK TABLES;\n\n\n")

    f_2.write("DROP TABLE IF EXISTS `popular_rank`;\n")
    f_2.write("CREATE TABLE `popular_rank` (\n" + \
            "  `timestamp` char(14) DEFAULT NULL,\n" + \
            "  `id` char(7) DEFAULT NULL,\n" + \
            "  `temporalGranularity` char(8) DEFAULT NULL,\n" + \
            "  `articleAidList` varchar(1000) DEFAULT NULL\n) ENGINE=InnoDB DEFAULT CHARSET=utf8;\n\n")
    f_2.write("LOCK TABLES `popular_rank` WRITE;\n")
    f_2.write("INSERT INTO `popular_rank` VALUES\n")
    # Write top 5 week rank
    f_2.write("  (" + \
        "\"" + str(time_within_week) + "\", " + \
        "\"" + str(2) + "\", " + \
        "\"" + "week" + "\", " + \
        "\"" + get_top_5_article_ids(top_5_week) + "\"),\n")
    # Write top 5 month rank
    f_2.write("  (" + \
        "\"" + str(time_within_month) + "\", " + \
        "\"" + str(3) + "\", " + \
        "\"" + "month" + "\", " + \
        "\"" + get_top_5_article_ids(top_5_month) + "\");\n")
    f_2.write("UNLOCK TABLES;\n\n\n")
